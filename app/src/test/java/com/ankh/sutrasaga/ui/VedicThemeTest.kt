package com.ankh.sutrasaga.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankh.sutrasaga.ui.theme.DarkCosmicColors
import com.ankh.sutrasaga.ui.theme.LightBhojpatraColors
import com.ankh.sutrasaga.ui.theme.VedicRadii
import com.ankh.sutrasaga.ui.theme.VedicSpacing
import com.ankh.sutrasaga.ui.theme.VedicThemeMode
import com.ankh.sutrasaga.ui.theme.VedicThemeState
import com.ankh.sutrasaga.ui.theme.VedicTypography
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class VedicThemeTest {

    @Test
    fun testLightBhojpatraColorsTokens() {
        val colors = LightBhojpatraColors
        assertEquals(Color(0xFFFDFBF7), colors.backgroundPrimary)
        assertEquals(Color(0xFFFFFFFF), colors.surfaceCard)
        assertEquals(Color(0xFFE65100), colors.primarySaffron)
        assertEquals(Color(0xFFD4AF37), colors.secondaryGold)
        assertEquals(Color(0xFF212121), colors.textPrimary)
        assertEquals(Color(0xFF616161), colors.textSecondary)
        assertEquals(Color(0x33D4AF37), colors.borderSubtle)
        assertEquals(Color(0xFF2E7D32), colors.statusSuccess)
        assertEquals(Color(0xFFC62828), colors.statusError)
        assertFalse(colors.isDark)
    }

    @Test
    fun testDarkCosmicColorsTokens() {
        val colors = DarkCosmicColors
        assertEquals(Color(0xFF0F172A), colors.backgroundPrimary)
        assertEquals(Color(0xFF1E293B), colors.surfaceCard)
        assertEquals(Color(0xFFFF9800), colors.primarySaffron)
        assertEquals(Color(0xFFFFD700), colors.secondaryGold)
        assertEquals(Color(0xFFF8FAFC), colors.textPrimary)
        assertEquals(Color(0xFF94A3B8), colors.textSecondary)
        assertEquals(Color(0x40FFD700), colors.borderSubtle)
        assertEquals(Color(0xFF4ADE80), colors.statusSuccess)
        assertEquals(Color(0xFFEF4444), colors.statusError)
        assertTrue(colors.isDark)
    }

    @Test
    fun testSacredGeometrySpacingAndRadiiTokens() {
        val spacing = VedicSpacing()
        assertEquals(4.dp, spacing.xs)
        assertEquals(8.dp, spacing.sm)
        assertEquals(16.dp, spacing.md)
        assertEquals(24.dp, spacing.lg)
        assertEquals(32.dp, spacing.xl)

        val radii = VedicRadii()
        assertEquals(12.dp, radii.card)
        assertEquals(8.dp, radii.button)
        assertEquals(999.dp, radii.pill)
    }

    @Test
    fun testDualFontTypographyScale() {
        val typo = VedicTypography()

        // Classical Serif Scale
        assertEquals(28.sp, typo.h1.fontSize)
        assertEquals(34.sp, typo.h1.lineHeight)
        assertEquals(FontWeight.W700, typo.h1.fontWeight)
        assertEquals(FontFamily.Serif, typo.h1.fontFamily)

        assertEquals(22.sp, typo.h2.fontSize)
        assertEquals(28.sp, typo.h2.lineHeight)
        assertEquals(FontWeight.W600, typo.h2.fontWeight)
        assertEquals(FontFamily.Serif, typo.h2.fontFamily)

        assertEquals(14.sp, typo.subtitle.fontSize)
        assertEquals(20.sp, typo.subtitle.lineHeight)
        assertEquals(FontStyle.Italic, typo.subtitle.fontStyle)
        assertEquals(FontFamily.Serif, typo.subtitle.fontFamily)

        // Tabular Monospace Scale (Tabular numbers enabled)
        assertEquals(36.sp, typo.mathDisplay.fontSize)
        assertEquals(FontWeight.W700, typo.mathDisplay.fontWeight)
        assertEquals(1.sp, typo.mathDisplay.letterSpacing)
        assertEquals("tnum", typo.mathDisplay.fontFeatureSettings)
        assertEquals(FontFamily.Monospace, typo.mathDisplay.fontFamily)

        assertEquals(20.sp, typo.mathStep.fontSize)
        assertEquals(FontWeight.W500, typo.mathStep.fontWeight)
        assertEquals("tnum", typo.mathStep.fontFeatureSettings)
        assertEquals(FontFamily.Monospace, typo.mathStep.fontFamily)

        assertEquals(16.sp, typo.bodyText.fontSize)
        assertEquals(24.sp, typo.bodyText.lineHeight)
    }

    @Test
    fun testThemeStateTransitions() {
        val themeState = VedicThemeState(VedicThemeMode.DARK)
        assertEquals(VedicThemeMode.DARK, themeState.themeMode)

        themeState.toggleTheme()
        assertEquals(VedicThemeMode.LIGHT, themeState.themeMode)

        themeState.toggleTheme()
        assertEquals(VedicThemeMode.DARK, themeState.themeMode)

        themeState.setTheme(VedicThemeMode.SYSTEM)
        assertEquals(VedicThemeMode.SYSTEM, themeState.themeMode)
    }
}
