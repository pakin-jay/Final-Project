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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.courtsmash.ui.components.ErrorBanner
import com.example.courtsmash.ui.components.HolyLightCard
import com.example.courtsmash.ui.components.HolyLightChip
import com.example.courtsmash.ui.components.HolyLightPrimaryButton
import com.example.courtsmash.ui.components.LoadingState
import com.example.courtsmash.ui.theme.CardBorderTint
import com.example.courtsmash.ui.theme.ChampagneGold
import com.example.courtsmash.ui.theme.DeepCharcoalText
import com.example.courtsmash.ui.theme.DeepGold
import com.example.courtsmash.ui.theme.LuminousGoldLight
import com.example.courtsmash.ui.theme.PureWhiteSurface
import com.example.courtsmash.ui.theme.WarmGrayText
import com.example.courtsmash.ui.theme.WarmIvoryBackground
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun BookingScreen(
    viewModel: BookingViewModel,
    onBackClick: () -> Unit,
    onBookingSuccess: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    // Available dates for next 7 days
    val availableDates = rememberAvailableDates()
    val timeSlots = listOf("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00")
    val durationOptions = (1..10).toList()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmIvoryBackground)
    ) {
        if (state.isLoading) {
            LoadingState("Preparing booking options...")
        } else if (state.court != null) {
            val court = state.court!!
            val scrollState = rememberScrollState()

            Column(modifier = Modifier.fillMaxSize()) {
                // Top App Bar
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
                        text = "Book ${court.name}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepCharcoalText
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(scrollState)
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    // Court Summary Card
                    HolyLightCard(
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = LuminousGoldLight.copy(alpha = 0.5f)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = court.name,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepCharcoalText
                                )
                                Text(
                                    text = "Rate: ฿${court.pricePerHour}/hr • Open ${court.openingTime} - ${court.closingTime}",
                                    fontSize = 12.sp,
                                    color = WarmGrayText
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // 1. Select Date
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = ChampagneGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Select Date",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepCharcoalText
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(availableDates) { (dateStr, labelStr) ->
                            HolyLightChip(
                                text = labelStr,
                                selected = state.selectedDate == dateStr,
                                onClick = { viewModel.onDateSelected(dateStr) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // 2. Select Time Slot
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = ChampagneGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Select Start Time",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepCharcoalText
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(timeSlots) { time ->
                            HolyLightChip(
                                text = time,
                                selected = state.startTime == time,
                                onClick = { viewModel.onTimeSelected(time) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // 3. Select Duration
                    Text(
                        text = "Booking Duration (Hours)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepCharcoalText
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(durationOptions) { hours ->
                            HolyLightChip(
                                text = "$hours ${if (hours == 1) "Hour" else "Hours"}",
                                selected = state.durationHours == hours,
                                onClick = { viewModel.onDurationChanged(hours) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Validation Error Banner (if any)
                    state.validationError?.let { error ->
                        ErrorBanner(message = error)
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    // Price Breakdown Card
                    HolyLightCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Price Summary",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepCharcoalText
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Court Fee (฿${court.pricePerHour} × ${state.durationHours}h)",
                                    fontSize = 14.sp,
                                    color = WarmGrayText
                                )
                                Text(
                                    text = "฿${court.pricePerHour * state.durationHours}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = DeepCharcoalText
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Facility Charge",
                                    fontSize = 14.sp,
                                    color = WarmGrayText
                                )
                                Text(
                                    text = "Free",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = DeepGold
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Divider(color = CardBorderTint)
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Total Payable",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepCharcoalText
                                )
                                Text(
                                    text = "฿${state.totalPrice}",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ChampagneGold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Bottom Action Dock
                Surface(
                    color = PureWhiteSurface,
                    shadowElevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.padding(20.dp)) {
                        HolyLightPrimaryButton(
                            text = if (state.isSubmitting) "Processing..." else "Confirm & Book Court",
                            enabled = state.validationError == null && !state.isSubmitting,
                            onClick = {
                                viewModel.confirmBooking { bookingId ->
                                    onBookingSuccess(bookingId)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun rememberAvailableDates(): List<Pair<String, String>> {
    val dates = mutableListOf<Pair<String, String>>()
    val sdfValue = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val sdfDisplay = SimpleDateFormat("EEE, MMM d", Locale.US)
    val cal = Calendar.getInstance()

    for (i in 0..6) {
        val valStr = sdfValue.format(cal.time)
        val dispStr = if (i == 0) "Today (${sdfDisplay.format(cal.time)})" else sdfDisplay.format(cal.time)
        dates.add(Pair(valStr, dispStr))
        cal.add(Calendar.DAY_OF_YEAR, 1)
    }
    return dates
}
