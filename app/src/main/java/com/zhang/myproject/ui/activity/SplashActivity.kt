package com.zhang.myproject.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.zhang.myproject.MainActivity
import com.zhang.myproject.base.AppGlobals
import com.zhang.myproject.base.activity.BaseActivity
import com.zhang.myproject.base.helper.MMkvHelperUtils
import com.zhang.myproject.common.ktx.countDownCoroutines
import com.zhang.myproject.init
import com.zhang.myproject.ui.pop.PrivacyPopWindow
import timber.log.Timber

/**
 * Date: 2023/10/10
 * Author : Zhang
 * Description :
 */
class SplashActivity : BaseActivity() {

    private var mPrivacyPopWindow: PrivacyPopWindow? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
//        installSplashScreen().setKeepOnScreenCondition { true }
//        MMkvHelperUtils.saveFirstStart(APPKeyConstant.FIRST_START)
//        initX5WebView()
        if (MMkvHelperUtils.getConsentToPrivacy()) {
            startMain()
        } else {
            showPrivacyDialog()
        }
    }

    private fun startMain() {
        countDownCoroutines(1, onFinish = {
            MMkvHelperUtils.setConsentToPrivacy(true)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            AppGlobals.get()?.init()
        })
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
            super.onKeyDown(keyCode, event)
        } else {
            false
        }
    }
}