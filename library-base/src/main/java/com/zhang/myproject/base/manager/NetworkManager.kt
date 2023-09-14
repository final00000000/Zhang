package com.zhang.myproject.base.manager

import com.zhang.myproject.base.data.NetWorkState


/**
 *  网络变化管理者
 */
class NetworkManager private constructor() {

    val mNetworkStateCallback = EventLiveData<NetWorkState>()

    companion object {
        val instance: NetworkManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkManager()
        }
    }

}