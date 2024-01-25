package com.zhang.home.fragment.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.scopeNetLife
import com.drake.net.Get
import com.zhang.home.fragment.data.CityWeatherData
import com.zhang.home.fragment.data.IPData
import com.zhang.myproject.base.RollApiConstant
import com.zhang.myproject.base.model.BaseViewModel
import kotlinx.coroutines.delay

/**
 * Date: 2024/1/12
 * Author : Zhang
 * Description :
 */
class HomeViewModel : BaseViewModel() {
    private var _homeLiveData = MutableLiveData<Pair<IPData, CityWeatherData>>()
    val homeLiveData: MutableLiveData<Pair<IPData, CityWeatherData>> get() = _homeLiveData

    override fun initViewModel() {
        getData()
    }

    private fun getData() {
        scopeNetLife {
            val ipData = Get<IPData>(RollApiConstant.ROLL_IP_ADDRESS).await()
            delay(1000)
            val cityWeatherData = Get<CityWeatherData>("${RollApiConstant.ROLL_QUERY_CURRENT_CITY_WEATHER}${ipData.city}").await()
            _homeLiveData.value = Pair(ipData, cityWeatherData)
        }
    }

}