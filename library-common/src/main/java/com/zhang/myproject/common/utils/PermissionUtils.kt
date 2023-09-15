package com.zhang.myproject.common.utils

import android.Manifest
import android.app.Activity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.permissionx.guolindev.PermissionX
import com.zhang.myproject.base.utils.toast.Toasty

/**
 * Date: 2023/9/15
 * Author : Zhang
 * Description :
 */
object PermissionUtils {
    fun AppCompatActivity.checkLocationPermission(): Boolean {
        var outcome: Boolean = false

        val list = mutableListOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        PermissionX.init(this).permissions(list)
            .request { allGranted, _, deniedList ->
                if (allGranted) {
                    outcome = true
                } else {
                    outcome = false
                    Toasty.warning("这些权限被拒绝: $deniedList")
                }
            }
        return outcome
    }

    fun Fragment.checkLocationPermission(): Boolean {
        var outcome = false

        val list = mutableListOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        PermissionX.init(this).permissions(list)
            .request { allGranted, _, deniedList ->
                if (allGranted) {
                    outcome = true
                } else {
                    outcome = false
                    Toasty.warning("这些权限被拒绝: $deniedList")
                }
            }
        return outcome
    }
}