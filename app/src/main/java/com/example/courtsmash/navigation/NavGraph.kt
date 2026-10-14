package com.example.courtsmash.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.courtsmash.ui.ai.AiAssistantScreen
import com.example.courtsmash.ui.ai.AiAssistantViewModel
import com.example.courtsmash.ui.booking.BookingScreen
import com.example.courtsmash.ui.booking.BookingSuccessScreen
import com.example.courtsmash.ui.booking.BookingViewModel
import com.example.courtsmash.ui.bookings.BookingDetailScreen
import com.example.courtsmash.ui.bookings.MyBookingsScreen
import com.example.courtsmash.ui.bookings.MyBookingsViewModel
import com.example.courtsmash.ui.chat.AiChatbotScreen
import com.example.courtsmash.ui.chat.AiChatbotViewModel
import com.example.courtsmash.ui.court.CourtDetailScreen
import com.example.courtsmash.ui.court.CourtDetailViewModel
import com.example.courtsmash.ui.home.HomeScreen
import com.example.courtsmash.ui.home.HomeViewModel
import com.example.courtsmash.ui.profile.ProfileScreen
import com.example.courtsmash.ui.profile.ProfileViewModel

@Composable
fun CourtSmashNavGraph(
    navController: NavHostController,
    dependencies: AppDependencies,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        // Home / Explore
        composable(Screen.Home.route) {
            val homeViewModel: HomeViewModel = viewModel(
                factory = AppViewModelFactory(dependencies)
            )
            HomeScreen(
                viewModel = homeViewModel,
                onCourtClick = { courtId ->
                    navController.navigate(Screen.CourtDetail.createRoute(courtId))
                },
                onBookingClick = { bookingId ->
                    navController.navigate(Screen.BookingDetail.createRoute(bookingId))
                },
                onExploreClick = {
                    navController.navigate(Screen.Home.route)
                }
            )
        }

        // Court Detail
        composable(
            route = Screen.CourtDetail.route,
            arguments = listOf(navArgument("courtId") { type = NavType.StringType })
        ) { backStackEntry ->
            val courtId = backStackEntry.arguments?.getString("courtId") ?: ""
            val detailViewModel: CourtDetailViewModel = viewModel(
                factory = AppViewModelFactory(dependencies, courtId = courtId)
            )
            CourtDetailScreen(
                viewModel = detailViewModel,
                onBackClick = { navController.popBackStack() },
                onBookClick = { cId ->
                    navController.navigate(Screen.BookingForm.createRoute(cId))
                }
            )
        }

        // Booking Form
        composable(
            route = Screen.BookingForm.route,
            arguments = listOf(navArgument("courtId") { type = NavType.StringType })
        ) { backStackEntry ->
            val courtId = backStackEntry.arguments?.getString("courtId") ?: ""
            val bookingViewModel: BookingViewModel = viewModel(
                factory = AppViewModelFactory(dependencies, courtId = courtId)
            )
            BookingScreen(
                viewModel = bookingViewModel,
                onBackClick = { navController.popBackStack() },
                onBookingSuccess = { bookingId ->
                    navController.navigate(Screen.BookingSuccess.createRoute(bookingId)) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                    }
                }
            )
        }

        // Booking Success
        composable(
            route = Screen.BookingSuccess.route,
            arguments = listOf(navArgument("bookingId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getString("bookingId") ?: ""
            BookingSuccessScreen(
                bookingId = bookingId,
                bookingRepository = dependencies.bookingRepository,
                onViewBookingsClick = {
                    navController.navigate(Screen.MyBookings.route) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                    }
                },
                onHomeClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                    }
                }
            )
        }

        // My Bookings
        composable(Screen.MyBookings.route) {
            val myBookingsViewModel: MyBookingsViewModel = viewModel(
                factory = AppViewModelFactory(dependencies)
            )
            MyBookingsScreen(
                viewModel = myBookingsViewModel,
                onBookingClick = { bookingId ->
                    navController.navigate(Screen.BookingDetail.createRoute(bookingId))
                },
                onExploreClick = {
                    navController.navigate(Screen.Home.route)
                }
            )
        }

        // Booking Detail
        composable(
            route = Screen.BookingDetail.route,
            arguments = listOf(navArgument("bookingId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getString("bookingId") ?: ""
            BookingDetailScreen(
                bookingId = bookingId,
                bookingRepository = dependencies.bookingRepository,
                onBackClick = { navController.popBackStack() }
            )
        }

        // AI Assistant (Court Matcher)
        composable(Screen.AiAssistant.route) {
            val aiViewModel: AiAssistantViewModel = viewModel(
                factory = AppViewModelFactory(dependencies)
            )
            AiAssistantScreen(
                viewModel = aiViewModel,
                onCourtClick = { courtId ->
                    navController.navigate(Screen.CourtDetail.createRoute(courtId))
                }
            )
        }

        // AI Chatbot (Q&A Expert)
        composable(Screen.AiChatbot.route) {
            val chatViewModel: AiChatbotViewModel = viewModel(
                factory = AppViewModelFactory(dependencies)
            )
            AiChatbotScreen(viewModel = chatViewModel)
        }

        // Profile / Settings
        composable(Screen.Profile.route) {
            val profileViewModel: ProfileViewModel = viewModel(
                factory = AppViewModelFactory(dependencies)
            )
            ProfileScreen(viewModel = profileViewModel)
        }
    }
}
