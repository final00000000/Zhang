package com.zhang.myproject.common.utils

import android.content.Context
import android.os.Build
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.zhang.myproject.base.utils.toast.Toasty
import com.zhang.myproject.base.utils.toast.Toasty.LENGTH_LONG
import com.zhang.myproject.common.helple.PermissionConstant

/**
 * Date: 2023/9/15
 * Author : Zhang
 * Description :
 */
object PermissionUtils {
    fun Context.checkLocationPermission(success: (Boolean) -> Unit) {
        XXPermissions.with(this)
            // 申请多个权限
            .permission(PermissionConstant.locationList)
            // 设置权限请求拦截器（局部设置）
            //.interceptor(new PermissionInterceptor())
            // 设置不触发错误检测机制（局部设置）
            //.unchecked()
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                    if (!allGranted) {
                        Toasty.warning("获取部分权限成功，但部分权限未正常授予")
                        success.invoke(false)
                        return
                    }
                    Toasty.success("获取定位权限成功")
                    success.invoke(true)
                }

                override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                    if (doNotAskAgain) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            Toasty.warning("权限被永久拒绝，请手动授予定位权限\n设置为始终允许",LENGTH_LONG)
                        } else {
                            Toasty.warning("权限被永久拒绝，请手动授予定位权限")
                        }
                    } else {
                        Toasty.warning("获取定位权限失败")
                    }
                    success.invoke(false)
                }
            })
    }
}