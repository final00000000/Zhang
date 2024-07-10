package com.zhang.home.fragment.ui.activity

import android.content.Intent
import android.os.Bundle
import coil.load
import com.drake.brv.BindingAdapter
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.zhang.home.R
import com.zhang.home.databinding.ActivityTodayInHistoryBinding
import com.zhang.home.databinding.ItemTodayInHistoryBinding
import com.zhang.home.fragment.data.ToDayData
import com.zhang.home.fragment.model.TodayInHistoryViewModel
import com.zhang.myproject.base.activity.BaseVBVMActivity
import com.zhang.myproject.common.ktx.moduleToJson

/**
 * Date: 2024/1/16
 * Author : Zhang
 * Description :
 */
//@Route(path = TheRouterConstant.TODAY_IN_HISTORY)
class TodayInHistoryActivity :
    BaseVBVMActivity<ActivityTodayInHistoryBinding, TodayInHistoryViewModel>(R.layout.activity_today_in_history) {

    private var mAdapter: BindingAdapter? = null

    override fun initView(savedInstanceState: Bundle?) {
        mViewBinding.apply {
            setToolbarTitle(R.string.today_in_history)
            mViewModel.initViewModel()
            initRV()
        }
    }

    override fun setOnViewClick() {

    }

    private fun initRV() {
        mViewBinding.apply {
            sRefresh.onRefresh {
                mAdapter = rvData.linear().setup {
                    addType<ToDayData>(R.layout.item_today_in_history)
                    onBind {
                        val binding = getBinding<ItemTodayInHistoryBinding>()
                        binding.apply {
                            ivIcon.load(getModel<ToDayData>().picUrl) {
                                placeholder(R.drawable.icon_default_image)
                                error(R.drawable.icon_default_image)
                            }
                            tvTitle.text = getModel<ToDayData>().title
                            tvContent.text = getModel<ToDayData>().details
                        }
                    }
                    R.id.cl_root.onClick {
                        val data = mAdapter?._data?.get(layoutPosition) as ToDayData
                        startActivity(Intent(this@TodayInHistoryActivity, TodayInHistoryDetailActivity::class.java).apply {
                            putExtra("data", moduleToJson(data))
                        })
                    }
                }
            }.showLoading()
        }
    }

    override fun createObserver() {
        mViewBinding.apply {
            mViewModel.toDayLiveData.observe(this@TodayInHistoryActivity) {
                finishLoading()
                mAdapter?.models = it
                sRefresh.showContent()
            }
        }
    }
}