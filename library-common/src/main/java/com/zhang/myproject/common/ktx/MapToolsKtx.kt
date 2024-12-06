package com.zhang.myproject.common.ktx

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.transformations
import coil3.transform.CircleCropTransformation
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.maps.AMap
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.MyLocationStyle
import com.makeramen.roundedimageview.RoundedImageView
import com.zhang.myproject.base.utils.SystemModelUtils
import com.zhang.myproject.base.utils.dpToPx
import com.zhang.myproject.base.utils.find
import com.zhang.myproject.common.R
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Date: 2024/11/29
 * Author : Zhang
 * Description :
 */


/**
 * View进入动画
 */
fun View.fadeIn() {
    if (visibility == View.VISIBLE) return
    val animationSet = AnimationSet(false)
    val alphaAnimation: Animation = AlphaAnimation(0f, 1f)
    val translateAnimation: Animation = TranslateAnimation(
        Animation.RELATIVE_TO_SELF,
        0f,
        Animation.RELATIVE_TO_SELF,
        0f,
        Animation.RELATIVE_TO_SELF,
        1f,
        Animation.RELATIVE_TO_SELF,
        0f
    )
    animationSet.duration = 500
    animationSet.addAnimation(alphaAnimation)
    animationSet.addAnimation(translateAnimation)
    animationSet.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) {
        }

        override fun onAnimationEnd(animation: Animation?) {
            visibility = View.VISIBLE
        }

        override fun onAnimationRepeat(animation: Animation?) {
        }
    })
    startAnimation(animationSet)
}

/**
 * View离开动画
 */
fun View.fadeOut() {
    if (visibility != View.VISIBLE) return
    val animationSet = AnimationSet(true)
    val alphaAnimation: Animation = AlphaAnimation(1f, 0f)
    val translateAnimation: Animation = TranslateAnimation(
        Animation.RELATIVE_TO_SELF,
        0f,
        Animation.RELATIVE_TO_SELF,
        0f,
        Animation.RELATIVE_TO_SELF,
        0f,
        Animation.RELATIVE_TO_SELF,
        1f
    )
    animationSet.duration = 500
    animationSet.addAnimation(alphaAnimation)
    animationSet.addAnimation(translateAnimation)
    startAnimation(animationSet)
    visibility = View.GONE
}

/**
 * 设置地图是否是夜间模式
 */
fun AMap.setMapSkinModel() {
    mapType = if (!SystemModelUtils.isSystemHighModel()) {
        AMap.MAP_TYPE_NIGHT
    } else {
        AMap.MAP_TYPE_NORMAL
    }
}

fun Context.setMapAvatar(map: AMap?) {
    this.let {
        val imageLoader = ImageLoader.Builder(it)
            .crossfade(true) // 启用淡入淡出效果
            .build()

        val request = ImageRequest
            .Builder(it)
            .data(getDrawableRes(R.mipmap.icon_default_avatar)) // 加载资源
            .size(dpToPx(24f)) // 设置目标尺寸
            .transformations(CircleCropTransformation()) // 圆形裁剪
            .target(
                onSuccess = {
                    map?.myLocationStyle = getMyLocationStyle()
                    map?.isMyLocationEnabled = true
                },
                onError = {
                    // 可选：处理加载失败的占位图
                }
            )
            .build()
        imageLoader.enqueue(request)

    }
}

fun Context.getMyLocationStyle(): MyLocationStyle {
    return MyLocationStyle().apply {
        showMyLocation(true)
        // 圆圈的边框颜色
        strokeColor(Color.TRANSPARENT)
        // 圆圈的填充颜色
        radiusFillColor(Color.TRANSPARENT)
        // 圆圈的边框宽度
        strokeWidth(0f)
        // 定位时间
        interval(1000)
        // 锚点
        anchor(0.5f, 0.5f)
        myLocationIcon(BitmapDescriptorFactory.fromView(getLocationMarkerView()))
        // 蓝点
        /**
         * 蓝点模式
         * myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);//只定位一次。
         * myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;//定位一次，且将视角移动到地图中心点。
         * myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW) ;//连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位）
         * myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE);//连续定位、且将视角移动到地图中心点，地图依照设备方向旋转，定位点会跟随设备移动。（1秒1次定位）
         * myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
         * //以下三种模式从5.1.0版本开始提供
         * myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
         * myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动。
         * myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，地图依照设备方向旋转，并且蓝点会跟随设备移动。
         *
         * */
        myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE)
    }
}

fun Context.getLocationMarkerView(): View {
    val view = View.inflate(this, R.layout.layout_map_marker_view, null)
    val headerView = view.find<RoundedImageView>(R.id.iv_head)
    headerView.run {
        this.setImageDrawable((getDrawableRes(R.mipmap.icon_default_avatar)))
    }
    return view
}

/**
 * 显示仪表盘骑行结束弹窗
 * @receiver Context
 * @param type
 * 1：正常结束里程超过100m超过30秒  2：未超过100米  3：上传轨迹失败，重新保存 4：切车弹窗 5：导航界面中途退出
 */
