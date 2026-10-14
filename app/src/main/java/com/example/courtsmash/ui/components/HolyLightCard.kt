package com.example.courtsmash.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.courtsmash.ui.theme.CardBorderTint
import com.example.courtsmash.ui.theme.PureWhiteSurface

@Composable
fun HolyLightCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    cornerRadius: Dp = 20.dp,
    border: BorderStroke? = BorderStroke(1.dp, CardBorderTint),
    containerColor: Color = PureWhiteSurface,
    elevation: Dp = 2.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier,
            shape = RoundedCornerShape(cornerRadius),
            colors = CardDefaults.cardColors(containerColor = containerColor),
            border = border,
            elevation = CardDefaults.cardElevation(defaultElevation = elevation),
            content = content
        )
    } else {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(cornerRadius),
            colors = CardDefaults.cardColors(containerColor = containerColor),
            border = border,
            elevation = CardDefaults.cardElevation(defaultElevation = elevation),
            content = content
        )
    }
}
