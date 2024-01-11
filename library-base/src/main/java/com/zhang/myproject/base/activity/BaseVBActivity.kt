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
import androidx.viewbinding.ViewBinding
import com.zhang.myproject.base.R
import com.zhang.myproject.base.callback.ActivityBaseCallBack
import com.zhang.myproject.base.data.NetWorkState
import com.zhang.myproject.base.utils.getStringRes
import com.zhang.myproject.base.utils.getViewBindingForActivity
import com.zhang.myproject.base.utils.initToolbarBarHeight
import com.zhang.myproject.base.utils.singleClick
import com.zhang.myproject.base.utils.toast.Toasty

/**
 * @Author : zhang
 * @Create Time : 2022/3/23
 * @Class Describe : 描述
 * @Project Name : MyDemo
 */
abstract class BaseVBActivity<VB : ViewBinding>(@LayoutRes layoutID: Int) : BaseActivity(), ActivityBaseCallBack {

    protected lateinit var mViewBinding: VB

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
            findViewById<View>(R.id.vvImmersionView).initToolbarBarHeight()
        } else {
            setContentView(initViewBinding())
        }

        //初始化控件
        initView(savedInstanceState)
        // 设置监听
        setOnViewClick()
    }

    private fun initViewBinding(): View {
        mViewBinding = getViewBindingForActivity(layoutInflater)
        return mViewBinding.root
    }

    protected abstract fun initView(savedInstanceState: Bundle?)

    protected abstract fun setOnViewClick()


    override fun startLoading() {

    }

    override fun finishLoading() {

    }

    override fun showEmptyView() {

    }

    override fun showNoNetWorkView(netWorkSuccess: Boolean?) {

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

    protected fun killMyself() {
        finish()
    }

}