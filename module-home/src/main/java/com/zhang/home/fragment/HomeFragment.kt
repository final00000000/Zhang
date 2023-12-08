package com.zhang.home.fragment

import android.os.Bundle
import com.zhang.home.R
import com.zhang.home.databinding.FragmentHomeBinding
import com.zhang.myproject.base.fragment.BaseFragment
import com.zhang.myproject.base.utils.singleClick

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment().apply { arguments = Bundle().apply {} }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mViewBinding.apply {
            tvHome.singleClick {
            }
        }
    }

    override fun initData() {
        mViewBinding.apply {

        }
    }

    override fun setOnViewClick() {
        mViewBinding.apply {

        }
    }

}