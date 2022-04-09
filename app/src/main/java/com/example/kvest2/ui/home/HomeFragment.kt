package com.example.kvest2.ui.home

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kvest2.R
import com.example.kvest2.databinding.HomeFragmentBinding
import com.example.kvest2.ui.camera.CameraPreview
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment(), OnAzimuthChangedListener {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    private lateinit var binding: HomeFragmentBinding

    private lateinit var layout: View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this, HomeViewModelFactory(requireContext()))
            .get(HomeViewModel::class.java)

        binding = HomeFragmentBinding.inflate(inflater, container, false)

        val view = binding.root
        layout = binding.frameLayout
        // in onCreate() initialize FusedLocationProviderClient

        onRequestLocationPermissions(view)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        getLocationUpdates()

        val location : Location = Location("aboba")
        val location1 : Location = Location("aboba")
        viewModel.setLocationInstances(location, location1)
        onRequestCameraPermission(view)

        startCamera()
        setupListeners()


        return binding.root
    }

    private fun getLocation()
    {

    }


    lateinit var myCurrentAzimuth : MyCurrentAzimuth
    /*метод setupListeners служит для инициализации слушателей местоположения и азимута - здесь
    мы вызываем конструкторы классов MyCurrentLocation и MyCurrentAzimuth и выполняем их методы start*/
    private fun setupListeners() {
        myCurrentAzimuth = MyCurrentAzimuth(this, requireContext())
        myCurrentAzimuth.start()
    }
    private fun startCamera() {
        var mCamera = viewModel.getCameraInstance()

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
                layout.showSnackbar(
                    view,
                    getString(R.string.camera_permission_granted),
                    Snackbar.LENGTH_SHORT,
                    null
                ) {}
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
    lateinit var currentDeviceLocation : Location
    private fun getLocationUpdates()
    {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationRequest = LocationRequest()
        locationRequest.interval = 3000
        locationRequest.fastestInterval = 500
        locationRequest.smallestDisplacement = 1f // 170 m = 0.1 mile
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY //set according to your app function
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return

                if (locationResult.locations.isNotEmpty()) {
                    // get latest location
                    currentDeviceLocation =
                        locationResult.lastLocation
                    // use your location object
                    // get latitude , longitude and other info from this
                    binding.textLatitude.text = currentDeviceLocation.latitude.toString()
                    binding.textLongitude.text = currentDeviceLocation.longitude.toString()
                    Log.i(TAG, currentDeviceLocation.latitude.toString())
                    Log.i(TAG, currentDeviceLocation.longitude.toString())



                    testLocation.longitude = 91.442496
                    testLocation.latitude = 53.722128

                    distanceToTask = viewModel.getDistanceToTask(testLocation,currentDeviceLocation)

                    viewModel.taskLocation = testLocation
                    viewModel.deviceLocation = currentDeviceLocation


                    binding.textDistanceToPoint.text = distanceToTask.toString()


                }


            }
        }
    }
    var distanceToTask : Float = 0.0f
    //тестовая точка "задания" квеста
    val testLocation : Location = Location("aboba")

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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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
    }

    // start receiving location update when activity  visible/foreground
    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    private val DISTANCE_ACCURACY = 20.0
    private val AZIMUTH_ACCURACY = 10.0
    var mAzimuthTeoretical : Double = 0.0
    var mAzimuthReal : Double = 0.0
    override fun onAzimuthChanged(azimuth: Double) {
        mAzimuthReal = azimuth
        mAzimuthTeoretical = viewModel.calculateTeoreticalAzimuth()

        val minAngle = viewModel.calculateAzimuthAccuracy(mAzimuthTeoretical)!![0]
        val maxAngle = viewModel.calculateAzimuthAccuracy(mAzimuthTeoretical)!![1]

        if ((viewModel.isBetween(minAngle, maxAngle, mAzimuthReal)&& distanceToTask<DISTANCE_ACCURACY)) {
            //pointerIcon.setVisibility(View. VISIBLE );
            binding.textIsPointHitted.text = "Попал!"
        } else {
            //pointerIcon.setVisibility(View. INVISIBLE );
            binding.textIsPointHitted.text = "Мимо..."
        }

        binding.textCurrentAzimuth.text = mAzimuthReal.toString()
        binding.textTargetAzimuth.text = mAzimuthTeoretical.toString()
    }

}
