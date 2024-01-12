package com.zhang.myproject.base.data

/**
 * Date: 2024/1/12
 * Author : Zhang
 * Description :
 */
open class BaseRollNetData<T>(
    var code: Int = 1,
    var msg: String = "",
    var data: T,
)
