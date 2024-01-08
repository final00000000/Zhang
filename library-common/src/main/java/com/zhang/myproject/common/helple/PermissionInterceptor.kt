package com.zhang.myproject.common.helple

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.annotation.NonNull
import com.hjq.permissions.IPermissionInterceptor
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.OnPermissionPageCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.PermissionFragment
import com.hjq.permissions.XXPermissions
import com.kongzue.dialogx.dialogs.CustomDialog
import com.kongzue.dialogx.interfaces.OnBindView
import com.zhang.myproject.base.utils.toast.Toasty
import com.zhang.myproject.common.pop.PermissionRequestFailPopWindow
import com.zhang.myproject.resource.R

/**
 * Date: 2024/1/4
 * Author : Zhang
 * Description : 权限申请拦截器
*/
class PermissionInterceptor : IPermissionInterceptor {
    /** 权限申请标记  */
    private var mRequestFlag = false

    /** 拒绝权限弹窗*/
    private var mPermissionRequestFailPopWindow: PermissionRequestFailPopWindow? = null

    private val mHandler = Handler(Looper.getMainLooper())

    /** 权限说明*/
    private var mDialog: CustomDialog? = null

    override fun launchPermissionRequest(
        activity: Activity, allPermissions: MutableList<String>, callback: OnPermissionCallback?
    ) {
        mRequestFlag = true
        val deniedPermissions: MutableList<String?>? =
            XXPermissions.getDenied(activity, allPermissions)
        val title = activity.getString(
            R.string.common_permission_message,
            PermissionNameConvert.getPermissionTitle(activity, deniedPermissions)
        )
        val message =
            PermissionNameConvert.getPermissionMessageDescription(activity, deniedPermissions)

        val activityOrientation = activity.resources.configuration.orientation
        var showPopupWindow = activityOrientation == Configuration.ORIENTATION_PORTRAIT
        for (permission in allPermissions) {
            if (!XXPermissions.isSpecial(permission)) {
                continue
            }
            if (XXPermissions.isGranted(activity, permission)) {
                continue
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R &&
                TextUtils.equals(Permission.MANAGE_EXTERNAL_STORAGE, permission)
            ) {
                continue
            }
            // 如果申请的权限带有特殊权限，并且还没有授予的话
            // 就不用 PopupWindow 对话框来显示，而是用 Dialog 来显示
            showPopupWindow = false
            break
        }
        if (showPopupWindow) {
            PermissionFragment.launch(activity, ArrayList<String>(allPermissions), this, callback)
            // 延迟 300 毫秒是为了避免出现 PopupWindow 显示然后立马消失的情况
            // 因为框架没有办法在还没有申请权限的情况下，去判断权限是否永久拒绝了，必须要在发起权限申请之后
            // 所以只能通过延迟显示 PopupWindow 来做这件事，如果 300 毫秒内权限申请没有结束，证明本次申请的权限没有永久拒绝
            mHandler.postDelayed({
                if (!mRequestFlag) {
                    return@postDelayed
                }
                if (activity.isFinishing || activity.isDestroyed) {
                    return@postDelayed
                }
                showDialogTips(title, message)
            }, 300)
        }
    }

    override fun grantedPermissionRequest(
        activity: Activity,
        allPermissions: MutableList<String>,
        grantedPermissions: MutableList<String>,
        allGranted: Boolean,
        callback: OnPermissionCallback?
    ) {
        if (callback == null) {
            return
        }
        callback.onGranted(grantedPermissions, allGranted)
    }

    override fun deniedPermissionRequest(
        activity: Activity,
        allPermissions: MutableList<String?>,
        deniedPermissions: MutableList<String?>,
        doNotAskAgain: Boolean,
        callback: OnPermissionCallback?
    ) {
        dismissDialog()
        callback?.onDenied(deniedPermissions, doNotAskAgain)
        if (doNotAskAgain) {
            if (deniedPermissions.size == 1 && Permission.ACCESS_MEDIA_LOCATION == deniedPermissions[0]) {
                Toasty.info("获取媒体位置权限失败\n请清除应用数据后重试")
                return
            }
            showPermissionSettingDialog(activity, allPermissions, deniedPermissions, callback)
            return
        }
        if (deniedPermissions.size == 1) {
            val deniedPermission = deniedPermissions[0]
            val backgroundPermissionOptionLabel = getBackgroundPermissionOptionLabel(activity)
            if (Permission.ACCESS_BACKGROUND_LOCATION == deniedPermission) {
                Toasty.info(
                    activity.getString(
                        R.string.common_permission_background_location_fail_hint,
                        backgroundPermissionOptionLabel
                    )
                )
                return
            }
            if (Permission.BODY_SENSORS_BACKGROUND == deniedPermission) {
                Toasty.info(
                    activity.getString(
                        R.string.common_permission_background_sensors_fail_hint,
                        backgroundPermissionOptionLabel
                    )
                )
                return
            }
        }
        val message: String?
        val permissionNames: MutableList<String?> =
            PermissionNameConvert.permissionsToNames(activity, deniedPermissions)
        message = if (permissionNames.isNotEmpty()) {
            activity.getString(
                R.string.common_permission_fail_assign_hint,
                PermissionNameConvert.listToString(activity, permissionNames)
            )
        } else {
            activity.getString(R.string.common_permission_fail_hint)
        }
        showPopupWindow(activity, "title", message) {
            startSettings(activity, allPermissions, deniedPermissions, callback)
        }
    }

