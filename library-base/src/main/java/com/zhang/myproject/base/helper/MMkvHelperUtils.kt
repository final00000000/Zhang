package com.zhang.myproject.base.helper

import com.zhang.myproject.resource.constant.APPKeyConstant

/**
 * Date: 2023/9/14
 * Author : Zhang
 * Description :
 */
object MMkvHelperUtils {


    /**
     * 设置手机陀螺仪方向角度
     */
    fun setGyroAngle(angle: Float) {
        MMkvHelper.putObject(APPKeyConstant.GYRO_ANGLE, angle)
    }

    /**
     * 获取手机陀螺仪方向角度
     */
    fun getGyroAngle(): Float = MMkvHelper.getObject(APPKeyConstant.GYRO_ANGLE, Float::class.java) ?: 0f

    /**
     * 是否同意隐私政策
     */
    fun setConsentToPrivacy(isConfirm: Boolean) {
        MMkvHelper.putObject(APPKeyConstant.CONSENT_TO_PRIVACY, isConfirm)
    }

    /**
     * 获取是否同意隐私政策
     */
    fun getConsentToPrivacy(): Boolean = MMkvHelper.getObject(APPKeyConstant.CONSENT_TO_PRIVACY, Boolean::class.java) ?: false

    /**
     * 获取是否是第一次启动
     */
    fun getFirstStart(): String? {
        return MMkvHelper.getObject(APPKeyConstant.FIRST_START, String::class.java)
    }

    fun saveFirstStart(b: String?) {
        MMkvHelper.putObject(APPKeyConstant.FIRST_START, b)
    }

    /**
     * 获取主题是否跟随系统模式
     */
    fun getNightSystemAutoMode(): Boolean {
        return MMkvHelper.getMMkv().decodeBool(APPKeyConstant.AUTO_MODEL)
    }

    /**
     * 获取当前主题样式
     */
    fun getCurrentNightMode(): Boolean {
        return MMkvHelper.getMMkv().decodeBool(APPKeyConstant.NIGHT_MODEL)
    }


    /**
     * 设置当前主题样式
     */
    fun setNightMode(mode: Boolean) {
        MMkvHelper.getMMkv().encode(APPKeyConstant.NIGHT_MODEL, mode)
    }

    /**
     * 设置主题是否跟随系统模式
     */
    fun setNightSystemAutoMode(mode: Boolean) {
        MMkvHelper.getMMkv().encode(APPKeyConstant.AUTO_MODEL, mode)
    }
}