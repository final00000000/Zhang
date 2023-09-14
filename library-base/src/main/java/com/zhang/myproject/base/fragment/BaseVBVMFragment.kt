package com.zhang.myproject.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.zhang.myproject.base.data.NetWorkState
import com.zhang.myproject.base.manager.NetworkManager
import com.zhang.myproject.base.utils.getViewBindingForActivity
import java.lang.reflect.ParameterizedType

/**
 * @Author : zhang
 * @Create Time : 2022/3/9
 * @Class Describe : 描述
 * @Project Name : MyDemo
 */
abstract class BaseVBVMFragment<VB : ViewBinding, VM : ViewModel> : Fragment() {

    lateinit var mViewModel: VM

    lateinit var mViewBinding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        NetworkManager.instance.mNetworkStateCallback.observeSticky(this) {
            onNetworkStateChanged(it)
        }
    }

    /**
     * 网络变化监听 子类重写
     */
    open fun onNetworkStateChanged(netState: NetWorkState) {}

    fun initViewBinding(): View {
        mViewBinding = getViewBindingForActivity(layoutInflater)
        return mViewBinding.root
    }

    private fun initViewModel() {
        try {
            //这里获得到的是类的泛型的类型
            val type = javaClass.genericSuperclass
            if (type != null && type is ParameterizedType) {
                val actualTypeArguments = type.actualTypeArguments
                val tClass = actualTypeArguments[1]
                mViewModel = ViewModelProvider(
                    this,
                    ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
                )[tClass as Class<VM>]
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}