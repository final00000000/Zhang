package com.zhang.myproject.base.activity

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.zhang.myproject.base.data.NetWorkState
import com.zhang.myproject.base.manager.NetworkManager
import com.zhang.myproject.base.utils.getViewBindingForActivity

/**
 * @Author : zhang
 * @Create Time : 2022/3/23
 * @Class Describe : 描述
 * @Project Name : MyDemo
 */
abstract class BaseVbActivity<VB : ViewBinding> : AppCompatActivity() {

    lateinit var mViewBinding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NetworkManager.instance.mNetworkStateCallback.observeSticky(this) {
            onNetworkStateChanged(it)
        }
    }

    fun initViewBinding(): View {
        mViewBinding = getViewBindingForActivity(layoutInflater)
        return mViewBinding.root
    }

    /**
     * 网络变化监听 子类重写
     */
    open fun onNetworkStateChanged(netState: NetWorkState) {}

    /**
     * @param true：浅色模式 false：深色模式
     */
    var systemColorMode: Boolean? = null

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        when (newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            /** 浅色模式*/
            Configuration.UI_MODE_NIGHT_NO -> {
                systemColorMode = true
            }
            /** 深色模式*/
            Configuration.UI_MODE_NIGHT_YES -> {
                //初始化设置沉浸式状态栏
                systemColorMode = false
            }

            else -> {}
        }
    }
}