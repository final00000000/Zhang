package com.zhang.home.fragment.ui.fragment

import android.content.Intent
import android.os.Bundle
import com.zhang.home.R
import com.zhang.home.databinding.FragmentHomeBinding
import com.zhang.home.fragment.initHomeShowData
import com.zhang.home.fragment.model.HomeViewModel
import com.zhang.home.fragment.ui.activity.TodayInHistoryActivity
import com.zhang.myproject.base.fragment.BaseVBVMFragment
import com.zhang.myproject.base.utils.initToolbarBarHeight
import com.zhang.myproject.base.utils.singleClick

class HomeFragment : BaseVBVMFragment<FragmentHomeBinding, HomeViewModel>(R.layout.fragment_home) {

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment().apply { arguments = Bundle().apply {} }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mViewBinding.apply {
            mViewModel.initViewModel()
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
        mViewModel.homeLiveData.observe(this) {
            finishLoading()
            it?.let {
                mViewBinding.tvIp.initHomeShowData(it.first, it.second, it.third)
            }
        }
    }

}