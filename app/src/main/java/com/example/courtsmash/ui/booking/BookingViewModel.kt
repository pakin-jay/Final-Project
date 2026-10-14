package com.example.courtsmash.ui.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courtsmash.data.repository.BookingRepository
import com.example.courtsmash.data.repository.BookingValidationResult
import com.example.courtsmash.data.repository.CourtRepository
import com.example.courtsmash.domain.model.Court
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class BookingUiState(
    val court: Court? = null,
    val selectedDate: String = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date()),
    val startTime: String = "14:00",
    val durationHours: Int = 1,
    val totalPrice: Int = 0,
    val validationError: String? = null,
    val isSubmitting: Boolean = false,
    val isSuccessBookingId: String? = null,
    val isLoading: Boolean = true
)

class BookingViewModel(
    private val courtRepository: CourtRepository,
    private val bookingRepository: BookingRepository,
    private val courtId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookingUiState())
    val uiState: StateFlow<BookingUiState> = _uiState.asStateFlow()

    init {
        loadCourt()
    }

    private fun loadCourt() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val court = courtRepository.getCourtByIdSync(courtId)
            if (court != null) {
                _uiState.value = _uiState.value.copy(
                    court = court,
                    totalPrice = court.pricePerHour * _uiState.value.durationHours,
                    isLoading = false
                )
                validateCurrentSelection()
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    validationError = "Court not found."
                )
            }
        }
    }

    fun onDateSelected(date: String) {
        _uiState.value = _uiState.value.copy(selectedDate = date)
        validateCurrentSelection()
    }

    fun onTimeSelected(time: String) {
        _uiState.value = _uiState.value.copy(startTime = time)
        validateCurrentSelection()
    }

    fun onDurationChanged(duration: Int) {
        val court = _uiState.value.court
        val price = if (court != null) court.pricePerHour * duration else 0
        _uiState.value = _uiState.value.copy(
            durationHours = duration,
            totalPrice = price
        )
        validateCurrentSelection()
    }

    private fun validateCurrentSelection() {
        val court = _uiState.value.court ?: return
        viewModelScope.launch {
            val result = bookingRepository.validateBooking(
                court = court,
                bookingDate = _uiState.value.selectedDate,
                startTime = _uiState.value.startTime,
                durationHours = _uiState.value.durationHours
            )
            val errorMsg = if (result is BookingValidationResult.Error) result.message else null
            _uiState.value = _uiState.value.copy(validationError = errorMsg)
        }
    }

    fun confirmBooking(onSuccess: (String) -> Unit) {
        val court = _uiState.value.court ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmitting = true)
            val result = bookingRepository.createBooking(
                court = court,
                bookingDate = _uiState.value.selectedDate,
                startTime = _uiState.value.startTime,
                durationHours = _uiState.value.durationHours
            )
            result.onSuccess { booking ->
                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    isSuccessBookingId = booking.id
                )
                onSuccess(booking.id)
            }.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    validationError = exception.localizedMessage ?: "Booking failed. Please try again."
                )
            }
        }
    }
}
