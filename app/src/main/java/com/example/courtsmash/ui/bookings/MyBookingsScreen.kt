package com.example.courtsmash.ui.bookings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.courtsmash.domain.model.Booking
import com.example.courtsmash.domain.model.BookingStatus
import com.example.courtsmash.ui.components.CourtImage
import com.example.courtsmash.ui.components.EmptyState
import com.example.courtsmash.ui.components.HolyLightCard
import com.example.courtsmash.ui.components.HolyLightSecondaryButton
import com.example.courtsmash.ui.components.StatusBadge
import com.example.courtsmash.ui.theme.ChampagneGold
import com.example.courtsmash.ui.theme.DeepCharcoalText
import com.example.courtsmash.ui.theme.DeepGold
import com.example.courtsmash.ui.theme.MutedRedError
import com.example.courtsmash.ui.theme.PureWhiteSurface
import com.example.courtsmash.ui.theme.WarmGrayText
import com.example.courtsmash.ui.theme.WarmIvoryBackground

@Composable
fun MyBookingsScreen(
    viewModel: MyBookingsViewModel,
    onBookingClick: (String) -> Unit,
    onExploreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    var cancelDialogBookingId by remember { mutableStateOf<String?>(null) }
    var deleteDialogBookingId by remember { mutableStateOf<String?>(null) }

    val currentList = when (state.selectedTab) {
        BookingStatus.UPCOMING -> state.upcomingBookings
        BookingStatus.COMPLETED -> state.pastBookings
        BookingStatus.CANCELLED -> state.cancelledBookings
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmIvoryBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Screen Title Header with Explore Quick Action
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 12.dp, top = 16.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My Bookings",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepCharcoalText
                )
                TextButton(onClick = onExploreClick) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Explore",
                            tint = ChampagneGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Explore Courts",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepGold
                        )
                    }
                }
            }

            // Tabs Header
            TabRow(
                selectedTabIndex = state.selectedTab.ordinal,
                containerColor = PureWhiteSurface,
                contentColor = ChampagneGold,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[state.selectedTab.ordinal]),
                        color = ChampagneGold,
                        height = 3.dp
                    )
                }
            ) {
                BookingStatus.entries.forEach { status ->
                    val count = when (status) {
                        BookingStatus.UPCOMING -> state.upcomingBookings.size
                        BookingStatus.COMPLETED -> state.pastBookings.size
                        BookingStatus.CANCELLED -> state.cancelledBookings.size
                    }
                    val title = when (status) {
                        BookingStatus.UPCOMING -> "Upcoming ($count)"
                        BookingStatus.COMPLETED -> "Past ($count)"
                        BookingStatus.CANCELLED -> "Cancelled ($count)"
                    }
                    Tab(
                        selected = state.selectedTab == status,
                        onClick = { viewModel.onTabSelected(status) },
                        text = {
                            Text(
                                text = title,
                                fontSize = 13.sp,
                                fontWeight = if (state.selectedTab == status) FontWeight.Bold else FontWeight.Medium,
                                color = if (state.selectedTab == status) DeepCharcoalText else WarmGrayText
                            )
                        }
                    )
                }
            }

            // List Content or Empty State
            if (currentList.isEmpty()) {
                val emptyMessage = when (state.selectedTab) {
                    BookingStatus.UPCOMING -> "You have no upcoming court reservations."
                    BookingStatus.COMPLETED -> "No past court bookings recorded."
                    BookingStatus.CANCELLED -> "No cancelled bookings."
                }
                EmptyState(
                    title = "No Bookings",
                    message = emptyMessage,
                    actionButtonText = if (state.selectedTab == BookingStatus.UPCOMING) "Explore Courts" else null,
                    onActionClick = if (state.selectedTab == BookingStatus.UPCOMING) onExploreClick else null
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(currentList, key = { it.id }) { booking ->
                        BookingCardItem(
                            booking = booking,
                            onClick = { onBookingClick(booking.id) },
                            onCancelClick = { cancelDialogBookingId = booking.id },
                            onDeleteClick = { deleteDialogBookingId = booking.id }
                        )
                    }
                }
            }
        }

        // Cancel Confirmation Dialog
        cancelDialogBookingId?.let { bookingId ->
            AlertDialog(
                onDismissRequest = { cancelDialogBookingId = null },
                title = {
                    Text(
                        text = "Cancel Reservation?",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepCharcoalText
                    )
                },
                text = {
                    Text(
                        text = "Are you sure you want to cancel this reservation? It will be moved to your Cancelled tab.",
                        fontSize = 14.sp,
                        color = WarmGrayText
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.cancelBooking(bookingId)
                            cancelDialogBookingId = null
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = MutedRedError)
                    ) {
                        Text(text = "Cancel Reservation", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { cancelDialogBookingId = null }) {
                        Text(text = "Keep Reservation", color = DeepCharcoalText)
                    }
                },
                containerColor = PureWhiteSurface,
                shape = RoundedCornerShape(20.dp)
            )
        }

        // Delete Confirmation Dialog
        deleteDialogBookingId?.let { bookingId ->
            AlertDialog(
                onDismissRequest = { deleteDialogBookingId = null },
                title = {
                    Text(
                        text = "Delete Booking Record?",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepCharcoalText
                    )
                },
                text = {
                    Text(
                        text = "Permanently delete this booking record from your local database?",
                        fontSize = 14.sp,
                        color = WarmGrayText
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteBooking(bookingId)
                            deleteDialogBookingId = null
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = MutedRedError)
                    ) {
                        Text(text = "Delete Permanently", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { deleteDialogBookingId = null }) {
                        Text(text = "Cancel", color = DeepCharcoalText)
                    }
                },
                containerColor = PureWhiteSurface,
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

@Composable
private fun BookingCardItem(
    booking: Booking,
    onClick: () -> Unit,
    onCancelClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    HolyLightCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "REF: ${booking.bookingReference}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ChampagneGold
                )
                StatusBadge(status = booking.status)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                CourtImage(
                    imageUrl = booking.courtImageUrl,
                    contentDescription = booking.courtName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = booking.courtName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepCharcoalText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${booking.bookingDate} • ${booking.startTime} (${booking.durationHours}h)",
                        fontSize = 13.sp,
                        color = WarmGrayText
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Total: ฿${booking.totalPrice}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepCharcoalText
                    )
                }
            }

            if (booking.status == BookingStatus.UPCOMING) {
                Spacer(modifier = Modifier.height(14.dp))
                HolyLightSecondaryButton(
                    text = "Cancel Booking",
                    onClick = onCancelClick,
                    height = 40.dp
                )
            } else if (booking.status == BookingStatus.CANCELLED) {
                Spacer(modifier = Modifier.height(14.dp))
                HolyLightSecondaryButton(
                    text = "Delete Record",
                    onClick = onDeleteClick,
                    height = 40.dp
                )
            }
        }
    }
}
