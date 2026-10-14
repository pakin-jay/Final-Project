package com.example.courtsmash.data.repository

import com.example.courtsmash.data.remote.AiRecommendationRequestDto
import com.example.courtsmash.data.remote.AiRecommendationResponseDto
import com.example.courtsmash.data.remote.CandidateCourtDto
import com.example.courtsmash.data.remote.NetworkClient
import com.example.courtsmash.data.remote.OpenRouterMessage
import com.example.courtsmash.data.remote.OpenRouterRequest
import com.example.courtsmash.domain.model.AiRecommendation
import com.example.courtsmash.domain.model.AiRecommendationResponse
import com.example.courtsmash.domain.model.Court
import com.google.gson.Gson

sealed class AiResult {
    data class Success(val response: AiRecommendationResponse) : AiResult()
    data class Error(val message: String, val isConfigError: Boolean = false) : AiResult()
    object Loading : AiResult()
}

class AiRepository {

    private val gson = Gson()

    suspend fun getCourtRecommendations(
        budgetThb: Int,
        bookingDate: String,
        startTime: String,
        durationHours: Int,
        preferredLocation: String,
        preferredFacilities: List<String>,
        candidateCourts: List<Court>,
        apiKey: String = NetworkClient.openRouterApiKey
    ): AiResult {
        if (candidateCourts.isEmpty()) {
            return AiResult.Error("No candidate courts available for recommendations.")
        }

        val calculatedDuration = durationHours.coerceAtLeast(1)
        val minPrice = candidateCourts.minOf { it.pricePerHour }
        val minRequiredTotal = minPrice * calculatedDuration

        // Strict budget check: if budget is lower than cheapest court for duration, return empty list immediately
        if (budgetThb < minRequiredTotal) {
            val noMatchSummary = "No courts found within your max budget of ฿$budgetThb for a $calculatedDuration-hour session. The lowest court rate is ฿$minPrice/hr (Total ฿$minRequiredTotal for $calculatedDuration hrs). Please increase your budget or reduce duration."
            return AiResult.Success(
                AiRecommendationResponse(
                    recommendations = emptyList(),
                    assistantSummary = noMatchSummary,
                    isDemoFallback = false
                )
            )
        }

        val candidateDtos = candidateCourts.map { court ->
            CandidateCourtDto(
                id = court.id,
                name = court.name,
                pricePerHour = court.pricePerHour,
                openingTime = court.openingTime,
                closingTime = court.closingTime,
                facilities = court.facilities
            )
        }

        val requestDto = AiRecommendationRequestDto(
            budgetThb = budgetThb,
            date = bookingDate,
            startTime = startTime,
            durationHours = calculatedDuration,
            preferredLocation = preferredLocation,
            preferredFacilities = preferredFacilities,
            candidateCourts = candidateDtos
        )

        // Attempt live OpenRouter AI API call
        if (apiKey.isNotBlank()) {
            try {
                val liveAiResult = callOpenRouterAiApi(requestDto, candidateCourts, apiKey)
                if (liveAiResult != null) {
                    return AiResult.Success(liveAiResult)
                }
            } catch (e: Exception) {
                // Fall back to local recommendation algorithm if OpenRouter fails or times out
            }
        }

        return generateLocalFallbackRecommendations(requestDto, candidateCourts)
    }

