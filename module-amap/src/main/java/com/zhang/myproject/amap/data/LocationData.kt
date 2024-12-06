package com.zhang.myproject.amap.data

/**
 * Date: 2024/11/4
 * Author : Zhang
 * Description :
 */
data class LocationData(
    val current: Current,
    val daily: MutableList<Daily>,
    val hourly: MutableList<Hourly>,
    val lat: String,
    val location: Location,
    val lon: String,
    val sun: Sun,
    val today: Today
)

data class Current(
    val airQuality: AirQuality,
    val description: String,
    val feelsLike: Int,
    val temperature: Int
)

data class Daily(
    val date: String,
    val dayDescription: String,
    val high: Int,
    val low: Int,
    val nightDescription: String,
    val rainProbability: String
)

data class Hourly(
    val rainProbability: String,
    val temperature: Int,
    val time: String
)

data class Location(
    val city: String,
    val region: String
)

data class Sun(
    val duration: String,
    val sunrise: String,
    val sunset: String
)

data class Today(
    val day: String,
    val description: String,
    val high: Int,
    val low: Int,
    val night: String,
    val nightDescription: String
)

data class AirQuality(
    val category: String,
    val statement: String
)