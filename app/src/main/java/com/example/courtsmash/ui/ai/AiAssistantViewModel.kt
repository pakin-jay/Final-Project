package com.example.courtsmash.ui.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courtsmash.data.repository.AiRepository
import com.example.courtsmash.data.repository.AiResult
import com.example.courtsmash.data.repository.CourtRepository
import com.example.courtsmash.domain.model.AiRecommendationResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class AiAssistantUiState(
    val budgetThb: Int = 1000,
    val durationHours: Int = 2,
    val selectedDate: String = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date()),
    val startTime: String = "18:00",
    val preferredLocation: String = "Bangkok",
    val selectedFacilities: List<String> = listOf("Air Conditioning", "Yonex Flooring"),
    val aiResponse: AiRecommendationResponse? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class AiAssistantViewModel(
    private val aiRepository: AiRepository,
    private val courtRepository: CourtRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AiAssistantUiState())
    val uiState: StateFlow<AiAssistantUiState> = _uiState.asStateFlow()

    init {
        // Auto fetch initial AI recommendations
        fetchRecommendations()
    }

    fun onBudgetChanged(budget: Int) {
        _uiState.value = _uiState.value.copy(budgetThb = budget)
    }

    fun onDurationChanged(duration: Int) {
        _uiState.value = _uiState.value.copy(durationHours = duration)
    }

    fun onFacilityToggle(facility: String) {
        val current = _uiState.value.selectedFacilities.toMutableList()
        if (current.contains(facility)) {
            current.remove(facility)
        } else {
            current.add(facility)
        }
        _uiState.value = _uiState.value.copy(selectedFacilities = current)
    }

    fun fetchRecommendations() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val courts = courtRepository.allCourts.first()
            val result = aiRepository.getCourtRecommendations(
                budgetThb = _uiState.value.budgetThb,
                bookingDate = _uiState.value.selectedDate,
                startTime = _uiState.value.startTime,
                durationHours = _uiState.value.durationHours,
                preferredLocation = _uiState.value.preferredLocation,
                preferredFacilities = _uiState.value.selectedFacilities,
                candidateCourts = courts
            )

            when (result) {
                is AiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        aiResponse = result.response,
                        isLoading = false
                    )
                }
                is AiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = result.message,
                        isLoading = false
                    )
                }
                is AiResult.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }
}
