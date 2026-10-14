package com.example.courtsmash.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.example.courtsmash.R
import com.example.courtsmash.ui.theme.LuminousGoldLight

fun getCourtDrawableResId(imageUrl: String): Int? {
    return when {
        imageUrl.contains("b1") -> R.drawable.b1
        imageUrl.contains("b2") -> R.drawable.b2
        imageUrl.contains("b3") -> R.drawable.b3
        imageUrl.contains("b4") -> R.drawable.b4
        imageUrl.contains("b5") -> R.drawable.b5
        imageUrl.contains("b6") -> R.drawable.b6
        imageUrl.contains("b7") -> R.drawable.b7
        imageUrl.contains("b8") -> R.drawable.b8
        else -> null
    }
}

@Composable
fun CourtImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val resId = getCourtDrawableResId(imageUrl)
    val model: Any = resId ?: imageUrl

    Box(modifier = modifier.background(LuminousGoldLight)) {
        AsyncImage(
            model = model,
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = Modifier.fillMaxSize()
        )
    }
}
