package com.zhang.myproject.base.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.therouter.TheRouter

/**
 * @Author : zhang
 * @Create Time : 2022/3/9
 * @Class Describe : 描述
 * @Project Name : MyDemo
 */
abstract class BaseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheRouter.inject(this)
    }
    /**
     * 检测 宿主 是否还存活
     */
    open fun isAlive(): Boolean {
        return !(isRemoving || isDetached || activity == null)
    }

}