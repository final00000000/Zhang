package com.zhang.myproject.common.helple

import android.content.Context
import android.os.Build
import com.hjq.permissions.Permission
import com.zhang.myproject.resource.R

/**
 * Date: 2023/12/8
 * Author : Zhang
 * Description :  权限名称转换器
 */
object PermissionNameConvert {
    /**
     * 获取权限名称
     */
    fun getPermissionTitle(context: Context, permissions: MutableList<String?>?): String {
        return listToString(context, permissionsToNames(context, permissions))
    }

    /**
     * 获取权限名称
     */
    fun getPermissionMessageDescription(
        context: Context,
        permissions: MutableList<String?>?
    ): String {
        return listToString(context, permissionsNamesDescription(context, permissions))
    }

    /**
     * String 列表拼接成一个字符串
     */
    fun listToString(context: Context, hints: MutableList<String?>?): String {
        if (hints.isNullOrEmpty()) {
            return context.getString(R.string.common_permission_unknown)
        }
        val builder = StringBuilder()
        for (text in hints) {
            if (builder.isEmpty()) {
                builder.append(text)
            } else {
                builder.append("、").append(text)
            }
        }
        return builder.toString()
    }

    /**
     * 将权限列表转换成对应名称列表
     */
    fun permissionsToNames(
        context: Context?, permissions: MutableList<String?>?
    ): MutableList<String?> {
        val permissionNames: MutableList<String?> = mutableListOf()
        if (context == null) {
            return permissionNames
        }
        if (permissions == null) {
            return permissionNames
        }
        for (permission in permissions) {
            when (permission) {
                Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE -> {
                    val hint = context.getString(R.string.common_permission_storage)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.READ_MEDIA_IMAGES, Permission.READ_MEDIA_VIDEO, Permission.READ_MEDIA_VISUAL_USER_SELECTED -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val hint = context.getString(R.string.common_permission_image_and_video)
                        if (!permissionNames.contains(hint)) {
                            permissionNames.add(hint)
                        }
                    }
                }

                Permission.READ_MEDIA_AUDIO -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val hint = context.getString(R.string.common_permission_music_and_audio)
                        if (!permissionNames.contains(hint)) {
                            permissionNames.add(hint)
                        }
                    }
                }

                Permission.CAMERA -> {
                    val hint = context.getString(R.string.common_permission_camera)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.RECORD_AUDIO -> {
                    val hint = context.getString(R.string.common_permission_microphone)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_BACKGROUND_LOCATION -> {
                    val hint: String =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !permissions.contains(
                                Permission.ACCESS_FINE_LOCATION
                            ) && !permissions.contains(Permission.ACCESS_COARSE_LOCATION)
                        ) {
                            context.getString(R.string.common_permission_location_background)
                        } else {
                            context.getString(R.string.common_permission_location)
                        }
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.BODY_SENSORS, Permission.BODY_SENSORS_BACKGROUND -> {
                    val hint: String =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !permissions.contains(
                                Permission.BODY_SENSORS
                            )
                        ) {
                            context.getString(R.string.common_permission_body_sensors_background)
                        } else {
                            context.getString(R.string.common_permission_body_sensors)
                        }
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.BLUETOOTH_SCAN, Permission.BLUETOOTH_CONNECT, Permission.BLUETOOTH_ADVERTISE -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        val hint = context.getString(R.string.common_permission_nearby_devices)
                        if (!permissionNames.contains(hint)) {
                            permissionNames.add(hint)
                        }
                    }
                }

