package com.zhang.myproject.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.zhang.myproject.base.R
import com.zhang.myproject.base.callback.FragmentBaseCallBack
import com.zhang.myproject.base.data.NetWorkState
import com.zhang.myproject.base.manager.NetworkManager
import com.zhang.myproject.base.utils.getStringRes
import com.zhang.myproject.base.utils.getViewBindingForActivity
import com.zhang.myproject.base.utils.initToolbarBarHeight
import com.zhang.myproject.base.utils.toast.Toasty

/**
 * @Author : zhang
 * @Create Time : 2022/3/23
 * @Class Describe : 描述
 * @Project Name : MyDemo
 */
abstract class BaseVBFragment<VB : ViewBinding>(@LayoutRes val layoutId: Int) : BaseFragment(), FragmentBaseCallBack {

    protected lateinit var mViewBinding: VB

    /** 是否显示toolbar*/
    override fun isLayoutToolbar(): Boolean = false

    /** toolbar View*/
    private var mToolbarView: View? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return if (isLayoutToolbar()) {
            if (mToolbarView == null) {
                mToolbarView = layoutInflater.inflate(R.layout.activity_base, container, false)
            }
            mToolbarView?.apply {
                /**
                 * 添加内容区
                 */
                findViewById<FrameLayout>(R.id.baseContent).addView(initViewBinding())
                /**
                 * toolbar返回键
                 */
                findViewById<AppCompatImageView>(R.id.ivPageBack).isVisible = false
                findViewById<View>(R.id.vvImmersionView).initToolbarBarHeight()
            }
        } else {
            initViewBinding()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        NetworkManager.instance.mNetworkStateCallback.observeSticky(this) {
            onNetworkStateChanged(it)
        }
        init(savedInstanceState)
    }

    private fun init(savedInstanceState: Bundle?) {
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

    /**
     * 设置toolbar标题
     */
    protected fun setToolbarTitle(@StringRes titleTxt: Int) {
        if (!isLayoutToolbar()) {
            return
        }
        mToolbarView?.let {
            it.findViewById<TextView>(R.id.tvPageTitle).text = getStringRes(titleTxt)
        }
    }

    /**
     * 设置toolbar右侧文案
     */
    protected fun setToolbarRightText(@StringRes rightTxt: Int) {
        if (!isLayoutToolbar()) {
            return
        }
        mToolbarView?.let {
            it.findViewById<TextView>(R.id.tvRightTitle).apply {
                isVisible = getStringRes(rightTxt).isNotEmpty()
                text = getStringRes(rightTxt)
            }
        }
    }

    /**
     * 设置toolbar右侧图片
     */
    protected fun setToolbarRightIcon(rightIcon: Int) {
        if (!isLayoutToolbar()) {
            return
        }
        mToolbarView?.let {
            it.findViewById<ImageView>(R.id.ivRightIcon).apply {
                isVisible = true
                setImageResource(rightIcon)
            }
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


    override fun startLoading() {

    }

    override fun finishLoading() {

    }

    override fun showEmptyView() {

    }

    override fun showNoNetWorkView(netWorkSuccess: Boolean?) {

    }


}

