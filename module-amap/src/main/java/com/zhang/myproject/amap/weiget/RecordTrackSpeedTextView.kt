package com.zhang.myproject.amap.weiget

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.zhang.myproject.amap.R
import com.zhang.myproject.base.utils.dpToPx
import com.zhang.myproject.base.utils.getStringRes

class RecordTrackSpeedTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    private var tvSpeed: TextView? = null
    private var tvTips: TextView? = null

    init {
        gravity = Gravity.CENTER
        orientation = VERTICAL
        setPadding(0, 0, 0, dpToPx(14F))
        tvSpeed = AppCompatTextView(context)
        tvSpeed?.setTextColor(ContextCompat.getColor(context, R.color.color_242942_White))
        tvSpeed?.textSize = 86F
        tvSpeed?.typeface = Typeface.DEFAULT_BOLD
        tvSpeed?.layoutParams = LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        tvSpeed?.text = "00"
        addView(tvSpeed)

        tvTips = AppCompatTextView(context)
        tvTips?.setTextColor(ContextCompat.getColor(context, R.color.color_8C8C8E_999999))
        tvTips?.textSize = 14F
        tvTips?.layoutParams = LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        (tvTips?.layoutParams as LayoutParams).topMargin = -dpToPx(14F)
        tvTips?.text = getStringRes(R.string.speed_km_h)
        addView(tvTips)
    }

    /**
     * 设置速度
     */
    fun setSpeed(value: Float?) {
        (value ?: 0F).let {
            tvSpeed?.text = if (it.toInt() > 9) "$it" else "0$it"
        }
    }

    /**
     * 设置速度
     */
    fun setSpeed(value: String?) {
        tvSpeed?.text = value
    }

    /**
     * 设置描述
     */
    fun setTips(tips: String?) {
        tvTips?.text = tips
    }

}