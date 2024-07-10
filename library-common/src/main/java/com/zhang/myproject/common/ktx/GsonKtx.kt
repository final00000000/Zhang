package com.zhang.myproject.common.ktx

import com.google.gson.Gson

/**
 * Date: 2024/7/10
 * Author : Zhang
 * Description : gson转换工具类
 */
private val mGson = Gson()

/**
 * @param jsonString json字符串
 * @param cls        需要转化的类型
 * @param <T>        需要转化的类型
 * @return 返回实体对象
 * @fun 根据不同类型进行json到实体间的转化
</T> */
fun <T> jsonToModule(jsonString: String?, cls: Class<T>?): T {
    return mGson.fromJson(jsonString, cls)
}

/**
 * @param cls 需要转化的类型
 * @param <T> 需要转化的类型
 * @return 返回Json字符串
 * @fun 根据不同类型进行实体到json间的转化
</T> */
fun <T> moduleToJson(cls: T): String {
    return mGson.toJson(cls)
}
