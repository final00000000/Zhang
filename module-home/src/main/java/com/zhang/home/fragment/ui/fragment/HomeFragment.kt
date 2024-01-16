package com.zhang.home.fragment.ui.fragment

import android.content.Intent
import android.os.Bundle
import com.zhang.home.R
import com.zhang.home.databinding.FragmentHomeBinding
import com.zhang.home.fragment.model.HomeViewModel
import com.zhang.home.fragment.ui.activity.TodayInHistoryActivity
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
            vSpace.initToolbarBarHeight()
        }
    }

    override fun setOnViewClick() {
        mViewBinding.apply {
            tvTodayInHistory.singleClick {
                startActivity(Intent(requireActivity(), TodayInHistoryActivity::class.java))
            }
        }
    }

    override fun createObserver() {
    }

}