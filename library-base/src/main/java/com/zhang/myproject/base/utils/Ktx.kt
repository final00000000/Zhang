package com.zhang.myproject.base.utils

import android.app.Activity
import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.content.IntentFilter
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.ProcessLifecycleOwner
import com.zhang.myproject.base.manager.ActivityManager
import com.zhang.myproject.base.manager.NetworkStateReceive
import timber.log.Timber

class Ktx : ContentProvider() {

    companion object {
        lateinit var app: Application
        private var mNetworkStateReceive: NetworkStateReceive? = null
        var watchActivityLife = true
        var watchAppLife = true
    }

    override fun onCreate(): Boolean {
        val application = context!!.applicationContext as Application
        install(application)
        return true
    }

    private fun install(application: Application) {
        app = application
        mNetworkStateReceive = NetworkStateReceive()
        app.registerReceiver(
            mNetworkStateReceive,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        if (watchActivityLife) application.registerActivityLifecycleCallbacks(LifeCycleCallBack())
        if (watchAppLife) ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifeObserver)
    }


    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? = null


    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int = 0

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0

    override fun getType(uri: Uri): String? = null
}

class LifeCycleCallBack : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Timber.d(" onActivityCreated ${activity.localClassName}")
    }

    override fun onActivityStarted(activity: Activity) {
        Timber.d(" onActivityStarted ${activity.localClassName}")
    }

    override fun onActivityResumed(activity: Activity) {
        Timber.d(" onActivityResumed ${activity.localClassName}")
    }

    override fun onActivityPaused(activity: Activity) {
        Timber.d(" onActivityPaused ${activity.localClassName}")
    }


    override fun onActivityDestroyed(activity: Activity) {
        Timber.d(" onActivityDestroyed ${activity.localClassName}")
        ActivityManager.instance.finishAllActivity()
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Timber.d(" onActivitySaveInstanceState ${activity.localClassName}")
    }

    override fun onActivityStopped(activity: Activity) {
        Timber.d(" onActivityStopped ${activity.localClassName}")
    }


}