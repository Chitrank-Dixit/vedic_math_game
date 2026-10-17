package com.ankh.sutrasaga.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankh.sutrasaga.ui.theme.LocalVedicThemeState
import com.ankh.sutrasaga.ui.theme.VedicTheme
import com.ankh.sutrasaga.ui.theme.VedicThemeMode

/**
 * Typography variants mapped directly to the Vedic Design System Scale.
 */
enum class VedicTextVariant {
    H1,             // Classical Serif Sutra Title (28sp, 700)
    H2,             // Classical Serif Section Header (22sp, 600)
    SUBTITLE,       // Classical Serif Sutra Translation (14sp, Italic)
    MATH_DISPLAY,   // Tabular Monospace Primary Equation (36sp, 700, tnum)
    MATH_STEP,      // Tabular Monospace Intermediate Steps (20sp, 500, tnum)
    BODY,           // Body Text (16sp, 400)
    BODY_SMALL,     // Caption / Muted (14sp, 400)
    BADGE,          // Pill / Badge Text (12sp, 700)
    SUCCESS,        // Tulsi / Emerald Status Text (14sp, 600)
    ERROR           // Crimson Status Text (14sp, 600)
}

/**
 * Standard VedicText wrapper component applying typography scale and theme-aware colors.
 */
@Composable
fun VedicText(
    text: String,
    variant: VedicTextVariant = VedicTextVariant.BODY,
    modifier: Modifier = Modifier,
    color: Color? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    val typography = VedicTheme.typography
    val colors = VedicTheme.colors

    val style = when (variant) {
        VedicTextVariant.H1 -> typography.h1
        VedicTextVariant.H2 -> typography.h2
        VedicTextVariant.SUBTITLE -> typography.subtitle
        VedicTextVariant.MATH_DISPLAY -> typography.mathDisplay
        VedicTextVariant.MATH_STEP -> typography.mathStep
        VedicTextVariant.BODY -> typography.bodyText
        VedicTextVariant.BODY_SMALL -> typography.bodySmall
        VedicTextVariant.BADGE -> typography.badge
        VedicTextVariant.SUCCESS -> typography.bodySmall.copy(fontWeight = FontWeight.W600)
        VedicTextVariant.ERROR -> typography.bodySmall.copy(fontWeight = FontWeight.W600)
    }

    val resolvedColor = color ?: when (variant) {
        VedicTextVariant.H1 -> if (colors.isDark) colors.secondaryGold else colors.textPrimary
        VedicTextVariant.H2 -> colors.textPrimary
        VedicTextVariant.SUBTITLE -> if (colors.isDark) colors.secondaryGold else colors.primarySaffron
        VedicTextVariant.MATH_DISPLAY -> if (colors.isDark) colors.secondaryGold else colors.primarySaffron
        VedicTextVariant.MATH_STEP -> if (colors.isDark) Color(0xFF38BDF8) else colors.textPrimary
        VedicTextVariant.BODY -> colors.textPrimary
        VedicTextVariant.BODY_SMALL -> colors.textSecondary
        VedicTextVariant.BADGE -> if (colors.isDark) colors.secondaryGold else colors.primarySaffron
        VedicTextVariant.SUCCESS -> colors.statusSuccess
        VedicTextVariant.ERROR -> colors.statusError
    }

    Text(
        text = text,
        style = style,
        color = resolvedColor,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow,
        modifier = modifier
    )
}

/**
 * Standard VedicCard wrapper container using surfaceCard, borderSubtle, and 12dp card radii.
 */
@Composable
fun VedicCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(VedicTheme.radii.card),
    backgroundColor: Color? = null,
    borderColor: Color? = null,
    elevation: Dp = 4.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = VedicTheme.colors
    val resolvedBg = backgroundColor ?: colors.surfaceCard
    val resolvedBorder = borderColor ?: colors.borderSubtle

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = elevation,
                shape = shape,
                ambientColor = colors.shadowColor,
                spotColor = colors.glowColor
            ),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = resolvedBg),
        border = BorderStroke(1.dp, resolvedBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(VedicTheme.spacing.md),
            content = content
        )
    }
}

/**
 * Standard VedicButton wrapper component using primarySaffron and 8dp button radii.
 */
@Composable
fun VedicButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isOutlined: Boolean = false,
    enabled: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null
) {
    val colors = VedicTheme.colors
    val radii = VedicTheme.radii
    val shape = RoundedCornerShape(radii.button)

    if (isOutlined) {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier.height(44.dp),
            shape = shape,
            border = BorderStroke(1.dp, colors.borderSubtle),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = colors.primarySaffron,
                containerColor = Color.Transparent
            ),
            enabled = enabled
        ) {
            if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    } else {
        Button(
            onClick = onClick,
            modifier = modifier.height(44.dp),
            shape = shape,
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primarySaffron,
                contentColor = if (colors.isDark) Color(0xFF0F172A) else Color.White
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp),
            enabled = enabled
        ) {
            if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

/**
 * Standard VedicBadge pill wrapper component using 999dp pill radii.
 */
@Composable
fun VedicBadge(
    text: String,
    modifier: Modifier = Modifier,
    isSuccess: Boolean = false,
    isGold: Boolean = true
) {
    val colors = VedicTheme.colors
    val radii = VedicTheme.radii
    val shape = RoundedCornerShape(radii.pill)

    val (bg, contentColor) = when {
        isSuccess -> Pair(
            colors.statusSuccess.copy(alpha = 0.15f),
            colors.statusSuccess
        )
        isGold -> Pair(
            colors.secondaryGold.copy(alpha = 0.15f),
            colors.secondaryGold
        )
        else -> Pair(
            colors.primarySaffron.copy(alpha = 0.15f),
            colors.primarySaffron
        )
    }

    Box(
        modifier = modifier
            .clip(shape)
            .background(bg)
            .border(1.dp, contentColor.copy(alpha = 0.4f), shape)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = VedicTheme.typography.badge,
            color = contentColor
        )
    }
}

/**
 * Interactive Theme Switcher toggle component (Bhojpatra Parchment <-> Cosmic Midnight).
 */
@Composable
fun VedicThemeSwitcher(
    modifier: Modifier = Modifier
) {
    val themeState = LocalVedicThemeState.current
    val colors = VedicTheme.colors

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(VedicTheme.radii.pill))
            .background(colors.surfaceCard)
            .border(1.dp, colors.borderSubtle, RoundedCornerShape(VedicTheme.radii.pill))
            .semantics {
                contentDescription = "Switch theme between Light and Dark mode"
            }
            .clickable(
                onClickLabel = "Toggle theme mode",
                onClick = { themeState.toggleTheme() }
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (colors.isDark) "🌙" else "☀️",
            fontSize = 15.sp,
            color = colors.textPrimary
        )
    }
}
