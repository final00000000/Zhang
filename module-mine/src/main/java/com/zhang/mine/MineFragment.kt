package com.zhang.mine

import android.os.Bundle
import com.zhang.mine.databinding.FragmentMineBinding
import com.zhang.myproject.base.fragment.BaseFragment

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
    }
}