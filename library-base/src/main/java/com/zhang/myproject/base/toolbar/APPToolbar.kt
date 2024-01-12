package com.zhang.myproject.base.toolbar

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import com.zhang.myproject.base.utils.dpToPx
import com.zhang.myproject.resource.R

/**
 * Date: 2024/1/12
 * Author : Zhang
 * Description :
 */
class APPToolbar : LinearLayoutCompat {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    @SuppressLint("CustomViewStyleable", "Recycle")
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        dividerDrawable = ColorDrawable()
        orientation = HORIZONTAL
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, dpToPx(50f))
        requestLayout()
        val obtain = context.obtainStyledAttributes(attrs, R.styleable.app_base_toolbar)
        obtain.initLeftIcon()
    }

    @SuppressLint("Recycle", "CustomViewStyleable")
    private fun TypedArray?.initLeftIcon() {
        this?.apply {
            val leftIcon = getDrawable(R.styleable.app_base_toolbar_leftIcon)
            val leftIconVisible = getBoolean(R.styleable.app_base_toolbar_leftIconVisible, true)
            val leftIconAppearance = getResourceId(R.styleable.app_base_toolbar_leftIconAppearance, 0)
            val leftObtain = context.obtainStyledAttributes(leftIconAppearance, R.styleable.leftIconAppearance)
            val leftIconSize = leftObtain.getDimensionPixelSize(R.styleable.leftIconAppearance_leftIconSize, dpToPx(48f))
            if (!leftIconVisible) {
                return@apply
            }
            val ivLeftIcon = AppCompatImageView(context)
            ivLeftIcon.apply {
                val layoutParams = LayoutParams(leftIconSize, LayoutParams.WRAP_CONTENT)
                layoutParams.marginEnd = dpToPx(8F)
                layoutParams.gravity = Gravity.CENTER_VERTICAL
                setImageResource(com.zhang.myproject.base.R.drawable.ic_back)
                ivLeftIcon.layoutParams = layoutParams
            }
            leftIcon?.let { ivLeftIcon.setImageDrawable(it) }
            addView(ivLeftIcon)
            recycle()
        }
    }
}