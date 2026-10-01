package com.ankh.sutrasaga.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

enum class VedicThemeMode {
    LIGHT,  // Bhojpatra Parchment
    DARK,   // Cosmic Midnight
    SYSTEM  // Follow Device OS Setting
}

class VedicThemeState(
    initialMode: VedicThemeMode = VedicThemeMode.DARK
) {
    var themeMode by mutableStateOf(initialMode)
        private set

    fun setTheme(mode: VedicThemeMode) {
        themeMode = mode
    }

    fun toggleTheme() {
        themeMode = if (themeMode == VedicThemeMode.DARK) {
            VedicThemeMode.LIGHT
        } else {
            VedicThemeMode.DARK
        }
    }
}

val LocalVedicColors = staticCompositionLocalOf { DarkCosmicColors }
val LocalVedicTypography = staticCompositionLocalOf { VedicTypography() }
val LocalVedicSpacing = staticCompositionLocalOf { VedicSpacing() }
val LocalVedicRadii = staticCompositionLocalOf { VedicRadii() }
val LocalVedicThemeState = staticCompositionLocalOf { VedicThemeState() }

object VedicTheme {
    val colors: VedicColors
        @Composable
        @ReadOnlyComposable
        get() = LocalVedicColors.current

    val typography: VedicTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalVedicTypography.current

    val spacing: VedicSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalVedicSpacing.current

    val radii: VedicRadii
        @Composable
        @ReadOnlyComposable
        get() = LocalVedicRadii.current

    val themeState: VedicThemeState
        @Composable
        @ReadOnlyComposable
        get() = LocalVedicThemeState.current

    val isDark: Boolean
        @Composable
        @ReadOnlyComposable
        get() = LocalVedicColors.current.isDark
}

@Composable
fun VedicThemeProvider(
    themeState: VedicThemeState = remember { VedicThemeState(VedicThemeMode.DARK) },
    content: @Composable () -> Unit
) {
    val systemInDark = isSystemInDarkTheme()
    val isDark = when (themeState.themeMode) {
        VedicThemeMode.LIGHT -> false
        VedicThemeMode.DARK -> true
        VedicThemeMode.SYSTEM -> systemInDark
    }

    val colors = if (isDark) DarkCosmicColors else LightBhojpatraColors
    val typography = remember { VedicTypography() }
    val spacing = remember { VedicSpacing() }
    val radii = remember { VedicRadii() }

    // Material 3 Bridge
    val materialColorScheme = if (isDark) {
        darkColorScheme(
            primary = colors.primarySaffron,
            onPrimary = colors.backgroundPrimary,
            secondary = colors.secondaryGold,
            onSecondary = colors.backgroundPrimary,
            background = colors.backgroundPrimary,
            surface = colors.surfaceCard,
            onBackground = colors.textPrimary,
            onSurface = colors.textPrimary,
            error = colors.statusError
        )
    } else {
        lightColorScheme(
            primary = colors.primarySaffron,
            onPrimary = colors.surfaceCard,
            secondary = colors.secondaryGold,
            onSecondary = colors.surfaceCard,
            background = colors.backgroundPrimary,
            surface = colors.surfaceCard,
            onBackground = colors.textPrimary,
            onSurface = colors.textPrimary,
            error = colors.statusError
        )
    }

    CompositionLocalProvider(
        LocalVedicColors provides colors,
        LocalVedicTypography provides typography,
        LocalVedicSpacing provides spacing,
        LocalVedicRadii provides radii,
        LocalVedicThemeState provides themeState
    ) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            typography = AnkhTypography,
            content = content
        )
    }
}
