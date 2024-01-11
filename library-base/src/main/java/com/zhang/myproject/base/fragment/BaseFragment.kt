package com.zhang.myproject.base.fragment

import androidx.fragment.app.Fragment

/**
 * @Author : zhang
 * @Create Time : 2022/3/9
 * @Class Describe : 描述
 * @Project Name : MyDemo
 */
abstract class BaseFragment : Fragment() {

    /**
     * 检测 宿主 是否还存活
     */
    open fun isAlive(): Boolean {
        return !(isRemoving || isDetached || activity == null)
    }

}