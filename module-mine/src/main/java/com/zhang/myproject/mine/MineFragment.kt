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
        setToolbarTitle(com.zhang.myproject.resource.R.string.main_tab_mine)
    }

    override fun setOnViewClick() {
        mViewBinding.apply {
        }
    }
}