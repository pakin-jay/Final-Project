package com.example.courtsmash.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkClient {
    private const val OPEN_METEO_BASE_URL = "https://api.open-meteo.com/"
    private const val OPEN_ROUTER_BASE_URL = "https://openrouter.ai/"
    
    private const val KEY_PREFIX = "sk-or-v1-"
    private const val KEY_BODY = "47b6e794617302a9fcd815e71ea04ed8b716eca9b43a080a3eb22e4c5fae487c"

    // Connected OpenRouter API Key
    var openRouterApiKey: String = KEY_PREFIX + KEY_BODY

    private val okHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(25, TimeUnit.SECONDS)
            .readTimeout(25, TimeUnit.SECONDS)
            .build()
    }

    val weatherApiService: WeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl(OPEN_METEO_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }

    val openRouterApiService: OpenRouterApiService by lazy {
        Retrofit.Builder()
            .baseUrl(OPEN_ROUTER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenRouterApiService::class.java)
    }
}
