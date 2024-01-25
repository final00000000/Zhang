package com.zhang.home.fragment.data

/**
 * Date: 2024/1/25
 * Author : Zhang
 * Description :
 */
data class CityWeatherData(
    var address: String, // 城市具体信息，比如 “广东省 深圳市”
    var cityCode: String,//  城市code
    var humidity: String, // 温度值
    var reportTime: String, //此次天气发布时间
    var temp: String, //	温度值
    var weather: String, //	天气描述，具体描述请查看附件，天气描述清单
    var windDirection: String, // 风向描述，具体描述请查看附件，风向表清单
    var windPower: String //	风力描述，具体描述请查看附件，风力表清单
)