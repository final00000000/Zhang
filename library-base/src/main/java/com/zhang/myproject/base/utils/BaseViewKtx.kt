package com.zhang.myproject.base.utils

import android.view.View
import android.view.ViewGroup
import com.gyf.immersionbar.ktx.statusBarHeight
import com.zhang.myproject.base.AppGlobals

/**
 * Date: 2024/1/5
 * Author : Zhang
 * Description :
 */

fun View?.initToolbarBarHeight() {
    this?.let {
        val layoutParams = it.layoutParams as ViewGroup.LayoutParams
        layoutParams.height = AppGlobals.get()?.statusBarHeight ?: 0
        it.layoutParams = layoutParams
    }
}