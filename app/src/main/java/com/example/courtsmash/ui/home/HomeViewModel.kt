package com.example.courtsmash.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courtsmash.data.local.CourtSeedData
import com.example.courtsmash.data.repository.BookingRepository
import com.example.courtsmash.data.repository.CourtRepository
import com.example.courtsmash.data.repository.WeatherRepository
import com.example.courtsmash.data.repository.WeatherResult
import com.example.courtsmash.domain.model.Booking
import com.example.courtsmash.domain.model.BookingStatus
import com.example.courtsmash.domain.model.Court
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val searchQuery: String = "",
    val selectedFacility: String = "All",
    val courts: List<Court> = emptyList(),
    val filteredCourts: List<Court> = emptyList(),
    val upcomingBooking: Booking? = null,
    val weatherResult: WeatherResult = WeatherResult.Loading,
    val isLoading: Boolean = false
)

class HomeViewModel(
    private val courtRepository: CourtRepository,
    private val bookingRepository: BookingRepository,
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedFacility = MutableStateFlow("All")
    private val _weatherState = MutableStateFlow<WeatherResult>(WeatherResult.Loading)

    init {
        viewModelScope.launch {
            courtRepository.ensureSeedData()
            loadWeather()
        }
    }

    val uiState: StateFlow<HomeUiState> = combine(
        courtRepository.allCourts.catch { emit(CourtSeedData.sampleCourts) },
        bookingRepository.allBookings.catch { emit(emptyList()) },
        _searchQuery,
        _selectedFacility,
        _weatherState
    ) { courts, bookings, query, facility, weather ->
        val activeCourts = if (courts.isNotEmpty()) courts else CourtSeedData.sampleCourts

        val filtered = activeCourts.filter { court ->
            val matchesQuery = query.isBlank() ||
                    court.name.contains(query, ignoreCase = true) ||
                    court.address.contains(query, ignoreCase = true)

            val matchesFacility = facility == "All" ||
                    court.facilities.any { it.contains(facility, ignoreCase = true) }

            matchesQuery && matchesFacility
        }

        val upcoming = bookings.firstOrNull { it.status == BookingStatus.UPCOMING }

        HomeUiState(
            searchQuery = query,
            selectedFacility = facility,
            courts = activeCourts,
            filteredCourts = filtered,
            upcomingBooking = upcoming,
            weatherResult = weather,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = HomeUiState(
            courts = CourtSeedData.sampleCourts,
            filteredCourts = CourtSeedData.sampleCourts,
            isLoading = false
        )
    )

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onFacilitySelected(facility: String) {
        _selectedFacility.value = facility
    }

    fun loadWeather() {
        viewModelScope.launch {
            _weatherState.value = WeatherResult.Loading
            _weatherState.value = weatherRepository.getWeatherForLocation(13.7563, 100.5018, "Bangkok")
        }
    }
}
