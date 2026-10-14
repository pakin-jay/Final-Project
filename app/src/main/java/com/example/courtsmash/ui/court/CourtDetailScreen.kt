package com.example.courtsmash.ui.court

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.courtsmash.ui.components.ErrorBanner
import com.example.courtsmash.ui.components.HolyLightCard
import com.example.courtsmash.ui.components.HolyLightPrimaryButton
import com.example.courtsmash.ui.components.LoadingState
import com.example.courtsmash.ui.components.PriceBadge
import com.example.courtsmash.ui.components.RatingBadge
import com.example.courtsmash.ui.components.WeatherWidget
import com.example.courtsmash.ui.theme.ChampagneGold
import com.example.courtsmash.ui.theme.DeepCharcoalText
import com.example.courtsmash.ui.theme.LuminousGoldLight
import com.example.courtsmash.ui.theme.PureWhiteSurface
import com.example.courtsmash.ui.theme.WarmGrayText
import com.example.courtsmash.ui.theme.WarmIvoryBackground

@Composable
fun CourtDetailScreen(
    viewModel: CourtDetailViewModel,
    onBackClick: () -> Unit,
    onBookClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmIvoryBackground)
    ) {
        if (state.isLoading) {
            LoadingState("Loading court details...")
        } else if (state.error != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                ErrorBanner(
                    message = state.error ?: "An unexpected error occurred.",
                    onRetry = viewModel::loadCourtDetails
                )
            }
        } else if (state.court != null) {
            val court = state.court!!
            val scrollState = rememberScrollState()

            Column(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.weight(1f)) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    ) {
                        // Image Header
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp)
                        ) {
                            com.example.courtsmash.ui.components.CourtImage(
                                imageUrl = court.imageUrl,
                                contentDescription = court.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            // Gradient Top Overlay for Back Button
                            Box(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .align(Alignment.TopStart)
                            ) {
                                IconButton(
                                    onClick = onBackClick,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(PureWhiteSurface.copy(alpha = 0.9f))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Back",
                                        tint = DeepCharcoalText
                                    )
                                }
                            }
                        }

                        // Content Body
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = court.name,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepCharcoalText
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = null,
                                            tint = ChampagneGold,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = court.address,
                                            fontSize = 13.sp,
                                            color = WarmGrayText
                                        )
                                    }
                                }
                                RatingBadge(rating = court.rating, reviewCount = court.reviewCount)
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Operating Hours
                            HolyLightCard(
                                modifier = Modifier.fillMaxWidth(),
                                containerColor = LuminousGoldLight.copy(alpha = 0.4f),
                                elevation = 0.dp
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = "Hours",
                                        tint = ChampagneGold,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "Operating Hours",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = WarmGrayText
                                        )
                                        Text(
                                            text = "${court.openingTime} - ${court.closingTime} (Daily)",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DeepCharcoalText
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Description
                            Text(
                                text = "About Venue",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepCharcoalText
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = court.description,
                                fontSize = 14.sp,
                                lineHeight = 22.sp,
                                color = WarmGrayText
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            // Facilities
                            Text(
                                text = "Amenities & Facilities",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepCharcoalText
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                court.facilities.chunked(2).forEach { rowFacilities ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        rowFacilities.forEach { facility ->
                                            Row(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(PureWhiteSurface)
                                                    .padding(12.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.CheckCircle,
                                                    contentDescription = null,
                                                    tint = ChampagneGold,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = facility,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    color = DeepCharcoalText
                                                )
                                            }
                                        }
                                        if (rowFacilities.size == 1) {
                                            Spacer(modifier = Modifier.weight(1f))
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Weather Feature
                            Text(
                                text = "Venue Conditions",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepCharcoalText
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            WeatherWidget(weatherResult = state.weatherResult)

                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }

                // Bottom CTA Dock
                Surface(
                    color = PureWhiteSurface,
                    shadowElevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Hourly Rate",
                                fontSize = 12.sp,
                                color = WarmGrayText
                            )
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "฿${court.pricePerHour}",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepCharcoalText
                                )
                                Text(
                                    text = " / hour",
                                    fontSize = 12.sp,
                                    color = WarmGrayText,
                                    modifier = Modifier.padding(bottom = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                        HolyLightPrimaryButton(
                            text = "Book This Court",
                            onClick = { onBookClick(court.id) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}