    private suspend fun callOpenRouterAiApi(
        request: AiRecommendationRequestDto,
        candidateCourts: List<Court>,
        apiKey: String
    ): AiRecommendationResponse? {
        val systemPrompt = """
            You are CourtSmash AI Assistant, an expert badminton court recommendation engine.
            Analyze the user request and candidate courts. Evaluate budget, duration, operating hours, and requested amenities.
            STRICT RULE: ONLY recommend courts where (court.pricePerHour * durationHours) <= budgetThb.
            If a court's total price exceeds budgetThb, DO NOT include it in recommendations.
            Rank qualifying courts from best match to worst match.
            You MUST output ONLY a valid JSON object matching this schema:
            {
              "assistantSummary": "A concise 2-3 sentence AI summary explaining the top court recommendations based on requested budget and amenities.",
              "recommendations": [
                {
                  "courtId": "court_id_string",
                  "matchScore": 95,
                  "reasoning": "Reason why this court fits user criteria.",
                  "highlights": ["Amenity 1", "Amenity 2"]
                }
              ]
            }
        """.trimIndent()

        val candidateSummary = request.candidateCourts.joinToString("\n") { c ->
            "- ID: ${c.id}, Name: ${c.name}, Price: ฿${c.pricePerHour}/hr, Total for ${request.durationHours}h: ฿${c.pricePerHour * request.durationHours}, Amenities: ${c.facilities.joinToString(", ")}"
        }

        val userPrompt = """
            User Preferences:
            - Max Session Budget: ฿${request.budgetThb}
            - Requested Date: ${request.date}
            - Start Time: ${request.startTime}
            - Session Duration: ${request.durationHours} hours
            - Preferred Area: ${request.preferredLocation}
            - Preferred Amenities: ${request.preferredFacilities.joinToString(", ")}

            Candidate Courts Available:
            $candidateSummary
        """.trimIndent()

        val messages = listOf(
            OpenRouterMessage("system", systemPrompt),
            OpenRouterMessage("user", userPrompt)
        )

        val routerRequest = OpenRouterRequest(
            model = "google/gemini-2.0-flash-lite-001",
            messages = messages
        )

        val authHeader = "Bearer $apiKey"
        val response = NetworkClient.openRouterApiService.createChatCompletion(
            authorization = authHeader,
            request = routerRequest
        )

        val jsonContent = response.choices?.firstOrNull()?.message?.content ?: return null
        val dtoResponse = gson.fromJson(jsonContent, AiRecommendationResponseDto::class.java) ?: return null

        val courtMap = candidateCourts.associateBy { it.id }
        val validatedRecommendations = mutableListOf<AiRecommendation>()

        for (rec in dtoResponse.recommendations) {
            val matchedCourt = courtMap[rec.courtId]
            if (matchedCourt != null) {
                val calcPrice = matchedCourt.pricePerHour * request.durationHours
                // Strict budget enforcement: Exclude if calcPrice > budgetThb
                if (calcPrice <= request.budgetThb) {
                    validatedRecommendations.add(
                        AiRecommendation(
                            courtId = matchedCourt.id,
                            courtName = matchedCourt.name,
                            matchScore = rec.matchScore.coerceIn(1, 100),
                            reasoning = rec.reasoning,
                            estimatedPrice = calcPrice,
                            highlights = rec.highlights
                        )
                    )
                }
            }
        }

        return AiRecommendationResponse(
            recommendations = validatedRecommendations,
            assistantSummary = if (validatedRecommendations.isEmpty()) {
                "No courts qualify within your ฿${request.budgetThb} budget for a ${request.durationHours}-hour session."
            } else {
                dtoResponse.assistantSummary
            },
            isDemoFallback = false
        )
    }

    private fun generateLocalFallbackRecommendations(
        request: AiRecommendationRequestDto,
        candidateCourts: List<Court>
    ): AiResult {
        val calculatedDuration = request.durationHours.coerceAtLeast(1)
        
        // Strictly filter courts that fit within budget
        val matchingCourts = candidateCourts.filter { court ->
            (court.pricePerHour * calculatedDuration) <= request.budgetThb
        }

        if (matchingCourts.isEmpty()) {
            val minPrice = candidateCourts.minOf { it.pricePerHour }
            val minRequiredTotal = minPrice * calculatedDuration
            val emptySummary = "No courts available for ฿${request.budgetThb} budget and ${calculatedDuration}h session. Lowest court rate is ฿${minPrice}/hr (฿${minRequiredTotal} total)."
            return AiResult.Success(
                AiRecommendationResponse(
                    recommendations = emptyList(),
                    assistantSummary = emptySummary,
                    isDemoFallback = true
                )
            )
        }

        val ranked = matchingCourts.map { court ->
            val totalCost = court.pricePerHour * calculatedDuration
            var score = 75

            val matchingFacilities = court.facilities.intersect(request.preferredFacilities.toSet()).size
            score += (matchingFacilities * 6)
            score = (score + (court.rating * 3).toInt()).coerceIn(65, 99)

            val reasoning = buildString {
                append("${court.name} costs ฿${totalCost} for ${calculatedDuration} hours (฿${court.pricePerHour}/hr), fitting inside your ฿${request.budgetThb} budget. ")
                if (matchingFacilities > 0) {
                    append("Offers requested amenities: ${court.facilities.intersect(request.preferredFacilities.toSet()).joinToString(", ")}.")
                }
            }

            AiRecommendation(
                courtId = court.id,
                courtName = court.name,
                matchScore = score,
                reasoning = reasoning,
                estimatedPrice = totalCost,
                highlights = court.facilities.take(3)
            )
        }.sortedByDescending { it.matchScore }

        val summary = "AI Engine matched ${ranked.size} courts that fit within your ฿${request.budgetThb} budget for a ${calculatedDuration}h session."

        return AiResult.Success(
            AiRecommendationResponse(
                recommendations = ranked,
                assistantSummary = summary,
                isDemoFallback = true
            )
        )
    }
}
