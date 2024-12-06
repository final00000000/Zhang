package com.zhang.myproject.common.utils

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.location.Criteria
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import com.blankj.utilcode.util.ActivityUtils
import com.drake.logcat.LogCat
import com.zhang.myproject.common.utils.PermissionUtils.checkLocationPermission
import kotlin.math.abs

/**
 * Date: 2024/6/4
 * Author : Zhang
 * Description :
 */
object DashboardUtils {
    @SuppressLint("MissingPermission")
    fun gpsCoordinateData(context: Context, callback: (Triple<Double, Double, Double>) -> Unit) {
        val criteria = Criteria()
        criteria.accuracy = Criteria.ACCURACY_COARSE //低精度，如果设置为高精度，依然获取不了location。
        criteria.isAltitudeRequired = true //不要求海拔
        criteria.isBearingRequired = true //不要求方位
        criteria.isCostAllowed = true //允许有花费
        criteria.powerRequirement = Criteria.POWER_LOW //低功耗

        //获取LocationManager
        val locationManager = context.getSystemService("android.content.Context.LOCATION_SERVICE") as LocationManager

        // 获取最好的定位方式
        var provider = locationManager.getBestProvider(criteria, true) // true 代表从打开的设备中查找

        // 获取所有可用的位置提供器
        val providerList = locationManager.getProviders(true)
        // 测试一般都在室内，这里颠倒了书上的判断顺序
        provider = if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            LocationManager.NETWORK_PROVIDER
        } else if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            LocationManager.GPS_PROVIDER
        } else {
            // 当没有可用的位置提供器时，弹出Toast提示用户
            Log.e("测试", "gpsCoordinateData：请打开您的GPS或定位服务")
            return
        }

        val locationListener = LocationListener { location ->
            //当位置改变的时候调用
            //经度
            val longitude = location.longitude
            //纬度
            val latitude = location.latitude
            //海拔
            val altitude = location.altitude
            callback.invoke(Triple(latitude, longitude, altitude))
        }

        context.checkLocationPermission {
            if (it) {
                //获取上次的location
                var location = locationManager.getLastKnownLocation(provider)
                /**
                 * 参1:选择定位的方式
                 * 参2:定位的间隔时间
                 * 参3:当位置改变多少时进行重新定位
                 * 参4:位置的回调监听
                 */
                locationManager.requestLocationUpdates(provider, 1000, 0f, locationListener)
                while (location == null) {
                    location = locationManager.getLastKnownLocation(provider)
                }
                //移除更新监听
                locationManager.removeUpdates(locationListener)
                location.let {
                    // 不为空,显示地理位置经纬度 //
                    // 经度
                    val longitude = location.longitude
                    //纬度
                    val latitude = location.latitude
                    //海拔
                    val altitude = location.altitude
                    callback.invoke(Triple(latitude, longitude, altitude))
                }
            } else {
                LogCat.e("获取定位权限失败")
            }
        }
    }


    private var mBearingAngle = -1f
    private var gravity = FloatArray(3)
    private var geomagnetic = FloatArray(3)
    private var azimuth = 0f
    private var currentAzimuth = 0f
    private var lastBackPressTime: Long = -1L
    private const val ALPHA = 0.25f
    private const val ANGLE_THRESHOLD = 0.1f  // 角度变化阈值

    fun compassAzimuth(event: SensorEvent?, callBack: (Float, String) -> Unit) {
        event?.let {
            val alpha = 0.97f
            synchronized(this) {
                if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0]
                    gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1]
                    gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2]
                }
                if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                    geomagnetic[0] = alpha * geomagnetic[0] + (1 - alpha) * event.values[0]
                    geomagnetic[1] = alpha * geomagnetic[1] + (1 - alpha) * event.values[1]
                    geomagnetic[2] = alpha * geomagnetic[2] + (1 - alpha) * event.values[2]
                }
                val R = FloatArray(9)
                val I = FloatArray(9)
                val success = SensorManager.getRotationMatrix(R, I, gravity, geomagnetic)
                if (success) {
                    val orientation = FloatArray(3)
                    SensorManager.getOrientation(R, orientation)
                    azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
                    azimuth = (azimuth + 360) % 360
                    currentAzimuth = lowPassFilter(azimuth, currentAzimuth)
                    val currentTime = System.currentTimeMillis()
                    if (mBearingAngle != currentAzimuth && abs(mBearingAngle - currentAzimuth) >= ANGLE_THRESHOLD && currentTime - lastBackPressTime > 100) {
                        mBearingAngle = currentAzimuth
                        callBack.invoke(currentAzimuth, getBearingAngle(currentAzimuth.toInt()))
                        lastBackPressTime = currentTime
                    }
                }
            }
        }
    }

    private fun lowPassFilter(input: Float, output: Float): Float {
        return output + ALPHA * (input - output)
    }

    fun getBearingAngle(angle: Int): String {
        return when (angle) {
            in 0..22 -> "北 $angle°"
            in 23..67 -> "东北 $angle°"
            in 68..112 -> "东 $angle°"
            in 113..157 -> "东南 $angle°"
            in 158..202 -> "南 $angle°"
            in 203..247 -> "西南 $angle°"
            in 248..292 -> "西 $angle°"
            in 293..337 -> "西北 $angle°"
            in 338..360 -> "北 $angle°"
            else -> "北 $angle°"
        }
    }

    /**
     * 度对度分秒. 十进制度转度分秒(xxx° ==> xxx°xxx′xxx″)
     */
    fun convertToDMS(coordinate: Double, positive: String, negative: String): String {
        var coordinate = coordinate
        val direction = if (coordinate >= 0) positive else negative
        coordinate = abs(coordinate)
        val degrees = coordinate.toInt()
        coordinate = (coordinate - degrees) * 60
        val minutes = coordinate.toInt()
        val seconds = (coordinate - minutes) * 60
        return direction + degrees + "°" + minutes + "’" + String.format("%.0f", seconds) + "’’"
    }
}
