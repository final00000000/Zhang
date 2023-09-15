package com.zhang.myproject.base.utils

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.zhang.myproject.base.AppGlobals
import com.zhang.myproject.base.helper.MmkvHelper

/**
 * 系统主题工具类
 */
object SystemModelUtils {
    val isSystemHighModel: Boolean
        get() {
            val autoModel: Boolean =
                MmkvHelper.getInstance().mmkv.decodeBool("APPKeyConstant.AUTO_MODEL")

            //如果不是跟随系统模式 就使用用户设置的值
            if (!autoModel) {
                val nightModel: Boolean =
                    MmkvHelper.getInstance().mmkv.decodeBool("APPKeyConstant.NIGHT_MODEL")
                AppCompatDelegate.setDefaultNightMode(if (nightModel) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
                return !nightModel
            }
            val uiMode: Int? = AppGlobals.get()?.resources?.configuration?.uiMode
            val dayNightUiMode = uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)

            //当前是暗色主题
            //当前是自动主题
            dayNightUiMode?.let {
                return if (it == Configuration.UI_MODE_NIGHT_NO) {
                    //当前是亮色主题
                    true
                } else {
                    it != Configuration.UI_MODE_NIGHT_YES
                }
            }
            return true
        }
}