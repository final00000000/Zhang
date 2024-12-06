package com.zhang.myproject.amap.view.service

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMapUtils
import com.amap.api.maps.model.LatLng
import com.amap.api.trace.TraceLocation
import com.drake.logcat.LogCat
import com.jeremyliao.liveeventbus.LiveEventBus
import com.zhang.myproject.amap.R
import com.zhang.myproject.amap.data.AMapServiceData
import com.zhang.myproject.amap.data.LatLngBean
import com.zhang.myproject.amap.view.activity.RecordTrackActivity
import com.zhang.myproject.base.helper.MMkvHelperUtils
import com.zhang.myproject.common.constant.LiveBusConstant
import com.zhang.myproject.common.ktx.createCyclingGoLocationOption
import com.zhang.myproject.helper.PathSmoothTool
import com.zhang.myproject.resource.data.RecordTrackPoyLinesData
import kotlin.math.abs


class RecordTrackService : Service(), AMapLocationListener {

    private var mLocationClient: AMapLocationClient? = null

    /**
     * 路径去偏移
     */
    private val mPathSmoothTool = PathSmoothTool()

    /**
     * 当前记录点
     */
    private var mCurrentCount = 0

    /**
     * 当前距离
     */
    private var mCurrentDistance = 0f

    /**
     * 最大车速
     */
    private var mMaxSpeed: Float = 0f

    /**
     * 平均速速
     */
    private var mAverageSpeed: Float = 0f

    private var mTimeCount = 0


    /**
     * 是否正在定位中
     */
    private var mIsBeingLocation = false

    /**
     * 服务类
     */
    private var mMapServiceData: AMapServiceData? = null