fun Context.showDashBoardFinishDialog(type: Int, action: (Boolean) -> Unit) {
    var backRes = 0
    var title = ""
    var content = ""
    var left = ""
    var right = ""
    var resId = 0
    when (type) {
        1 -> {
            backRes = R.drawable.shape_dialog_bg_light_blue_12
            title = getStringRes(R.string.end_of_ride1)
            content = getStringRes(R.string.check_track_over)
            left = getStringRes(R.string.cancel)
            right = getStringRes(R.string.main_go_look)
            resId = R.mipmap.icon_end_of_ride
        }

        2 -> {
            backRes = R.drawable.shape_dialog_bg_edf5fc_white_12
            title = getStringRes(R.string.end_of_ride)
            content = getStringRes(R.string.not_over_distance)
            right = getStringRes(R.string.home_dialog_i_know)
            resId = R.mipmap.icon_abnormal_interruption_of_ride
        }

        3 -> {
            backRes = R.drawable.shape_dialog_bg_edf5fc_white_12
            title = getStringRes(R.string.track_saving_failure)
            content = getStringRes(R.string.resave_track)
            left = getStringRes(R.string.cancel)
            right = getStringRes(R.string.resave)
            resId = R.mipmap.icon_abnormal_interruption_of_ride
        }

        4 -> {
            backRes = R.drawable.shape_dialog_bg_edf5fc_white_12
            title = getStringRes(R.string.is_end_cycling_go)
            content = "车辆【哈哈哈】${getStringRes(R.string.beingUsed_dashboard_cycling_go)}"
            left = getStringRes(R.string.cancel)
            right = getStringRes(R.string.confirm_switch)
            resId = R.mipmap.icon_change_vehicle
        }

        5 -> {
            backRes = R.drawable.shape_dialog_bg_light_blue_12
            title = getStringRes(R.string.whether_to_end_this_navigation)
            content = getStringRes(R.string.whether_to_end_this_navigation_tips)
            left = getStringRes(R.string.cancel)
            right = getStringRes(R.string.confirm_over)
            resId = R.mipmap.icon_end_over_navigation
        }
    }

    ShowDashboardPop(
        this,
        backgroundRes = backRes,
        title = title,
        content = content,
        resId = resId,
        titleSize = 18,
        contentSize = 14,
        leftStr = left,
        rightStr = right,
        isCancel = false,
        btnLeft = {
            action.invoke(false)
        },
        btnRight = {
            action.invoke(true)
        }).showPopupWindow()
}

/**
 * 骑行GO定位设置
 */
fun createCyclingGoLocationOption(): AMapLocationClientOption {
    return AMapLocationClientOption().apply {
        /**
         * 设置签到场景，相当于设置为：
         * option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
         * option.setOnceLocation(false);
         * option.setOnceLocationLatest(false);
         * option.setMockEnable(false);
         * option.setWifiScan(true);
         *
         * 其他属性均为模式属性。
         * 如果要改变其中的属性，请在在设置定位场景之后进行
         */
        /*高精度定位*/
        locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        /*定位场景
        * SignIn签到场景 只进行一次定位返回最接近真实位置的定位结果（定位速度可能会延迟1-3s）
        * Sport 运动场景 高精度连续定位，适用于有户内外切换的场景，卫星定位和网络定位相互切换，卫星定位成功之后网络定位不再返回，卫星信号断开之后一段时间才会返回网络结果
        * Transport 出行场景 高精度连续定位，适用于有户内外切换的场景，卫星定位和网络定位相互切换，卫星定位成功之后网络定位不再返回，卫星信号断开之后一段时间才会返回网络结果
        * */
        locationPurpose = AMapLocationClientOption.AMapLocationPurpose.Transport
        /*定位间隔*/
        interval = 1000
        /*传感器*/
        isSensorEnable = true
        /*拉起高德服务*/
        isSelfStartServiceEnable = true
        isOnceLocation = false
    }
}

/**
 * 米 转换 千米
 * @param allLength Int
 */
fun getMToKMUnit(allLength: Float): String {
    return if (allLength > 1000) {
        "km"
    } else {
        "m"
    }
}

/**
 * 米 转换 千米
 * @param allLength Int
 */
fun getStringMToKMNoUnit(allLength: Float, digit: Int = 1): String {
    return if (allLength >= 1000) {
        stringFormatIsContainsZero(allLength)
    } else {
        formatNumDownRemoveZero(allLength.toString(), digit)
    }
}

/**
 * string格式化 是否包含0
 */
fun stringFormatIsContainsZero(allLength: Float): String {
    val number = String.format("%.1f", allLength / 1000)
    return if (number.contains(".0") || number.contains(".00")) {
        number.split(".0")[0]
    } else {
        BigDecimal(number).setScale(1, RoundingMode.DOWN).stripTrailingZeros().toString()
    }
}

/**
 * https://blog.csdn.net/qq_36414608/article/details/131203413
 * setScale(1,BigDecimal.ROUND_DOWN)直接删除多余的小数位，如2.35会变成2.3
 * 去除零 只能保留一位 两位会出现数据不全问题
 */
fun formatNumDownRemoveZero(str: String, digit: Int = 1): String {
    val format = BigDecimal(str).setScale(digit, RoundingMode.DOWN).toString()
    return if (format.contains(".0") || format.contains(".00")) format.split(".0")[0] else format
}

fun getIsInfiniteOrIsNan(speed: Float, num: Float): Float =
    if (speed.isInfinite() || num.isNaN()) 0f else num

fun conversionTime(startTimeLong: Long, totalPausedTime: Long, endTimeLong: Long): Int {
    return try {
        // 计算时间差（以毫秒为单位）
        val timeDifferenceInMillis = kotlin.math.abs(endTimeLong - startTimeLong - totalPausedTime)
        // 将时间差转换为秒
        (timeDifferenceInMillis / 1000).toInt()
    } catch (e: Exception) {
        0
    }
}
