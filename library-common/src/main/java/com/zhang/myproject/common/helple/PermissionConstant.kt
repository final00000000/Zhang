package com.zhang.myproject.common.helple

import android.Manifest
import android.os.Build

/**
 * Date: 2023/9/15
 * Author : Zhang
 * Description :
 */
object PermissionConstant {

    /**
     * 定位权限数组
     */
    val locationList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        mutableListOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
    } else {
        mutableListOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }
    val SYSTEM_ALERT_WINDOW = mutableListOf(Manifest.permission.SYSTEM_ALERT_WINDOW)
}