package com.zhang.myproject.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.adapters.ViewGroupBindingAdapter.setListener
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.zhang.myproject.base.data.NetWorkState
import com.zhang.myproject.base.fragment.BaseVBVMFragment
import com.zhang.myproject.base.manager.NetworkManager

/**
 * @Author : zhang
 * @Create Time : 2022/3/23
 * @Class Describe : 描述
 * @Project Name : MyDemo
 */
abstract class BaseNetWorkFragment<VB : ViewBinding, VM : ViewModel>(@LayoutRes layoutId: Int) :
    BaseVBVMFragment<VB, VM>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return initViewBinding()
    }

    protected abstract fun initView(savedInstanceState: Bundle?)

    protected abstract fun setOnViewClick()

    protected abstract fun createObserver()

    /**
     * 网络变化监听 子类重写
     */
    override fun onNetworkStateChanged(netState: NetWorkState) {}


}

