package com.zhang.myproject.base.utils

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
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

/**
 *  dp转px
 */
fun dpToPx(dpVal: Float): Int {
    return AppGlobals.get()?.applicationContext?.let { dpConversionPx(it, dpVal) } ?: 0
}

/**
 * dp转px
 */
private fun dpConversionPx(context: Context, dpVal: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.resources.displayMetrics).toInt()
}


inline fun <reified T : View> View.find(@IdRes id: Int): T = findViewById(id)
inline fun <reified T : View> Activity.find(@IdRes id: Int): T = findViewById(id)