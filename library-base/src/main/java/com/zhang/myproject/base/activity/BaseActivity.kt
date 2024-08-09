package com.zhang.myproject.base.activity

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ImmersionBar
import com.zhang.myproject.base.callback.ActivityBaseInterface
import com.zhang.myproject.base.helper.MMkvHelperUtils


/**
 * @Author : zhang
 * @Create Time : 2021-11-15 13:11:13
 * @Class Describe : 描述
 * @Project Name : KotlinDemo
 */
abstract class BaseActivity : AppCompatActivity(), ActivityBaseInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersionBar.with(this).fitsSystemWindows(false).statusBarDarkFont(true).init()
    }


    private fun isStatusBarDarkFont(): Boolean {
        val uiMode = resources.configuration.uiMode
        val dayNightUiMode = uiMode and Configuration.UI_MODE_NIGHT_MASK
        return if (MMkvHelperUtils.getNightSystemAutoMode()) {
            dayNightUiMode == Configuration.UI_MODE_NIGHT_NO
        } else !MMkvHelperUtils.getCurrentNightMode()
    }
}