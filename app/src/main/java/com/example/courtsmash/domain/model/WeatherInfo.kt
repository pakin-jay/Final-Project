package com.example.courtsmash.domain.model

data class WeatherInfo(
    val temperature: Double,
    val weatherCode: Int,
    val weatherDescription: String,
    val windSpeed: Double,
    val isDay: Boolean,
    val cityName: String = "Bangkok"
)
