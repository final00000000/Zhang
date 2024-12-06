package com.zhang.myproject.helper

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.Surface
import android.view.WindowManager
import com.amap.api.maps.model.Marker
import com.blankj.utilcode.util.ActivityUtils
import kotlin.math.abs


/**
 * 定位图标指示方向，跟随手机指向转动
 */
class SensorEventHelper() : SensorEventListener {

    private val sensorManager by lazy { ActivityUtils.getTopActivity()?.getSystemService(Context.SENSOR_SERVICE) as SensorManager }

    private var mMarker: Marker? = null

    private var sListener: SensorListener? = null

    fun setSensorListener(listener: SensorListener?) {
        this.sListener = listener
    }

    fun registerSensorListener() {
        ae()
        a()
    }

    fun unRegisterSensorListener() {
        b()
    }

    fun setCurrentMarker(marker: Marker?) {
        mMarker = marker
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent) {
        val type: Int = event.sensor.type
        if (type == 3) {
            val f: Float = event.values[0]
            val a: Float = a(f)
            if (abs(this.h - f) < 3.0f) {
                return
            }
            this.h = a
            c(a)
            return
        }

        if (type == 1) {
            this.i = event.values.clone()
        } else if (type == 2) {
            this.j = event.values.clone()
        }
        val a2 = a(i, j)
        if (abs(h - a2) < 3.0f) {
            return
        }
        this.h = a2
        c(a2)
    }

    private fun a(f: Float): Float {
        return b(f)
    }


    private var b: Sensor? = null
    private var c: Sensor? = null
    private var d: Sensor? = null
    private var i = FloatArray(3)
    private var j = FloatArray(3)
    private var k = FloatArray(3)
    private var l = FloatArray(9)
    private var h = 0f
    private var m = true

    private fun ae() {
        try {
            if (c()) {
                this.b = sensorManager.getDefaultSensor(3)
                return
            }
            c = sensorManager.getDefaultSensor(1)
            d = sensorManager.getDefaultSensor(2)
        } catch (th: Throwable) {
            th.printStackTrace()
        }
    }

    private fun c(): Boolean {
        for (sensor in sensorManager.getSensorList(-1)) {
            val type = sensor.type
            sensor.stringType
            if (type == 3) {
                return true
            }
        }
        return false
    }

    fun a() {
        b?.let {
            sensorManager.registerListener(this, b, 3)
        }
        if (c != null && this.d != null) {
            sensorManager.registerListener(this, c, 3)
            sensorManager.registerListener(this, d, 3)
        }
    }

    fun b() {
        b?.let {
            sensorManager.unregisterListener(this, b)
        }
        if (c != null && d != null) {
            sensorManager.unregisterListener(this, c)
            sensorManager.unregisterListener(this, d)
        }
    }


    fun a(z: Boolean) {
        this.m = z
    }

    private fun a(fArr: FloatArray, fArr2: FloatArray): Float {
        if (!SensorManager.getRotationMatrix(this.l, null, fArr, fArr2)) {
            return 0.0f
        }
        SensorManager.getOrientation(this.l, this.k)
        val fArr3: FloatArray = this.k
        fArr3[0] = Math.toDegrees(fArr3[0].toDouble()).toFloat()
        return this.k[0]
    }

    private fun b(f: Float): Float {
        var a: Float = (f + getScreenRotationOnPhone(ActivityUtils.getTopActivity())) % 360.0f
        if (a > 180.0f) {
            a -= 360.0f
        } else if (a < -180.0f) {
            a += 360.0f
        }
        return if (a.isNaN()) {
            0.0f
        } else a
    }

    private fun c(f: Float) {
        try {
            val azimuth = (f % 360 + 360) % 360
            if (this.m) {
                sListener?.sensorCallBack(azimuth)
                mMarker?.let { it.rotateAngle = f }
                return
            }
            mMarker?.let { it.rotateAngle = 360.0f - f }
            sListener?.sensorCallBack(azimuth)
        } catch (th: Throwable) {
            th.printStackTrace()
        }
    }

    companion object {
        /**
         * 获取当前屏幕旋转角度
         *
         * @param context
         * @return 0表示是竖屏; 90表示是左横屏; 180表示是反向竖屏; 270表示是右横屏
         */
        fun getScreenRotationOnPhone(context: Context): Int {
            val display = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
            when (display.rotation) {
                Surface.ROTATION_0 -> return 0
                Surface.ROTATION_90 -> return 90
                Surface.ROTATION_180 -> return 180
                Surface.ROTATION_270 -> return -90
            }
            return 0
        }
    }
}

interface SensorListener {
    fun sensorCallBack(azimuth: Float)
}