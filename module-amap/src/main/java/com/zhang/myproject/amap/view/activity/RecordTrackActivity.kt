package com.zhang.myproject.amap.view.activity

import android.annotation.SuppressLint
import android.app.ForegroundServiceStartNotAllowedException
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.AMapGestureListener
import com.amap.api.maps.model.BitmapDescriptor
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Polyline
import com.amap.api.maps.model.PolylineOptions
import com.amap.api.navi.AMapNavi
import com.gyf.immersionbar.ImmersionBar
import com.jeremyliao.liveeventbus.LiveEventBus
import com.zhang.myproject.amap.R
import com.zhang.myproject.amap.data.AMapServiceData
import com.zhang.myproject.amap.databinding.ActivityRecordTrackBinding
import com.zhang.myproject.amap.listener.IAMapNaviListener
import com.zhang.myproject.amap.pop.RecordTrackSettingPop
import com.zhang.myproject.amap.view.service.RecordTrackService
import com.zhang.myproject.amap.viewmodel.RecordTrackViewModel
import com.zhang.myproject.amap.weiget.ActionCallBack
import com.zhang.myproject.base.activity.BaseVBVMActivity
import com.zhang.myproject.base.helper.MMkvHelperUtils
import com.zhang.myproject.base.utils.SystemModelUtils
import com.zhang.myproject.base.utils.singleClick
import com.zhang.myproject.base.utils.toast.Toasty
import com.zhang.myproject.common.constant.LiveBusConstant
import com.zhang.myproject.common.ktx.conversionTime
import com.zhang.myproject.common.ktx.fadeIn
import com.zhang.myproject.common.ktx.fadeOut
import com.zhang.myproject.common.ktx.getIsInfiniteOrIsNan
import com.zhang.myproject.common.ktx.getMToKMUnit
import com.zhang.myproject.common.ktx.getStringMToKMNoUnit
import com.zhang.myproject.common.ktx.requestLocationPermission
import com.zhang.myproject.common.ktx.setMapAvatar
import com.zhang.myproject.common.ktx.setMapSkinModel
import com.zhang.myproject.common.ktx.showDashBoardFinishDialog
import com.zhang.myproject.common.utils.DashboardUtils
import com.zhang.myproject.common.utils.TimeUtils
import com.zhang.myproject.common.utils.TimerHelper
import com.zhang.myproject.common.utils.TimerListener
import com.zhang.myproject.helper.RECORD_TRACK_FINISH
import com.zhang.myproject.helper.RECORD_TRACK_PAUSE
import com.zhang.myproject.helper.RECORD_TRACK_RESUME
import com.zhang.myproject.helper.RECORD_TRACK_START
import com.zhang.myproject.helper.SensorEventHelper
import com.zhang.myproject.helper.SensorListener
import com.zhang.myproject.resource.data.RecordTrackPauseData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.jessyan.autosize.utils.AutoSizeUtils


/**
 * Date: 2023/6/14
 * Author : Zhang
 * Description :
 */
