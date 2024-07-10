package com.zhang.myproject.base.utils

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType

/**
 * @Author : zhang
 * @Create Time : 2022/3/23
 * @Class Describe : 描述
 */

/**
 * 获取当前类绑定的泛型ViewModel-clazz
 */
fun <VM> getVmClass(obj: AppCompatActivity): VM {
    return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as VM
}

inline fun <reified VM : ViewModel> AppCompatActivity.viewModelOf(
    factory: ViewModelProvider.Factory
) = ViewModelProvider(this, factory)[VM::class.java]


// 定义一个扩展函数用于创建ViewModel
fun <VM : ViewModel> AppCompatActivity.createViewModel(application: Application): VM {
    val type = (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VM>
    return ViewModelProvider(
        this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    )[type]
}