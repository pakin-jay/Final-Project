package com.example.courtsmash.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.courtsmash.domain.model.BookingStatus
import com.example.courtsmash.ui.theme.ChampagneGold
import com.example.courtsmash.ui.theme.DeepCharcoalText
import com.example.courtsmash.ui.theme.DeepGold
import com.example.courtsmash.ui.theme.LuminousGoldLight
import com.example.courtsmash.ui.theme.MutedGreenSuccess
import com.example.courtsmash.ui.theme.MutedGreenSuccessBg
import com.example.courtsmash.ui.theme.MutedRedError
import com.example.courtsmash.ui.theme.MutedRedErrorBg
import com.example.courtsmash.ui.theme.WarmIvoryBackground

@Composable
fun RatingBadge(
    rating: Double,
    reviewCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(LuminousGoldLight)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Rating",
            tint = DeepGold,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "$rating ($reviewCount)",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = DeepCharcoalText
        )
    }
}

@Composable
fun PriceBadge(
    pricePerHour: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(ChampagneGold)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "฿$pricePerHour",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = "/hr",
            fontSize = 11.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White.copy(alpha = 0.85f)
        )
    }
}

@Composable
fun FacilityTag(
    name: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = name,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        color = DeepCharcoalText,
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(WarmIvoryBackground)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

@Composable
fun StatusBadge(
    status: BookingStatus,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, label) = when (status) {
        BookingStatus.UPCOMING -> Triple(MutedGreenSuccessBg, MutedGreenSuccess, "Upcoming")
        BookingStatus.COMPLETED -> Triple(LuminousGoldLight, DeepGold, "Completed")
        BookingStatus.CANCELLED -> Triple(MutedRedErrorBg, MutedRedError, "Cancelled")
    }

    Text(
        text = label,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = textColor,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}
