package com.zhang.home.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhang.home.R
import com.zhang.home.databinding.FragmentHomeBinding
import com.zhang.myproject.base.fragment.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply { arguments = Bundle().apply {} }
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun initData() {
    }

    override fun setOnViewClick() {
    }
}