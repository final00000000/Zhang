package com.zhang.myproject

import android.app.Application
import com.amap.api.location.AMapLocationClient
import com.drake.logcat.LogCat
import com.tencent.mmkv.BuildConfig
import com.tencent.mmkv.MMKV
import com.zhang.myproject.base.manager.ActivityManager
import com.zhang.myproject.base.utils.toast.Toasty
import timber.log.Timber

/**
 * Date: 2023/10/10
 * Author : Zhang
 * Description :
 */

/**
 * 初始化Mkv
 */
fun Application.initMMKV() {
    MMKV.initialize(this)
}

/**
 * 初始化打印日志框架
 */
fun initTimber() {
    if (BuildConfig.DEBUG) {
        Timber.plant(Timber.DebugTree())
    }
}

/**
 * 初始化Activity生命周期监听
 */
fun Application.initActivityManager() {
    ActivityManager.instance.init(this)
}

/**
 * 初始化toasty
 */
fun Application.initToasty() {
    Toasty.init(this)
}

/**
 * 初始化刷新头
 */
fun initRefreshStyle() {
    /*  SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
          MaterialHeader(context)
      }

      SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
          ClassicsFooter(context)
      }*/
}

/**
 * 同意隐私政策后初始化
 */
fun Application.init() {
    initAMap()
}

private fun Application.initAMap() {
    AMapLocationClient.updatePrivacyShow(this, true, true)
    AMapLocationClient.updatePrivacyAgree(this, true)
}

fun initLogcat(){
    LogCat.setDebug(BuildConfig.DEBUG) // 全局开关
}