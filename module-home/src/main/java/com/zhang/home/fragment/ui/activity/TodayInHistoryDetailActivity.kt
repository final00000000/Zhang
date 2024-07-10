package com.zhang.home.fragment.ui.activity

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import coil.load
import com.therouter.router.Route
import com.zhang.home.R
import com.zhang.home.databinding.ActivityTodayInHistoryDetailBinding
import com.zhang.home.fragment.model.TodayInHistoryDetailViewModel
import com.zhang.myproject.base.activity.BaseVBVMActivity
import com.zhang.myproject.common.constant.TheRouterConstant
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Date: 2024/7/10
 * Author : Zhang
 * Description :
 */
@Route(path = TheRouterConstant.TODAY_IN_HISTORY_DETAILS)
class TodayInHistoryDetailActivity :
    BaseVBVMActivity<ActivityTodayInHistoryDetailBinding, TodayInHistoryDetailViewModel>(R.layout.activity_today_in_history_detail) {

    override fun initView(savedInstanceState: Bundle?) {
        val picUrl = intent?.getStringExtra("picUrl")
        val details = intent?.getStringExtra("details")
        mViewBinding.ivImg.load(picUrl) {
            placeholder(R.drawable.icon_default_image)
        }
        mViewBinding.tvData.text = details
        lifecycleScope.launch {
            delay(1000)
            finishLoading()
        }
    }

    override fun setOnViewClick() {
    }

    override fun createObserver() {
    }
}