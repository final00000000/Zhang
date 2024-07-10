package com.zhang.home.fragment.ui.activity

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import coil.load
import com.zhang.home.R
import com.zhang.home.databinding.ActivityTodayInHistoryDetailBinding
import com.zhang.home.fragment.data.ToDayData
import com.zhang.home.fragment.model.TodayInHistoryDetailViewModel
import com.zhang.myproject.base.activity.BaseVBVMActivity
import com.zhang.myproject.common.ktx.jsonToModule
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Date: 2024/7/10
 * Author : Zhang
 * Description :
 */
//@Route(path = TheRouterConstant.TODAY_IN_HISTORY_DETAILS)
class TodayInHistoryDetailActivity :
    BaseVBVMActivity<ActivityTodayInHistoryDetailBinding, TodayInHistoryDetailViewModel>(R.layout.activity_today_in_history_detail) {

    override fun initView(savedInstanceState: Bundle?) {
        val data = intent?.getStringExtra("data")
        data?.let {
            val toDayData = jsonToModule(data, ToDayData::class.java)
            mViewBinding.ivImg.load(toDayData.picUrl) {
                placeholder(R.drawable.bg_error)
                error(R.drawable.bg_error)
            }
            mViewBinding.tvData.text = toDayData.details
        }
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