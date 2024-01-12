@file:Suppress("UNCHECKED_CAST", "MemberVisibilityCanBePrivate")

package com.zhang.myproject.base.net

import android.util.Log
import com.drake.net.convert.JSONConvert
import com.drake.net.request.MediaConst.JSON
import com.google.gson.GsonBuilder
import okhttp3.Response
import org.json.JSONObject
import java.lang.reflect.Type


class GsonConverter : JSONConvert(code = "code", message = "msg") {
    companion object {
        private val gson = GsonBuilder().serializeNulls().create()
    }

    override fun <R> onConvert(succeed: Type, response: Response): R? {
        Log.e("测试","测试_19：$succeed-------$response");
        return super.onConvert(succeed, response)
    }

    override fun <R> String.parseBody(succeed: Type): R? {
        val string = try {
            JSONObject(this).getString("data")
        } catch (e: Exception) {
            this
        }
        return gson.fromJson<R>(string, succeed)
    }
}