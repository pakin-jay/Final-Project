package com.example.courtsmash.domain.model

data class AiRecommendation(
    val courtId: String,
    val courtName: String,
    val matchScore: Int, // 1 to 100
    val reasoning: String,
    val estimatedPrice: Int,
    val highlights: List<String>
)

data class AiRecommendationResponse(
    val recommendations: List<AiRecommendation>,
    val assistantSummary: String,
    val isDemoFallback: Boolean = false
)
