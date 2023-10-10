package com.zhang.myproject.common.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.zhang.myproject.base.AppGlobals

/**
 * Date: 2023/10/10
 * Author : Zhang
 * Description :
 */


/**
 * 获取颜色值
 * @receiver Activity
 * @param id Int
 * @return Int
 */
fun getColorRes(@ColorRes id: Int): Int =
    AppGlobals.get()?.let { ContextCompat.getColor(it, id) } ?: 0

/**
 * 获取资源图片
 * @receiver Activity
 * @param id Int
 * @return Drawable?
 */
fun getDrawableRes(@DrawableRes id: Int): Drawable? =
    AppGlobals.get()?.let { ContextCompat.getDrawable(it, id) }