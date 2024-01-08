package com.zhang.myproject.base.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.viewbinding.ViewBinding
import com.gyf.immersionbar.ktx.statusBarHeight
import com.zhang.myproject.base.R
import com.zhang.myproject.base.callback.IsBase
import com.zhang.myproject.base.utils.getViewBindingForActivity
import com.zhang.myproject.base.utils.initToolbarBarHeight
import com.zhang.myproject.base.utils.singleClick
import timber.log.Timber

/**
 * @Author : zhang
 * @Create Time : 2022/3/23
 * @Class Describe : 描述
 * @Project Name : MyDemo
 */
abstract class BaseVbActivity<VB : ViewBinding>(@LayoutRes layoutID: Int) : BaseActivity(), IsBase {

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
        netWorkSuccess?.let {
            if (!it) {

            }
        }
    }

    protected fun killMyself() {
        finish()
    }

    protected fun setToolBarTitle(title: String) {
        if (isLayoutToolbar()) {
            /**
             * toolbar标题
             */
            findViewById<AppCompatTextView>(R.id.tvPageTitle).text = title
        }
    }

}