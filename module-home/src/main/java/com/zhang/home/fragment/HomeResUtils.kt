package com.zhang.home.fragment

import android.annotation.SuppressLint
import android.widget.TextView
import com.zhang.home.fragment.data.AccessorInfoData
import com.zhang.home.fragment.data.CityWeatherData
import com.zhang.home.fragment.data.IPData

/**
 * Date: 2024/1/25
 * Author : Zhang
 * Description :
 */
@SuppressLint("SetTextI18n")
fun TextView?.initHomeShowData(data: IPData, cityData: CityWeatherData, accessorInfo: AccessorInfoData) {
    this?.let {
        it.text =
            "访问者信息：\n" +
                    "访问Time：${accessorInfo.time}    ${accessorInfo.week}\n" +
                    "IP: ${data.ip}\t\t地址：${data.province}-${data.city}\n" +
                    (accessorInfo.system.takeIf { system -> !system.contains("undefined undefined") }
                        ?.let { "系统：${accessorInfo.system}\n" } ?: run { "" }) +
                    "${if (data.initIsp()) "运营商：${data.isp}" else "暂未获取到正确运营商，请确保已经插卡"}\n\n" +
                    "今日天气情况：\n" +
                    "最低温：${accessorInfo.low}   最高温：${accessorInfo.high}\n" +
                    "当前温度：${cityData.temp}    湿度：${cityData.humidity}\n" +
                    "风向风力：${accessorInfo.fl} \n" +
                    "天气更新时间：${cityData.reportTime}\n\n" +
                    "温馨提示：${accessorInfo.tip}"
    }
}

private fun IPData?.initIsp() =
    this?.isp?.contains("移动") == true || this?.isp?.contains("联通") == true || this?.isp?.contains("电信") == true
