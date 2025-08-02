package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.compass

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.core.view.isVisible
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.base.BaseActivity
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.safeClick
import com.earthmap.map.ltv.tracker.databinding.ActivityCompassBinding
import kotlin.math.pow
import kotlin.math.sqrt

class CompassActivity : BaseActivity<ActivityCompassBinding>(), SensorEventListener {
    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityCompassBinding {
        return ActivityCompassBinding.inflate(layoutInflater)
    }

    private var sensorManager: SensorManager? = null
    private var currentDegree = 0f
    private var magneticFieldStrength = 0f

    override fun updateUI(savedInstanceState: Bundle?) {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        binding.tvNotSensor.isVisible = !hasSensor()
        binding.lnContent.isVisible = hasSensor()
        binding.ivBack.safeClick {
            finish()
        }
    }

    private fun hasSensor(): Boolean {
        val hasRotationVector = sensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) != null
        val hasMagneticField = sensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null
        return hasRotationVector && hasMagneticField
    }

    override fun onResume() {
        super.onResume()
        if (hasSensor()) {
            sensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)?.also {
                sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
            }
            sensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also {
                sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }


    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return
        when (event.sensor.type) {
            Sensor.TYPE_ROTATION_VECTOR -> {
                val rotationMatrix = FloatArray(9)
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)

                val orientation = FloatArray(3)
                SensorManager.getOrientation(rotationMatrix, orientation)

                val azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
                val degree = (azimuth + 360) % 360

                rotateCompass(degree)
                binding.tvAngle.text = "${degree.toInt()}°"
                binding.tvTrueHeading.text = "${azimuth.toInt()}"
                binding.tvAxis.text = "x = ${"%.0f".format(event.values[0] * 100)}°  y = ${"%.0f".format(event.values[1] * 100)}°"
            }

            Sensor.TYPE_MAGNETIC_FIELD -> {
                val magnitude = sqrt(
                    event.values[0].pow(2) +
                            event.values[1].pow(2) +
                            event.values[2].pow(2)
                )
                magneticFieldStrength = magnitude
                binding.tvMagnetic.text = "${"%.0f".format(magneticFieldStrength)} µT"
            }
        }
    }

    private fun rotateCompass(degree: Float) {
        val animation = RotateAnimation(
            currentDegree,
            -degree,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        animation.duration = 250
        animation.fillAfter = true

        binding.ivCompass.startAnimation(animation)
        currentDegree = -degree
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}