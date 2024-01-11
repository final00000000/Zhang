package com.zhang.found

import android.os.Bundle
import com.zhang.found.databinding.FragmentFoundBinding
import com.zhang.myproject.base.fragment.BaseVBFragment

class FoundFragment : BaseVBFragment<FragmentFoundBinding>(R.layout.fragment_found) {
    companion object {
        @JvmStatic
        fun newInstance() =
            FoundFragment().apply { arguments = Bundle().apply {} }
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun setOnViewClick() {
    }
}