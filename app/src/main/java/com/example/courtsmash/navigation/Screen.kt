package com.example.courtsmash.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    
    object CourtDetail : Screen("court_detail/{courtId}") {
        fun createRoute(courtId: String) = "court_detail/$courtId"
    }

    object BookingForm : Screen("booking_form/{courtId}") {
        fun createRoute(courtId: String) = "booking_form/$courtId"
    }

    object BookingSuccess : Screen("booking_success/{bookingId}") {
        fun createRoute(bookingId: String) = "booking_success/$bookingId"
    }

    object MyBookings : Screen("my_bookings")

    object BookingDetail : Screen("booking_detail/{bookingId}") {
        fun createRoute(bookingId: String) = "booking_detail/$bookingId"
    }

    object AiAssistant : Screen("ai_assistant")

    object AiChatbot : Screen("ai_chatbot")

    object Profile : Screen("profile")
}
