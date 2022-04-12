package com.example.kvest2.ui.home

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.hardware.Camera
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.kvest2.R
import com.example.kvest2.data.entity.Quest
import com.example.kvest2.data.entity.TaskAnswerRelated
import com.example.kvest2.data.entity.TaskUserRelated
import com.example.kvest2.data.model.AppCurrentTasksSingleton
import com.example.kvest2.data.model.AppUserSingleton
import com.example.kvest2.databinding.HomeFragmentBinding
import com.example.kvest2.ui.camera.CameraPreview
import com.example.kvest2.ui.home.dialog.OfferToAnswerTheQuestionDialogFragment
import com.example.kvest2.ui.task.dialog.ChooseTaskDialogFragment
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import okhttp3.internal.immutableListOf

class HomeFragment : Fragment(), OnAzimuthChangedListener {
    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by activityViewModels {
        HomeViewModelFactory(binding.root.context)
    }

    private lateinit var binding: HomeFragmentBinding
    private lateinit var layout: View
    private lateinit var chooseTask: ChooseTaskDialogFragment
    private var dialog: OfferToAnswerTheQuestionDialogFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        layout = binding.frameLayout

        onRequestLocationPermissions(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        getLocationUpdates()

        onRequestCameraPermission(binding.root)

        startCamera()
        setupListeners()
        initObservers()

        handleCurrentTasks(AppCurrentTasksSingleton.currentTasks.value)

        return binding.root
    }

    private fun initObservers() = with(binding) {
        AppCurrentTasksSingleton.currentTask.observe(viewLifecycleOwner) {
            if (it != null) {
                dialog = OfferToAnswerTheQuestionDialogFragment(it) { answer ->
                    Toast.makeText(requireContext(), answer, Toast.LENGTH_SHORT)
                        .show()
                }

                testLocation = viewModel.getLocationFromTask(it.task)
                if (viewModel.getLocationFromTask(it.task) != null) azimuthToTask = viewModel.calculateTeoreticalAzimuth(viewModel.getLocationFromTask(it.task)!!)
            }
        }

        AppCurrentTasksSingleton.currentTasks.observe(viewLifecycleOwner) {
            handleCurrentTasks(it)
        }

        viewModel.isCanDisplayed.observe(viewLifecycleOwner) {
            if (it && dialog != null) {
                if (dialog!!.isAdded) {
                    dialog!!.show(parentFragmentManager, "offerToTransfer")
                }
            }
        }
    }

    private fun handleCurrentTasks(tasksAnswers: List<TaskAnswerRelated>?) {
        if (tasksAnswers == null || tasksAnswers.isEmpty()) {
            binding.apply {
                cardGeoData.visibility = CardView.GONE
                btnShowCurrentTasks.visibility = Button.GONE
                cardTaskNotExist.visibility = CardView.VISIBLE
            }

            return
        }

        val quest = AppCurrentTasksSingleton.currentQuest.value!!

        binding.apply {
            btnShowCurrentTasks.visibility = Button.VISIBLE
            cardGeoData.visibility = CardView.VISIBLE
            cardTaskNotExist.visibility = CardView.GONE
        }

        AppCurrentTasksSingleton.currentTasksUser.observe(viewLifecycleOwner) { tasksUsers ->
            if (tasksUsers != null) {
                initChooseTaskDialog(quest, tasksUsers, tasksAnswers)
            } else {
                Log.e("current-tasks-users", "Список пользовательских задач null")
                val emptyTasksUsersList = mutableListOf<TaskUserRelated>()
                initChooseTaskDialog(quest, emptyTasksUsersList, tasksAnswers)
            }
        }
    }

    private fun initChooseTaskDialog(
        quest: Quest,
        tasksUsers: MutableList<TaskUserRelated>,
        tasksAnswers: List<TaskAnswerRelated>
    ) {
        chooseTask = ChooseTaskDialogFragment(quest, tasksUsers, tasksAnswers) {
            viewModel.saveTaskHowCurrent(it.task)
            AppCurrentTasksSingleton.currentTask.value = it
        }

        Log.i("current-tasks-dialog", "Проинициализировано диалоговое окно " +
                "квест ${quest.name}, пользовательские задачи ${tasksUsers.size}, " +
                "все задачи ${tasksAnswers.size}")
    }

