package com.zhang.myproject.common.utils

import com.therouter.TheRouter
import com.zhang.myproject.common.constant.TheRouterConstant

/**
 * Date: 2024/7/10
 * Author : Zhang
 * Description :
 */
object TheRouterUtils {

    fun goTodayInHistory() {
        TheRouter.build(TheRouterConstant.TODAY_IN_HISTORY).navigation()
    }

    fun goTodayInHistoryDetails(picUrl: String?, details: String?) {
        TheRouter.build(TheRouterConstant.TODAY_IN_HISTORY_DETAILS)
            .withString("picUrl", picUrl)
            .withString("details", details)
            .navigation()
    }
}