package com.zhang.myproject.base.helper

import com.zhang.myproject.resource.constant.APPKeyConstant
import com.zhang.myproject.resource.data.RecordTrackPauseData
import com.zhang.myproject.resource.data.RecordTrackPoyLinesData

/**
 * Date: 2023/9/14
 * Author : Zhang
 * Description :
 */
object MMkvHelperUtils {

    var AMapServiceIsRun = false

    /* 骑行GO开始时间戳*/
    var timestampStartTime = 0L

    /**
     * 是否是自动开启骑行GO
     * 0自动开启
     * 1自动关闭
     * 2手动开启
     * 3手动关闭
     */
    var AutoStartRecordTrack = 0

    /**
     * 骑行go 是否暂停
     */
    var cyclingGoHasPaused = false

    var cyclingCurrentStatus = ""

    var poyLinesList = mutableListOf<RecordTrackPoyLinesData>()


    /**
     * 设置手机陀螺仪方向角度
     */
    fun setGyroAngle(angle: Float) {
        MMkvHelper.putObject(APPKeyConstant.GYRO_ANGLE, angle)
    }

    /**
     * 获取手机陀螺仪方向角度
     */
    fun getGyroAngle(): Float = MMkvHelper.getObject(APPKeyConstant.GYRO_ANGLE, Float::class.java) ?: 0f

    /**
     * 是否同意隐私政策
     */
    fun setConsentToPrivacy(isConfirm: Boolean) {
        MMkvHelper.putObject(APPKeyConstant.CONSENT_TO_PRIVACY, isConfirm)
    }

    /**
     * 获取是否同意隐私政策
     */
    fun getConsentToPrivacy(): Boolean = MMkvHelper.getObject(APPKeyConstant.CONSENT_TO_PRIVACY, Boolean::class.java) ?: false

    /**
     * 获取是否是第一次启动
     */
    fun getFirstStart(): String? {
        return MMkvHelper.getObject(APPKeyConstant.FIRST_START, String::class.java)
    }

    fun saveFirstStart(b: String?) {
        MMkvHelper.putObject(APPKeyConstant.FIRST_START, b)
    }

    /**
     * 获取主题是否跟随系统模式
     */
    fun getNightSystemAutoMode(): Boolean {
        return MMkvHelper.getMMkv().decodeBool(APPKeyConstant.AUTO_MODEL)
    }

    /**
     * 获取当前主题样式
     */
    fun getCurrentNightMode(): Boolean {
        return MMkvHelper.getMMkv().decodeBool(APPKeyConstant.NIGHT_MODEL)
    }


    /**
     * 设置当前主题样式
     */
    fun setNightMode(mode: Boolean) {
        MMkvHelper.getMMkv().encode(APPKeyConstant.NIGHT_MODEL, mode)
    }

    /**
     * 设置主题是否跟随系统模式
     */
    fun setNightSystemAutoMode(mode: Boolean) {
        MMkvHelper.getMMkv().encode(APPKeyConstant.AUTO_MODEL, mode)
    }

    /**
     * 保存仪表盘默认视图 设置
     * @param mode Boolean
     */
    fun saveDashboardDefaultView(mode: Boolean) {
        MMkvHelper.putObject("dashboard_default_view", mode)
    }

    // 获取仪表盘默认视图 设置  true：仪表盘模式 false：地图模式
    fun getDashboardDefaultView(): Boolean = MMkvHelper.getObject("dashboard_default_view", Boolean::class.java) ?: true

    /**
     * 仪表盘设置-》开机自动记录轨迹
     */
    fun saveDashboardBootAutoRecordsTrack(isAuto: Boolean) {
        MMkvHelper.putObject("dashboard_boot_auto_records_track", isAuto)
    }

    /**
     * 仪表盘设置-》开机自动记录轨迹
     */
    fun getDashboardBootAutoRecordsTrack(): Boolean =
        MMkvHelper.getObject("dashboard_boot_auto_records_track", Boolean::class.java) ?: false

    /**
     * 仪表盘设置-》开启导航屏幕常亮
     */
    fun saveDashboardSteadyOnScreen(isAuto: Boolean) {
        MMkvHelper.putObject("dashboard_steady_on_screen", isAuto)
    }

    /**
     *仪表盘设置-》开启导航屏幕常亮
     */
    fun getDashboardSteadyOnScreen(): Boolean = MMkvHelper.getObject("dashboard_steady_on_screen", Boolean::class.java) ?: false


    /**
     * 仪表盘设置-》锁屏开启仪表盘
     */
    fun saveLockScreenOpensTheDashboard(isAuto: Boolean) {
        MMkvHelper.putObject("lock_screen_opens_the_dashboard", isAuto)
    }

    /**
     *仪表盘设置-》锁屏开启仪表盘
     */
    fun getLockScreenOpensTheDashboard(): Boolean =
        MMkvHelper.getObject("lock_screen_opens_the_dashboard", Boolean::class.java) ?: false


    /**
     * 骑行GO指南针开启状态
     */
    fun setCyclingGoCompass(isEnable: Boolean) {
        MMkvHelper.putObject("cycling_go_compass", isEnable)
    }

    /**
     * 骑行GO指南针开启状态
     */
    fun getCyclingGoCompass(): Boolean =
        MMkvHelper.getObject("cycling_go_compass", Boolean::class.java) ?: true

    /**
     * 保存仪表盘 颜色显示模式设置
     * 0：跟随系统   1：浅色模式   2：暗黑模式
     */
    fun saveDashboardShowColorView(mode: String) {
        MMkvHelper.putObject("dashboard_show_color_view", mode)
    }

    /**
     * 骑行GO暂停数据
     */
    fun saveRecordTrackPauseData(data: RecordTrackPauseData) {
        MMkvHelper.putObject("cycling_go_pause_data", data)
    }

    /**
     * 骑行GO暂停数据
     */
    fun getRecordTrackPauseData(): RecordTrackPauseData =
        MMkvHelper.getObject("cycling_go_pause_data", RecordTrackPauseData::class.java) ?: RecordTrackPauseData()
}