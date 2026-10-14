package com.example.courtsmash.ui.booking

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.courtsmash.data.repository.BookingRepository
import com.example.courtsmash.ui.components.HolyLightCard
import com.example.courtsmash.ui.components.HolyLightPrimaryButton
import com.example.courtsmash.ui.components.HolyLightSecondaryButton
import com.example.courtsmash.ui.theme.CardBorderTint
import com.example.courtsmash.ui.theme.ChampagneGold
import com.example.courtsmash.ui.theme.DeepCharcoalText
import com.example.courtsmash.ui.theme.DeepGold
import com.example.courtsmash.ui.theme.LuminousGoldLight
import com.example.courtsmash.ui.theme.MutedGreenSuccess
import com.example.courtsmash.ui.theme.MutedGreenSuccessBg
import com.example.courtsmash.ui.theme.WarmGrayText
import com.example.courtsmash.ui.theme.WarmIvoryBackground

@Composable
fun BookingSuccessScreen(
    bookingId: String,
    bookingRepository: BookingRepository,
    onViewBookingsClick: () -> Unit,
    onHomeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bookingState by bookingRepository.getBookingById(bookingId).collectAsState(initial = null)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmIvoryBackground)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        if (bookingState != null) {
            val booking = bookingState!!
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Success Badge Header
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(MutedGreenSuccessBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = MutedGreenSuccess,
                        modifier = Modifier.size(52.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Booking Confirmed!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepCharcoalText
                )

                Text(
                    text = "Your court reservation has been saved successfully.",
                    fontSize = 14.sp,
                    color = WarmGrayText,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Reservation Ticket Card
                HolyLightCard(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = LuminousGoldLight.copy(alpha = 0.4f)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "BOOKING REFERENCE",
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
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Divider(color = CardBorderTint)
                        Spacer(modifier = Modifier.height(14.dp))

                        DetailRow("Court Name", booking.courtName)
                        Spacer(modifier = Modifier.height(8.dp))
                        DetailRow("Date", booking.bookingDate)
                        Spacer(modifier = Modifier.height(8.dp))
                        DetailRow("Time", "${booking.startTime} (${booking.durationHours}h)")
                        Spacer(modifier = Modifier.height(8.dp))
                        DetailRow("Total Paid", "฿${booking.totalPrice}")
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                HolyLightPrimaryButton(
                    text = "View My Bookings",
                    onClick = onViewBookingsClick
                )

                Spacer(modifier = Modifier.height(12.dp))

                HolyLightSecondaryButton(
                    text = "Return to Explore",
                    onClick = onHomeClick
                )
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 13.sp, color = WarmGrayText)
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DeepCharcoalText)
    }
}
