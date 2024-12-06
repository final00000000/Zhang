package com.zhang.myproject.amap.weiget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isVisible
import com.zhang.myproject.amap.data.AMapServiceData
import com.zhang.myproject.amap.R
import com.zhang.myproject.base.utils.SystemModelUtils
import com.zhang.myproject.base.utils.singleClick
import com.zhang.myproject.common.ktx.getIsInfiniteOrIsNan
import com.zhang.myproject.common.ktx.getMToKMUnit
import com.zhang.myproject.common.ktx.getStringMToKMNoUnit
import com.zhang.myproject.common.utils.TimeUtils
import com.zhang.myproject.resource.data.RecordTrackPauseData

/**
 * Date: 2023/6/13
 * Author : Zhang
 * Description :
 */
class NavigationSpeedView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    private var mCurrentSpeed = 0f

    private var tvSpeed: AppCompatTextView
    private var tvDistance: AppCompatTextView
    private var tvDistanceKm: AppCompatTextView
    private var tvTime: AppCompatTextView
    private var tvMaxSpeed: AppCompatTextView
    private var llSpeedCount: LinearLayoutCompat
    private var llSpeed: LinearLayoutCompat
    private var tvMileageTips: AppCompatTextView
    private var tvTimeTip: AppCompatTextView
    private var tvMaxSpeedKm: AppCompatTextView
    private var tvMaxSpeedTips: AppCompatTextView


    init {
        View.inflate(context, R.layout.layout_dash_board_navigation_speed_view, this)
        tvSpeed = findViewById(R.id.tv_speed)
        tvDistance = findViewById(R.id.tv_distance)
        tvDistanceKm = findViewById(R.id.tv_distance_km)
        tvTime = findViewById(R.id.tv_time)
        tvMaxSpeed = findViewById(R.id.tv_max_speed)
        llSpeedCount = findViewById(R.id.ll_speed_count)
        llSpeed = findViewById(R.id.ll_speed)
        tvMileageTips = findViewById(R.id.tv_mileage_tips)
        tvTimeTip = findViewById(R.id.tv_time_tip)
        tvMaxSpeedKm = findViewById(R.id.tv_max_speed_km)
        tvMaxSpeedTips = findViewById(R.id.tv_max_speed_tips)
        click()
    }

    private fun click() {
        llSpeed.singleClick {
            llSpeedCount.isVisible = llSpeedCount.visibility != View.VISIBLE
        }
    }

    fun setSpeedTxt(isPause: Boolean, speed: Float = mCurrentSpeed) {
        if (isPause) {
            tvSpeed.text = "--"
        } else {
            tvSpeed.text = "$speed"
        }
    }

    // 设置暂停前数据
    fun setPauseData(p0: RecordTrackPauseData) {
        tvSpeed.text = "--"
        tvTime.text = TimeUtils.timeConversion(p0.time)
        tvDistance.text = p0.distance
        tvDistanceKm.text = getMToKMUnit(p0.distance.toFloatOrNull() ?: 0f)
        tvMaxSpeed.text = p0.maxSpeed
        postInvalidate()
    }

    /**
     *
     * @param p0
     */
    fun updateUI(p0: AMapServiceData?) {
        p0?.let {
            val speed = getStringMToKMNoUnit(getIsInfiniteOrIsNan(it.speed, it.speed), 0)
            tvSpeed.text = if (speed.toInt() > 9) speed else "0${speed}"
            tvDistance.text = getStringMToKMNoUnit(getIsInfiniteOrIsNan(mCurrentSpeed, it.distance), 0)
            tvDistanceKm.text = getMToKMUnit(it.distance)

            mCurrentSpeed = it.speed

            tvMaxSpeed.text = getStringMToKMNoUnit(getIsInfiniteOrIsNan(it.speed, it.maxSpeed), 0)
            updateBackground(it.speed, SystemModelUtils.isSystemHighModel())
            postInvalidate()
        }
    }

    fun updateBackground(speed: Float, model: Boolean) {
        /*    when (speed.toInt()) {
                in 0..25 -> {
                    llSpeedCount.setBackgroundResource(if (model) R.drawable.dashboard_navi_speed_view_gradient_98c9f6_eff4f9_shape else R.drawable.dashboard_navi_speed_view_gradient_98c9f6_eff4f9_shape_black)
                    llSpeed.setBackgroundResource(if (model) R.drawable.corners_solid_white_black_stroke_2_4075ff_round else R.drawable.corners_solid_white_black_stroke_2_4075ff_round_black)
                    tvSpeed.setTextColor(context.getColor(R.color.color_4075FF))
                }

                in 26..44 -> {
                    llSpeedCount.setBackgroundResource(if (model) R.drawable.dashboard_navi_speed_view_gradient_98f6e0_eff4f9_shape else R.drawable.dashboard_navi_speed_view_gradient_98f6e0_eff4f9_shape_black)
                    llSpeed.setBackgroundResource(if (model) R.drawable.corners_solid_white_black_stroke_2_0ed984_round else R.drawable.corners_solid_white_black_stroke_2_0ed984_round_black)
                    tvSpeed.setTextColor(context.getColor(R.color.color_0ED984))
                }

                in 45..999 -> {
                    llSpeedCount.setBackgroundResource(if (model) R.drawable.dashboard_navi_speed_view_gradient_f6c098_eff4f9_shape else R.drawable.dashboard_navi_speed_view_gradient_f6c098_eff4f9_shape_black)
                    llSpeed.setBackgroundResource(if (model) R.drawable.corners_solid_white_black_stroke_2_fd8e50_round else R.drawable.corners_solid_white_black_stroke_2_fd8e50_round_black)
                    tvSpeed.setTextColor(context.getColor(R.color.color_FD8E50))
                }
            }
            tvDistance.setTextColor(Color.parseColor(if (model) "#000000" else "#FFFFFF"))
            tvDistanceKm.setTextColor(Color.parseColor(if (model) "#000000" else "#FFFFFF"))
            tvMileageTips.setTextColor(Color.parseColor(if (model) "#80858C" else "#999999"))
            tvTime.setTextColor(Color.parseColor(if (model) "#000000" else "#FFFFFF"))
            tvTimeTip.setTextColor(Color.parseColor(if (model) "#80858C" else "#999999"))
            tvMaxSpeed.setTextColor(Color.parseColor(if (model) "#000000" else "#FFFFFF"))
            tvMaxSpeedKm.setTextColor(Color.parseColor(if (model) "#000000" else "#FFFFFF"))
            tvMaxSpeedTips.setTextColor(Color.parseColor(if (model) "#80858C" else "#999999"))
            postInvalidate()*/
    }

    fun setTimer(timer: Int) {
        tvTime.text = TimeUtils.timeConversion(timer)
    }

}