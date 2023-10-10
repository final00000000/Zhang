package com.zhang.myproject.base.activity

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.viewbinding.ViewBinding
import com.gyf.immersionbar.ImmersionBar
import com.zhang.myproject.base.R
import com.zhang.myproject.base.data.NetWorkState
import com.zhang.myproject.base.manager.NetworkManager
import com.zhang.myproject.base.utils.SystemModelUtils
import com.zhang.myproject.base.utils.singleClick

/**
 * @Author : zhang
 * @Create Time : 2022/3/23
 * @Class Describe : 描述
 * @Project Name : MyDemo
 */
abstract class BaseNetWorkActivity<VB : ViewBinding, VM : ViewModel>(@LayoutRes layoutId: Int) :
    BaseVBVMActivity<VB, VM>() {

    /**
     * 是否需要带toolbar的布局
     * 如果不需要自带的toolbar 就在对应的activity重写该方法
     * @return Boolean
     */
    override fun isLayoutToolbar(): Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isLayoutToolbar()) {
            setContentView(R.layout.activity_base)
            /**
             * 添加内容区
             */
            findViewById<FrameLayout>(R.id.baseContent).addView(initViewBinding())
            /**
             * toolbar返回键
             */
            findViewById<AppCompatImageView>(R.id.ivPageBack).singleClick { killMyself() }
        } else {
            setContentView(initViewBinding())
        }

        init(savedInstanceState)
    }

    private fun init(savedInstanceState: Bundle?) {
        // 设置沉浸式
        initImmersionBar()
        //初始化控件
        initView(savedInstanceState)
        // 设置监听
        setOnViewClick()
        // 数据观察
        createObserver()

        NetworkManager.instance.mNetworkStateCallback.observeSticky(this) {
            onNetworkStateChanged(it)
        }
    }

    protected abstract fun initView(savedInstanceState: Bundle?)

    protected abstract fun setOnViewClick()

    protected abstract fun createObserver()


    override fun startLoading() {

    }

    override fun finishLoading() {

    }

    override fun showEmptyView() {

    }

    override fun showNoNetWorkView(netWorkSuccess: Boolean?) {
        netWorkSuccess?.let {
            if (!it) {

            }
        }
    }

    /**
     * 网络变化监听 子类重写
     */
    override fun onNetworkStateChanged(netState: NetWorkState) {
        showNoNetWorkView(netState.isSuccess)
    }

    /**
     * 初始化设置沉浸式状态栏
     */
    private fun initImmersionBar() {
        ImmersionBar.with(this)
            .fitsSystemWindows(false)
            .statusBarDarkFont(true, 0.2f)
            .init()
    }

    private fun killMyself() {
        finish()
    }
}

