package com.zhang.myproject.base.helper

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tencent.mmkv.MMKV
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * 应用模块: storage
 *
 *
 * 类描述: 腾讯MMKV序列化存储工具类
 *
 *
 */
object MMkvHelper {

    private var mMMkv: MMKV = MMKV.defaultMMKV()

    fun getMMkv() = mMMkv

    /**
     * 存入map集合
     *
     * @param key 标识
     * @param map 数据集合
     */
    fun saveInfo(key: String?, map: Map<String?, Any?>?) {
        val gson = Gson()
        val mJsonArray = JSONArray()
        var mJsonObject: JSONObject? = null
        try {
            mJsonObject = JSONObject(gson.toJson(map))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        mJsonArray.put(mJsonObject)
        mMMkv.encode(key, mJsonArray.toString())

    }

    /**
     * 获取map集合
     *
     * @param key 标识
     */
    fun getInfo(key: String?): Map<String, String> {
        val itemMap: MutableMap<String, String> = HashMap()
        val result = mMMkv.decodeString(key, "")
        try {
            val array = JSONArray(result)
            for (i in 0 until array.length()) {
                val itemObject = array.getJSONObject(i)
                val names = itemObject.names()
                if (names != null) {
                    for (j in 0 until names.length()) {
                        val name = names.getString(j)
                        val value = itemObject.getString(name)
                        itemMap[name] = value
                    }
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return itemMap
    }

    /**
     * 扩展MMKV类使其支持对象存储
     */
    fun <T> getObject(key: String?, t: Class<T>?): T? {
        val str = mMMkv.decodeString(key, null)
        return if (!TextUtils.isEmpty(str)) {
            GsonBuilder().create().fromJson(str, t)
        } else {
            null
        }
    }

    fun putObject(key: String?, any: Any?) {
        val jsonString = GsonBuilder().create().toJson(any)
        mMMkv.encode(key, jsonString)
    }

    fun removeObject(key: String?) {
        mMMkv.remove(key)
    }

    fun clearAll() {
        mMMkv.clearAll()
    }

}