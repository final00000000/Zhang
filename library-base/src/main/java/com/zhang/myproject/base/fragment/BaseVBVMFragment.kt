package com.zhang.myproject.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.kongzue.dialogx.dialogs.WaitDialog
import com.zhang.myproject.base.callback.FragmentBaseCallBack
import com.zhang.myproject.base.data.NetWorkState
import com.zhang.myproject.base.manager.NetworkManager
import com.zhang.myproject.base.utils.dpToPx
import com.zhang.myproject.base.utils.getStringRes
import com.zhang.myproject.base.utils.getViewBindingForActivity
import com.zhang.myproject.base.utils.toast.Toasty
import java.lang.reflect.ParameterizedType

/**
 * @Author : zhang
 * @Create Time : 2022/3/23
 * @Class Describe : 描述
 * @Project Name : MyDemo
 */
abstract class BaseVBVMFragment<VB : ViewBinding, VM : ViewModel>(@LayoutRes val layoutId: Int) : BaseFragment(),
    FragmentBaseCallBack {

    protected lateinit var mViewModel: VM

    protected lateinit var mViewBinding: VB

    private var mLoading: WaitDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return initViewBinding()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLoading()
        initViewModel()
        NetworkManager.instance.mNetworkStateCallback.observeSticky(this) {
            onNetworkStateChanged(it)
        }
        init(savedInstanceState)
    }

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

    private fun init(savedInstanceState: Bundle?) {
        //初始化控件
        initView(savedInstanceState)
        // 设置监听
        setOnViewClick()
        // 数据观察
        createObserver()
        // 显示loading
        showLoading()
    }

    private fun showLoading() {
        // 开始loading
        startLoading()
        // 销毁loading
        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    finishLoading()
                }
            }
        })
    }

    private fun initViewBinding(): View {
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

    protected abstract fun initView(savedInstanceState: Bundle?)

    protected abstract fun setOnViewClick()

    protected abstract fun createObserver()

    /**
     * 网络变化监听 子类重写
     */
    open fun onNetworkStateChanged(netState: NetWorkState) {
        if (!netState.isSuccess) {
            Toasty.error(getStringRes(com.zhang.myproject.resource.R.string.net_error))
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

    }


}

