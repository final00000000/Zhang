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
import com.kongzue.dialogx.dialogs.WaitDialog
import com.zhang.myproject.base.R
import com.zhang.myproject.base.data.NetWorkState
import com.zhang.myproject.base.manager.NetworkManager
import com.zhang.myproject.base.utils.dpToPx
import com.zhang.myproject.base.utils.find
import com.zhang.myproject.base.utils.getStringRes
import com.zhang.myproject.base.utils.getViewBindingForActivity
import com.zhang.myproject.base.utils.getVmClass
import com.zhang.myproject.base.utils.initToolbarBarHeight
import com.zhang.myproject.base.utils.singleClick
import com.zhang.myproject.base.utils.toast.Toasty


/**
 * @Author : zhang
 * @Create Time : 2022/3/9
 * @Class Describe : 描述
 * @Project Name : MyDemo
 */
abstract class BaseVBVMActivity<VB : ViewBinding, VM : ViewModel>(@LayoutRes val layoutID: Int) : BaseActivity() {


    protected lateinit var mViewModel: VM

    protected lateinit var mViewBinding: VB

    private var mLoading: WaitDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NetworkManager.instance.mNetworkStateCallback.observeSticky(this) {
            onNetworkStateChanged(it)
        }
        mViewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[getVmClass(this)]
        if (isLayoutToolbar()) {
            try {
                setContentView(R.layout.activity_base)
                find<View>(R.id.vvImmersionView).initToolbarBarHeight()
                /**
                 * 添加内容区
                 */
                find<FrameLayout>(R.id.baseContent).addView(initViewBinding())
                /**
                 * toolbar返回键
                 */
                find<AppCompatImageView>(R.id.ivPageBack).singleClick { finish() }
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
        //TheRouter.inject(this)
        initLoading()
        //初始化控件
        initView(savedInstanceState)
        // 设置监听
        setOnViewClick()
        // 数据观察
        createObserver()

//        startLoading()

        NetworkManager.instance.mNetworkStateCallback.observeSticky(this) {
            onNetworkStateChanged(it)
        }
    }

    protected abstract fun initView(savedInstanceState: Bundle?)

    protected abstract fun setOnViewClick()

    protected abstract fun createObserver()

    private fun initLoading() {
        mLoading = WaitDialog.build()
            .setMinHeight(dpToPx(60f))
            .setMinWidth(dpToPx(60f))
            .setMaxWidth(dpToPx(60f))
            .setMaxHeight(dpToPx(60f))
            .setOnBackPressedListener {
                false
            }
    }


    override fun startLoading() {
        if (mLoading != null && mLoading?.isShow == false) {
            mLoading?.show()
        } else {
            mLoading?.doDismiss()
        }
    }

    override fun finishLoading() {
        if (mLoading != null && mLoading?.isShow == true) {
            mLoading?.doDismiss()
        }
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
        find<TextView>(R.id.tvPageTitle).text = getStringRes(titleTxt)
    }

    /**
     * 设置toolbar右侧文案
     */
    protected fun setToolbarRightText(@StringRes rightTxt: Int) {
        if (!isLayoutToolbar()) {
            return
        }
        find<TextView>(R.id.tvRightTitle).apply {
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
        find<ImageView>(R.id.ivRightIcon).apply {
            isVisible = true
            setImageResource(rightIcon)
        }
    }

    /**
     * 网络变化监听 子类重写
     */
    open fun onNetworkStateChanged(netState: NetWorkState) {
        if (!netState.isSuccess) {
            Toasty.error(getStringRes(com.zhang.myproject.resource.R.string.net_error))
        }
    }
}