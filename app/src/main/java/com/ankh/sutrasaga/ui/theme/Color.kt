package com.ankh.sutrasaga.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Vedic Sacred Gold Palette
val VedicGold = Color(0xFFFFB300)
val VedicGoldLight = Color(0xFFFFD54F)
val VedicGoldDark = Color(0xFFD97706)
val VedicGoldGlow = Color(0x66FFB300)

// Ethereal Cyber Cyan Palette
val CyberCyan = Color(0xFF00E5FF)
val CyberCyanLight = Color(0xFF38BDF8)
val CyberCyanDark = Color(0xFF0284C7)
val CyberCyanGlow = Color(0x6600E5FF)

// Cosmic Deep Slate & Dark Glass Surfaces
val CosmicBackground = Color(0xFF0B1120)
val GlassSurfaceDark = Color(0xE60F172A)
val GlassCardDark = Color(0xEE1E293B)
val GlassScrim = Color(0xCC0B1120)
val SurfaceCard = Color(0xFF1E293B)
val SurfaceCardElevated = Color(0xFF26334D)

// Accents & State Colors
val SuccessEmerald = Color(0xFF10B981)
val SuccessEmeraldGlow = Color(0x4010B981)
val ErrorCrimson = Color(0xFFEF4444)
val BossCrimsonDark = Color(0xFF991B1B)
val BossCrimsonLight = Color(0xFFDC2626)
val AccentPurple = Color(0xFF8B5CF6)
val AmberHighlight = Color(0xFFF59E0B)

// Typography Text Colors
val TextWhitePrimary = Color(0xFFF8FAFC)
val TextLightSecondary = Color(0xFFCBD5E1)
val TextMuted = Color(0xFF94A3B8)
val TextDarkPrimary = Color(0xFF0F172A)

// Gradients
val GoldGradientBrush = Brush.horizontalGradient(
    listOf(Color(0xFFFFB300), Color(0xFFF59E0B))
)

val CyanGradientBrush = Brush.horizontalGradient(
    listOf(Color(0xFF38BDF8), Color(0xFF00E5FF))
)

val BossGradientBrush = Brush.horizontalGradient(
    listOf(Color(0xFFB91C1C), Color(0xFFEA580C))
)

val HologramBorderBrush = Brush.linearGradient(
    listOf(Color(0x9900E5FF), Color(0x99FFB300), Color(0x9938BDF8))
)

val GlassCardGradientBrush = Brush.verticalGradient(
    listOf(Color(0xFA1E293B), Color(0xFA0F172A))
)