    private lateinit var mNotificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        mMapServiceData = AMapServiceData()
        mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        MMkvHelperUtils.AMapServiceIsRun = true
        mMapServiceData?.startTime = System.currentTimeMillis()
        initLocation()
        createNotice()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    override fun onDestroy() {
        MMkvHelperUtils.AMapServiceIsRun = false
        stopLocation()
        mTimeCount = 0
        mMapServiceData?.recordLatLngList?.clear()
        LiveEventBus.get<Boolean>("exit_lock").post(true)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    /**
     * 初始化定位
     */
    private fun initLocation() {
        try {
            mLocationClient = AMapLocationClient(application)
        } catch (e: Exception) {
            LogCat.e("initLocation: ${e.message}")
        }
        mLocationClient?.setLocationOption(createCyclingGoLocationOption())
        mLocationClient?.setLocationListener(this)
        startLocation()
    }

    /**
     * 开始定位
     */
    private fun startLocation() {
        mLocationClient?.run {
            if (!mIsBeingLocation) {
                mIsBeingLocation = true
                startLocation()
            }
        }
    }

    /**
     * 结束定位
     */
    private fun stopLocation() {
        mLocationClient?.run {
            mIsBeingLocation = false
            mMapServiceData?.endTime = System.currentTimeMillis()
            stopLocation()
        }
    }


    @SuppressLint("WrongConstant", "UnspecifiedImmutableFlag")
    private fun createNotice() {
        // 唯一的通知通道的id.
        val notificationChannelId = "200"
        //用户可见的通道名称
        val channelName = "雅迪智行定位服务"
        //通道的重要程度
        val importance = NotificationManager.IMPORTANCE_HIGH

        val notificationChannel = NotificationChannel(notificationChannelId, channelName, importance)
        notificationChannel.lightColor = Color.BLUE
        notificationChannel.importance = NotificationManager.IMPORTANCE_HIGH
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(notificationChannel)

        val activityIntent = Intent(this, RecordTrackActivity::class.java)
        val activityOptions = ActivityOptions.makeBasic().apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                pendingIntentBackgroundActivityStartMode =
                    ActivityOptions.MODE_BACKGROUND_ACTIVITY_START_ALLOWED
            }
        }
//        val pendingIntent = PendingIntent.getActivity(this, 0, myIntent, PendingIntent.FLAG_IMMUTABLE)
        val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= 31) {
            PendingIntent.getActivity(
                this,
                1,
                activityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                activityOptions.toBundle()
            )
        } else {
            PendingIntent.getActivity(this, 1, activityIntent, 0, activityOptions.toBundle())
        }
        // 创建通知
        val notification = Notification.Builder(applicationContext, notificationChannelId)
            .setOngoing(true)
            .setSmallIcon(R.drawable.icon_default_image)
            .setContentText("仪表盘轨迹正在记录中")
            .setWhen(System.currentTimeMillis())
            .setContentIntent(pendingIntent)
            .build()
        if (Build.VERSION.SDK_INT >= 34) {
            startForeground(
                201,
                notification, FOREGROUND_SERVICE_TYPE_LOCATION
            )
        } else {
            startForeground(
                201,
                notification
            )
        }
    }

    @Override
    override fun onLocationChanged(aMapLocation: AMapLocation?) {
        aMapLocation?.run {
            if (errorCode == 0 && accuracy <= 3000) {
                LatLng(latitude, longitude).run {
                    val paused = MMkvHelperUtils.cyclingGoHasPaused
                    val traceLocation = TraceLocation()
                    traceLocation.latitude = latitude
                    traceLocation.longitude = longitude
                    mMapServiceData?.latitude = latitude
                    mMapServiceData?.longitude = longitude

                    MMkvHelperUtils.poyLinesList.add(RecordTrackPoyLinesData(latitude, longitude, paused))

                    if (!paused) {
                        traceLocation.bearing = bearing
                        traceLocation.speed = speed
                        traceLocation.time = time
                        mMapServiceData?.time = mTimeCount++
                        mMapServiceData?.bearing = bearing
                    }

                    //判断当前点是否是有效点（根据点的移动速度）
                    val filterResult = mPathSmoothTool.filterPos(traceLocation, paused)
                    if (filterResult.first) {
                        //计算速度
                        if (!paused) {
                            mMapServiceData?.speed = (speed * 3.6).toFloat()
                            //计算距离（前一个点减当前点的距离）
                            if (mPathSmoothTool.mListPoint.size > 1) {
                                val lastTraceLocation = mPathSmoothTool.mListPoint[mPathSmoothTool.mListPoint.size - 2]
                                val lastPoint = LatLng(lastTraceLocation.latitude, lastTraceLocation.longitude)
                                val distance = AMapUtils.calculateLineDistance(lastPoint, this)
                                mCurrentDistance += distance
                            }
                            //记录点的坐标（用来绘制到地图上的路径）
                            mMapServiceData?.recordLatLngList?.add(LatLng(latitude, longitude))
                        }
                        //记录要上报的点的坐标（用来上报到服务器）
                        mCurrentCount++
                        if (mCurrentCount == 5) {
                            mCurrentCount = 0
                            val latLngBean = LatLngBean()
                            latLngBean.trId = ""
                            latLngBean.trackStatus = if (paused) 1 else 0
                            latLngBean.latitude = latitude
                            latLngBean.longitude = longitude
                            latLngBean.sort = mMapServiceData?.updateLatLngList?.size?.toLong()
                            latLngBean.speed = abs((speed * 3.6).toInt())
                            mMapServiceData?.updateLatLngList?.add(latLngBean)
                        }
                    } else {
                        if (!paused) {
                            //如果速度超限，不把速度重置为0
                            val d = speed * 3.6
                            if (filterResult.second != 1 && d < 1) {
                                mMapServiceData?.speed = 0f
                            } else {
                                mMapServiceData?.speed = (speed * 3.6).toFloat()
                            }
                            if (mPathSmoothTool.mListPoint.size > 0) {
                                val lastTraceLocation = mPathSmoothTool.mListPoint[mPathSmoothTool.mListPoint.size - 1]
                                mMapServiceData?.latitude = lastTraceLocation.latitude
                                mMapServiceData?.longitude = lastTraceLocation.longitude
                            }
                        }
                    }

                    if (!paused) {
                        //计算最大速速
                        if ((mMapServiceData?.speed ?: 0F) > mMaxSpeed) {
                            mMaxSpeed = mMapServiceData?.speed ?: 0F
                        }

                        //计算平均速度
                        mAverageSpeed = if (mCurrentDistance == 0f) {
                            0f
                        } else {
                            (mCurrentDistance / ((mMapServiceData?.time ?: 0) / 3.6)).toFloat()
                        }
                        mMapServiceData?.distance = mCurrentDistance
                        mMapServiceData?.maxSpeed = mMaxSpeed
                        mMapServiceData?.avgSpeed = mAverageSpeed
                    }
                    LiveEventBus.get<AMapServiceData>(LiveBusConstant.AMap.SEND_RECORD_TRACK_SERVICE_DATA).post(mMapServiceData)
                }
            } else {
                LogCat.e("location Error, ErrCode:$errorCode, errInfo:$errorInfo")
            }
        }
    }

}