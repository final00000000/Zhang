package com.zhang.myproject

import android.app.Application
import com.zhang.myproject.common.utils.mApplication
import leakcanary.LeakCanary

/**
 * Date: 2023/10/10
 * Author : Zhang
 * Description :
 */
abstract class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initMMKV()
        initToasty()
        initLogcat()
        initTimber()
        initActivityManager()
        initDialogX()
        initRefreshStyle()
        initNet()
        initStatus()
        mApplication = this
    }

}