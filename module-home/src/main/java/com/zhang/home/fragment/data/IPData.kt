package com.zhang.home.fragment.data

/**
 * Date: 2024/1/12
 * Author : Zhang
 * Description :
 */
data class IPData(
    var ip: String? = "", // 访问者的ip地址
    var province: String? = "", // 省份
    var code: String? = "", // 省份id
    var city: String? = "", // 城市
    var cityId: String? = "", // 	城市id
    var isp: String? = "", //网络服务商名称 例如 电信
    var desc: String? = "", //拼接好的描述信息
)
