package com.zhang.myproject.common.helple

import com.tencent.mmkv.MMKV
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
        MmkvHelper.getInstance().putObject(APPKeyConstant.GYRO_ANGLE, angle)
    }

    /**
     * 获取手机陀螺仪方向角度
     */
    fun getGyroAngle(): Float =
        MmkvHelper.getInstance().getObject(APPKeyConstant.GYRO_ANGLE, Float::class.java) ?: 0f
}