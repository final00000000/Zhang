package com.zhang.myproject

import android.os.Bundle
import com.zhang.myproject.base.activity.BaseActivity
import com.zhang.myproject.databinding.ActivitySplashBinding

/**
 * Date: 2023/10/10
 * Author : Zhang
 * Description :
 */
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun setOnViewClick() {
    }

    override fun startLoading() {
    }

    override fun finishLoading() {
    }

    override fun showEmptyView() {
    }

    override fun showNoNetWorkView(netWorkSuccess: Boolean?) {
    }
}