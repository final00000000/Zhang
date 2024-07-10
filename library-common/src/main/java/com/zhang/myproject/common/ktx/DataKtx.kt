package com.zhang.myproject.common.ktx

/**
 * 获取非空值的内容
 */
fun String?.toStringNoNull(default: String = ""): String {
    return if ("null" == this || this.isNullOrEmpty()) {
        default
    } else {
        this
    }
}