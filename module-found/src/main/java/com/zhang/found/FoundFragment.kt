package com.zhang.found

import android.os.Bundle
import com.zhang.found.databinding.FragmentFoundBinding
import com.zhang.myproject.base.fragment.BaseFragment

class FoundFragment : BaseFragment<FragmentFoundBinding>(R.layout.fragment_found) {
    companion object {
        @JvmStatic
        fun newInstance() =
            FoundFragment().apply { arguments = Bundle().apply {} }
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun initData() {
    }

    override fun setOnViewClick() {
    }
}