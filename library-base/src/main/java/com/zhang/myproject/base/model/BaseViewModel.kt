package com.zhang.myproject.base.model

import androidx.lifecycle.ViewModel

/**
 * Date: 2024/1/16
 * Author : Zhang
 * Description :
 */
abstract class BaseViewModel : ViewModel() {

    abstract fun initViewModel()
}