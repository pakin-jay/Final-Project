package com.example.courtsmash.data.repository

import com.example.courtsmash.data.remote.NetworkClient
import com.example.courtsmash.domain.model.WeatherInfo

sealed class WeatherResult {
    data class Success(val weather: WeatherInfo) : WeatherResult()
    data class Error(val message: String) : WeatherResult()
    object Loading : WeatherResult()
}

class WeatherRepository {

    suspend fun getWeatherForLocation(latitude: Double, longitude: Double, cityName: String = "Bangkok"): WeatherResult {
        return try {
            val response = NetworkClient.weatherApiService.getCurrentWeather(latitude, longitude)
            val current = response.currentWeather
            if (current != null) {
                val description = getWeatherDescription(current.weathercode)
                WeatherResult.Success(
                    WeatherInfo(
                        temperature = current.temperature,
                        weatherCode = current.weathercode,
                        weatherDescription = description,
                        windSpeed = current.windspeed,
                        isDay = current.isDay == 1,
                        cityName = cityName
                    )
                )
            } else {
                WeatherResult.Error("Weather data unavailable")
            }
        } catch (e: Exception) {
            WeatherResult.Error("Failed to fetch weather: ${e.localizedMessage ?: "Network error"}")
        }
    }

    private fun getWeatherDescription(code: Int): String {
        return when (code) {
            0 -> "Clear Sky"
            1, 2, 3 -> "Partly Cloudy"
            45, 48 -> "Foggy"
            51, 53, 55 -> "Light Drizzle"
            61, 63, 65 -> "Rainy"
            80, 81, 82 -> "Showers"
            95, 96, 99 -> "Thunderstorm"
            else -> "Fair Weather"
        }
    }
}
