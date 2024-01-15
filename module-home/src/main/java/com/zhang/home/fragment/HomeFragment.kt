package com.zhang.home.fragment

import android.os.Bundle
import com.zhang.home.R
import com.zhang.home.databinding.FragmentHomeBinding
import com.zhang.myproject.base.fragment.BaseVBVMFragment
import com.zhang.myproject.base.utils.initToolbarBarHeight
import com.zhang.myproject.base.utils.singleClick
import com.zhang.myproject.common.utils.getStringRes

class HomeFragment : BaseVBVMFragment<FragmentHomeBinding, HomeViewModel>(R.layout.fragment_home) {

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment().apply { arguments = Bundle().apply {} }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mViewBinding.apply {
            vToolbar.vvImmersionView.initToolbarBarHeight()
            vToolbar.tvPageTitle.text = getStringRes(R.string.main_tab_home)
            tvHome.singleClick {
                mViewModel.getData()
            }
        }
    }

    override fun setOnViewClick() {
        mViewBinding.apply {

        }
    }

    override fun createObserver() {
    }

}