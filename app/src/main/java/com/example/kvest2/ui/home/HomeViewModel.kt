package com.example.kvest2.ui.home

import android.hardware.Camera
import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kvest2.data.entity.Answer
import com.example.kvest2.data.entity.Task
import com.example.kvest2.data.entity.TaskAnswerRelated
import com.example.kvest2.data.entity.User
import com.example.kvest2.data.model.AppCurrentTasksSingleton
import com.example.kvest2.data.repository.QuestRepository
import com.example.kvest2.data.repository.TaskRepository
import com.example.kvest2.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.abs


class HomeViewModel(
    private val userRepository: UserRepository,
    private val questRepository: QuestRepository,
    private val taskRepository: TaskRepository,
    private val currentUser: User
) : ViewModel() {
    /** A safe way to get an instance of the Camera object. */


    fun getCameraInstance(): Camera? {
        return try {
            Camera.open() // attempt to get a Camera instance
        } catch (e: Exception) {
            // Camera is not available (in use or does not exist)
            null // returns null if camera is unavailable
        }
    }

    val isCanDisplayed = MutableLiveData<Boolean>(false)


    /*нам понадобятся, помимо прочего, две константы для хранения допустимых отклонений дистанции и азимута
    устройства от целевых. Значения подобраны практически, вы можете их менять, чтобы облегчить, или наоборот,
    усложнить задачу поиска покемона. Точность дистанции указана в условных единицах, равных примерно 0.9м,
    а точность азимута - в градусах*/
    private val DISTANCE_ACCURACY = 20.0
    private val AZIMUTH_ACCURACY = 7.0


    fun getDistanceToTask(taskLocation: Location, userLocation: Location): Float {
        return userLocation.distanceTo(taskLocation)
    }

    fun setLocationInstances(tLocation: Location, dLocation: Location)
    {
        taskLocation = tLocation
        deviceLocation = dLocation
    }
    lateinit var taskLocation: Location
    lateinit var deviceLocation:Location

    val currentTask : MutableLiveData<TaskAnswerRelated> = MutableLiveData<TaskAnswerRelated>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val currentQuest = questRepository.findCurrentQuestByUserId(currentUser.id)

            if (currentQuest != null) {
                val tasks = taskRepository.getAllRelatedByQuestId(currentQuest.quest.id)

                AppCurrentTasksSingleton.currentTasks.postValue(tasks)
                AppCurrentTasksSingleton.currentQuest.postValue(currentQuest)
            }

            currentTask.postValue(AppCurrentTasksSingleton.currentTasks.value?.get(0))
        }
    }

    fun getLocationFromTask(task:Task) : Location?
    {
        val longitude = task.longitude.toDoubleOrNull()
        val latitude = task.latitude.toDoubleOrNull()

        if(longitude == null && latitude == null) {
            return null
        } else {
            val location = Location("aboba")
            location.latitude = latitude!!
            location.longitude = longitude!!
            return location
        }
    }
    /*вычисляем теоретический азимут по формуле, о которой я говорил в начале урока.
    Вычисление азимута для разных четвертей производим на основе таблицы. */
    fun calculateTeoreticalAzimuth(): Double {
        val dX: Double = taskLocation.latitude - deviceLocation.latitude
        val dY: Double = taskLocation.longitude - deviceLocation.longitude
        var phiAngle: Double
        var azimuth = 0.0
        val tanPhi: Double = abs(dY / dX)
        phiAngle = kotlin.math.atan(tanPhi)
        phiAngle = Math.toDegrees(phiAngle)
        if (dX > 0 && dY > 0) { // I quarter
            return phiAngle.also { azimuth = it }
        } else if (dX < 0 && dY > 0) { // II
            return 180 - phiAngle.also { azimuth = it }
        } else if (dX < 0 && dY < 0) { // III
            return 180 + phiAngle.also { azimuth = it }
        } else if (dX > 0 && dY < 0) { // IV
            return 360 - phiAngle.also { azimuth = it }
        }
        return phiAngle
    }


    //расчитываем точность азимута, необходимую для отображения покемона
     fun calculateAzimuthAccuracy(azimuth: Double): List<Double>? {
        var minAngle: Double = azimuth - AZIMUTH_ACCURACY
        var maxAngle: Double = azimuth + AZIMUTH_ACCURACY
        val minMax: MutableList<Double> = ArrayList()
        if (minAngle < 0) minAngle += 360.0
        if (maxAngle >= 360) maxAngle -= 360.0
        minMax.clear()
        minMax.add(minAngle)
        minMax.add(maxAngle)
        return minMax
    }

    //Метод isBetween определяет, находится ли азимут в целевом диапазоне с учетом допустимых отклонений
     fun isBetween(minAngle: Double, maxAngle: Double, azimuth: Double): Boolean {
        if (minAngle > maxAngle) {
            if (isBetween(0.0, maxAngle, azimuth) && isBetween(
                    minAngle,
                    360.0,
                    azimuth
                )
            ) return true
        } else {
            if (azimuth > minAngle && azimuth < maxAngle) return true
        }
        return false
    }



}