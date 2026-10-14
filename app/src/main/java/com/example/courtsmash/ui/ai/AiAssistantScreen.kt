package com.example.courtsmash.ui.ai

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.courtsmash.domain.model.AiRecommendation
import com.example.courtsmash.ui.components.ErrorBanner
import com.example.courtsmash.ui.components.FacilityTag
import com.example.courtsmash.ui.components.HolyLightCard
import com.example.courtsmash.ui.components.HolyLightChip
import com.example.courtsmash.ui.components.HolyLightPrimaryButton
import com.example.courtsmash.ui.components.LoadingState
import com.example.courtsmash.ui.theme.CardBorderTint
import com.example.courtsmash.ui.theme.ChampagneGold
import com.example.courtsmash.ui.theme.DeepCharcoalText
import com.example.courtsmash.ui.theme.DeepGold
import com.example.courtsmash.ui.theme.LuminousGold
import com.example.courtsmash.ui.theme.LuminousGoldLight
import com.example.courtsmash.ui.theme.WarmGrayText
import com.example.courtsmash.ui.theme.WarmIvoryBackground

@Composable
fun AiAssistantScreen(
    viewModel: AiAssistantViewModel,
    onCourtClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val facilityList = listOf("Air Conditioning", "Yonex Flooring", "Parking", "Showers", "Pro Shop")
    val durationOptions = (1..10).toList()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmIvoryBackground)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Header Section
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(LuminousGoldLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "AI",
                                tint = ChampagneGold,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "AI Court Matcher",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepCharcoalText
                            )
                            Text(
                                text = "Smart recommendation engine for budget & court specs",
                                fontSize = 12.sp,
                                color = WarmGrayText
                            )
                        }
                    }
                }
            }

            // Preferences Card
            item {
                HolyLightCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    elevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "Filter",
                                tint = ChampagneGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Match Preferences",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepCharcoalText
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Budget Slider
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Max Session Budget",
                                fontSize = 13.sp,
                                color = WarmGrayText
                            )
                            Text(
                                text = "฿${state.budgetThb}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = ChampagneGold
                            )
                        }
                        Slider(
                            value = state.budgetThb.toFloat(),
                            onValueChange = { viewModel.onBudgetChanged(it.toInt()) },
                            valueRange = 150f..4000f,
                            steps = 76,
                            colors = SliderDefaults.colors(
                                thumbColor = ChampagneGold,
                                activeTrackColor = ChampagneGold,
                                inactiveTrackColor = CardBorderTint
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Duration Chips
                        Text(
                            text = "Booking Duration",
                            fontSize = 13.sp,
                            color = WarmGrayText
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        androidx.compose.foundation.lazy.LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(durationOptions) { hours ->
                                HolyLightChip(
                                    text = "$hours ${if (hours == 1) "Hour" else "Hours"}",
                                    selected = state.durationHours == hours,
                                    onClick = { viewModel.onDurationChanged(hours) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Facilities Multi-select
                        Text(
                            text = "Preferred Amenities",
                            fontSize = 13.sp,
                            color = WarmGrayText
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            facilityList.take(3).forEach { facility ->
                                HolyLightChip(
                                    text = facility,
                                    selected = state.selectedFacilities.contains(facility),
                                    onClick = { viewModel.onFacilityToggle(facility) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        HolyLightPrimaryButton(
                            text = if (state.isLoading) "Analyzing Courts..." else "Generate AI Match",
                            enabled = !state.isLoading,
                            onClick = { viewModel.fetchRecommendations() }
                        )
                    }
                }
            }

            // Results Section
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

            if (state.isLoading) {
                item {
                    LoadingState("AI Engine is ranking candidate courts...")
                }
            } else if (state.errorMessage != null) {
                item {
                    Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                        ErrorBanner(
                            message = state.errorMessage!!,
                            onRetry = { viewModel.fetchRecommendations() }
                        )
                    }
                }
            } else if (state.aiResponse != null) {
                val response = state.aiResponse!!

                // Assistant Summary Banner
                item {
                    HolyLightCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 6.dp),
                        containerColor = LuminousGoldLight,
                        elevation = 1.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = if (response.isDemoFallback) "AI Recommendation Summary (Local Engine)" else "AI Recommendation Summary",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepGold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = response.assistantSummary,
                                fontSize = 13.sp,
                                color = DeepCharcoalText,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                if (response.recommendations.isEmpty()) {
                    item {
                        com.example.courtsmash.ui.components.EmptyState(
                            title = "No Matching Courts",
                            message = "No courts qualify within your ฿${state.budgetThb} budget for a ${state.durationHours}-hour session. Please increase your session budget or shorten session duration.",
                            actionButtonText = "Increase Budget (+฿300)",
                            onActionClick = {
                                viewModel.onBudgetChanged(state.budgetThb + 300)
                                viewModel.fetchRecommendations()
                            }
                        )
                    }
                } else {
                    item {
                        Text(
                            text = "Top Court Recommendations",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepCharcoalText,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                        )
                    }

                    items(response.recommendations, key = { it.courtId }) { rec ->
                        RecommendationItemCard(
                            recommendation = rec,
                            onClick = { onCourtClick(rec.courtId) },
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecommendationItemCard(
    recommendation: AiRecommendation,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    HolyLightCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = recommendation.courtName,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepCharcoalText,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(ChampagneGold)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${recommendation.matchScore}% Match",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = recommendation.reasoning,
                fontSize = 13.sp,
                color = WarmGrayText,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Calculated Total",
                        fontSize = 11.sp,
                        color = WarmGrayText
                    )
                    Text(
                        text = "฿${recommendation.estimatedPrice}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = ChampagneGold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "View & Book",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepCharcoalText
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = ChampagneGold
                    )
                }
            }
        }
    }
}