    private var testLocation: Location? = null
    private var azimuthToTask : Double? = null

    lateinit var myCurrentAzimuth: MyCurrentAzimuth

    /*метод setupListeners служит для инициализации слушателей местоположения и азимута - здесь
    мы вызываем конструкторы классов MyCurrentLocation и MyCurrentAzimuth и выполняем их методы start*/
    private fun setupListeners() {
        binding.btnShowCurrentTasks.setOnClickListener {
            chooseTask.show(parentFragmentManager, "choose-task")
        }

        myCurrentAzimuth = MyCurrentAzimuth(this, requireContext())
        myCurrentAzimuth.start()
    }

    var mCamera: Camera? = null
    private fun startCamera() {
        mCamera = viewModel.getCameraInstance()

        var mPreview: CameraPreview? = null

        mPreview = mCamera?.let {
            CameraPreview(requireContext(), it)
        }
        mPreview?.also {
            val preview: FrameLayout = binding.cameraPreview
            preview.addView(it)
        }


    }

    private fun View.showSnackbar(
        view: View,
        msg: String,
        length: Int,
        actionMessage: CharSequence?,
        action: (View) -> Unit
    ) {
        val snackbar = Snackbar.make(view, msg, length)
        if (actionMessage != null) {
            snackbar.setAction(actionMessage) {
                action(this)
            }.show()
        } else {
            snackbar.show()
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("Permission: ", "Granted")
            } else {
                Log.i("Permission: ", "Denied")
            }
        }

