package com.zhang.home.fragment.data

/**
 * Date: 2024/1/12
 * Author : Zhang
 * Description :
 */
@kotlinx.serialization.Serializable
data class ToDayData(
    var code: Int,
    var msg: String,
    var data: MutableList<ToDayData1>,
) {
    data class ToDayData1(

        var picUrl: String? = "", // 历史事件所对应的图片，可能为空
        var title: String? = "", // 历史事件的名称
        var year: String? = "", // 该历史事件发生所对应的年份
        var month: Int? = 1, // 该历史事件发生所对应的月份
        var day: Int? = 1, // 该历史事件发生所对应的日期
        var details: String? = "", //历史事件的详细介绍，如果type=1，则此字段有返回值，否则不返回

    )
}
