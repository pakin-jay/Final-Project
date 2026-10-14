package com.example.courtsmash.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courtsmash.data.repository.BookingRepository
import com.example.courtsmash.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ProfileUiState(
    val displayName: String = "Alex Morgan",
    val preferredLocation: String = "Bangkok, Thailand",
    val isEditing: Boolean = false,
    val showDataClearedToast: Boolean = false
)

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val bookingRepository: BookingRepository
) : ViewModel() {

    private val _isEditing = MutableStateFlow(false)
    private val _showToast = MutableStateFlow(false)

    val uiState: StateFlow<ProfileUiState> = combine(
        userRepository.displayName,
        userRepository.preferredLocation,
        _isEditing,
        _showToast
    ) { name, location, editing, toast ->
        ProfileUiState(
            displayName = name,
            preferredLocation = location,
            isEditing = editing,
            showDataClearedToast = toast
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProfileUiState()
    )

    fun updateProfile(name: String, location: String) {
        viewModelScope.launch {
            userRepository.updateDisplayName(name)
            userRepository.updatePreferredLocation(location)
            _isEditing.value = false
        }
    }

    fun toggleEditing() {
        _isEditing.value = !_isEditing.value
    }

    fun clearBookingHistory() {
        viewModelScope.launch {
            bookingRepository.clearAllBookings()
            _showToast.value = true
        }
    }

    fun dismissToast() {
        _showToast.value = false
    }
}
