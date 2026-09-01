package com.ankh.sutrasaga.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * VedicParchmentTokens — Warm Indian-manuscript color palette, typography, and borders
 * for the Duolingo-inspired character dialogue and tutorial scenes.
 */
object VedicParchmentTokens {

    // Backgrounds & Surfaces
    val ParchmentBase = Color(0xFFFDF9F0)
    val ParchmentLayer = Color(0xFFF8F1E5)
    val ParchmentDarker = Color(0xFFEFE4D2)
    val ParchmentCardBg = Color(0xFFFFFDF9)
    val ParchmentCardAltBg = Color(0xFFFAF5EC)

    // Saffron, Ochre & Gold Accents
    val SaffronPrimary = Color(0xFFD97706)
    val SaffronDark = Color(0xFFB45309)
    val SaffronLight = Color(0xFFF59E0B)
    val SaffronGlow = Color(0xFFFDE68A)
    val GoldTrim = Color(0xFFCA8A04)

    // Inks & Text Colors (High Contrast on Parchment)
    val InkDark = Color(0xFF2C1810)
    val InkMedium = Color(0xFF4A3B32)
    val InkLight = Color(0xFF786558)
    val InkMuted = Color(0xFF9E8E81)

    // Vedic Forest Green (Guru Accents & Correct Handshake)
    val ForestGreenDark = Color(0xFF14532D)
    val ForestGreenPrimary = Color(0xFF15803D)
    val ForestGreenLight = Color(0xFF22C55E)
    val ForestGreenBg = Color(0xFFDCFCE7)
    val ForestGreenBorder = Color(0xFF86EFAC)

    // Shishya Amber / Ochre Tags
    val ShishyaOrange = Color(0xFFC2410C)
    val ShishyaOrangeBg = Color(0xFFFFEDD5)
    val ShishyaOrangeBorder = Color(0xFFFDBA74)

    // Wooden Chalkboard Slate Colors
    val ChalkSlateBg = Color(0xFF1E293B)
    val ChalkSlateInner = Color(0xFF0F172A)
    val ChalkSlateBorder = Color(0xFF854D0E)
    val ChalkSlateWood = Color(0xFF713F12)
    val ChalkSlateWoodLight = Color(0xFFA16207)
    val ChalkTextWhite = Color(0xFFF8FAFC)
    val ChalkTextGold = Color(0xFFFDE047)
    val ChalkTextHighlightBg = Color(0x33CA8A04)

    // Interactive Handshake Selection Tiles
    val TileDefaultBg = Color(0xFFFDFBF7)
    val TileDefaultBorder = Color(0xFFD6C7B2)
    val TileDefaultText = Color(0xFF2C1810)

    val TileSelectedBg = Color(0xFFFEF3C7)
    val TileSelectedBorder = Color(0xFFD97706)

    val TileCorrectBg = Color(0xFF15803D)
    val TileCorrectBorder = Color(0xFF166534)
    val TileCorrectText = Color(0xFFFFFFFF)

    val TileIncorrectBg = Color(0xFFFEE2E2)
    val TileIncorrectBorder = Color(0xFFDC2626)
    val TileIncorrectText = Color(0xFF991B1B)

    // Gradients
    val ParchmentGradient = Brush.verticalGradient(
        listOf(ParchmentBase, ParchmentLayer, ParchmentDarker)
    )

    val WoodFrameGradient = Brush.horizontalGradient(
        listOf(ChalkSlateWood, ChalkSlateWoodLight, ChalkSlateWood)
    )

    val SuccessBtnGradient = Brush.horizontalGradient(
        listOf(ForestGreenPrimary, ForestGreenDark)
    )
}
