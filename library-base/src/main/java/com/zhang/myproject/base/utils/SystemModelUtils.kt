package com.zhang.myproject.base.utils

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.zhang.myproject.base.AppGlobals.get
import com.zhang.myproject.base.helper.MMkvHelper
import com.zhang.myproject.resource.constant.APPKeyConstant

/**
 * Date: 2024/1/4
 * Author : Zhang
 * Description :系统主题工具类
 */
object SystemModelUtils {
    fun isSystemHighModel(): Boolean {
        val autoModel: Boolean = MMkvHelper.getMMkv().decodeBool(APPKeyConstant.AUTO_MODEL)

        //如果不是跟随系统模式 就使用用户设置的值
        if (!autoModel) {
            val nightModel: Boolean = MMkvHelper.getMMkv().decodeBool(APPKeyConstant.NIGHT_MODEL)
            AppCompatDelegate.setDefaultNightMode(if (nightModel) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
            return !nightModel
        }
        val uiMode = get()!!.resources.configuration.uiMode
        val dayNightUiMode = uiMode and Configuration.UI_MODE_NIGHT_MASK

        //当前是暗色主题
        //当前是自动主题
        return if (dayNightUiMode == Configuration.UI_MODE_NIGHT_NO) {
            //当前是亮色主题
            true
        } else {
            dayNightUiMode != Configuration.UI_MODE_NIGHT_YES
        }
    }
}