                Permission.NEARBY_WIFI_DEVICES -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val hint = context.getString(R.string.common_permission_nearby_devices)
                        if (!permissionNames.contains(hint)) {
                            permissionNames.add(hint)
                        }
                    }
                }

                Permission.READ_PHONE_STATE, Permission.CALL_PHONE, Permission.ADD_VOICEMAIL, Permission.USE_SIP, Permission.READ_PHONE_NUMBERS, Permission.ANSWER_PHONE_CALLS -> {
                    val hint = context.getString(R.string.common_permission_phone)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.GET_ACCOUNTS, Permission.READ_CONTACTS, Permission.WRITE_CONTACTS -> {
                    val hint = context.getString(R.string.common_permission_contacts)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.READ_CALENDAR, Permission.WRITE_CALENDAR -> {
                    val hint = context.getString(R.string.common_permission_calendar)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.READ_CALL_LOG, Permission.WRITE_CALL_LOG, Permission.PROCESS_OUTGOING_CALLS -> {
                    val hint =
                        context.getString(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) R.string.common_permission_call_logs else R.string.common_permission_phone)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.ACTIVITY_RECOGNITION -> {
                    val hint =
                        context.getString(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) R.string.common_permission_activity_recognition_api30 else R.string.common_permission_activity_recognition_api29)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.ACCESS_MEDIA_LOCATION -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val hint =
                            context.getString(R.string.common_permission_access_media_location)
                        if (!permissionNames.contains(hint)) {
                            permissionNames.add(hint)
                        }
                    }
                }

                Permission.SEND_SMS, Permission.RECEIVE_SMS, Permission.READ_SMS, Permission.RECEIVE_WAP_PUSH, Permission.RECEIVE_MMS -> {
                    val hint = context.getString(R.string.common_permission_sms)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.MANAGE_EXTERNAL_STORAGE -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        val hint = context.getString(R.string.common_permission_all_file_access)
                        if (!permissionNames.contains(hint)) {
                            permissionNames.add(hint)
                        }
                    }
                }

                Permission.REQUEST_INSTALL_PACKAGES -> {
                    val hint = context.getString(R.string.common_permission_install_unknown_apps)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.SYSTEM_ALERT_WINDOW -> {
                    val hint = context.getString(R.string.common_permission_display_over_other_apps)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.WRITE_SETTINGS -> {
                    val hint = context.getString(R.string.common_permission_modify_system_settings)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.NOTIFICATION_SERVICE -> {
                    val hint = context.getString(R.string.common_permission_allow_notifications)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.POST_NOTIFICATIONS -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val hint = context.getString(R.string.common_permission_post_notifications)
                        if (!permissionNames.contains(hint)) {
                            permissionNames.add(hint)
                        }
                    }
                }

                Permission.BIND_NOTIFICATION_LISTENER_SERVICE -> {
                    val hint =
                        context.getString(R.string.common_permission_allow_notifications_access)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.PACKAGE_USAGE_STATS -> {
                    val hint = context.getString(R.string.common_permission_apps_with_usage_access)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.SCHEDULE_EXACT_ALARM -> {
                    val hint = context.getString(R.string.common_permission_alarms_reminders)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.ACCESS_NOTIFICATION_POLICY -> {
                    val hint = context.getString(R.string.common_permission_do_not_disturb_access)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS -> {
                    val hint = context.getString(R.string.common_permission_ignore_battery_optimize)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.BIND_VPN_SERVICE -> {
                    val hint = context.getString(R.string.common_permission_vpn)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.PICTURE_IN_PICTURE -> {
                    val hint = context.getString(R.string.common_permission_picture_in_picture)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.GET_INSTALLED_APPS -> {
                    val hint = context.getString(R.string.common_permission_get_installed_apps)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                else -> {}
            }
        }
        return permissionNames
    }

    /**
     * 将权限列表转换成对应名称列表
     */
    private fun permissionsNamesDescription(
        context: Context?, permissions: MutableList<String?>?
    ): MutableList<String?> {
        val permissionNames: MutableList<String?> = mutableListOf()
        if (context == null) {
            return permissionNames
        }
        if (permissions == null) {
            return permissionNames
        }
        for (permission in permissions) {
            when (permission) {
                Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE -> {
                    val hint =
                        "用于在添加、发布、下载、分享、识别图片或视频等场景中读取写入相册和上传文件内容"
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.READ_MEDIA_IMAGES, Permission.READ_MEDIA_VIDEO, Permission.READ_MEDIA_VISUAL_USER_SELECTED -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val hint =
                            "用于在添加、发布、下载、分享、识别图片或视频等场景中读取写入相册和上传文件内容"
                        if (!permissionNames.contains(hint)) {
                            permissionNames.add(hint)
                        }
                    }
                }

                Permission.READ_MEDIA_AUDIO -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val hint = "用于上传音频场景"
                        if (!permissionNames.contains(hint)) {
                            permissionNames.add(hint)
                        }
                    }
                }

                Permission.CAMERA -> {
                    val hint = "用于发表动态拍照、录制视频、扫码识别场景"
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.RECORD_AUDIO -> {
                    val hint = "用于语音录入场景"
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_BACKGROUND_LOCATION -> {
                    val hint: String =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !permissions.contains(
                                Permission.ACCESS_FINE_LOCATION
                            ) && !permissions.contains(Permission.ACCESS_COARSE_LOCATION)
                        ) {
                            "用于在发表动态，发现附近好友动态及车辆定位信息查看、搜索导航场景"
                        } else {
                            "用于在发表动态，发现附近好友动态及车辆定位信息查看、搜索导航场景"
                        }
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.BODY_SENSORS, Permission.BODY_SENSORS_BACKGROUND -> {
                    val hint: String =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !permissions.contains(
                                Permission.BODY_SENSORS
                            )
                        ) {
                            context.getString(R.string.common_permission_body_sensors_background)
                        } else {
                            context.getString(R.string.common_permission_body_sensors)
                        }
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.BLUETOOTH_SCAN, Permission.BLUETOOTH_CONNECT, Permission.BLUETOOTH_ADVERTISE -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        val hint = "用于扫描附近设备、绑定车辆、蓝牙解锁等场景"
                        if (!permissionNames.contains(hint)) {
                            permissionNames.add(hint)
                        }
                    }
                }

                Permission.NEARBY_WIFI_DEVICES -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val hint = "用于扫描附近设备、绑定车辆、蓝牙解锁等场景"
                        if (!permissionNames.contains(hint)) {
                            permissionNames.add(hint)
                        }
                    }
                }

                Permission.READ_PHONE_STATE, Permission.CALL_PHONE, Permission.ADD_VOICEMAIL, Permission.USE_SIP, Permission.READ_PHONE_NUMBERS, Permission.ANSWER_PHONE_CALLS -> {
                    val hint = context.getString(R.string.common_permission_phone)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.GET_ACCOUNTS, Permission.READ_CONTACTS, Permission.WRITE_CONTACTS -> {
                    val hint = "用于读取通讯录导入APP场景"
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.READ_CALENDAR, Permission.WRITE_CALENDAR -> {
                    val hint = context.getString(R.string.common_permission_calendar)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.READ_CALL_LOG, Permission.WRITE_CALL_LOG, Permission.PROCESS_OUTGOING_CALLS -> {
                    val hint =
                        context.getString(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) R.string.common_permission_call_logs else R.string.common_permission_phone)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.ACTIVITY_RECOGNITION -> {
                    val hint =
                        context.getString(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) R.string.common_permission_activity_recognition_api30 else R.string.common_permission_activity_recognition_api29)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.ACCESS_MEDIA_LOCATION -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val hint =
                            context.getString(R.string.common_permission_access_media_location)
                        if (!permissionNames.contains(hint)) {
                            permissionNames.add(hint)
                        }
                    }
                }

                Permission.SEND_SMS, Permission.RECEIVE_SMS, Permission.READ_SMS, Permission.RECEIVE_WAP_PUSH, Permission.RECEIVE_MMS -> {
                    val hint = context.getString(R.string.common_permission_sms)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.MANAGE_EXTERNAL_STORAGE -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        val hint = context.getString(R.string.common_permission_all_file_access)
                        if (!permissionNames.contains(hint)) {
                            permissionNames.add(hint)
                        }
                    }
                }

                Permission.REQUEST_INSTALL_PACKAGES -> {
                    val hint = context.getString(R.string.common_permission_install_unknown_apps)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.SYSTEM_ALERT_WINDOW -> {
                    val hint = context.getString(R.string.common_permission_display_over_other_apps)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.WRITE_SETTINGS -> {
                    val hint = context.getString(R.string.common_permission_modify_system_settings)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.NOTIFICATION_SERVICE -> {
                    val hint = context.getString(R.string.common_permission_allow_notifications)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.POST_NOTIFICATIONS -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val hint = context.getString(R.string.common_permission_post_notifications)
                        if (!permissionNames.contains(hint)) {
                            permissionNames.add(hint)
                        }
                    }
                }

                Permission.BIND_NOTIFICATION_LISTENER_SERVICE -> {
                    val hint =
                        context.getString(R.string.common_permission_allow_notifications_access)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.PACKAGE_USAGE_STATS -> {
                    val hint = context.getString(R.string.common_permission_apps_with_usage_access)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.SCHEDULE_EXACT_ALARM -> {
                    val hint = context.getString(R.string.common_permission_alarms_reminders)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.ACCESS_NOTIFICATION_POLICY -> {
                    val hint = context.getString(R.string.common_permission_do_not_disturb_access)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS -> {
                    val hint = context.getString(R.string.common_permission_ignore_battery_optimize)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.BIND_VPN_SERVICE -> {
                    val hint = context.getString(R.string.common_permission_vpn)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.PICTURE_IN_PICTURE -> {
                    val hint = context.getString(R.string.common_permission_picture_in_picture)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                Permission.GET_INSTALLED_APPS -> {
                    val hint = context.getString(R.string.common_permission_get_installed_apps)
                    if (!permissionNames.contains(hint)) {
                        permissionNames.add(hint)
                    }
                }

                else -> {}
            }
        }
        return permissionNames
    }
}