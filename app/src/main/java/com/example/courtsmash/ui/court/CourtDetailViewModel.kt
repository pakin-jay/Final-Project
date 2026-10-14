package com.example.courtsmash.ui.court

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courtsmash.data.repository.CourtRepository
import com.example.courtsmash.data.repository.WeatherRepository
import com.example.courtsmash.data.repository.WeatherResult
import com.example.courtsmash.domain.model.Court
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CourtDetailUiState(
    val court: Court? = null,
    val weatherResult: WeatherResult = WeatherResult.Loading,
    val isLoading: Boolean = true,
    val error: String? = null
)

class CourtDetailViewModel(
    private val courtRepository: CourtRepository,
    private val weatherRepository: WeatherRepository,
    private val courtId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(CourtDetailUiState())
    val uiState: StateFlow<CourtDetailUiState> = _uiState.asStateFlow()

    init {
        loadCourtDetails()
    }

    fun loadCourtDetails() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val court = courtRepository.getCourtByIdSync(courtId)
            if (court != null) {
                _uiState.value = _uiState.value.copy(
                    court = court,
                    isLoading = false
                )
                loadWeather(court.latitude, court.longitude, court.name)
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Court details could not be found."
                )
            }
        }
    }

    private fun loadWeather(lat: Double, lng: Double, name: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(weatherResult = WeatherResult.Loading)
            val result = weatherRepository.getWeatherForLocation(lat, lng, name)
            _uiState.value = _uiState.value.copy(weatherResult = result)
        }
    }
}
