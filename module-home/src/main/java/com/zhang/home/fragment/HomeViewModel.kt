package com.zhang.home.fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.scopeNetLife
import com.drake.net.Get
import com.zhang.home.fragment.data.ToDayData
import com.zhang.myproject.base.RollApiConstant
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Date: 2024/1/12
 * Author : Zhang
 * Description :
 */
class HomeViewModel : ViewModel() {


    fun getData() {
        scopeNetLife {
            val await = Get<ToDayData>(RollApiConstant.ROLL_TO_DAY_API) {
                param("type", 1)
            }.await()
            Log.e("测试", "测试_26：$await");
        }
    }
}