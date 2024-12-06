package com.zhang.myproject.amap.pop

import android.content.Context
import android.view.Gravity
import android.view.animation.Animation
import com.zhang.myproject.amap.R
import com.zhang.myproject.amap.databinding.LayoutRecordTrackSettingBinding
import com.zhang.myproject.base.helper.MMkvHelperUtils
import com.zhang.myproject.base.utils.SystemModelUtils
import com.zhang.myproject.base.utils.singleClick
import com.zhang.myproject.common.ktx.checkAlertWindowPermission
import com.zhang.myproject.common.ktx.getDrawableRes
import com.zhang.myproject.common.ktx.requestAlertWindowPermission
import com.zhang.myproject.common.ktx.viewBindViewBinding
import razerdp.basepopup.BasePopupWindow
import razerdp.util.animation.AnimationHelper
import razerdp.util.animation.Direction
import razerdp.util.animation.TranslationConfig


class RecordTrackSettingPop(
    private var mContext: Context,
    private val onModelClickListener: ((Boolean) -> Unit)? = null,
    private val onScreenListener: ((Boolean) -> Unit)? = null,
    private val onCompassListener: ((Boolean) -> Unit)? = null,
) : BasePopupWindow(mContext) {

    private var onCompassAction: (() -> Unit)? = null

    private var mBinding: LayoutRecordTrackSettingBinding? = null

    init {
        mBinding = mContext.viewBindViewBinding(R.layout.layout_record_track_setting) as LayoutRecordTrackSettingBinding
        contentView = mBinding?.root
        setOutSideDismiss(true)
        popupGravity = Gravity.BOTTOM
        initView()
        initClick()
    }

    override fun onCreateShowAnimation(): Animation? {
        return AnimationHelper.asAnimation().withTranslation(TranslationConfig().from(Direction.BOTTOM)).toShow()
    }

    override fun onCreateDismissAnimation(): Animation? {
        return AnimationHelper.asAnimation().withTranslation(TranslationConfig.TO_BOTTOM).toDismiss()
    }

    private fun initView() {
        mBinding?.apply {
            ivClose.singleClick { dismiss() }
            swiLockScreen.isChecked =
                checkAlertWindowPermission(mContext) && MMkvHelperUtils.getLockScreenOpensTheDashboard()

            if (MMkvHelperUtils.getDashboardDefaultView()) {
                rbDashboardMode.isChecked = true
            } else {
                rbMapMode.isChecked = true
            }

            if (MMkvHelperUtils.getNightSystemAutoMode()) { // true 跟随系统  else->
                rbFollowingSystem.isChecked = true
            } else {
                if (MMkvHelperUtils.getCurrentNightMode()) { // true黑夜模式 false-》浅色模式
                    rbDarkMode.isChecked = true
                } else {
                    rbLightMode.isChecked = true
                }
            }

            swiAutoTrack.isChecked = MMkvHelperUtils.getDashboardBootAutoRecordsTrack()
            swiSteady.isChecked = MMkvHelperUtils.getDashboardSteadyOnScreen()
            swiCompass.isChecked = MMkvHelperUtils.getCyclingGoCompass()

            swiCompass.setOnCheckedChangeListener { _, b ->
                //指南针
                MMkvHelperUtils.setCyclingGoCompass(b)
                onCompassAction?.invoke()
            }
            /*    when (MMkvHelperUtils.getDashboardShowColorView()) {
                    "0" -> rbFollowingSystem.isChecked = true
                    "1" -> rbLightMode.isChecked = true
                    "2" -> rbDarkMode.isChecked = true
                    else -> rbFollowingSystem.isChecked = true
                }*/

            onDismissListener = object : OnDismissListener() {
                override fun onDismiss() {
                    //开机自动记录轨迹
                    MMkvHelperUtils.saveDashboardBootAutoRecordsTrack(swiAutoTrack.isChecked)
                    //指南针
                    MMkvHelperUtils.setCyclingGoCompass(swiCompass.isChecked)
                    //屏幕常亮
                    MMkvHelperUtils.saveDashboardSteadyOnScreen(swiSteady.isChecked)
                    //锁屏开启仪表盘
                    MMkvHelperUtils.saveLockScreenOpensTheDashboard(swiLockScreen.isChecked)
                    //默认视图
                    MMkvHelperUtils.saveDashboardDefaultView(if (rbDashboardMode.isChecked) true else !rbMapMode.isChecked)
                    //显示模式
                    MMkvHelperUtils.saveDashboardShowColorView(if (rbFollowingSystem.isChecked) "0" else if (rbLightMode.isChecked) "1" else if (rbDarkMode.isChecked) "2" else "0")

                    onCompassListener?.invoke(swiCompass.isChecked)

                    onModelClickListener?.invoke(if (rbDashboardMode.isChecked) true else !rbMapMode.isChecked)

                    onScreenListener?.invoke(swiLockScreen.isChecked)
                }

            }
        }
    }

    fun setOnCompassAction(onAction: (() -> Unit)? = null) {
        onCompassAction = onAction
    }

    private fun initClick() {
        mBinding?.apply {
            swiLockScreen.thumbDrawable = getDrawableRes(R.drawable.record_track_setting_switch_thumb_white)

            if (SystemModelUtils.isSystemHighModel()) {
                swiLockScreen.trackDrawable = getDrawableRes(R.drawable.record_track_setting_switch_track_gray_green)
            } else {
                swiLockScreen.trackDrawable = getDrawableRes(R.drawable.record_track_setting_switch_track_gray_green1)
            }
            mBinding?.swiLockScreen?.setOnClickListener {
                if (!checkAlertWindowPermission(mContext)) {
                    dismiss()
                    requestPermission()
                } else {
                    swiLockScreen.isChecked = !swiLockScreen.isChecked
                }
            }
        }
    }

    private fun requestPermission() {
        mBinding?.swiLockScreen?.isChecked = false
        mContext.requestAlertWindowPermission { isSuccess: Boolean ->
            mBinding?.swiLockScreen?.isChecked = isSuccess
        }
    }
}