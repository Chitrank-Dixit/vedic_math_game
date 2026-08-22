package com.ankh.sutrasaga.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Vedic Mathematics Design Token Color Palette System
 * Supports Light Mode (Bhojpatra Parchment) & Dark Mode (Cosmic Midnight).
 */
@Immutable
data class VedicColors(
    val backgroundPrimary: Color,
    val surfaceCard: Color,
    val primarySaffron: Color,
    val secondaryGold: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val borderSubtle: Color,
    val statusSuccess: Color,
    val statusError: Color,
    val shadowColor: Color,
    val glowColor: Color,
    val isDark: Boolean
) {
    val saffronGradient: Brush
        get() = Brush.horizontalGradient(
            if (isDark) listOf(primarySaffron, Color(0xFFF59E0B))
            else listOf(primarySaffron, Color(0xFFFF6D00))
        )

    val goldGradient: Brush
        get() = Brush.horizontalGradient(
            if (isDark) listOf(secondaryGold, Color(0xFFFBBF24))
            else listOf(secondaryGold, Color(0xFFC59B27))
        )

    val hologramBorder: Brush
        get() = Brush.linearGradient(
            if (isDark) listOf(Color(0x9900E5FF), secondaryGold, Color(0x9938BDF8))
            else listOf(Color(0x66E65100), secondaryGold, Color(0x66E65100))
        )
}

// -------------------------------------------------------------
// Light Mode (Bhojpatra Parchment) Design Tokens
// -------------------------------------------------------------
val LightBhojpatraColors = VedicColors(
    backgroundPrimary = Color(0xFFFDFBF7),  // Soft parchment base
    surfaceCard = Color(0xFFFFFFFF),        // Elevated card background
    primarySaffron = Color(0xFFE65100),     // Focus, energy, key interactive elements
    secondaryGold = Color(0xFFD4AF37),      // Wisdom, badges, streaks
    textPrimary = Color(0xFF212121),        // Charcoal ink for high readability
    textSecondary = Color(0xFF616161),      // Muted step descriptions
    borderSubtle = Color(0x33D4AF37),       // rgba(212, 175, 55, 0.2) Golden micro-borders
    statusSuccess = Color(0xFF2E7D32),      // Tulsi green for correct math inputs
    statusError = Color(0xFFC62828),        // Crimson red for errors
    shadowColor = Color(0x0D000000),        // Soft ambient drop shadow rgba(0,0,0,0.05)
    glowColor = Color(0x1AD4AF37),          // Subtle golden glow
    isDark = false
)

// -------------------------------------------------------------
// Dark Mode (Cosmic Midnight) Design Tokens
// -------------------------------------------------------------
val DarkCosmicColors = VedicColors(
    backgroundPrimary = Color(0xFF0F172A),  // Deep cosmic navy base
    surfaceCard = Color(0xFF1E293B),        // Elevated slate card background
    primarySaffron = Color(0xFFFF9800),     // Vibrant saffron glow
    secondaryGold = Color(0xFFFFD700),      // Bright gold accent
    textPrimary = Color(0xFFF8FAFC),        // Pure starlight white
    textSecondary = Color(0xFF94A3B8),      // Soft muted slate
    borderSubtle = Color(0x40FFD700),       // rgba(255, 215, 0, 0.25) Glowing golden border
    statusSuccess = Color(0xFF4ADE80),      // Emerald green for correct math inputs
    statusError = Color(0xFFEF4444),        // Vibrant red for errors
    shadowColor = Color(0x40000000),        // Deep shadow
    glowColor = Color(0x14FFD700),          // Subtle golden glow rgba(255, 215, 0, 0.08)
    isDark = true
)

// -------------------------------------------------------------
// Sacred Geometry Spacing Tokens (8pt Grid System)
// -------------------------------------------------------------
@Immutable
data class VedicSpacing(
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 16.dp,
    val lg: Dp = 24.dp,
    val xl: Dp = 32.dp
)

// -------------------------------------------------------------
// Sacred Geometry Border Radii Tokens
// -------------------------------------------------------------
@Immutable
data class VedicRadii(
    val card: Dp = 12.dp,       // Subtle rounded corners
    val button: Dp = 8.dp,      // Interactive touch targets
    val pill: Dp = 999.dp       // Badges, chips & tags
)
