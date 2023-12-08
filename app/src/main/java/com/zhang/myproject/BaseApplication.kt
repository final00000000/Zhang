package com.zhang.myproject

import android.app.Application
import com.kongzue.dialogx.DialogX
import com.zhang.myproject.common.utils.mApplication

/**
 * Date: 2023/10/10
 * Author : Zhang
 * Description :
 */
abstract class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initMMKV()
        initActivityManager()
        initToasty()
        initLogcat()
        initTimber()
        //初始化
        DialogX.init(this)
        mApplication = this
    }
}