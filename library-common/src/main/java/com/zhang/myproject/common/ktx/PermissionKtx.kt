package com.zhang.myproject.common.ktx

import android.content.Context
import android.provider.Settings
import com.hjq.permissions.XXPermissions
import com.zhang.myproject.common.helple.PermissionConstant
import com.zhang.myproject.common.helple.PermissionInterceptor

/**
 * Date: 2023/12/8
 * Author : Zhang
 * Description :
 */

fun Context.checkLocationPermission(): Boolean = XXPermissions.isGranted(this, PermissionConstant.locationList)

fun Context.requestLocationPermission(success: (Boolean) -> Unit) {
    XXPermissions.with(this)
        // 申请多个权限
        .permission(PermissionConstant.locationList)
        // 设置权限请求拦截器（局部设置）
        .interceptor(PermissionInterceptor())
        .request { _, allGranted ->
            success.invoke(allGranted)
        }
}

fun Context.requestAlertWindowPermission(success: (Boolean) -> Unit) {
    XXPermissions.with(this)
        // 申请多个权限
        .permission(PermissionConstant.SYSTEM_ALERT_WINDOW)
        // 设置权限请求拦截器（局部设置）
        .interceptor(PermissionInterceptor())
        .request { _, allGranted ->
            success.invoke(allGranted)
        }
}

/**
 * 检查是否有悬浮窗权限
 *
 * @param mContext
 * @return
 */
fun checkAlertWindowPermission(mContext: Context?): Boolean {
    return Settings.canDrawOverlays(mContext)
}