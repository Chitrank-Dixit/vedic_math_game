package com.ankh.sutrasaga.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = VedicGold,
    onPrimary = CosmicBackground,
    primaryContainer = VedicGoldDark,
    secondary = CyberCyan,
    onSecondary = CosmicBackground,
    secondaryContainer = CyberCyanDark,
    tertiary = AccentPurple,
    background = CosmicBackground,
    surface = SurfaceCard,
    onBackground = TextWhitePrimary,
    onSurface = TextWhitePrimary,
    surfaceVariant = SurfaceCardElevated,
    onSurfaceVariant = TextLightSecondary,
    error = ErrorCrimson
)

@Composable
fun AnkhTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = AnkhTypography,
        content = content
    )
}
