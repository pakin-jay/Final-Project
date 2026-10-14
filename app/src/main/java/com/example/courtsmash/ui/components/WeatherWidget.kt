package com.example.courtsmash.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.courtsmash.data.repository.WeatherResult
import com.example.courtsmash.ui.theme.ChampagneGold
import com.example.courtsmash.ui.theme.DeepCharcoalText
import com.example.courtsmash.ui.theme.LuminousGoldLight
import com.example.courtsmash.ui.theme.WarmGrayText

@Composable
fun WeatherWidget(
    weatherResult: WeatherResult,
    modifier: Modifier = Modifier
) {
    HolyLightCard(
        modifier = modifier.fillMaxWidth(),
        containerColor = LuminousGoldLight.copy(alpha = 0.5f),
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.WbSunny,
                contentDescription = "Weather",
                tint = ChampagneGold,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                when (weatherResult) {
                    is WeatherResult.Success -> {
                        val w = weatherResult.weather
                        Text(
                            text = "${w.cityName} • ${w.temperature}°C",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepCharcoalText
                        )
                        Text(
                            text = "${w.weatherDescription} • Wind: ${w.windSpeed} km/h",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = WarmGrayText
                        )
                    }
                    is WeatherResult.Loading -> {
                        Text(
                            text = "Fetching live weather...",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = WarmGrayText
                        )
                    }
                    is WeatherResult.Error -> {
                        Text(
                            text = "Weather: Outdoor conditions normal",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = DeepCharcoalText
                        )
                        Text(
                            text = "Live API update unavailable (${weatherResult.message.take(20)}...)",
                            fontSize = 11.sp,
                            color = WarmGrayText
                        )
                    }
                }
            }
        }
    }
}
