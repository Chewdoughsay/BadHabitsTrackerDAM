package com.example.badhabitstrackerdam.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

class ShakeDetector(
    context: Context,
    private val onShake: () -> Unit
) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    // Shake detection parameters
    private var lastShakeTime: Long = 0
    private val shakeThreshold = 12.0f // Force needed to trigger shake
    private val shakeInterval = 1000L // Minimum time between shakes (ms)

    fun start() {
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            // Calculate total acceleration (magnitude of vector)
            val acceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()

            // Remove gravity (9.8 m/s²)
            val accelerationWithoutGravity = acceleration - SensorManager.GRAVITY_EARTH

            // Detect shake
            val currentTime = System.currentTimeMillis()
            if (accelerationWithoutGravity > shakeThreshold) {
                if (currentTime - lastShakeTime > shakeInterval) {
                    lastShakeTime = currentTime
                    onShake()
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not needed for shake detection
    }
}