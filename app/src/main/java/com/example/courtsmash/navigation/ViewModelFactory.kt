package com.example.courtsmash.navigation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.courtsmash.data.local.CourtSmashDatabase
import com.example.courtsmash.data.local.PreferencesManager
import com.example.courtsmash.data.repository.AiRepository
import com.example.courtsmash.data.repository.BookingRepository
import com.example.courtsmash.data.repository.CourtRepository
import com.example.courtsmash.data.repository.UserRepository
import com.example.courtsmash.data.repository.WeatherRepository
import com.example.courtsmash.ui.ai.AiAssistantViewModel
import com.example.courtsmash.ui.booking.BookingViewModel
import com.example.courtsmash.ui.bookings.MyBookingsViewModel
import com.example.courtsmash.ui.chat.AiChatbotViewModel
import com.example.courtsmash.ui.court.CourtDetailViewModel
import com.example.courtsmash.ui.home.HomeViewModel
import com.example.courtsmash.ui.profile.ProfileViewModel

class AppDependencies(context: Context) {
    private val database = CourtSmashDatabase.getDatabase(context)
    private val preferencesManager = PreferencesManager(context)

    val courtRepository = CourtRepository(database.courtDao())
    val bookingRepository = BookingRepository(database.bookingDao())
    val weatherRepository = WeatherRepository()
    val aiRepository = AiRepository()
    val userRepository = UserRepository(preferencesManager)
}

class AppViewModelFactory(
    private val dependencies: AppDependencies,
    private val courtId: String? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(
                    dependencies.courtRepository,
                    dependencies.bookingRepository,
                    dependencies.weatherRepository
                ) as T
            }
            modelClass.isAssignableFrom(CourtDetailViewModel::class.java) -> {
                CourtDetailViewModel(
                    dependencies.courtRepository,
                    dependencies.weatherRepository,
                    courtId ?: ""
                ) as T
            }
            modelClass.isAssignableFrom(BookingViewModel::class.java) -> {
                BookingViewModel(
                    dependencies.courtRepository,
                    dependencies.bookingRepository,
                    courtId ?: ""
                ) as T
            }
            modelClass.isAssignableFrom(MyBookingsViewModel::class.java) -> {
                MyBookingsViewModel(dependencies.bookingRepository) as T
            }
            modelClass.isAssignableFrom(AiAssistantViewModel::class.java) -> {
                AiAssistantViewModel(
                    dependencies.aiRepository,
                    dependencies.courtRepository
                ) as T
            }
            modelClass.isAssignableFrom(AiChatbotViewModel::class.java) -> {
                AiChatbotViewModel() as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(
                    dependencies.userRepository,
                    dependencies.bookingRepository
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class ${modelClass.name}")
        }
    }
}
