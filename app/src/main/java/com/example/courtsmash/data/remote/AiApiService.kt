package com.example.courtsmash.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

interface AiApiService {
    @POST("api/recommend")
    suspend fun getRecommendations(
        @Body request: AiRecommendationRequestDto
    ): AiRecommendationResponseDto
}
