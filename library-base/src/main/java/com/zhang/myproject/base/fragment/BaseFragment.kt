package com.zhang.myproject.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.viewbinding.ViewBinding
import com.zhang.myproject.base.data.NetWorkState

/**
 * @Author : zhang
 * @Create Time : 2022/3/9
 * @Class Describe : 描述
 * @Project Name : MyDemo
 */
abstract class BaseFragment<VB : ViewBinding>(@LayoutRes layoutId: Int) :
    BaseVbFragment<VB>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //初始化控件
        initView(savedInstanceState)
        //数据的操作
        initData()
        // 设置监听
        setOnViewClick()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return initViewBinding()
    }

    protected abstract fun initView(savedInstanceState: Bundle?)

    protected abstract fun initData()

    protected abstract fun setOnViewClick()

    override fun onNetworkStateChanged(netState: NetWorkState) {}
}