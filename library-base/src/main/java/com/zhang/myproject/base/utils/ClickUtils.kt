package com.zhang.myproject.base.utils

import android.view.View

/**
 * Date: 2023/9/13
 * Author : Zhang
 * Description :
 */

/***
 * 防止快速点击
 */
inline fun View.singleClick(crossinline listener: (View) -> Unit) {
    val minTime = 1000
    var lastTime = 0L
    setOnClickListener {
        val tmpTime = System.currentTimeMillis()
        if (tmpTime - lastTime > minTime) {
            lastTime = tmpTime
            listener(this)
        }
    }
}