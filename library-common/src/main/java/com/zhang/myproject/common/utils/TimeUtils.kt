package com.zhang.myproject.common.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

/**
 * 时间工具类
 */
object TimeUtils {
    /**
     * 将时间戳转换为时间
     */
    fun stampToTime(s: Long): String {
        val res: String
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        //将时间戳转换为时间
        val date = Date(s)
        //将时间调整为yyyy-MM-dd HH:mm:ss时间样式
        res = simpleDateFormat.format(date)
        return res
    }

    /**
     * 将时间戳转换为时间
     */
    @SuppressLint("SimpleDateFormat")
    fun stampToTime(s: Long, format: String? = "yyyy-MM-dd HH:mm:ss"): String {
        val res: String
        val simpleDateFormat = SimpleDateFormat(format)
        //将时间戳转换为时间
        val date = Date(s)
        //将时间调整为yyyy-MM-dd HH:mm:ss时间样式
        res = simpleDateFormat.format(date)
        return res
    }

    /**
     * @Author：WJD
     * @description：格式化秒为时分秒的格式
     */
    fun timeConversion(time: Int): String {
        var hour = 0
        var minutes = 0
        var sencond = 0
        val temp = time % 3600
        if (time >= 3600) {
            hour = time / 3600
            if (temp != 0) {
                if (temp > 60) {
                    minutes = temp / 60
                    if (temp % 60 != 0) {
                        sencond = temp % 60
                    }
                } else {
                    sencond = temp
                }
            }
        } else {
            minutes = time / 60
            if (time % 60 != 0) {
                sencond = time % 60
            }
        }
        return (if (hour < 10) "0$hour" else hour).toString() + ":" + (if (minutes < 10) "0$minutes" else minutes) + ":" + if (sencond < 10) "0$sencond" else sencond
    }

    /**
     * 不带秒
     *
     * @param mss
     */
    fun formatDateTime(mss: Long): String? {
        var DateTimes: String? = null
        val days = mss / (60 * 60 * 24)
        val hours = mss % (60 * 60 * 24) / (60 * 60)
        val minutes = mss % (60 * 60) / 60
        val seconds = mss % 60
        if (days > 0) {
            DateTimes = days.toString() + "天" + hours + "时" + minutes + "分"
        } else if (hours > 0) {
            DateTimes = hours.toString() + "时" + minutes + "分"
        } else if (minutes > 0) {
            DateTimes = minutes.toString() + "分钟"
        }
        return DateTimes
    }

    /**
     * 不带秒
     *
     * @param mss
     */
    fun formatDateTime1(mss: Int): String {
        val DateTimes: String
        val days = (mss / (60 * 60 * 24)).toLong()
        val hours = (mss % (60 * 60 * 24) / (60 * 60)).toLong()
        val minutes = (mss % (60 * 60) / 60).toLong()
        val seconds = (mss % 60).toLong()
        DateTimes = if (days > 0) {
            days.toString() + "天" + hours + "时" + minutes + "分"
        } else if (hours > 0) {
            hours.toString() + "时" + minutes + "分"
        } else if (minutes > 0) {
            minutes.toString() + "分钟"
        } else if (seconds > 0 && seconds <= 60) {
            "1分钟"
        } else {
            "0分钟"
        }
        return DateTimes
    }

    /**
     * 检查当前本地时间是否在给定的时间段内
     *
     * @param startTime 开始时间，格式为 "yyyy-MM-dd HH:mm:ss"
     * @param endTime   结束时间，格式为 "yyyy-MM-dd HH:mm:ss"
     * @return 如果当前时间在给定范围内，则为 true，否则为 false
     */
    fun isCurrentTimeWithinRange(startTime: String?, endTime: String?): Boolean {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val startDateTime = LocalDateTime.parse(startTime, formatter)
        val endDateTime = LocalDateTime.parse(endTime, formatter)
        val currentDateTime = LocalDateTime.now()
        return currentDateTime.isAfter(startDateTime) && currentDateTime.isBefore(endDateTime)
    }
}
