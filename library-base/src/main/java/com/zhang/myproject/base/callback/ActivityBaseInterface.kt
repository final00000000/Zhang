package com.zhang.myproject.base.callback

/**
 * @Author : zhang
 * @Create Time : 2021/11/24
 * @Class Describe : 描述
 * @Project Name : MyDemo
 */
interface ActivityBaseInterface {
    /**
     * 是否需要使用带有TitleBar的父容器
     */
    fun isLayoutToolbar(): Boolean

    /**
     * 开始显示加载框
     */
    fun startLoading()

    /**
     * 关闭加载框
     */
    fun finishLoading()

    /**
     * 空视图
     */
    fun showEmptyView()

    /**
     * 无网视图
     */
    fun showNoNetWorkView(netWorkSuccess: Boolean?)

}