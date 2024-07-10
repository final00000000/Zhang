package com.zhang.myproject

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import com.angcyo.tablayout.delegate2.ViewPager2Delegate
import com.zhang.found.FoundFragment
import com.zhang.home.fragment.ui.fragment.HomeFragment
import com.zhang.myproject.adapter.ViewPager2Adapter
import com.zhang.myproject.amap.view.fragment.AMapFragment
import com.zhang.myproject.base.activity.BaseVBActivity
import com.zhang.myproject.base.manager.ActivityManager
import com.zhang.myproject.base.utils.toast.Toasty
import com.zhang.myproject.databinding.ActivityMainBinding
import com.zhang.myproject.mine.MineFragment

/**
 * Date: 2023/7/6
 * Author : Zhang
 * Description :
 */
class MainActivity : BaseVBActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun isLayoutToolbar(): Boolean = false

    private var mExitTime: Long = 0

    private var fragmentList = mutableListOf<Fragment>(
        HomeFragment.newInstance(),
        FoundFragment.newInstance(),
        AMapFragment.newInstance(),
        MineFragment.newInstance(),
    )

    override fun initView(savedInstanceState: Bundle?) {
        mViewBinding.apply {
            ViewPager2Delegate.install(mainViewPager, tabLayout)

            val viewPager2Adapter = ViewPager2Adapter(this@MainActivity, fragmentList)
            mainViewPager.isUserInputEnabled = false
            mainViewPager.adapter = viewPager2Adapter
            tabLayout.observeIndexChange { _, toIndex, _, _ ->
                mainViewPager.setCurrentItem(toIndex, false)
                when (toIndex) {
                    0 -> {

                    }

                    1 -> {

                    }

                    2 -> {

                    }

                    3 -> {

                    }
                }
            }
        }
    }

    override fun setOnViewClick() {
    }

//    override fun createObserver() {
//    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit()
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    private fun exit() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            Toasty.info("再按一次退出应用")
            mExitTime = System.currentTimeMillis()
        } else {
            ActivityManager.instance.finishAllActivity()
        }
    }
}