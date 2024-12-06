package com.zhang.myproject.common.utils

import android.os.Handler
import android.os.Looper
import android.os.Message
import java.util.Timer
import java.util.TimerTask

/**
 * Date: 2023/6/25
 * Author : Zhang
 * Description : 计时器工具类
 */
object TimerHelper {

    private var timerCallBack: TimerListener? = null
    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    private var mCount = 0


    fun startTimer(delay: Long, count: Int = 0) {
        stopTimer()
        mCount = count
        timerTask = object : TimerTask() {
            override fun run() {
                mCount++
                timerHandler.sendMessage(timerHandler.obtainMessage(0, mCount))
            }
        }
        timer = Timer()
        timer?.schedule(timerTask, delay, delay)
    }

    fun stopTimer() {
        timer?.cancel()
        mCount = 0
        timer = null
        timerTask = null
        timerHandler.removeCallbacksAndMessages(null)
    }

    fun timerListener(timer: TimerListener) {
        this.timerCallBack = timer
    }

    private var timerHandler = object : Handler(Looper.myLooper() ?: Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 0) {
                timerCallBack?.timerListener(msg.obj.toString().toLong())
            }
        }
    }
}

interface TimerListener {
    fun timerListener(timer: Long)
}
