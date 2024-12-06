package com.zhang.myproject.amap.weiget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import com.zhang.myproject.amap.R
import com.zhang.myproject.base.utils.getColorRes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.abs
import kotlin.math.ceil


class RecordTrackSpeedRoundView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    init {
        setBackgroundResource(R.color.transparent)
    }

    /** 扫描角度 */
    private var sweepAngle = 0F

    /** 目标角度 */
    private var targetAngle = 0F

    /** 当前速度 */
    private var mCurrentSpeed = 0f

    /** 最大速度 */
    private var mMaxSpeed = 80f

    /** 低速阀值 */
    private var mLowSpeed = 25F

    /** 中速阀值 */
    private var mMediumSpeed = 44F

    /** 是否正在移动 */
    private var isMove: Boolean = false

    /** 移动总时间 默认500ms*/
    private var moveTime: Float = 500F

    /** 屏幕刷新率 默认120hz */
    private var refreshRate: Int = 120

    /** 刷新移动角度 */
    private var refreshMoveAngle: Float = 0F

    /** 刷新间隔时间 */
    private var refreshIntervalTime: Long = 0L


    private var lowSpeedColor = intArrayOf(
        getColorRes(R.color.dashboard_low_speed_color_4),
        getColorRes(R.color.dashboard_low_speed_color_3),
        getColorRes(R.color.dashboard_low_speed_color_2),
        getColorRes(R.color.dashboard_low_speed_color_1),
    )

    private var lowSpeedStrokeColor = intArrayOf(
        getColorRes(R.color.dashboard_low_speed_stroke_color_3),
        getColorRes(R.color.dashboard_low_speed_stroke_color_2),
        getColorRes(R.color.dashboard_low_speed_stroke_color_1),
    )

    private var mediumSpeedColor = intArrayOf(
        context.getColor(R.color.dashboard_medium_speed_color_4),
        context.getColor(R.color.dashboard_medium_speed_color_3),
        context.getColor(R.color.dashboard_medium_speed_color_2),
        context.getColor(R.color.dashboard_medium_speed_color_1),
    )

    private var mediumSpeedStrokeColor = intArrayOf(
        getColorRes(R.color.dashboard_medium_speed_stroke_color_3),
        getColorRes(R.color.dashboard_medium_speed_stroke_color_2),
        getColorRes(R.color.dashboard_medium_speed_stroke_color_1),
    )

    private var highSpeedColor = intArrayOf(
        context.getColor(R.color.dashboard_high_speed_color_4),
        context.getColor(R.color.dashboard_high_speed_color_3),
        context.getColor(R.color.dashboard_high_speed_color_2),
        context.getColor(R.color.dashboard_high_speed_color_1),
    )

    private var highSpeedStrokeColor = intArrayOf(
        getColorRes(R.color.dashboard_high_speed_stroke_color_3),
        getColorRes(R.color.dashboard_high_speed_stroke_color_2),
        getColorRes(R.color.dashboard_high_speed_stroke_color_1),
    )

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        val centerX = width / 2
        val centerY = height / 2
        val radius = centerX.coerceAtMost(centerY)
        val arcWidth = radius.toFloat() * (1 - 180F / 280)
        val strokeWidth = arcWidth * 4 / 50

        // 设置扇形的绘制范围，以及起始角度和扫描角度
        val rectFArc = RectF(
            (centerX - radius + arcWidth / 2),
            (centerY - radius + arcWidth / 2),
            (centerX + radius - arcWidth / 2),
            (centerY + radius - arcWidth / 2)
        )
        // 绘制扇形
        val paint = Paint()
        paint.isAntiAlias = true
        paint.strokeCap = Paint.Cap.BUTT
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = arcWidth
        val positions = floatArrayOf(0.0f, 0.3333F, 0.6666f, 1.0f) // 渐变颜色的位置
        paint.shader =
            LinearGradient(centerY.toFloat(), 0F, centerY.toFloat(), height.toFloat(), getArcColor(), positions, Shader.TileMode.CLAMP)
        canvas.drawArc(rectFArc, 90F, sweepAngle, false, paint)

        // 设置扇形的绘制范围，以及起始角度和扫描角度
        val rectFStroke = RectF(
            (centerX - radius + strokeWidth / 2 + 2),
            (centerY - radius + strokeWidth / 2 + 2),
            (centerX + radius - strokeWidth / 2 - 2),
            (centerY + radius - strokeWidth / 2 - 2)
        )
        // 绘制描边
        paint.strokeWidth = strokeWidth
        val strokePositions = floatArrayOf(0.0f, 0.5f, 1.0f) // 渐变颜色的位置
        paint.shader = LinearGradient(
            centerY.toFloat(),
            0F,
            centerY.toFloat(),
            height.toFloat(),
            getArcStrokeColor(),
            strokePositions,
            Shader.TileMode.CLAMP
        )
        canvas.drawArc(rectFStroke, 90F, sweepAngle, false, paint)
    }

    /**
     * 设置速度
     */
    fun updateSpeed(speed: Float) {
        mCurrentSpeed = ceil(abs(speed))
        if (mCurrentSpeed > mMaxSpeed) {
            mCurrentSpeed = mMaxSpeed
        }
        CoroutineScope(Dispatchers.Main).launch {
            move(mCurrentSpeed * (360 / mMaxSpeed))
        }
    }

    private suspend fun move(angle: Float) {
        //目标角度与所需角度相同，则不处理
        if (targetAngle == angle) {
            return
        }
        //赋值目标角度
        targetAngle = angle
        //计算刷新间隔时间
        refreshIntervalTime = (moveTime / (moveTime / 1000 * refreshRate)).toLong()
        //计算刷新移动角度
        refreshMoveAngle = abs(angle - sweepAngle) / (moveTime / 1000 * refreshRate)
        //如果正在移动则不处理
        if (isMove) {
            return
        }
        isMove = true
        isMove = withContext(Dispatchers.IO) {
            while (sweepAngle != targetAngle) {
                delay(refreshIntervalTime)
                if (sweepAngle > targetAngle) {
                    sweepAngle -= refreshMoveAngle
                    if (sweepAngle < targetAngle) {
                        sweepAngle = targetAngle
                    }
                } else {
                    sweepAngle += refreshMoveAngle
                    if (sweepAngle > targetAngle) {
                        sweepAngle = targetAngle
                    }
                }
                // 切换到主线程更新UI
                withContext(Dispatchers.Main) {
                    postInvalidate()
                }
            }
            false
        }

    }

    /**
     * 获取扇形区域颜色
     */
    private fun getArcColor(): IntArray {
        return if (mCurrentSpeed <= mLowSpeed) {
            lowSpeedColor
        } else if (mCurrentSpeed <= mMediumSpeed) {
            mediumSpeedColor
        } else {
            highSpeedColor
        }
    }

    /**
     * 获取描线颜色
     */
    private fun getArcStrokeColor(): IntArray {
        return if (mCurrentSpeed <= mLowSpeed) {
            lowSpeedStrokeColor
        } else if (mCurrentSpeed <= mMediumSpeed) {
            mediumSpeedStrokeColor
        } else {
            highSpeedStrokeColor
        }
    }
}