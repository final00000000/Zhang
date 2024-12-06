package com.zhang.home.fragment.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.drake.net.Get
import com.drake.net.scope.NetCoroutineScope
import com.zhang.home.fragment.data.AccessorInfoData
import com.zhang.home.fragment.data.CityWeatherData
import com.zhang.home.fragment.data.IPData
import com.zhang.myproject.base.api.HanApiConstant
import com.zhang.myproject.base.api.RollApiConstant
import com.zhang.myproject.base.model.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import timber.log.Timber

/**
 * Date: 2024/1/12
 * Author : Zhang
 * Description :
 */
class HomeViewModel : BaseViewModel() {
    private var _homeLiveData = MutableLiveData<Triple<IPData, CityWeatherData, AccessorInfoData>?>()
    val homeLiveData: MutableLiveData<Triple<IPData, CityWeatherData, AccessorInfoData>?> get() = _homeLiveData

    override fun initViewModel() {
        getData()
    }

    private fun getData() {
        scopeNetLife {
            val ipData = Get<IPData>(RollApiConstant.ROLL_IP_ADDRESS).await()
            delay(1000)
            val cityWeatherData = Get<CityWeatherData>("${RollApiConstant.ROLL_QUERY_CURRENT_CITY_WEATHER}${ipData.city}").await()
            val accessorInfo = Get<AccessorInfoData>(HanApiConstant.VISITOR_INFORMATION).await()
            _homeLiveData.value = Triple(ipData, cityWeatherData, accessorInfo)
        }.catch {
            _homeLiveData.value = null
            Timber.d("测试_37：${it.message}")
        }
    }

}

/**
 * ViewModel升级到282版本 net请求库暂时未兼容 暂时解决防止崩溃 待Net请求库更新
 * @receiver ViewModel
 * @param dispatcher CoroutineDispatcher
 * @param block [@kotlin.ExtensionFunctionType] SuspendFunction1<CoroutineScope, Unit>
 * @return NetCoroutineScope
 */
fun ViewModel.scopeNetLife(
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    block: suspend CoroutineScope.() -> Unit
): NetCoroutineScope {
    val scope = NetCoroutineScope(dispatcher = dispatcher).launch(block)
    addCloseable (scope.toString(), scope)
    return getCloseable(scope.toString()) ?: NetCoroutineScope()
}
