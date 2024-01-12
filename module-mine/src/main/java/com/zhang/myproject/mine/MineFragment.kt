package com.zhang.myproject.mine

import android.os.Bundle
import com.zhang.myproject.base.fragment.BaseVBFragment
import com.zhang.myproject.mine.databinding.FragmentMineBinding

class MineFragment : BaseVBFragment<FragmentMineBinding>(R.layout.fragment_mine) {
    companion object {
        @JvmStatic
        fun newInstance() =
            MineFragment().apply { arguments = Bundle().apply {} }
    }

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun setOnViewClick() {
        mViewBinding.apply {
        }
    }
}