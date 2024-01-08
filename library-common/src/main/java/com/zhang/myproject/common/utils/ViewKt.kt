package com.zhang.myproject.common.utils

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
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

/**
 */
fun getStringRes(@StringRes stringId: Int): String =
    AppGlobals.get()?.let { it.getString(stringId) } ?: ""

/**
 * 绑定viewBinding
 * context.viewBindViewBinding(R.layout.XXX) as XXXBinding
 */
fun Context.viewBindViewBinding(@LayoutRes layout: Int): ViewDataBinding? =
    DataBindingUtil.bind(View.inflate(this, layout, null))


/**
 * 获得屏幕宽度
 *
 * @param context
 * @return
 */
fun getScreenWidth(context: Context): Int {
    val wm = context
        .getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val outMetrics = DisplayMetrics()
    wm.defaultDisplay.getMetrics(outMetrics)
    return outMetrics.widthPixels
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
