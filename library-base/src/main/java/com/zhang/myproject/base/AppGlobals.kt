package com.zhang.myproject.base

import android.app.Application

object AppGlobals {
    private var application: Application? = null
    fun get(): Application? {
        if (application == null) {
            try {
                application = Class.forName("android.app.ActivityThread")
                    .getMethod("currentApplication")
                    .invoke(null) as Application
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        return application
    }
}