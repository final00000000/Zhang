package com.zhang.myproject

import android.app.Application
import android.content.Context
import com.amap.api.location.AMapLocationClient
import com.tencent.mmkv.BuildConfig
import com.tencent.mmkv.MMKV
import com.zhang.myproject.base.manager.ActivityManager
import com.zhang.myproject.base.utils.toast.Toasty
import timber.log.Timber


/**
 * @Author : zhang
 * @Create Time : 2021-11-15 13:10:36
 * @Class Describe : 描述
 * @Project Name : MyDemo
 */
class MyProjectApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
    }

}