package com.zhang.myproject.base.activity

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.zhang.myproject.base.R
import com.zhang.myproject.base.callback.ActivityBaseCallBack
import com.zhang.myproject.base.data.NetWorkState
import com.zhang.myproject.base.manager.NetworkManager
import com.zhang.myproject.base.utils.getStringRes
import com.zhang.myproject.base.utils.getViewBindingForActivity
import com.zhang.myproject.base.utils.getVmClass
import com.zhang.myproject.base.utils.initToolbarBarHeight
import com.zhang.myproject.base.utils.singleClick
import com.zhang.myproject.base.utils.toast.Toasty
import timber.log.Timber


/**
 * @Author : zhang
 * @Create Time : 2022/3/9
 * @Class Describe : 描述
 * @Project Name : MyDemo
 */
abstract class BaseVBVMActivity<VB : ViewBinding, VM : ViewModel>(@LayoutRes val layoutID: Int) : BaseActivity(), ActivityBaseCallBack {


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
            findViewById<View>(R.id.vvImmersionView).initToolbarBarHeight()
            /**
             * 添加内容区
             */
            findViewById<FrameLayout>(R.id.baseContent).addView(initViewBinding())
            /**
             * toolbar返回键
             */
            try {
                findViewById<AppCompatImageView>(R.id.ivPageBack).singleClick { killMyself() }
            } catch (e: Exception) {
                e.printStackTrace()
            }
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
     * 设置toolbar标题
     */
    protected fun setToolbarTitle(@StringRes titleTxt: Int) {
        if (!isLayoutToolbar()) {
            return
        }
        findViewById<TextView>(R.id.tvPageTitle).text = getStringRes(titleTxt)
    }

    /**
     * 设置toolbar右侧文案
     */
    protected fun setToolbarRightText(@StringRes rightTxt: Int) {
        if (!isLayoutToolbar()) {
            return
        }
        findViewById<TextView>(R.id.tvRightTitle).apply {
            isVisible = getStringRes(rightTxt).isNotEmpty()
            text = getStringRes(rightTxt)
        }
    }

    /**
     * 设置toolbar右侧图片
     */
    protected fun setToolbarRightIcon(rightIcon: Int) {
        if (!isLayoutToolbar()) {
            return
        }
        findViewById<ImageView>(R.id.ivRightIcon).apply {
            isVisible = true
            setImageResource(rightIcon)
        }
    }

    /**
     * 网络变化监听 子类重写
     */
    protected fun onNetworkStateChanged(netState: NetWorkState) {
        if (!netState.isSuccess) {
            Toasty.error(getStringRes(com.zhang.myproject.resource.R.string.net_error))
        }
    }

    private fun killMyself() {
        Timber.d("测试_159：finish")
        finish()
    }

}