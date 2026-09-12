package com.ankh.sutrasaga.ui.theme

import androidx.compose.runtime.Composable

/**
 * Main application theme wrapper for Ankh: The Sutra Saga.
 * Bridges VedicThemeProvider tokens (Bhojpatra Parchment & Cosmic Midnight)
 * with Material 3 components.
 */
@Composable
fun AnkhTheme(
    themeState: VedicThemeState? = null,
    content: @Composable () -> Unit
) {
    if (themeState != null) {
        VedicThemeProvider(themeState = themeState, content = content)
    } else {
        VedicThemeProvider(content = content)
    }
}
