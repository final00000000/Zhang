package com.zhang.myproject

import android.app.Application
import com.amap.api.location.AMapLocationClient
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

var mApplication: Application? = null

/**
 * 初始化Mkv
 */
fun initMMKV() {
    MMKV.initialize(mApplication)
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
fun initActivityManager() {
    mApplication?.let { ActivityManager.instance.init(it) }
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

fun initAMap() {
    AMapLocationClient.updatePrivacyShow(mApplication, true, true)
    AMapLocationClient.updatePrivacyAgree(mApplication, true)
}

fun initToasty() {
    mApplication?.let { Toasty.init(it) }
}