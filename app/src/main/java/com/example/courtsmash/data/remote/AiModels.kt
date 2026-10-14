package com.example.courtsmash.data.remote

import com.google.gson.annotations.SerializedName

data class CandidateCourtDto(
    val id: String,
    val name: String,
    val pricePerHour: Int,
    val openingTime: String,
    val closingTime: String,
    val facilities: List<String>
)

data class AiRecommendationRequestDto(
    val budgetThb: Int,
    val date: String,
    val startTime: String,
    val durationHours: Int,
    val preferredLocation: String,
    val preferredFacilities: List<String>,
    val candidateCourts: List<CandidateCourtDto>
)

data class RecommendedCourtDto(
    val courtId: String,
    val matchScore: Int,
    val reasoning: String,
    val highlights: List<String>
)

data class AiRecommendationResponseDto(
    val recommendations: List<RecommendedCourtDto>,
    val assistantSummary: String
)
