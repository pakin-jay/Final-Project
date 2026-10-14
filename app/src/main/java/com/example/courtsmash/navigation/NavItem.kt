package com.example.courtsmash.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : NavItem(Screen.Home.route, "Explore", Icons.Default.Home)
    object Bookings : NavItem(Screen.MyBookings.route, "Bookings", Icons.Default.ConfirmationNumber)
    object AiAssistant : NavItem(Screen.AiAssistant.route, "AI Assist", Icons.Default.AutoAwesome)
    object AiChatbot : NavItem(Screen.AiChatbot.route, "AI Chat", Icons.Default.QuestionAnswer)
    object Profile : NavItem(Screen.Profile.route, "Profile", Icons.Default.Person)

    companion object {
        val bottomNavItems = listOf(Home, Bookings, AiAssistant, AiChatbot, Profile)
    }
}
