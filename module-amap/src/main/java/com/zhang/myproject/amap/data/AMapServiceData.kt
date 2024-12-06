package com.zhang.myproject.amap.data

import com.amap.api.maps.model.LatLng

/**
 * Date: 2024/12/2
 * Author : Zhang
 * Description :
 */
data class AMapServiceData(
    /**
     * 开始时间
     */
    var startTime: Long = 0,

    /**
     * 结束时间
     */
    var endTime: Long = 0,

    /**
     * 纬度
     */
    var latitude: Double = 0.0,

    /**
     * 经度
     */
    var longitude: Double = 0.0,

    /**
     * 速度
     */
    var speed: Float = 0f,

    /**
     * 时间
     */
    var time: Int = 0,

    /**
     * 距离
     */
    var distance: Float = 0f,

    /**
     * 角度
     */
    var bearing: Float = 0f,

    /**
     * 最大速度
     */
    var maxSpeed: Float = 0f,

    /**
     * 平均速速
     */
    var avgSpeed: Float = 0f,

    /**
     * 剩余里程 单位 米
     */
    var remainingMileage: Float = 0f,

    /**
     * 所用时间 s
     */
    var takeTime: Int = 0,

    /**
     * 到达时间
     */
    var timeOfArrival: Int = 0,

    var iconType: Int = 0,

    var nextRouteName: String = "",

    var curStepRetainDistance: Int = 0,

    /**
     * 记录坐标点集合
     */
    val recordLatLngList: MutableList<LatLng> = mutableListOf(),

    /**
     * 需要上报的点
     */
    val updateLatLngList: MutableList<LatLngBean> = mutableListOf(),
)

data class LatLngBean(
    var trackStatus: Int? = null,
    var trId: String? = null,
    var longitude: Double? = null,
    var latitude: Double? = null,
    var sort: Long? = null,
    var speed: Int? = null,
)
