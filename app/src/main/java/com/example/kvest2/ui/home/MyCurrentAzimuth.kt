package com.example.kvest2.ui.home

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager


class MyCurrentAzimuth(azimuthListener: OnAzimuthChangedListener, context: Context) :
    SensorEventListener {
    private var sensorManager: SensorManager? = null
    private var sensor: Sensor? = null
    private var azimuthFrom: Float = 0f
    private var azimuthTo: Float = 0f
    private val mAzimuthListener: OnAzimuthChangedListener = azimuthListener
    var mContext: Context = context

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
    override fun onSensorChanged(event: SensorEvent) {
        azimuthFrom = azimuthTo
        val orientation = FloatArray(3)
        val rMat = FloatArray(9)
        SensorManager.getRotationMatrixFromVector(rMat, event.values)
        azimuthTo = ((Math.toDegrees(
            SensorManager.getOrientation(rMat, orientation)[0]
                .toDouble()
        ) + 360).toInt() % 360).toFloat()
        mAzimuthListener.onAzimuthChanged(azimuthFrom, azimuthTo)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

}