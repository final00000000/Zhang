package com.zhang.myproject.resource.weiget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.google.android.material.switchmaterial.SwitchMaterial
import com.zhang.myproject.resource.R

class CommonSwitch : SwitchMaterial {

    private val mPaint by lazy { Paint(Paint.ANTI_ALIAS_FLAG) }
    private var isShowCover = false

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    }

    fun showCover(isShow: Boolean) {
        if (isShowCover == isShow) return
        isShowCover = isShow
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isShowCover) {
            mPaint.color = ContextCompat.getColor(context, R.color.color_DDE0EB_464646)
            val cornerRadius = measuredHeight.toFloat() / 2
            canvas.drawRoundRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), cornerRadius, cornerRadius, mPaint)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        isPressed = false
        if (ev.action == MotionEvent.ACTION_UP) {
            isPressed = true
            listener?.onClick(this)
        }
        return true
    }

    private var listener: OnClickListener? = null
    override fun setOnClickListener(listener: OnClickListener?) {
        this.listener = listener
    }

}