package com.example.courtsmash.ui.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val HolyLightColorScheme = lightColorScheme(
    primary = ChampagneGold,
    onPrimary = PureWhiteSurface,
    primaryContainer = LuminousGoldLight,
    onPrimaryContainer = DeepGold,
    secondary = LuminousGold,
    onSecondary = DeepCharcoalText,
    secondaryContainer = LuminousGoldLight,
    onSecondaryContainer = DeepCharcoalText,
    tertiary = MutedGreenSuccess,
    onTertiary = PureWhiteSurface,
    background = WarmIvoryBackground,
    onBackground = DeepCharcoalText,
    surface = PureWhiteSurface,
    onSurface = DeepCharcoalText,
    surfaceVariant = PureWhiteSurfaceVariant,
    onSurfaceVariant = WarmGrayText,
    outline = CardBorderTint,
    outlineVariant = SoftBeigeDivider,
    error = MutedRedError,
    onError = PureWhiteSurface,
    errorContainer = MutedRedErrorBg,
    onErrorContainer = MutedRedError
)

private val HolyDarkColorScheme = darkColorScheme(
    primary = ChampagneGold,
    onPrimary = DeepCharcoalText,
    primaryContainer = DeepGold,
    onPrimaryContainer = LuminousGoldLight,
    secondary = LuminousGold,
    onSecondary = DeepCharcoalText,
    background = WarmIvoryBackground,
    onBackground = DeepCharcoalText,
    surface = PureWhiteSurface,
    onSurface = DeepCharcoalText,
    surfaceVariant = PureWhiteSurfaceVariant,
    onSurfaceVariant = WarmGrayText,
    outline = CardBorderTint,
    outlineVariant = SoftBeigeDivider
)

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

@Composable
fun CourtSmashTheme(
    darkTheme: Boolean = false, // Default to Holy Light Theme
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) HolyDarkColorScheme else HolyLightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val activity = view.context.findActivity()
            if (activity != null) {
                val window = activity.window
                window.statusBarColor = colorScheme.background.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = HolyLightShapes,
        content = content
    )
}
