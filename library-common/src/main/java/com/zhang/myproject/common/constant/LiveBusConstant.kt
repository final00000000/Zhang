package com.zhang.myproject.common.constant

/**
 * Date: 2024/12/6
 * Author : Zhang
 * Description :
 */
object LiveBusConstant {
    object AMap {
        const val SEND_RECORD_TRACK_SERVICE_DATA = "send_record_track_service_data"

        /**
         * 退出，以及切车 token失效 结束骑行GO
         */
        const val EXIT_RECORD_TRACK = "exit_record_track"

        /**
         * 开始时间
         */
        const val RECORD_TRACK_START_TIME = "record_track_start_time"

        /**
         * 骑行GO定时器
         */
        const val RECORD_TRACK_GO_TIMER = "record_track_go_timer"
    }
}