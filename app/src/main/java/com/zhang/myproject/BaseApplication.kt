package com.zhang.myproject

import android.app.Application

/**
 * Date: 2023/10/10
 * Author : Zhang
 * Description :
 */
abstract class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        mApplication = this
    }
}