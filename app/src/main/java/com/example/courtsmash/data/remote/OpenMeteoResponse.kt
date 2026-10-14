package com.example.courtsmash.data.remote

import com.google.gson.annotations.SerializedName

data class OpenMeteoResponse(
    val latitude: Double,
    val longitude: Double,
    @SerializedName("current_weather")
    val currentWeather: CurrentWeatherData?
)

data class CurrentWeatherData(
    val temperature: Double,
    val windspeed: Double,
    val weathercode: Int,
    @SerializedName("is_day")
    val isDay: Int,
    val time: String?
)
