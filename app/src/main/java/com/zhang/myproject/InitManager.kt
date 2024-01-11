package com.zhang.myproject

import android.app.Application
import com.amap.api.location.AMapLocationClient
import com.drake.logcat.LogCat
import com.kongzue.dialogx.DialogX
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
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
fun Application?.initMMKV() {
    this?.let {
        MMKV.initialize(it)
    }
}

/**
 * 初始化打印日志框架
 */
fun Application?.initTimber() {
    this?.let {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}

/**
 * 初始化Activity生命周期监听
 */
fun Application?.initActivityManager() {
    this?.let {
        ActivityManager.instance.init(it)
    }
}

/**
 * 初始化toasty
 */
fun Application?.initToasty() {
    this?.let {
        Toasty.init(it)
    }
}

/**
 * 初始化刷新头
 */
fun Application?.initRefreshStyle() {
    this?.let {
        //全局设置默认的 Header
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.apply {
                autoRefresh()//自动刷新
                setEnableHeaderTranslationContent(true) //是否下拉Header的时候向下平移列表或者内容
                setEnableLoadMoreWhenContentNotFull(false)//是否在列表不满一页时候开启上拉加载功能
            }
            ClassicsHeader(context)
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            ClassicsFooter(context)
        }
    }
}

/**
 * 同意隐私政策后初始化
 */
fun Application?.init() {
    this?.let {
        initAMap()
    }
}

private fun Application?.initAMap() {
    this?.let {
        AMapLocationClient.updatePrivacyShow(it, true, true)
        AMapLocationClient.updatePrivacyAgree(it, true)
    }
}

fun Application?.initLogcat() {
    this?.let {
        LogCat.setDebug(BuildConfig.DEBUG) // 全局开关
    }
}

fun Application?.initDialogX() {
    this?.let {
        //初始化
        DialogX.init(it)
    }
}