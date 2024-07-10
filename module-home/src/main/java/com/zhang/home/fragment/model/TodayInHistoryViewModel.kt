package com.zhang.home.fragment.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.scopeNetLife
import com.drake.net.Get
import com.zhang.home.fragment.data.ToDayData
import com.zhang.myproject.base.api.RollApiConstant
import com.zhang.myproject.base.model.BaseViewModel
import com.zhang.myproject.common.constant.ApiRequestValue

/**
 * Date: 2024/1/16
 * Author : Zhang
 * Description :
 */
class TodayInHistoryViewModel : BaseViewModel() {

    private var _toDayData = MutableLiveData<MutableList<ToDayData>>()
    val toDayLiveData: MutableLiveData<MutableList<ToDayData>> get() = _toDayData

    override fun initViewModel() {
        getData()
    }

    private fun getData() {
        scopeNetLife {
            val await = Get<MutableList<ToDayData>>(RollApiConstant.ROLL_TO_DAY_API) {
                param(ApiRequestValue.TYPE, ApiRequestValue.TYPE_ONE)
            }.await()
            _toDayData.value = await
        }
    }
}