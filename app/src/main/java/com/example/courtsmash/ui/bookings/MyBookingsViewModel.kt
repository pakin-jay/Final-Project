package com.example.courtsmash.ui.bookings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courtsmash.data.repository.BookingRepository
import com.example.courtsmash.domain.model.Booking
import com.example.courtsmash.domain.model.BookingStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MyBookingsUiState(
    val selectedTab: BookingStatus = BookingStatus.UPCOMING,
    val upcomingBookings: List<Booking> = emptyList(),
    val pastBookings: List<Booking> = emptyList(),
    val cancelledBookings: List<Booking> = emptyList(),
    val isLoading: Boolean = false
)

class MyBookingsViewModel(
    private val bookingRepository: BookingRepository
) : ViewModel() {

    private val _selectedTab = MutableStateFlow(BookingStatus.UPCOMING)

    val uiState: StateFlow<MyBookingsUiState> = combine(
        bookingRepository.allBookings,
        _selectedTab
    ) { bookings, tab ->
        MyBookingsUiState(
            selectedTab = tab,
            upcomingBookings = bookings.filter { it.status == BookingStatus.UPCOMING },
            pastBookings = bookings.filter { it.status == BookingStatus.COMPLETED },
            cancelledBookings = bookings.filter { it.status == BookingStatus.CANCELLED },
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = MyBookingsUiState()
    )

    fun onTabSelected(status: BookingStatus) {
        _selectedTab.value = status
    }

    fun cancelBooking(bookingId: String) {
        viewModelScope.launch {
            bookingRepository.cancelBooking(bookingId)
            _selectedTab.value = BookingStatus.CANCELLED
        }
    }

    fun deleteBooking(bookingId: String) {
        viewModelScope.launch {
            bookingRepository.deleteBooking(bookingId)
        }
    }
}
