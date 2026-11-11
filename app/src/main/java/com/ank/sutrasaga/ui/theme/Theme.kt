package com.ank.sutrasaga.ui.theme

import androidx.compose.runtime.Composable

/**
 * Main application theme wrapper for Ank: The Sutra Saga.
 * Bridges VedicThemeProvider tokens (Bhojpatra Parchment & Cosmic Midnight)
 * with Material 3 components.
 */
@Composable
fun AnkTheme(
    themeState: VedicThemeState? = null,
    content: @Composable () -> Unit
) {
    if (themeState != null) {
        VedicThemeProvider(themeState = themeState, content = content)
    } else {
        VedicThemeProvider(content = content)
    }
}
