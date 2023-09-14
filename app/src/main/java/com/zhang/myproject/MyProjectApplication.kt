package com.zhang.myproject

import android.app.Application
import android.content.Context
import com.amap.api.location.AMapLocationClient
import com.tencent.mmkv.BuildConfig
import com.tencent.mmkv.MMKV
import com.zhang.myproject.base.manager.ActivityManager
import timber.log.Timber


/**
 * @Author : zhang
 * @Create Time : 2021-11-15 13:10:36
 * @Class Describe : 描述
 * @Project Name : MyDemo
 */
class MyProjectApplication : Application() {

    private var mContext: Context? = null

    fun getContext(): Context? = mContext

    override fun onCreate() {
        super.onCreate()
        mContext = this.applicationContext
        initMMKV()
        initTimber()
        initActivityManager()
        initRefreshStyle()
        initAMap()
    }

    /**
     * 初始化Mkv
     */
    private fun initMMKV() {
        MMKV.initialize(this)
    }

    /**
     * 初始化打印日志框架
     */
    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    /**
     * 初始化Activity生命周期监听
     */
    private fun initActivityManager() {
        ActivityManager.instance.init(this)
    }

    /**
     * 初始化刷新头
     */
    private fun initRefreshStyle() {
        /*  SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
              MaterialHeader(context)
          }

          SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
              ClassicsFooter(context)
          }*/
    }
    private fun initAMap(){
        AMapLocationClient.updatePrivacyShow(this, true, true)
        AMapLocationClient.updatePrivacyAgree(this, true)
    }

}