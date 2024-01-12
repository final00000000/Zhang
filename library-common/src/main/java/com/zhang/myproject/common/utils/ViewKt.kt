package com.zhang.myproject.common.utils

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.zhang.myproject.base.AppGlobals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach


/**
 * Date: 2023/10/10
 * Author : Zhang
 * Description :
 */


var mApplication: Application? = null

/**
 * 获取颜色值
 */
fun getColorRes(@ColorRes id: Int): Int =
    AppGlobals.get()?.let { ContextCompat.getColor(it, id) } ?: 0

/**
 * 获取资源图片
 */
fun getDrawableRes(@DrawableRes id: Int): Drawable? =
    AppGlobals.get()?.let { ContextCompat.getDrawable(it, id) }

/**
 */
fun getStringRes(@StringRes stringId: Int): String =
    AppGlobals.get()?.getString(stringId) ?: ""

/**
 * 绑定viewBinding
 * context.viewBindViewBinding(R.layout.XXX) as XXXBinding
 */
fun Context.viewBindViewBinding(@LayoutRes layout: Int): ViewDataBinding? =
    DataBindingUtil.bind(View.inflate(this, layout, null))

/**
 *  dp转px
 */
fun dpToPx(dpVal: Float): Int {
    return (AppGlobals.get()?.applicationContext ?: mApplication)?.let { dpConversionPx(it, dpVal) } ?: 0
}

/**
 * dp转px
 */
private fun dpConversionPx(context: Context, dpVal: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.resources.displayMetrics).toInt()
}

/**
 * 获得屏幕宽度
 */
fun Context?.getScreenWidth(): Int {
    this?.let {
        val dm: DisplayMetrics = it.resources.displayMetrics
        return dm.widthPixels
    }
    return 0
}

/**
 * 获得屏幕高度
 */
fun Context?.getScreenHeight(): Int {
    this?.let {
        val dm: DisplayMetrics = it.resources.displayMetrics
        return dm.heightPixels
    }
    return 0
}

/**
 * 计时器
 *
 * @param total Int 总时间
 * @param intervalTime Long 间隔时长
 * @param isCountDown Boolean true：计时器 ， false：倒计时
 * @param onTick Function1<Int, Unit>
 * @param onFinish Function0<Unit>
 * @param scope CoroutineScope
 * @return Job
 */
fun countDownCoroutines(
    total: Int = Int.MAX_VALUE,
    intervalTime: Long = 1000,
    isCountDown: Boolean = false,
    onTick: ((Int) -> Unit)? = null,
    onFinish: (() -> Unit)? = null,
    scope: CoroutineScope = GlobalScope
): Job {
    return flow {
        if (isCountDown) {
            for (i in 0 until total) {
                emit(i)
                delay(intervalTime)
            }
        } else {
            for (i in total downTo 0) {
                emit(i)
                delay(intervalTime)
            }
        }
    }.flowOn(Dispatchers.Default)
        .onCompletion { onFinish?.invoke() }
        .onEach { onTick?.invoke(it) }
        .flowOn(Dispatchers.Main)
        .launchIn(scope)
}

/**
 * TextView 添加图片
 */
fun TextView?.txtSetIcon(
    txt: String, drawable: Drawable?,
    startLocation: Int = 0, endLocation: Int = 1,
    width: Int = dpToPx(16f), height: Int = dpToPx(16f)
) {
    this?.let {
        // 创建 SpannableString
        val spannableString = SpannableString(txt)
        // 将图片添加到 SpannableString
        drawable?.let { icon ->
            icon.setBounds(0, 0, width, height)
            val imageSpan = ImageSpan(icon, ImageSpan.ALIGN_BASELINE)
            spannableString.setSpan(imageSpan, startLocation, endLocation, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        } ?: throw NullPointerException("drawable is null")
        // 设置 SpannableString 到 TextView
        it.text = spannableString
    } ?: throw NullPointerException("TextView is null")
}