@SuppressLint("SetTextI18n")
//@Route(path = RouterActivityPath.YadeaAmap.CYCLING_GO)
class RecordTrackActivity : BaseVBVMActivity<ActivityRecordTrackBinding, RecordTrackViewModel>(R.layout.activity_record_track),
    AMapLocationListener, IAMapNaviListener,
    TimerListener, SensorListener {

    private var mAMap: AMap? = null

    private var mAMapNavi: AMapNavi? = null

    private var mLocationClient: AMapLocationClient? = null

    private var mTimeCount = 0

    /**
     * 是否在滑动地图（在记录行程的时候）
     */
    private var mIsSlideMap = false

    private var mLatLng: LatLng? = null

    /**
     * 地图服务数据
     */
    private var mMapServiceBean: AMapServiceData? = null

    private var mStartCyclingGo = false

    private var mFirst = false

    private val mSensorHelper by lazy { SensorEventHelper() }


    private var lastClickTime: Long = 0

    // 存储所有的轨迹段
    private val polylineSegments = mutableListOf<Polyline>()

    // 当前段的 PolylineOptions
    private lateinit var currentPolylineOptions: PolylineOptions

    // 当前段的 Polyline
    private var currentPolyline: Polyline? = null

    private var currentSpeed = 0f

    companion object {

        const val DEBOUNCE_INTERVAL = 1000L // 设置1秒的防抖间隔

        /**
         * 预计行驶总里程（米）
         */
        var mDistance: Int = 0

        /**
         * 已经行驶时间（秒）
         */
        var mElapsedTime: Int = 0

        /**
         * 最大车速
         */
        var mMaxSpeed = 0f

        /**
         * 平均速速
         */
        var mAverageSpeed: Float = 0f

        /**
         * 开始时间
         */
        var mStartTime = ""

        /**
         * 结束时间
         */
        var mEndTime = ""

        //最后一次暂停时间
        var mLastPauseTime = 0L

        // 累计的暂停时间
        var mTotalPausedTime = 0L

        var mCacheTime = 0
    }

    override fun isLayoutToolbar(): Boolean = false
    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).titleBar(mViewBinding.clRoot).fitsSystemWindows(false)
            .statusBarDarkFont(SystemModelUtils.isSystemHighModel()).init()
        mViewModel.initViewModel()
        finishLoading()
        initData()
        initMap(savedInstanceState)
        initObserve()
        switchingMode(MMkvHelperUtils.getDashboardDefaultView())
    }

    override fun setOnViewClick() {
        mViewBinding.apply {
            ivBack.singleClick { finish() }
            ivDoubt.singleClick { jumpToH5() }

            ivSettings.singleClick {
                val dashboardSettingPop = RecordTrackSettingPop(this@RecordTrackActivity)
                dashboardSettingPop.setOnCompassAction {
                    compassVisible()
                    geographicVisible()
                }
                dashboardSettingPop.showPopupWindow()

            }

            ivMode.singleClick {
                if (ivMode.isSelected) {
                    switchingMode(true)
                } else {
                    switchingMode(false)
                }
            }
            ivMapLocation.setOnClickListener {
                requestPermission(1)
            }

            actionButton.setCallBack(object : ActionCallBack {
                override fun requestPermission(call: (Boolean) -> Unit) {
                    requestPermission(3) {
                        call.invoke(it)
                    }
                }

                override fun onStart() {
                    MMkvHelperUtils.cyclingCurrentStatus = RECORD_TRACK_START
                    //手动开启
                    MMkvHelperUtils.AutoStartRecordTrack = 2
                    MMkvHelperUtils.cyclingGoHasPaused = false
                    startCyclingGo()
                }

                override fun onPaused() {
                    pauseRecord()
                }

                override fun onResume() {
                    resumeRecord()
                }

                override fun onFinish() {
                    finishCyclingGo()
                }

            })
        }
    }

    override fun createObserver() {
    }

    private fun initData() {
        mViewBinding.apply {
            pagDashboardBg.apply {
                path = if (SystemModelUtils.isSystemHighModel()) "assets://starry_sky_light_bg.pag" else "assets://starry_sky_night_bg.pag"
                setRepeatCount(-1)
                setScaleMode(3)
            }
            pagDashboardBg1.apply {
                path = if (SystemModelUtils.isSystemHighModel()) "assets://speeding_light.pag" else "assets://speeding_night.pag"
                setRepeatCount(-1)
                setScaleMode(3)
            }
            if (MMkvHelperUtils.AMapServiceIsRun && MMkvHelperUtils.cyclingCurrentStatus == RECORD_TRACK_PAUSE) {
                setDashboardPauseData(MMkvHelperUtils.getRecordTrackPauseData())
            }
        }
    }


    private fun jumpToH5() {
        Toasty.info("跳转帮助H5")
    }

    private fun finishCyclingGo() {
        MMkvHelperUtils.cyclingCurrentStatus = RECORD_TRACK_FINISH
        MMkvHelperUtils.cyclingGoHasPaused = false
        startLoading()
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime < DEBOUNCE_INTERVAL) {
            // 如果两次点击间隔时间小于防抖间隔，则忽略此次调用
            return
        }
        lastClickTime = currentTime
        exitCyclingGo()
        MMkvHelperUtils.AutoStartRecordTrack = 3
        if (mDistance >= 100 && mElapsedTime >= 30) {
            uploadData()
        } else {
            finishLoading()
            showDashBoardFinishDialog(2) {
                uploadDataError()
            }
        }
    }

    private fun uploadData(isReUpload: Boolean = false, trId: String = "") {
        mMapServiceBean?.run {
            Toasty.success("上传到接口")
        }
    }

    private fun uploadCallBack(isSuccess: Boolean, trId: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            if (isSuccess) {
                showDashBoardFinishDialog(1) { confirm ->
                    uploadDataSuccess(confirm)
                }
            } else {
                showDashBoardFinishDialog(3) {
                    if (it) {
                        startLoading()
                        uploadData(isReUpload = true, trId = trId)
                    } else {
                        uploadDataError()
                    }
                }
            }
        }
    }

    private fun geographicVisible() {
        mViewBinding.apply {
            if (gpMapModel.visibility == View.VISIBLE) return
            llGeographicData.isVisible = MMkvHelperUtils.getCyclingGoCompass()
            if (MMkvHelperUtils.getCyclingGoCompass()) {
                tvOrientationAngle.visibility = View.VISIBLE
            } else {
                tvOrientationAngle.visibility = View.INVISIBLE
            }
        }
    }

    private fun compassVisible() {
        mViewBinding.recordTrackSpeed.orientationAngleVisible(MMkvHelperUtils.getCyclingGoCompass())
    }

    /**
     * 开启骑行
     */
    private fun startCyclingGo() {
        mViewBinding.apply {
            // 屏幕常亮
            MMkvHelperUtils.AMapServiceIsRun = true
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            MMkvHelperUtils.timestampStartTime = System.currentTimeMillis()
            mStartTime = TimeUtils.stampToTime(System.currentTimeMillis())
            startTimer()
            startService()
            startLocation()
        }

    }

    private fun startTimer(count: Int = 0) {
        TimerHelper.startTimer(1000, count)
        TimerHelper.timerListener(this@RecordTrackActivity)
    }

    private val bitmapCache by lazy {
        mapOf(
            "paused" to BitmapDescriptorFactory.fromResource(R.mipmap.icon_route_path_dotted_line_green),
            "active" to BitmapDescriptorFactory.fromResource(R.mipmap.icon_route_path_green)
        )
    }

    private fun initObserve() {
        mViewBinding.apply {
            LiveEventBus.get<AMapServiceData>(LiveBusConstant.AMap.SEND_RECORD_TRACK_SERVICE_DATA).observeForever {
                currentSpeed = it.speed
                if (mIsSlideMap) {
                    mTimeCount++
                    if (mTimeCount == 6) {
                        mTimeCount = 0
                        mIsSlideMap = false
                    }
                }
                if (mFirst) {
                    lifecycleScope.launch {
                        firstRecoveryPath()
                    }
                    mFirst = false
                } else {
                    lifecycleScope.launch {
                        recoveryPath()
                    }
                }
                if (!mIsSlideMap) {
                    mAMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 17f))
                }
                if (MMkvHelperUtils.cyclingGoHasPaused) {
                    return@observeForever
                }
                mMapServiceBean = it
                updateUI(it)
            }

            /**
             * 退出、切车、token失效 结束骑行GO
             * 切车上传数据 其他状况结束
             */
            LiveEventBus.get(LiveBusConstant.AMap.EXIT_RECORD_TRACK, Boolean::class.java).observeForever {
                exitCyclingGo()
                if (!it) {
                    MMkvHelperUtils.AutoStartRecordTrack = 1
                }
            }

        }
    }

    private fun setDashboardPauseData(p0: RecordTrackPauseData?) {
        mViewBinding.apply {
            p0?.let {
                recordTrackSpeed.setSpeedTxt(true)
                yadeaNaviSpeedView.setPauseData(it)
                tvDistance.text = it.distance
                tvDistanceKm.text = getMToKMUnit(it.distance.toFloatOrNull() ?: 0f)
                tvAvSpeed.text = it.avgSpeed
                tvMaxSpeed.text = it.maxSpeed
                tvTime.text = TimeUtils.timeConversion(it.time)
            }
        }
    }

    private fun uploadDataSuccess(hasConfirm: Boolean) {
        if (hasConfirm) {
            Toasty.info("跳转记录成功展示界面")
        }
        exitCyclingGo()
        uploadDataError()
    }

    private fun uploadDataError() {
        clearData()
        finish()
    }

    private fun exitCyclingGo() {
        mEndTime = TimeUtils.stampToTime(System.currentTimeMillis())
        stopService()
        stopLocation()
        TimerHelper.stopTimer()
        mAMapNavi?.let {
            it.stopNavi()
            it.stopSpeak()
        }
        mViewBinding.actionButton.resetDefault()
        MMkvHelperUtils.timestampStartTime = 0L
        mCacheTime = 0
        mTotalPausedTime = 0L
        MMkvHelperUtils.cyclingCurrentStatus = ""
        MMkvHelperUtils.saveRecordTrackPauseData(RecordTrackPauseData())
        MMkvHelperUtils.poyLinesList.clear()
        AMapNavi.destroy()
        mLocationClient?.onDestroy()
        lifecycleScope.launch(Dispatchers.IO) {
            //会耗时1s左右
            mViewBinding.mapView.onDestroy()
        }
    }

    /**
     * 初始化地图
     */
    private fun initMap(savedInstanceState: Bundle?) {
        mViewBinding.mapView.onCreate(savedInstanceState)
        mAMap = mViewBinding.mapView.map
        mAMap?.uiSettings?.let { uiSettings ->
            uiSettings.isZoomControlsEnabled = false
            uiSettings.isRotateGesturesEnabled = false
            uiSettings.isTiltGesturesEnabled = false
            uiSettings.logoPosition = AMapOptions.LOGO_POSITION_BOTTOM_CENTER
            uiSettings.setLogoBottomMargin(AutoSizeUtils.dp2px(this, -20f))
        }
        mAMap?.setLoadOfflineData(true)
        mAMap?.setMapSkinModel()
        mAMap?.isTrafficEnabled = false
        try {
            mLocationClient = AMapLocationClient(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mAMapNavi = AMapNavi.getInstance(this)
        mAMapNavi?.addAMapNaviListener(this)
        initLocation()
        mViewBinding.mapView.map.setAMapGestureListener(object : AMapGestureListener {
            override fun onDoubleTap(p0: Float, p1: Float) {}
            override fun onSingleTap(p0: Float, p1: Float) {}
            override fun onFling(p0: Float, p1: Float) {}
            override fun onScroll(p0: Float, p1: Float) {}
            override fun onLongPress(p0: Float, p1: Float) {}
            override fun onDown(p0: Float, p1: Float) {
                if (MMkvHelperUtils.AMapServiceIsRun) {
                    mTimeCount = 0
                    mIsSlideMap = true
                }
            }

            override fun onUp(p0: Float, p1: Float) {}

            override fun onMapStable() {}

        })

        if (MMkvHelperUtils.AMapServiceIsRun && (MMkvHelperUtils.AutoStartRecordTrack == 0 || MMkvHelperUtils.AutoStartRecordTrack == 2)) {
            mFirst = true
            startService()
        }
        if (intent.getBooleanExtra("start", false)) {
            requestPermission(2)
        }
    }

    /**
     * 初始化定位
     */
    private fun initLocation() {
        mLocationClient.run {
            stopLocation()
        }
        this.setMapAvatar(mAMap)
        mLocationClient?.setLocationListener(this)
        val locationOption = AMapLocationClientOption()
        locationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        locationOption.isOnceLocation = true
        locationOption.isOnceLocationLatest = true
        locationOption.interval = 1000
        locationOption.isSensorEnable = true
        locationOption.isSelfStartServiceEnable = true
        locationOption.setSensorEnable(true)
        mLocationClient?.setLocationOption(locationOption)
        refreshLocation()
        startLocation()
    }


    /**
     * 刷新位置
     */
    private fun refreshLocation() {
        val lastMapLocation: AMapLocation = mLocationClient?.lastKnownLocation ?: return
        lastMapLocation.run {
            mLatLng = LatLng(latitude, longitude)
            mAMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 17f))
        }
    }

    /**
     * 定位监听
     */
    override fun onLocationChanged(aMapLocation: AMapLocation?) {
        aMapLocation?.run {
            mLatLng = LatLng(latitude, longitude)
            setCoordinate()
            mAMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 17f))
        }
    }

    private fun setCoordinate() {
        DashboardUtils.gpsCoordinateData(this@RecordTrackActivity) { list ->
            setGeographicData(list.first, list.second, list.third)
        }
    }


    /**
     * true 仪表盘
     * false 地图
     * @param mode Boolean
     */
    private fun switchingMode(mode: Boolean) {
        mViewBinding.apply {
            mMapServiceBean?.speed?.let {
                if (it >= 45 && mode) {
                    pagDashboardBg1.play()
                    pagDashboardBg1.fadeIn()
                } else {
                    pagDashboardBg1.pause()
                    pagDashboardBg1.fadeOut()
                }
            }
            compassVisible()
            if (mode) {
                gpMapModel.fadeOut()
                gpDashboardModel.fadeIn()
                recordTrackSpeed.fadeIn()
                pagDashboardBg.fadeIn()
                pagDashboardBg.play()
                ivMapLocation.isVisible = false
                if (MMkvHelperUtils.getCyclingGoCompass()) {
                    llGeographicData.fadeIn()
                    tvOrientationAngle.fadeIn()
                } else {
                    geographicVisible()
                }
            } else {
                initLocation()
                gpMapModel.fadeIn()
                gpDashboardModel.fadeOut()
                recordTrackSpeed.fadeOut()
                pagDashboardBg.fadeOut()
                pagDashboardBg.pause()
                ivMapLocation.isVisible = true
                llGeographicData.fadeOut()
                tvOrientationAngle.fadeOut()
            }
            ivMode.isSelected = !mode
        }
    }

    /**
     * 更新仪表盘显示UI
     */
    private fun updateUI(p0: AMapServiceData) {
        mViewBinding.apply {
            p0.let {
                runOnUiThread {
                    mDistance = it.distance.toInt()
                    mStartTime = TimeUtils.stampToTime(it.startTime)
                    LiveEventBus.get<String>(LiveBusConstant.AMap.RECORD_TRACK_START_TIME).post(mStartTime)
                    yadeaNaviSpeedView.updateUI(it)
                    recordTrackSpeed.updateUI(it)
                    tvDistance.text = getStringMToKMNoUnit(it.distance, 0)
                    tvDistanceKm.text = getMToKMUnit(it.distance)

                    tvAvSpeed.text = getStringMToKMNoUnit(getIsInfiniteOrIsNan(it.speed, it.avgSpeed))
                    tvMaxSpeed.text = getStringMToKMNoUnit(getIsInfiniteOrIsNan(it.speed, it.maxSpeed), 0)

                    mMaxSpeed = tvMaxSpeed.text.toString().toFloatOrNull() ?: 0F
                    mAverageSpeed = tvAvSpeed.text.toString().toFloatOrNull() ?: 0F

                    if (it.speed.toInt() >= 45 && !ivMode.isSelected) {
                        pagDashboardBg1.isVisible = true
                        pagDashboardBg1.play()
                        tvTooFast.visibility = View.VISIBLE
                    } else {
                        pagDashboardBg1.isVisible = false
                        pagDashboardBg1.pause()
                        tvTooFast.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setPathPolyline() {
        val resId = if (MMkvHelperUtils.cyclingGoHasPaused) {
            R.mipmap.icon_route_path_dotted_line_green
        } else {
            R.mipmap.icon_route_path_green
        }
        val texture = BitmapDescriptorFactory.fromResource(resId)
        currentPolylineOptions = PolylineOptions().apply {
            lineJoinType(PolylineOptions.LineJoinType.LineJoinBevel)
            width(AutoSizeUtils.dp2px(this@RecordTrackActivity, 20f).toFloat())
            customTexture = texture
            isDottedLine = false
        }
        // 结束当前段落，保存并创建新段
        if (currentPolyline != null) {
            polylineSegments.add(currentPolyline!!)  // 将当前段添加到段落列表
        }

        // 创建新的 Polyline 段落
        currentPolyline = mAMap?.addPolyline(currentPolylineOptions)
    }

    /**
     * 重进界面恢复之前路线；
     */
    private fun firstRecoveryPath() {
        lifecycleScope.launch(Dispatchers.Main) {
            recoveryPathPolyline?.remove() // 在主线程移除
            val list = MMkvHelperUtils.poyLinesList
            val textureIndexList = mutableListOf<Int>()
            val iconList = mutableListOf<BitmapDescriptor>()

            list.forEachIndexed { index, item ->
                val bitmapDescriptor = if (item.hasPausedResources) {
                    bitmapCache["paused"]
                } else {
                    bitmapCache["active"]
                }
                bitmapDescriptor?.let { iconList.add(it) }
                textureIndexList.add(index)
            }

            val path = PolylineOptions().apply {
                width(AutoSizeUtils.dp2px(this@RecordTrackActivity, 20f).toFloat())
                isDottedLine = false
                isUseTexture = true
                addAll(list.map { LatLng(it.lat, it.lon) }.toMutableList())
                customTextureList = iconList
                customTextureIndex = textureIndexList
            }

            recoveryPathPolyline = mAMap?.addPolyline(path)
        }
    }

    private var recoveryPathPolyline: Polyline? = null

    /**
     * 重进界面恢复之前路线；
     */
    private fun recoveryPath() {
        if (!shouldUpdatePath()) return

        lifecycleScope.launch(Dispatchers.Main) {
            recoveryPathPolyline?.remove() // 在主线程移除
            val list = MMkvHelperUtils.poyLinesList
            val textureIndexList = mutableListOf<Int>()
            val iconList = mutableListOf<BitmapDescriptor>()

            list.forEachIndexed { index, item ->
                val bitmapDescriptor = if (item.hasPausedResources) {
                    bitmapCache["paused"]
                } else {
                    bitmapCache["active"]
                }
                bitmapDescriptor?.let { iconList.add(it) }
                textureIndexList.add(index)
            }

            val path = PolylineOptions().apply {
                width(AutoSizeUtils.dp2px(this@RecordTrackActivity, 20f).toFloat())
                isDottedLine = false
                isUseTexture = true
                addAll(list.map { LatLng(it.lat, it.lon) }.toMutableList())
                customTextureList = iconList
                customTextureIndex = textureIndexList
            }

            recoveryPathPolyline = mAMap?.addPolyline(path)
        }
    }

    private var lastUpdateTime = 0L

    private fun shouldUpdatePath(): Boolean {
        val currentTime = System.currentTimeMillis()
        return if (currentTime - lastUpdateTime > 700) { // 每 1 秒更新一次
            lastUpdateTime = currentTime
            true
        } else {
            false
        }
    }

    private fun startService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            requestLocationPermission {
                if (it) {
                    startIntentService()
                }
            }
        } else {
            startIntentService()
        }
    }

    private fun startIntentService() {
        val serviceIntent = Intent(this, RecordTrackService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            try {
                startForegroundService(serviceIntent)
            } catch (e: ForegroundServiceStartNotAllowedException) {
                val pendingIntent = PendingIntent.getService(
                    this, 0, serviceIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
                pendingIntent.send()
            }
        } else {
            startForegroundService(serviceIntent)
        }
    }

    /**
     * 关闭后台定位服务
     */
    private fun stopService() {
        val serviceIntent = Intent(this, RecordTrackService::class.java)
        stopService(serviceIntent)
    }

    /**
     * 开启定位
     */
    private fun startLocation() {
        mLocationClient?.startLocation()
    }

    /**
     * 关闭定位
     */
    private fun stopLocation() {
        mLocationClient?.stopLocation()
    }

    override fun onResume() {
        super.onResume()
        mViewBinding.mapView.onResume()
        if (MMkvHelperUtils.AMapServiceIsRun && MMkvHelperUtils.cyclingCurrentStatus == RECORD_TRACK_START || MMkvHelperUtils.cyclingCurrentStatus == RECORD_TRACK_RESUME) {
            TimerHelper.timerListener(this@RecordTrackActivity)
        }
        if (MMkvHelperUtils.AMapServiceIsRun) {
            mViewBinding.actionButton.setButtonStatus(MMkvHelperUtils.cyclingCurrentStatus, false)
        }
        registerSensor()
    }

    private fun registerSensor() {
        mSensorHelper.registerSensorListener()
        mSensorHelper.setSensorListener(this)
    }

    private fun setGeographicData(latitude: Double, longitude: Double, altitude: Double) {
        mViewBinding?.apply {
            tvEastLongitude.text = DashboardUtils.convertToDMS(longitude, "东经 ", "西经 ")
            tvNorthernLatitude.text = DashboardUtils.convertToDMS(latitude, "北纬 ", "南纬 ")
            if (altitude != 0.0) {
                tvAltitude.isVisible = true
                tvAltitude.text = "海拔${altitude.toInt()}m"
            } else {
                tvAltitude.isVisible = false
            }
        }
    }

    override fun onPause() {
        mViewBinding.mapView?.onPause()
        mSensorHelper.unRegisterSensorListener()
        cachedPauseData()
        super.onPause()
    }

    private fun cachedPauseData() {
        if (MMkvHelperUtils.AMapServiceIsRun && MMkvHelperUtils.cyclingCurrentStatus == RECORD_TRACK_PAUSE) {
            val goPauseData = RecordTrackPauseData()
            goPauseData.apply {
                time = mCacheTime
                distance = "${mViewBinding.tvDistance.text}"
                avgSpeed = "${mViewBinding.tvAvSpeed.text}"
                maxSpeed = "${mViewBinding.tvMaxSpeed.text}"
            }
            MMkvHelperUtils.saveRecordTrackPauseData(goPauseData)
        }
    }

    override fun onDestroy() {
        mAMap?.clear()
        mViewBinding.mapView.onDestroy()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mViewBinding?.mapView?.onSaveInstanceState(outState)
    }

    /**
     * 暂停记录
     */
    fun pauseRecord() {
        if (!MMkvHelperUtils.cyclingGoHasPaused) {
            TimerHelper.stopTimer()
            mLastPauseTime = System.currentTimeMillis() // 记录暂停开始的时间
            MMkvHelperUtils.cyclingGoHasPaused = true
            MMkvHelperUtils.cyclingCurrentStatus = RECORD_TRACK_PAUSE
            // 创建新段，使用暂停状态的纹理
            mViewBinding.recordTrackSpeed.setSpeedTxt(true)
            mViewBinding.yadeaNaviSpeedView.setSpeedTxt(true)
        }
    }

    /**
     * 继续记录
     */
    fun resumeRecord() {
        if (MMkvHelperUtils.cyclingGoHasPaused) {
            startTimer(mCacheTime)
            MMkvHelperUtils.cyclingGoHasPaused = false
            MMkvHelperUtils.cyclingCurrentStatus = RECORD_TRACK_RESUME
            mTotalPausedTime += System.currentTimeMillis() - mLastPauseTime
            // 创建新段，使用暂停状态的纹理
            setPathPolyline()
            val speed = getStringMToKMNoUnit(getIsInfiniteOrIsNan(currentSpeed, currentSpeed), 0)
            mViewBinding.yadeaNaviSpeedView.setSpeedTxt(false, speed.toFloat())
            mViewBinding.recordTrackSpeed.setSpeedTxt(false, speed.toFloat())
            mCacheTime = 0
            MMkvHelperUtils.saveRecordTrackPauseData(RecordTrackPauseData())
        }
    }

    override fun timerListener(timer: Long) {
        val startTime = MMkvHelperUtils.timestampStartTime
        // 计算经过的总时间，减去暂停的总时间
        val conversionTime = conversionTime(startTime, mTotalPausedTime, System.currentTimeMillis())
        mLastPauseTime = 0L
        mViewBinding.tvTime.text = TimeUtils.timeConversion(conversionTime)
        mCacheTime = conversionTime
        mElapsedTime = conversionTime
        mViewBinding.yadeaNaviSpeedView.setTimer(conversionTime)
        mViewBinding.yadeaNaviSpeedView.postInvalidate()
        LiveEventBus.get<Int>(LiveBusConstant.AMap.RECORD_TRACK_GO_TIMER).post(conversionTime)
    }

    private fun clearData() {
        mStartTime = ""
        mEndTime = ""
        mElapsedTime = 0
        mDistance = 0
        mMaxSpeed = 0f
        mAverageSpeed = 0f
    }

    /**
     * 获得仪表盘数据
     */
//    private fun getDashBoardData(): DashBoardBean {
//        return DashBoardBean(mStartTime, mEndTime, mElapsedTime, mDistance, mMaxSpeed, mAverageSpeed, 0, 0, 0)
//    }

    /**
     * 1：定位自己
     * 2：自动开启
     * 3：记录轨迹
     */
    private fun requestPermission(type: Int, callBack: ((Boolean) -> Unit)? = null) {
        requestLocationPermission {
            callBack?.invoke(it)
            if (it) {
                requestPermissionSuccess(type)
            } else {
                Toasty.error("为了保证了仪表盘的正常使用，请将定位权限设为始终允许")
            }
        }
    }

    private fun requestPermissionSuccess(type: Int) {
        when (type) {
            1 -> startLocation()
            2 -> autoStartCyclingGo()
            3 -> { //手动开启
                MMkvHelperUtils.AutoStartRecordTrack = 2
                startCyclingGo()
            }

            else -> {}
        }
        mIsSlideMap = false
    }

    private fun autoStartCyclingGo() {
        mStartCyclingGo = intent.getBooleanExtra("start", false)
        if (mStartCyclingGo) {
            lifecycleScope.launch {
                delay(100)
                startCyclingGo()
                mViewBinding.actionButton.autoStartRecord()
                MMkvHelperUtils.cyclingGoHasPaused = false
                MMkvHelperUtils.cyclingCurrentStatus = RECORD_TRACK_START
                MMkvHelperUtils.AutoStartRecordTrack = 0
            }
        }
    }

    override fun sensorCallBack(azimuth: Float) {
        runOnUiThread {
            mViewBinding.tvOrientationAngle.text = DashboardUtils.getBearingAngle(azimuth.toInt())
            mViewBinding.recordTrackSpeed.updateDegree(azimuth)
        }
    }
}