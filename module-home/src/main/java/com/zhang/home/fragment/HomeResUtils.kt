package com.zhang.home.fragment

import android.annotation.SuppressLint
import android.widget.TextView
import com.zhang.home.fragment.data.CityWeatherData
import com.zhang.home.fragment.data.IPData

/**
 * Date: 2024/1/25
 * Author : Zhang
 * Description :
 */
@SuppressLint("SetTextI18n")
fun TextView?.initHomeShowData(data: IPData, cityData: CityWeatherData) {
    this?.let {
        it.text =
            "访问者信息：\nIP: ${data.ip}\n地址：${data.province} ${data.city}\n${if (data.initIsp()) "运营商：${data.isp}" else "暂未获取到正确运营商，请确保已经插卡"}\n\n" +
                    "今日气温：${cityData.temp}\n湿度：${cityData.humidity}\n风力风向：${cityData.windPower}风  ${cityData.windDirection}风 \n更新时间：${cityData.reportTime}"
    }
}

private fun IPData?.initIsp() =
    this?.isp?.contains("移动") == true || this?.isp?.contains("联通") == true || this?.isp?.contains("电信") == true
