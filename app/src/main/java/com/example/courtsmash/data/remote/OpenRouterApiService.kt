package com.example.courtsmash.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

data class OpenRouterMessage(
    val role: String,
    val content: String
)

data class OpenRouterResponseFormat(
    val type: String = "json_object"
)

data class OpenRouterRequest(
    val model: String = "google/gemini-2.0-flash-lite-001",
    val messages: List<OpenRouterMessage>,
    @SerializedName("response_format")
    val responseFormat: OpenRouterResponseFormat? = OpenRouterResponseFormat("json_object")
)

data class OpenRouterChoice(
    val message: OpenRouterMessage?
)

data class OpenRouterResponse(
    val choices: List<OpenRouterChoice>?
)

interface OpenRouterApiService {
    @POST("api/v1/chat/completions")
    suspend fun createChatCompletion(
        @Header("Authorization") authorization: String,
        @Header("HTTP-Referer") referer: String = "https://courtsmash.example.com",
        @Header("X-Title") title: String = "CourtSmash",
        @Body request: OpenRouterRequest
    ): OpenRouterResponse
}
