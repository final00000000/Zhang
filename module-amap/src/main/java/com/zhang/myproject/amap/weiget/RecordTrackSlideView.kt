package com.zhang.myproject.amap.weiget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import com.zhang.myproject.amap.R
import com.zhang.myproject.base.helper.MMkvHelperUtils
import com.zhang.myproject.base.utils.dpToPx
import com.zhang.myproject.base.utils.getColorRes

@SuppressLint("ClickableViewAccessibility")
class RecordTrackSlideView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var mFlRoot: FrameLayout

    private var mFlSlide: FrameLayout

    private var mTvStart: AppCompatTextView
    private var vRect: View

    private var mDownX = 0f

    private var mIsSlide: Boolean = false

    private var mCurStatus = 0

    private var mOnSlideClickListener: (Int) -> Unit = { }

    init {
        View.inflate(context, R.layout.layout_record_track_slide_view, this)
        mFlRoot = findViewById(R.id.fl_root)
        mFlSlide = findViewById(R.id.fl_slide)
        mTvStart = findViewById(R.id.tv_start)
        vRect = findViewById(R.id.v_rect)
        if (MMkvHelperUtils.AMapServiceIsRun) {
            setStartNavigation()
        }
    }

    private var mInitialX: Float = 0f

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.apply {
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    mIsSlide = false
                    mDownX = x
                    mTvStart.isEnabled = mCurStatus != 0

                    // 保存初始位置
                    mInitialX = mFlSlide.x

                    // 检查触摸是否在滑块视图内
                    if (isTouchingSlideView(x, y)) {
                        mIsSlide = true
                        txtSetBG(true)
                    }
                }

                MotionEvent.ACTION_MOVE -> {
                    if (mIsSlide) {
                        mTvStart.isEnabled = mCurStatus != 0

                        // 更新滑块位置但保持在限制范围
                        val newX = (mInitialX + (x - mDownX)).coerceIn(
                            dpToPx(8f).toFloat(), width - mFlSlide.width - dpToPx(8f).toFloat()
                        )
                        mFlSlide.x = newX
                    }
                }

                MotionEvent.ACTION_UP -> {
                    txtSetBG(false)
                    if (mIsSlide && mCurStatus != 0) {
                        if (mFlSlide.x >= (width - dpToPx(40f)) * 0.8f) {
                            moveRight()  // 滑动到右边并回调
                        } else {
                            moveLeft()  // 未达到20%区域时滑回初始位置
                        }
                    } else if (!mIsSlide && mCurStatus == 0) {
                        mOnSlideClickListener.invoke(mCurStatus)
                        mCurStatus = 1
                        mFlSlide.clearAnimation()
                        mFlSlide.x = mInitialX
                        isVisibility(true)
                        mTvStart.text = context.getString(R.string.map_stop_trip_record)
                    }
                }
            }
        }
        return true
    }

    private fun moveLeft() {
        val endX = dpToPx(8f).toFloat()
        val translateAnimation = TranslateAnimation(0f, endX - mFlSlide.x, 0f, 0f)
        translateAnimation.duration = 100
        translateAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                mFlSlide.clearAnimation()
                mFlSlide.x = endX
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
        mFlSlide.startAnimation(translateAnimation)
    }

    private fun moveRight() {
        val endX = width - mFlSlide.width - dpToPx(8f).toFloat()
        val translateAnimation = TranslateAnimation(0f, endX - mFlSlide.x, 0f, 0f)
        translateAnimation.duration = 100
        translateAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                mFlSlide.clearAnimation()
                mFlSlide.x = endX
                if (mCurStatus != 0) {
                    mOnSlideClickListener.invoke(mCurStatus)  // 成功回调
                    mCurStatus = 0
                    mFlSlide.isVisible = false
                }
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
        mFlSlide.startAnimation(translateAnimation)
    }


    // 检查触摸位置 （x， y） 是否在 mFlSlide 视图的边界内。
    private fun isTouchingSlideView(x: Float, y: Float): Boolean {
        val slideRect = Rect()
        mFlSlide.getHitRect(slideRect)
        return slideRect.contains(x.toInt(), y.toInt())
    }

    fun setOnSlideClickListener(onSlideClickListener: (Int) -> Unit) {
        mOnSlideClickListener = onSlideClickListener
    }


    fun setStartNavigation() {
        mCurStatus = 1
        val params = mFlSlide.layoutParams as LayoutParams
        params.marginStart = dpToPx(8F)
        mFlSlide.layoutParams = params
        isVisibility(true)
    }

    private fun txtSetBG(aBoolean: Boolean) {
        if (aBoolean) {
            mTvStart.setTextColor(getColorRes(R.color.white))
            mTvStart.setBackgroundResource(R.drawable.shape_ff3535_ff455d_radius_40)
        } else {
            mTvStart.setTextColor(getColorRes(R.color.color_FF3535_FF455D))
            mTvStart.setBackgroundResource(R.drawable.corners_gradient_solid_color_ff3535_10_ff455d_10_radius_40)
        }
    }

    fun isVisibility(v: Boolean) {
        mFlSlide.isVisible = v
    }

    fun setContent(content: String) {
        mTvStart.text = content
    }

}