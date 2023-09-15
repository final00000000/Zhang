package com.zhang.myproject.base.activity

import android.os.Bundle
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.viewbinding.ViewBinding
import com.gyf.immersionbar.ImmersionBar
import com.zhang.myproject.base.R
import com.zhang.myproject.base.callback.IsBase
import com.zhang.myproject.base.utils.SystemModelUtils
import com.zhang.myproject.base.utils.singleClick


/**
 * @Author : zhang
 * @Create Time : 2021-11-15 13:11:13
 * @Class Describe : 描述
 * @Project Name : KotlinDemo
 */
abstract class BaseActivity<VB : ViewBinding>(@LayoutRes layoutId: Int) :
    BaseVbActivity<VB>(), IsBase {

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

        //初始化设置沉浸式状态栏
        initImmersionBar()
        //初始化控件
        initView(savedInstanceState)
        // 设置监听
        setOnViewClick()
    }

    protected abstract fun initView(savedInstanceState: Bundle?)

    protected abstract fun setOnViewClick()

    protected fun killMyself() {
        finish()
    }

    /**
     * 初始化设置沉浸式状态栏
     */
    private fun initImmersionBar() {
        ImmersionBar.with(this)
            .statusBarDarkFont(true, 0.2f)
            .init()
    }


}