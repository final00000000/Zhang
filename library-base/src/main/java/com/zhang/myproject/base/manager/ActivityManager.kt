package com.zhang.myproject.base.manager

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.os.Process
import com.drake.logcat.LogCat
import timber.log.Timber
import java.lang.ref.WeakReference

/**
 * 提供前后台状态监听 以及栈顶activity的服务
 */
class ActivityManager private constructor() {

    private val activityRefs = ArrayList<WeakReference<Activity>>()
    private val frontBackCallbacks = ArrayList<FrontBackCallback>()
    private var activityStartCount = 0
    private var front = true
    fun init(application: Application) {
        application.registerActivityLifecycleCallbacks(InnerActivityLifecycleCallbacks())
    }

    inner class InnerActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            activityRefs.add(WeakReference(activity))
        }

        override fun onActivityStarted(activity: Activity) {
            activityStartCount++
            //activityStartCount>0  说明应用处在可见状态，也就是前台
            //!front 之前是不是在后台
            if (!front && activityStartCount > 0) {
                front = true
                onFrontBackChanged(front)
            }
        }

        override fun onActivityResumed(activity: Activity) {
        }

        override fun onActivityPaused(activity: Activity) {
        }

        override fun onActivityStopped(activity: Activity) {
            activityStartCount--
            if (activityStartCount <= 0 && front) {
                front = false
                onFrontBackChanged(front)
            }
        }

        override fun onActivityDestroyed(activity: Activity) {
            for (activityRef in activityRefs) {
                if (activityRef.get() == activity) {
                    activityRefs.remove(activityRef)
                    break
                }
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }
    }

    private fun onFrontBackChanged(front: Boolean) {
        for (callback in frontBackCallbacks) {
            callback.onChanged(front)
        }
    }

    /**
     * 找出栈顶不为空，且没有被销毁的activity
     */
    fun getTopActivity(onlyAlive: Boolean): Activity? {
        if (activityRefs.size <= 0) {
            return null
        } else {
            val activityRef = activityRefs[activityRefs.size - 1]
            val activity: Activity? = activityRef.get()
            if (onlyAlive) {
                if (activity == null || activity.isFinishing || activity.isDestroyed) {
                    activityRefs.remove(activityRef)
                    return getTopActivity(onlyAlive)
                }
            }
            return activity
        }
    }

    /**
     * 获取站内activity的数量
     */
    fun getActivitySize(): Int {
        return if (activityRefs.size <= 0) 0 else activityRefs.size
    }


    fun addFrontBackCallback(callback: FrontBackCallback) {
        if (!frontBackCallbacks.contains(callback)) {
            frontBackCallbacks.add(callback)
        }
    }

    fun removeFrontBackCallback(callback: FrontBackCallback) {
        frontBackCallbacks.remove(callback)
    }


    /**
     * 获取指定的Activity
     */
    fun getActivity(cls: Class<*>): Activity? {
        activityRefs.forEach {
            it.get()?.let { activity ->
                if (activity.javaClass == cls) {
                    return activity
                }
            }
        }
        return null
    }

    /**
     * 判断是否存在MainActivity
     */
    fun isExistMainActivity() = compareActivity("MainActivity")

    /**
     * 检测是否存在当前Activity
     */
    private fun compareActivity(activityName: String): Boolean {
        activityRefs.forEach {
            it.get()?.let { activity ->
                if (activity.javaClass.simpleName == activityName) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * 结束除了指定Activity以外的所有activity
     *
     * @param activity
     */
    fun finishAllActivityExceptActivity(activity: Activity) {
        activityRefs.forEach {
            it.get()?.let { ac ->
                if (ac.javaClass != activity.javaClass) {
                    finishActivity(ac)
                }
            }
        }
        activityRefs.clear()
        activityRefs.add(WeakReference(activity))
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity?) {
        activity?.let {
            if (!it.isFinishing) {
                activity.finish()
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>?) {
        activityRefs.forEach {
            it.get()?.let { activity ->
                if (activity.javaClass == cls) {
                    finishActivity(activity)
                    return@finishActivity
                }
            }
        }
    }

    /**
     * 是否有activity
     */
    fun isActivity(): Boolean {
        return activityRefs.isNotEmpty()
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishActivity() {
        activityRefs.last {
            finishActivity(it.get())
            return
        }
    }


    fun finishActivityToMain() {
        activityRefs.forEach {
            it.get()?.let { activity ->
                if (!activity.javaClass.simpleName.equals("MainActivity") && getTopActivity(true) != activity) {
                    finishActivity(activity)
                }
            }
        }
    }


    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        activityRefs.forEach {
            it.get()?.let { activity ->
                finishActivity(activity)
            }
        }
        activityRefs.clear()
    }

    /**
     * @description：重启应用
     */
    fun appRestart() {
        getTopActivity(true)?.let {
            it.packageManager.getLaunchIntentForPackage(it.packageName)?.let { intent ->
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                it.startActivity(intent)
            }
        }
        //杀掉以前进程
        Process.killProcess(Process.myPid())
    }


    /**
     * 监听Activity是否被销毁
     */
    fun isActivityDestroyed(context: Context): Boolean {
        findActivity(context)?.let {
            return@isActivityDestroyed it.isDestroyed || it.isFinishing
        }
        return true
    }

    /**
     * 判断context 是不是activity 类型的
     */
    private fun findActivity(context: Context): Activity? {
        if (context is Activity) return context else if (context is ContextWrapper) return findActivity(
            context.baseContext
        )
        return null
    }

    /**
     * 退出应用程序
     */
    fun AppExit() {
        try {
            finishAllActivity()
        } catch (e: Exception) {
            activityRefs.clear()
            e.printStackTrace()
        }
        Process.killProcess(Process.myPid())
    }

    interface FrontBackCallback {
        fun onChanged(front: Boolean)
    }

    companion object {
        @JvmStatic
        val instance: ActivityManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ActivityManager()
        }
    }
}