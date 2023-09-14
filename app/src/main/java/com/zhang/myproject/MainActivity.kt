package com.zhang.myproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.angcyo.tablayout.delegate2.ViewPager2Delegate
import com.gyf.immersionbar.ImmersionBar
import com.zhang.home.fragment.HomeFragment
import com.zhang.mine.MineFragment
import com.zhang.myproject.adapter.ViewPager2Adapter
import com.zhang.myproject.base.activity.BaseNetWorkActivity
import com.zhang.myproject.databinding.ActivityMainBinding

/**
 * Date: 2023/7/6
 * Author : Zhang
 * Description :
 */
class MainActivity :
    BaseNetWorkActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main) {

    override fun isLayoutToolbar(): Boolean = false

    private var fragmentList = mutableListOf<Fragment>(
        HomeFragment.newInstance(),
        MineFragment.newInstance(),
    )

    override fun initView(savedInstanceState: Bundle?) {
        mViewBinding.apply {
            ViewPager2Delegate.install(mainViewPager, tabLayout)

            val viewPager2Adapter = ViewPager2Adapter(this@MainActivity, fragmentList)
            mainViewPager.adapter = viewPager2Adapter
        }
    }

    override fun setOnViewClick() {
    }

    override fun createObserver() {
    }
}