package com.zhang.myproject.amap.weiget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.isVisible
import com.zhang.myproject.amap.R
import com.zhang.myproject.amap.data.AMapServiceData
import com.zhang.myproject.base.helper.MMkvHelperUtils
import com.zhang.myproject.common.ktx.dpToPx
import com.zhang.myproject.common.ktx.getScreenWidth
import kotlin.math.abs


/**
 * Date: 2023/6/15
 * Author : Zhang
 * Description :
 */
class RecordTrackSpeedView : FrameLayout {
    private var vBg: View? = null //背景View
    private var vOrientationAngleBg: View? = null //方向角View
    private var backgroundWidth: Int? = null //背景宽度
    private var ydProgressBar: RecordTrackSpeedRoundView? = null //扇形进度条
    private var ivDirectionBG: ImageView? = null //指针View
    private var ivDirectionArrow: ImageView? = null //指针View
    private var tvSpeed: RecordTrackSpeedTextView? = null//速度View

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        //获取背景宽度
        backgroundWidth = getScreenWidth() - dpToPx(9F * 2)
        backgroundWidth?.takeIf { it > 0 }?.let { backgroundWidth ->

            //设置背景属性
            vBg = View(context)
            vBg?.setBackgroundResource(R.mipmap.icon_dashboard_speed_bg_round1)
            vBg?.layoutParams = LayoutParams(backgroundWidth, backgroundWidth)
            (vBg?.layoutParams as LayoutParams).gravity = Gravity.CENTER_HORIZONTAL
            addView(vBg)

            //设置方向角
            vOrientationAngleBg = View(context)
            vOrientationAngleBg?.setBackgroundResource(R.mipmap.icon_dashboard_orientation_angle_bg)
            vOrientationAngleBg?.layoutParams = LayoutParams(backgroundWidth, backgroundWidth)
            vOrientationAngleBg?.isVisible = MMkvHelperUtils.getCyclingGoCompass()
            (vOrientationAngleBg?.layoutParams as LayoutParams).gravity = Gravity.CENTER_HORIZONTAL
            addView(vOrientationAngleBg)

            //设置进度条
            ydProgressBar = RecordTrackSpeedRoundView(context)
            ydProgressBar?.layoutParams = LayoutParams(
                (backgroundWidth * 280f / 342F).toInt(), (backgroundWidth * 280f / 342F).toInt()
            )
            (ydProgressBar?.layoutParams as LayoutParams).gravity = Gravity.CENTER
            addView(ydProgressBar)

            //设置指针
            ivDirectionBG = ImageView(context)
            ivDirectionBG?.setBackgroundResource(R.mipmap.icon_dashboard_direction_bg_round1)
            ivDirectionBG?.layoutParams = LayoutParams(
                (backgroundWidth * 280f / 342F).toInt(), (backgroundWidth * 280f / 342F).toInt()
            )
            (ivDirectionBG?.layoutParams as LayoutParams).gravity = Gravity.CENTER
            addView(ivDirectionBG)

            //设置指针
            ivDirectionArrow = ImageView(context)
            ivDirectionArrow?.setBackgroundResource(R.mipmap.icon_dashboard_direction_arrow1)
            ivDirectionArrow?.layoutParams = LayoutParams(
                (backgroundWidth * 280f / 342F).toInt(), (backgroundWidth * 280f / 342F).toInt()
            )
            (ivDirectionArrow?.layoutParams as LayoutParams).gravity = Gravity.CENTER
            addView(ivDirectionArrow)

            //设置速度
            tvSpeed = RecordTrackSpeedTextView(context)
            tvSpeed?.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            (tvSpeed?.layoutParams as LayoutParams).gravity = Gravity.CENTER
            addView(tvSpeed)
        }
    }

    fun updateDegree(degree: Float) {
        if (!MMkvHelperUtils.getCyclingGoCompass()) return
        ivDirectionArrow?.rotation = degree
    }

    fun orientationAngleVisible(visible: Boolean) {
        vOrientationAngleBg?.isVisible = visible
        ivDirectionArrow?.setBackgroundResource(if (visible) R.mipmap.icon_dashboard_direction_arrow1 else R.mipmap.icon_dashboard_direction_1)
    }

    fun setSpeedTxt(isPause: Boolean, speed: Float = mSpeed) {
        if (isPause) {
            tvSpeed?.setSpeed("--")
        } else {
            tvSpeed?.setSpeed(speed)
        }
    }

    private var mSpeed = 0f

    fun updateUI(p0: AMapServiceData?) {
        p0?.let {
            tvSpeed?.setSpeed(p0.speed)
            ydProgressBar?.updateSpeed(p0.speed)
            mSpeed = p0.speed

            setBg()
        }
    }

    fun updatePauseUI(speed: Float) {
        ydProgressBar?.updateSpeed(speed)
        mSpeed = speed
        setBg()
    }

    private fun setBg() {
        val compass = MMkvHelperUtils.getCyclingGoCompass()
        when (abs(mSpeed.toInt())) {
            0 -> {
                vBg?.setBackgroundResource(R.mipmap.icon_dashboard_speed_bg_round1)
                ivDirectionBG?.setBackgroundResource(R.mipmap.icon_dashboard_direction_bg_round1)
                ivDirectionArrow?.setBackgroundResource(if (compass) R.mipmap.icon_dashboard_direction_arrow1 else R.mipmap.icon_dashboard_direction_1)
            }

            in 1..25 -> {
                vBg?.setBackgroundResource(R.mipmap.icon_dashboard_speed_bg_round2)
                ivDirectionBG?.setBackgroundResource(R.mipmap.icon_dashboard_direction_bg_round2)
                ivDirectionArrow?.setBackgroundResource(if (compass) R.mipmap.icon_dashboard_direction_arrow2 else R.mipmap.icon_dashboard_direction_2)
            }

            in 26..44 -> {
                vBg?.setBackgroundResource(R.mipmap.icon_dashboard_speed_bg_round3)
                ivDirectionBG?.setBackgroundResource(R.mipmap.icon_dashboard_direction_bg_round3)
                ivDirectionArrow?.setBackgroundResource(if (compass) R.mipmap.icon_dashboard_direction_arrow3 else R.mipmap.icon_dashboard_direction_3)
            }

            in 45..999 -> {
                vBg?.setBackgroundResource(R.mipmap.icon_dashboard_speed_bg_round4)
                ivDirectionBG?.setBackgroundResource(R.mipmap.icon_dashboard_direction_bg_round4)
                ivDirectionArrow?.setBackgroundResource(if (compass) R.mipmap.icon_dashboard_direction_arrow4 else R.mipmap.icon_dashboard_direction_4)
            }

            else -> {
                vBg?.setBackgroundResource(R.mipmap.icon_dashboard_speed_bg_round1)
                ivDirectionBG?.setBackgroundResource(R.mipmap.icon_dashboard_direction_bg_round1)
                ivDirectionArrow?.setBackgroundResource(if (compass) R.mipmap.icon_dashboard_direction_arrow1 else R.mipmap.icon_dashboard_direction_1)
            }
        }
    }
}