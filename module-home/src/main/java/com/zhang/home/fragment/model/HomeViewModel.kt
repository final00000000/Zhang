package com.zhang.home.fragment.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.scopeNetLife
import com.drake.net.Get
import com.zhang.home.fragment.data.AccessorInfoData
import com.zhang.home.fragment.data.CityWeatherData
import com.zhang.home.fragment.data.IPData
import com.zhang.myproject.base.api.HanApiConstant
import com.zhang.myproject.base.api.RollApiConstant
import com.zhang.myproject.base.model.BaseViewModel
import kotlinx.coroutines.delay
import timber.log.Timber

/**
 * Date: 2024/1/12
 * Author : Zhang
 * Description :
 */
class HomeViewModel : BaseViewModel() {
    private var _homeLiveData = MutableLiveData<Triple<IPData, CityWeatherData, AccessorInfoData>>()
    val homeLiveData: MutableLiveData<Triple<IPData, CityWeatherData, AccessorInfoData>> get() = _homeLiveData

    override fun initViewModel() {
        getData()
    }

    private fun getData() {
        scopeNetLife {
            val ipData = Get<IPData>(RollApiConstant.ROLL_IP_ADDRESS).await()
            delay(500)
            val cityWeatherData = Get<CityWeatherData>("${RollApiConstant.ROLL_QUERY_CURRENT_CITY_WEATHER}${ipData.city}").await()
            val accessorInfo = Get<AccessorInfoData>(HanApiConstant.VISITOR_INFORMATION).await()
            _homeLiveData.value = Triple(ipData, cityWeatherData, accessorInfo)
        }.catch {
            Timber.d("测试_37：${it.message}")
        }
    }

}