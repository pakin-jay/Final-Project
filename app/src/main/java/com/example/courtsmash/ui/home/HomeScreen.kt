package com.example.courtsmash.ui.home

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.courtsmash.domain.model.Booking
import com.example.courtsmash.domain.model.Court
import com.example.courtsmash.ui.components.EmptyState
import com.example.courtsmash.ui.components.FacilityTag
import com.example.courtsmash.ui.components.HolyLightCard
import com.example.courtsmash.ui.components.HolyLightChip
import com.example.courtsmash.ui.components.LoadingState
import com.example.courtsmash.ui.components.PriceBadge
import com.example.courtsmash.ui.components.RatingBadge
import com.example.courtsmash.ui.components.WeatherWidget
import com.example.courtsmash.ui.theme.CardBorderTint
import com.example.courtsmash.ui.theme.ChampagneGold
import com.example.courtsmash.ui.theme.DeepCharcoalText
import com.example.courtsmash.ui.theme.DeepGold
import com.example.courtsmash.ui.theme.LuminousGold
import com.example.courtsmash.ui.theme.LuminousGoldLight
import com.example.courtsmash.ui.theme.PureWhiteSurface
import com.example.courtsmash.ui.theme.WarmGrayText
import com.example.courtsmash.ui.theme.WarmIvoryBackground

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onCourtClick: (String) -> Unit,
    onBookingClick: (String) -> Unit,
    onExploreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    val facilityOptions = listOf("All", "Air Conditioning", "Yonex", "Parking", "Showers", "Pro Shop")

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmIvoryBackground)
    ) {
        if (state.isLoading) {
            LoadingState("Loading courts...")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                // Header Section
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(LuminousGoldLight),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.SportsTennis,
                                        contentDescription = "Logo",
                                        tint = ChampagneGold,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "CourtSmash",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepCharcoalText
                                    )
                                    Text(
                                        text = "Holy Light Badminton Club",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = WarmGrayText
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Search Bar
                        OutlinedTextField(
                            value = state.searchQuery,
                            onValueChange = viewModel::onSearchQueryChanged,
                            placeholder = {
                                Text(
                                    text = "Search courts by name or area...",
                                    color = WarmGrayText,
                                    fontSize = 14.sp
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = ChampagneGold
                                )
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = PureWhiteSurface,
                                unfocusedContainerColor = PureWhiteSurface,
                                focusedBorderColor = ChampagneGold,
                                unfocusedBorderColor = CardBorderTint
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Weather Widget
                item {
                    Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)) {
                        WeatherWidget(weatherResult = state.weatherResult)
                    }
                }

                // Upcoming Booking Quick Card (If available)
                state.upcomingBooking?.let { booking ->
                    item {
                        UpcomingBookingCard(
                            booking = booking,
                            onClick = { onBookingClick(booking.id) },
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                        )
                    }
                }

                // Facility Filter Chips
                item {
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        Text(
                            text = "Filter Amenities",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepCharcoalText,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                        )
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(facilityOptions) { facility ->
                                HolyLightChip(
                                    text = facility,
                                    selected = state.selectedFacility == facility,
                                    onClick = { viewModel.onFacilitySelected(facility) }
                                )
                            }
                        }
                    }
                }

                // Section Header
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Featured Courts",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepCharcoalText
                        )
                        Text(
                            text = "${state.filteredCourts.size} available",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = WarmGrayText
                        )
                    }
                }

                // Courts List or Empty State
                if (state.filteredCourts.isEmpty()) {
                    item {
                        EmptyState(
                            title = "No Courts Found",
                            message = "Try adjusting your search query or amenity filters to discover available courts.",
                            actionButtonText = "Reset Filters",
                            onActionClick = {
                                viewModel.onSearchQueryChanged("")
                                viewModel.onFacilitySelected("All")
                            }
                        )
                    }
                } else {
                    items(state.filteredCourts, key = { it.id }) { court ->
                        CourtCardItem(
                            court = court,
                            onClick = { onCourtClick(court.id) },
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UpcomingBookingCard(
    booking: Booking,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    HolyLightCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        containerColor = LuminousGoldLight,
        elevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(ChampagneGold),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ConfirmationNumber,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Upcoming Reservation",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepGold
                )
                Text(
                    text = booking.courtName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepCharcoalText
                )
                Text(
                    text = "${booking.bookingDate} at ${booking.startTime} (${booking.durationHours}h)",
                    fontSize = 12.sp,
                    color = WarmGrayText
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = DeepGold
            )
        }
    }
}

@Composable
private fun CourtCardItem(
    court: Court,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    HolyLightCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = 2.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                com.example.courtsmash.ui.components.CourtImage(
                    imageUrl = court.imageUrl,
                    contentDescription = court.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopEnd)
                ) {
                    PriceBadge(pricePerHour = court.pricePerHour)
                }
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.BottomStart)
                ) {
                    RatingBadge(rating = court.rating, reviewCount = court.reviewCount)
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = court.name,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepCharcoalText
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = ChampagneGold,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = court.address,
                        fontSize = 12.sp,
                        color = WarmGrayText,
                        maxLines = 1
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    court.facilities.take(3).forEach { facility ->
                        FacilityTag(name = facility)
                    }
                }
            }
        }
    }
}
