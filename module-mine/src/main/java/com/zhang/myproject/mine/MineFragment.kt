package com.zhang.myproject.mine

import android.content.Intent
import android.os.Bundle
import com.zhang.myproject.base.fragment.BaseFragment
import com.zhang.myproject.base.utils.singleClick
import com.zhang.myproject.mine.databinding.FragmentMineBinding

class MineFragment : BaseFragment<FragmentMineBinding>(R.layout.fragment_mine) {
    companion object {
        @JvmStatic
        fun newInstance() =
                MineFragment().apply { arguments = Bundle().apply {} }
    }

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun initData() {
    }

    override fun setOnViewClick() {
        mViewBinding.apply {
        }
    }
}