    private fun onRequestLocationPermissions(view: View) {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                layout.showSnackbar(
                    view,
                    getString(R.string.location_permission_granted),
                    Snackbar.LENGTH_SHORT,
                    null
                ) {}
            }
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                layout.showSnackbar(
                    view,
                    getString(R.string.location_permission_granted),
                    Snackbar.LENGTH_SHORT,
                    null
                ) {}
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                layout.showSnackbar(
                    view,
                    getString(R.string.location_permission_required),
                    Snackbar.LENGTH_INDEFINITE,
                    getString(R.string.ok)
                ) {
                    requestPermissionsLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            }
            else -> requestPermissionsLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
    }

    private fun onRequestCameraPermission(view: View) {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.d("VLAD-INFO", getString(R.string.camera_permission_granted))
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.CAMERA
            ) -> {
                layout.showSnackbar(
                    view,
                    getString(R.string.camera_permission_required),
                    Snackbar.LENGTH_INDEFINITE,
                    getString(R.string.ok)
                ) {
                    requestPermissionLauncher.launch(
                        Manifest.permission.CAMERA
                    )
                }
            }
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA
                )
            }
        }
    }


    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                Log.i("Permission FINE_LOCATION: ", "Granted")
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
                Log.i("Permission COARSE_LOCATION: ", "Granted")
            }
            else -> {
                // No location access granted.
                Log.i("Permission: ", "Denied")
            }
        }
    }

    // declare a global variable FusedLocationProviderClient
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // globally declare LocationRequest
    private lateinit var locationRequest: LocationRequest

    // globally declare LocationCallback
    private lateinit var locationCallback: LocationCallback


    /**
     * call this method in onCreate
     * onLocationResult call when location is changed
     */
    lateinit var currentDeviceLocation: Location
    private fun getLocationUpdates() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationRequest = LocationRequest()
        locationRequest.interval = 3000
        locationRequest.fastestInterval = 500
        locationRequest.smallestDisplacement = 1f // 170 m = 0.1 mile
        locationRequest.priority =
            LocationRequest.PRIORITY_HIGH_ACCURACY //set according to your app function
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return

                if (locationResult.locations.isNotEmpty()) {
                    // get latest location
                    currentDeviceLocation = locationResult.lastLocation

                    // use your location object
                    // get latitude , longitude and other info from this
                    binding.textLatitude.text = currentDeviceLocation.latitude.toString()
                    binding.textLongitude.text = currentDeviceLocation.longitude.toString()

                    Log.i(TAG, currentDeviceLocation.latitude.toString())
                    Log.i(TAG, currentDeviceLocation.longitude.toString())


                    //distanceToTask = viewModel.getDistanceToTask(taskLocation,currentDeviceLocation)
                    //viewModel.taskLocation = testLocation!!
                    viewModel.deviceLocation = currentDeviceLocation


                    //binding.distanceToPointInCircle.text = distanceToTask.toString()
                } else {
                    Log.i(TAG, "местоположения нету:(:(")
                }
            }
        }
    }

    var distanceToTask: Float = 0.0f
    //тестовая точка "задания" квеста
    //val testLocation : Location = Location("aboba")

    //start location updates
    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper() /* Looper */
        )
    }

    // stop location updates
    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    // stop receiving location update when activity not visible/foreground
    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
        mCamera?.stopPreview()
        myCurrentAzimuth.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
        mCamera?.stopPreview()
        myCurrentAzimuth.stop()
    }

    // start receiving location update when activity  visible/foreground
    override fun onResume() {
        super.onResume()
        startLocationUpdates()
        mCamera?.startPreview()
        myCurrentAzimuth.start()
    }

    private val DISTANCE_ACCURACY = 20.0


    var mAzimuthTeoretical: Double = 0.0
    var mAzimuthReal: Double = 0.0
    override fun onAzimuthChanged(azimuth: Double) {
        mAzimuthReal = azimuth
        if (!mAzimuthReal.isNaN() && azimuthToTask!=null) {
            mAzimuthTeoretical = azimuthToTask!!
            val minAngle = viewModel.calculateAzimuthAccuracy(mAzimuthTeoretical)!![0]
            val maxAngle = viewModel.calculateAzimuthAccuracy(mAzimuthTeoretical)!![1]

            if (viewModel.isBetween(minAngle, maxAngle, mAzimuthReal)) {
                //TODO видищь говнокодище? надо исправить....
                if (AppCurrentTasksSingleton.taskIsAvailable()) {
                    binding.circleWithDistance.visibility = View.VISIBLE
                    val task = AppCurrentTasksSingleton.getTask()

                    val location2 = Location("aaa")
                    location2.latitude = task.task.latitude.toDoubleOrNull()!!
                    location2.longitude = task.task.longitude.toDoubleOrNull()!!

                    distanceToTask = viewModel.getDistanceToTask(location2, currentDeviceLocation)
                    binding.distanceToPointInCircle.text = distanceToTask.toString()
                } else {
                    val task = AppCurrentTasksSingleton.currentTask.value
                }
            } else {
                binding.circleWithDistance.visibility = View.INVISIBLE
                distanceToTask = 0f
            }

            if ((viewModel.isBetween(
                    minAngle,
                    maxAngle,
                    mAzimuthReal
                ) && distanceToTask <= DISTANCE_ACCURACY)
            ) {
                //pointerIcon.setVisibility(View. VISIBLE );
                binding.textIsPointHitted.text = "Попал!"
                binding.textDistanceToPoint.visibility = View.VISIBLE

                //TODO вывод модалки для начала ответа (приостановить camera preview, получение геопозиции, изменения азимута)
                viewModel.isCanDisplayed.value = true

            } else {
                //pointerIcon.setVisibility(View. INVISIBLE );
                binding.textIsPointHitted.text = "Мимо..."
                binding.textDistanceToPoint.visibility = View.INVISIBLE
                viewModel.isCanDisplayed.value = false
            }

            binding.textCurrentAzimuth.text = mAzimuthReal.toString()
            binding.textTargetAzimuth.text = mAzimuthTeoretical.toString()
        } else {
            binding.textIsPointHitted.text = "Положение!"
            binding.circleWithDistance.visibility = View.INVISIBLE
            //mCamera?.stopPreview()
        }
    }
}
