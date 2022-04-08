package com.example.kvest2.ui.home

import android.hardware.Camera
import android.location.Location
import android.location.LocationManager
import androidx.lifecycle.ViewModel
import com.example.kvest2.data.repository.UserRepository
import kotlin.math.abs


class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {
    /** A safe way to get an instance of the Camera object. */
    lateinit var locationManager: LocationManager

    fun getCameraInstance(): Camera? {
        return try {
            Camera.open() // attempt to get a Camera instance
        } catch (e: Exception) {
            // Camera is not available (in use or does not exist)
            null // returns null if camera is unavailable
        }
    }

    /*нам понадобятся, помимо прочего, две константы для хранения допустимых отклонений дистанции и азимута
    устройства от целевых. Значения подобраны практически, вы можете их менять, чтобы облегчить, или наоборот,
    усложнить задачу поиска покемона. Точность дистанции указана в условных единицах, равных примерно 0.9м,
    а точность азимута - в градусах*/
    private val DISTANCE_ACCURACY = 20.0
    private val AZIMUTH_ACCURACY = 10.0


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
    var mAzimuthTeoretical : Double = 0.0
    var mAzimuthReal : Float = 0.0f

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