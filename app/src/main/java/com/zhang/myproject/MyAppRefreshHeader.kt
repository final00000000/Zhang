package com.zhang.myproject

import android.content.Context
import android.widget.FrameLayout
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.constant.SpinnerStyle

class MyAppRefreshHeader(context: Context) : FrameLayout(context), RefreshHeader {

    init {
        initView(context)
    }

    private fun initView(context: Context) {
    }

    override fun onStateChanged(refreshLayout: RefreshLayout, oldState: RefreshState, newState: RefreshState) {
        when (newState) {
            RefreshState.PullDownToRefresh -> {
            }
            RefreshState.PullUpToLoad -> {
            }
            RefreshState.PullDownCanceled -> {
            }
            RefreshState.PullUpCanceled -> {
            }
            else -> {
            }
        }
    }

    override fun getView() = this

    override fun getSpinnerStyle(): SpinnerStyle = SpinnerStyle.Translate

    override fun setPrimaryColors(vararg colors: Int) {
    }

    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {
    }

    override fun onMoving(isDragging: Boolean, percent: Float, offset: Int, height: Int, maxDragHeight: Int) {
    }

    override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
    }

    override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
    }

    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
        return 0
    }

    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {
    }

    override fun isSupportHorizontalDrag() = false
    override fun autoOpen(duration: Int, dragRate: Float, animationOnly: Boolean): Boolean {
       return true
    }
}