    override fun finishPermissionRequest(
        activity: Activity, allPermissions: MutableList<String?>,
        skipRequest: Boolean, callback: OnPermissionCallback?
    ) {
        dismissDialog()
        mRequestFlag = false
    }

    private fun showDialogTips(title: String, message: String) {
        mDialog = CustomDialog.build()
            .setCustomView(object :
                OnBindView<CustomDialog>(com.zhang.myproject.common.R.layout.permission_tips_dialog) {
                override fun onBind(dialog: CustomDialog?, v: View?) {
                    v?.findViewById<TextView>(com.zhang.myproject.common.R.id.tv_permission_description_title)?.text =
                        title
                    v?.findViewById<TextView>(com.zhang.myproject.common.R.id.tv_permission_description_message)?.text =
                        message
                }
            })
            .setAlign(CustomDialog.ALIGN.TOP)
            .show()
    }

    private fun showPopupWindow(
        activity: Activity,
        title: String,
        message: String,
        success: (Boolean) -> Unit
    ) {
        mPermissionRequestFailPopWindow = PermissionRequestFailPopWindow(activity).requestSetting {
            success.invoke(it)
        }
        mPermissionRequestFailPopWindow?.showPopup(title, message)
    }

    private fun dismissDialog() {
        if (mDialog == null) {
            return
        }
        if (mDialog?.isShow == false) {
            return
        }
        mDialog?.dismiss()
    }

    private fun dismissPopUpWindows() {
        if (mPermissionRequestFailPopWindow == null) {
            return
        }
        if (mPermissionRequestFailPopWindow?.isShowing == false) {
            return
        }
        mPermissionRequestFailPopWindow?.dismiss()
    }

    private fun showPermissionSettingDialog(
        activity: Activity?, allPermissions: MutableList<String?>,
        deniedPermissions: MutableList<String?>, callback: OnPermissionCallback?
    ) {
        if (activity == null || activity.isFinishing || activity.isDestroyed) {
            return
        }
        var message: String? = ""

        val permissionNames: MutableList<String?> =
            PermissionNameConvert.permissionsToNames(activity, deniedPermissions)

        if (permissionNames.isNotEmpty()) {
            if (deniedPermissions.size == 1) {
                val deniedPermission = deniedPermissions[0]
                if (Permission.ACCESS_BACKGROUND_LOCATION == deniedPermission) {
                    message = activity.getString(
                        R.string.common_permission_manual_assign_fail_background_location_hint,
                        getBackgroundPermissionOptionLabel(activity)
                    )
                } else if (Permission.BODY_SENSORS_BACKGROUND == deniedPermission) {
                    message = activity.getString(
                        R.string.common_permission_manual_assign_fail_background_sensors_hint,
                        getBackgroundPermissionOptionLabel(activity)
                    )
                }
            }
            if (TextUtils.isEmpty(message)) {
                message = activity.getString(
                    R.string.common_permission_manual_assign_fail_hint,
                    PermissionNameConvert.listToString(activity, permissionNames)
                )
            }
        } else {
            message = activity.getString(R.string.common_permission_manual_fail_hint)
        }
        showPopupWindow(activity, "22", message ?: "") {
            if (it) {
                startSettings(activity, allPermissions, deniedPermissions, callback)
            }
        }
    }

    private fun startSettings(
        activity: Activity, allPermissions: MutableList<String?>,
        deniedPermissions: MutableList<String?>, callback: OnPermissionCallback?
    ) {
        dismissPopUpWindows()
        XXPermissions.startPermissionActivity(activity,
            deniedPermissions, object : OnPermissionPageCallback {
                override fun onGranted() {
                    if (callback == null) {
                        return
                    }
                    callback.onGranted(allPermissions, true)
                }

                override fun onDenied() {
//                    showPermissionSettingDialog(
//                        activity, allPermissions,
//                        XXPermissions.getDenied(activity, allPermissions), callback
//                    )
                }
            })

    }

    /**
     * 获取后台权限的《始终允许》选项的文案
     */
    @NonNull
    private fun getBackgroundPermissionOptionLabel(context: Context): String {
        var backgroundPermissionOptionLabel = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            backgroundPermissionOptionLabel =
                context.packageManager.backgroundPermissionOptionLabel.toString()
        }
        if (TextUtils.isEmpty(backgroundPermissionOptionLabel)) {
            backgroundPermissionOptionLabel =
                context.getString(R.string.common_permission_background_default_option_label)
        }
        return backgroundPermissionOptionLabel
    }
}