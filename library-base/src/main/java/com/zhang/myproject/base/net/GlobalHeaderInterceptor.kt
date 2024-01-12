package com.zhang.myproject.base.net

import android.util.Log
import com.drake.net.Net
import com.drake.net.exception.ResponseException
import com.drake.net.interceptor.RequestInterceptor
import com.drake.net.request.BaseRequest
import com.zhang.myproject.base.RollApiConstant
import okhttp3.Interceptor
import okhttp3.Response


/** 演示添加全局请求头/参数 */
class GlobalHeaderInterceptor : RequestInterceptor {

    /** 本方法每次请求发起都会调用, 这里添加的参数可以是动态参数 */
    override fun interceptor(request: BaseRequest) {
        request.addQuery("app_id", RollApiConstant.ROLL_APP_ID)
        request.addQuery("app_secret", RollApiConstant.ROLL_APP_SECRET)
    }
}
class NetInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        Log.e("测试","测试_25：${request.body}---${response.body}");
        return response
    }
}