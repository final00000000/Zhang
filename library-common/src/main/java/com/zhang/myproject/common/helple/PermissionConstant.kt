package com.zhang.myproject.common.helple

import android.Manifest
import android.content.Context
import android.os.Build
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.zhang.myproject.base.utils.toast.Toasty

/**
 * Date: 2023/9/15
 * Author : Zhang
 * Description :
 */
object PermissionConstant {

    /**
     * 定位权限数组
     */
    val locationList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
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
}