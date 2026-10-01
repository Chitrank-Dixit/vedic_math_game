package com.ankh.sutrasaga.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Dual-Font Typography Scale Engine for Vedic Mathematics:
 * 1. Classical Serif Scale (Titles, Sutras, Sanskrit aphorisms, badges)
 * 2. Tabular Monospace Scale (Math formulas, step-by-step arithmetic with 'tnum' tabular figures)
 */
@Immutable
data class VedicTypography(
    // Classical Serif Scale
    val h1: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.W700,
        fontSize = 28.sp,
        lineHeight = 34.sp
    ),
    val h2: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.W600,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    val subtitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),

    // Tabular Monospace Scale (with 'tnum' feature for exact column alignment)
    val mathDisplay: TextStyle = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.W700,
        fontSize = 36.sp,
        letterSpacing = 1.sp,
        fontFeatureSettings = "tnum"
    ),
    val mathStep: TextStyle = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.W500,
        fontSize = 20.sp,
        fontFeatureSettings = "tnum"
    ),
    val bodyText: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    val bodySmall: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    val badge: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.W700,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
