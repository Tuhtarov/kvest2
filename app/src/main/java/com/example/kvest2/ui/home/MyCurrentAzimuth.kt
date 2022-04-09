package com.example.kvest2.ui.home

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.example.kvest2.BuildConfig.DEBUG


class MyCurrentAzimuth(azimuthListener: OnAzimuthChangedListener, context: Context) :
    SensorEventListener {
    private var sensorManager: SensorManager? = null
    private var sensor: Sensor? = null
    //private var azimuthFrom: Double = 0.0
    //private var azimuthTo: Double = 0.0
    private val mAzimuthListener: OnAzimuthChangedListener = azimuthListener
    private var mContext: Context = context

    //подключаемся к сенсору и регистрируем слушатель для данного датчика с заданной периодичностью
    //SENSOR_DELAY_UI - частота обновления пользовательского интерфейса.
    //TYPE_ROTATION_VECTOR - Возвращает положение устройства в пространстве в виде угла
    //относительно оси Z, указывающей на север.
    // Виртуальный датчик, берущий показания от акселерометра, гироскопа и датчика магнитного поля.
    fun start() {
        sensorManager = mContext.getSystemService(SENSOR_SERVICE) as SensorManager?
        sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        sensorManager!!.registerListener(
            this, sensor,
            SensorManager.SENSOR_DELAY_UI
        )
    }

    //Отменяет регистрацию слушателя для всех датчиков.
    fun stop() {
        sensorManager!!.unregisterListener(this)
    }

    //вызывается при новом событии датчика
    //получаем матрицу вращения устройства
    // в переменную azimuthTo сохраняем градусную меру угла поворота в радианах


    private val mRotHist: MutableList<FloatArray> = ArrayList()
    private var mRotHistIndex = 0

    // Change the value so that the azimuth is stable and fit your requirement
    private val mHistoryMaxLength = 40
    //var mGravity: FloatArray = FloatArray(9)
    var mMagnetic: FloatArray = FloatArray(9)
    var mRotationMatrix = FloatArray(9)

    // the direction of the back camera, only valid if the device is tilted up by
    // at least 25 degrees.
    private var mFacing: Double = java.lang.Double.NaN

    val TWENTY_FIVE_DEGREE_IN_RADIAN = 0.436332313f
    val ONE_FIFTY_FIVE_DEGREE_IN_RADIAN = 2.7052603f

    override fun onSensorChanged(event: SensorEvent) {

        if (event.sensor.type == Sensor.TYPE_GRAVITY) {
            //mGravity = event.values.clone()
        } else {
            mMagnetic = event.values.clone()
        }

            SensorManager.getRotationMatrixFromVector(mRotationMatrix, event.values)
                // inclination is the degree of tilt by the device independent of orientation (portrait or landscape)
                // if less than 25 or more than 155 degrees the device is considered lying flat
                val inclination = Math.acos(mRotationMatrix.get(8).toDouble()).toFloat()
                if (inclination < TWENTY_FIVE_DEGREE_IN_RADIAN
                    || inclination > ONE_FIFTY_FIVE_DEGREE_IN_RADIAN
                ) {
                    // mFacing is undefined, so we need to clear the history
                    clearRotHist()
                    mFacing = Double.NaN
                    mAzimuthListener.onAzimuthChanged(mFacing)
                } else {
                    setRotHist()
                    // mFacing = azimuth is in radian
                    mFacing = Math.toDegrees(findFacing()+360)%360
                    mAzimuthListener.onAzimuthChanged(mFacing)
                }





    }

    private fun clearRotHist() {
        if (DEBUG) {
            Log.d(TAG, "clearRotHist()")
        }
        mRotHist.clear()
        mRotHistIndex = 0
    }

    private fun setRotHist() {
        if (DEBUG) {
            Log.d(TAG, "setRotHist()")
        }
        val hist: FloatArray = mRotationMatrix.clone()
        if (mRotHist.size === mHistoryMaxLength) {
            mRotHist.removeAt(mRotHistIndex)
        }
        mRotHist.add(mRotHistIndex++, hist)
        mRotHistIndex %= mHistoryMaxLength
    }

    private fun findFacing(): Double {
        if (DEBUG) {
            Log.d(TAG, "findFacing()")
        }
        val averageRotHist = average(mRotHist)
        return Math.atan2(
            (-averageRotHist[2]).toDouble(),
            (-averageRotHist[5]).toDouble()
        )
    }

    fun average(values: List<FloatArray>): FloatArray {
        val result = FloatArray(9)
        for (value in values) {
            for (i in 0..8) {
                result[i] += value[i]
            }
        }
        for (i in 0..8) {
            result[i] = result[i] / values.size
        }
        return result
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

}