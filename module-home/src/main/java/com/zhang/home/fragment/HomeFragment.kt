package com.zhang.home.fragment

import android.os.Bundle
import com.zhang.home.R
import com.zhang.home.databinding.FragmentHomeBinding
import com.zhang.myproject.base.fragment.BaseVBFragment
import com.zhang.myproject.base.utils.initToolbarBarHeight
import com.zhang.myproject.base.utils.singleClick
import com.zhang.myproject.common.utils.getStringRes

class HomeFragment : BaseVBFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment().apply { arguments = Bundle().apply {} }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mViewBinding.apply {
            vToolbar.vvImmersionView.initToolbarBarHeight()
            vToolbar.tvPageTitle.text = getStringRes(com.zhang.myproject.resource.R.string.main_tab_home)
            tvHome.singleClick {
            }
        }
    }

    override fun setOnViewClick() {
        mViewBinding.apply {

        }
    }

}