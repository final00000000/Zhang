package com.zhang.myproject.amap.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.scopeNetLife
import com.drake.net.Get
import com.zhang.myproject.amap.data.LocationData
import com.zhang.myproject.base.api.WeatherApiConstant
import com.zhang.myproject.base.model.BaseViewModel

/**
 * Date: 2024/11/4
 * Author : Zhang
 * Description :
 */
class AMapViewModel : BaseViewModel() {
    private var _locationData = MutableLiveData<LocationData>()
    val locationData : MutableLiveData<LocationData> get() = _locationData
    override fun initViewModel() {

    }

    fun getLatLng(lat: Double, lon: Double) {
        scopeNetLife {
            Get<LocationData>(WeatherApiConstant.WEATHER) {
                param("lat", lat)
                param("lon", lon)
            }.await().apply {
                _locationData.value = this
            }
        }
    }
}