package com.zhang.myproject.base.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.FrameLayout
import android.widget.Space
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.gyf.immersionbar.ktx.immersionBar
import com.gyf.immersionbar.ktx.navigationBarHeight
import com.gyf.immersionbar.ktx.statusBarHeight
import com.zhang.myproject.base.R
import com.zhang.myproject.base.callback.IsBase
import com.zhang.myproject.base.data.NetWorkState
import com.zhang.myproject.base.manager.NetworkManager
import com.zhang.myproject.base.utils.getViewBindingForActivity
import com.zhang.myproject.base.utils.getVmClass
import com.zhang.myproject.base.utils.initToolbarBarHeight
import com.zhang.myproject.base.utils.singleClick
import timber.log.Timber


/**
 * @Author : zhang
 * @Create Time : 2022/3/9
 * @Class Describe : 描述
 * @Project Name : MyDemo
 */
abstract class BaseVBVMActivity<VB : ViewBinding, VM : ViewModel>(@LayoutRes layoutID: Int) : BaseActivity(), IsBase {


    protected lateinit var mViewModel: VM

    protected lateinit var mViewBinding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NetworkManager.instance.mNetworkStateCallback.observeSticky(this) {
            onNetworkStateChanged(it)
        }
        mViewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[getVmClass(this)]
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
            findViewById<View>(R.id.vvImmersionView).initToolbarBarHeight()
        } else {
            setContentView(initViewBinding())
        }

        init(savedInstanceState)
    }

    private fun initViewBinding(): View {
        mViewBinding = getViewBindingForActivity(layoutInflater)
        return mViewBinding.root
    }

    /**
     * 是否需要带toolbar的布局
     * 如果不需要自带的toolbar 就在对应的activity重写该方法
     * @return Boolean
     */
    override fun isLayoutToolbar(): Boolean = true

    private fun init(savedInstanceState: Bundle?) {
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
        netWorkSuccess?.let {}
    }

    /**
     * 网络变化监听 子类重写
     */
    private fun onNetworkStateChanged(netState: NetWorkState) {
        showNoNetWorkView(netState.isSuccess)
    }

    private fun killMyself() {
        finish()
    }

}