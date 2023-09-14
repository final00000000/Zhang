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
     * 设置主题是否跟随系统模式
     */
    fun setGyroAngle(angle: Float) {
        MmkvHelper.getInstance().putObject(APPKeyConstant.GYRO_ANGLE, angle)
    }

    /**
     * 设置主题是否跟随系统模式
     */
    fun getGyroAngle(): Float =
        MmkvHelper.getInstance().getObject(APPKeyConstant.GYRO_ANGLE, Float::class.java) ?: 0f
}