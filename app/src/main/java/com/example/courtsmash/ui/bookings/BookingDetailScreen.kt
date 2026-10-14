package com.example.courtsmash.ui.bookings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import coil.compose.AsyncImage
import com.example.courtsmash.data.repository.BookingRepository
import com.example.courtsmash.domain.model.BookingStatus
import com.example.courtsmash.ui.components.HolyLightCard
import com.example.courtsmash.ui.components.HolyLightSecondaryButton
import com.example.courtsmash.ui.components.StatusBadge
import com.example.courtsmash.ui.theme.CardBorderTint
import com.example.courtsmash.ui.theme.ChampagneGold
import com.example.courtsmash.ui.theme.DeepCharcoalText
import com.example.courtsmash.ui.theme.DeepGold
import com.example.courtsmash.ui.theme.LuminousGoldLight
import com.example.courtsmash.ui.theme.MutedRedError
import com.example.courtsmash.ui.theme.PureWhiteSurface
import com.example.courtsmash.ui.theme.WarmGrayText
import com.example.courtsmash.ui.theme.WarmIvoryBackground
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun BookingDetailScreen(
    bookingId: String,
    bookingRepository: BookingRepository,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bookingState by bookingRepository.getBookingById(bookingId).collectAsState(initial = null)
    var showCancelDialog by remember { mutableStateOf(false) }
    val scope = androidx.compose.runtime.rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmIvoryBackground)
    ) {
        if (bookingState != null) {
            val booking = bookingState!!

            Column(modifier = Modifier.fillMaxSize()) {
                // Header Top Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(PureWhiteSurface)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = DeepCharcoalText
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Reservation Details",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepCharcoalText
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    HolyLightCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "REFERENCE NUMBER",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepGold
                                    )
                                    Text(
                                        text = booking.bookingReference,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepCharcoalText
                                    )
                                }
                                StatusBadge(status = booking.status)
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                com.example.courtsmash.ui.components.CourtImage(
                                    imageUrl = booking.courtImageUrl,
                                    contentDescription = booking.courtName,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                )
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text(
                                        text = booking.courtName,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepCharcoalText
                                    )
                                    Text(
                                        text = booking.courtAddress,
                                        fontSize = 12.sp,
                                        color = WarmGrayText
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            Divider(color = CardBorderTint)
                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Booking Date", fontSize = 13.sp, color = WarmGrayText)
                                Text(text = booking.bookingDate, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DeepCharcoalText)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Start Time & Duration", fontSize = 13.sp, color = WarmGrayText)
                                Text(text = "${booking.startTime} (${booking.durationHours} hrs)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DeepCharcoalText)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Total Price", fontSize = 13.sp, color = WarmGrayText)
                                Text(text = "฿${booking.totalPrice}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = ChampagneGold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    if (booking.status == BookingStatus.UPCOMING) {
                        HolyLightSecondaryButton(
                            text = "Cancel Booking",
                            onClick = { showCancelDialog = true }
                        )
                    }
                }
            }

            if (showCancelDialog) {
                AlertDialog(
                    onDismissRequest = { showCancelDialog = false },
                    title = {
                        Text(
                            text = "Confirm Cancellation",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepCharcoalText
                        )
                    },
                    text = {
                        Text(
                            text = "Do you want to cancel this booking? This action cannot be undone.",
                            fontSize = 14.sp,
                            color = WarmGrayText
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                scope.launch {
                                    bookingRepository.cancelBooking(booking.id)
                                    showCancelDialog = false
                                    onBackClick()
                                }
                            },
                            colors = ButtonDefaults.textButtonColors(contentColor = MutedRedError)
                        ) {
                            Text(text = "Cancel Reservation", fontWeight = FontWeight.Bold)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showCancelDialog = false }) {
                            Text(text = "Back", color = DeepCharcoalText)
                        }
                    },
                    containerColor = PureWhiteSurface,
                    shape = RoundedCornerShape(20.dp)
                )
            }
        }
    }
}
