package com.zhang.myproject.common.helple

import com.zhang.myproject.base.helper.MMkvHelper
import com.zhang.myproject.common.constant.APPKeyConstant

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
    fun getGyroAngle(): Float =
        MMkvHelper.getObject(APPKeyConstant.GYRO_ANGLE, Float::class.java) ?: 0f

    /**
     * 是否同意隐私政策
     */
    fun setConsentToPrivacy(isConfirm : Boolean){
        MMkvHelper.putObject(APPKeyConstant.CONSENT_TO_PRIVACY,isConfirm)
    }
    /**
     * 获取是否同意隐私政策
     */
    fun getConsentToPrivacy():Boolean =
        MMkvHelper.getObject(APPKeyConstant.CONSENT_TO_PRIVACY,Boolean::class.java)  ?: false

}