package com.zhang.myproject

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.zhang.myproject.base.AppGlobals
import com.zhang.myproject.base.activity.BaseVBActivity
import com.zhang.myproject.base.helper.MMkvHelperUtils
import com.zhang.myproject.common.utils.mApplication
import com.zhang.myproject.databinding.ActivitySplashBinding
import com.zhang.myproject.resource.constant.APPKeyConstant
import timber.log.Timber
import java.util.Timer
import java.util.TimerTask

/**
 * Date: 2023/10/10
 * Author : Zhang
 * Description :
 */
@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseVBActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    override fun isLayoutToolbar(): Boolean = false

    private var mPrivacyPopWindow: PrivacyPopWindow? = null

    override fun initView(savedInstanceState: Bundle?) {
        MMkvHelperUtils.saveFirstStart(APPKeyConstant.FIRST_START)
        initX5WebView()
        if (MMkvHelperUtils.getConsentToPrivacy()) {
            startMain()
        } else {
            showPrivacyDialog()
        }
    }

    override fun setOnViewClick() {
    }

    private fun startMain() {
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                MMkvHelperUtils.setConsentToPrivacy(true)
                (AppGlobals.get() ?: mApplication)?.init()
//                finish()
            }
        }
        val timer = Timer()
        timer.schedule(task, 1000)
    }


    private fun showPrivacyDialog() {
        mPrivacyPopWindow = PrivacyPopWindow(this) {
            if (it == true) {
                startMain()
            } else {
                finish()
            }
        }
    }

    /**
     * 初始化webView
     */
    private fun initX5WebView() {
        // 在调用TBS初始化、创建WebView之前进行如下配置
        val map = HashMap<String, Any>()
        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
        QbSdk.initTbsSettings(map)
        QbSdk.setDownloadWithoutWifi(true)
        QbSdk.initX5Environment(this, object : QbSdk.PreInitCallback {
            override fun onCoreInitFinished() {}
            override fun onViewInitFinished(boolean: Boolean) {
                Timber.d("测试onViewInitFinished_75：${if (boolean) "X5 内核加载成功" else "X5 内核加载失败"}")
            }
        })
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode != KeyEvent.KEYCODE_BACK) {
            return super.onKeyDown(keyCode, event)
        } else {
            return false
        }
    }
}