package com.ankh.sutrasaga.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankh.sutrasaga.ui.theme.VedicTheme

/**
 * MentalMathNumpad — Minimalist Circular Thumb-Optimized Math Numpad
 *
 * Drives the rapid mental math input engine.
 * - Circular tactile keys with golden micro-ripple/glow feedback.
 * - Saffron submit button.
 * - Full semantic content descriptions for accessibility (TalkBack).
 * - Direct reactivity with StepByStepVisualization in VedicSutraCard.
 */
@Composable
fun MentalMathNumpad(
    onDigitClick: (Char) -> Unit,
    onBackspaceClick: () -> Unit,
    onClearClick: () -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier,
    secondaryActionLabel: String? = null,
    onSecondaryActionClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    keySize: Dp = 60.dp
) {
    val colors = VedicTheme.colors

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Rows 1-3 (Digits 1 to 9)
        val digitRows = listOf(
            listOf('1', '2', '3'),
            listOf('4', '5', '6'),
            listOf('7', '8', '9')
        )

        for (row in digitRows) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (digit in row) {
                    NumpadCircleKey(
                        label = digit.toString(),
                        contentDesc = "Digit $digit",
                        onClick = { onDigitClick(digit) },
                        size = keySize,
                        enabled = enabled
                    )
                }
            }
        }

        // Row 4: [Clear / Secondary] [0] [Backspace]
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Action Key (Clear or Secondary)
            if (secondaryActionLabel != null && onSecondaryActionClick != null) {
                NumpadActionKey(
                    label = secondaryActionLabel,
                    contentDesc = secondaryActionLabel,
                    onClick = onSecondaryActionClick,
                    size = keySize,
                    textColor = colors.secondaryGold,
                    enabled = enabled
                )
            } else {
                NumpadActionKey(
                    label = "CLR",
                    contentDesc = "Clear entered digits",
                    onClick = onClearClick,
                    size = keySize,
                    textColor = colors.statusError,
                    enabled = enabled
                )
            }

            // Center: Digit 0
            NumpadCircleKey(
                label = "0",
                contentDesc = "Digit 0",
                onClick = { onDigitClick('0') },
                size = keySize,
                enabled = enabled
            )

            // Right Action Key: Backspace
            NumpadActionKey(
                label = "⌫",
                contentDesc = "Backspace single digit",
                onClick = onBackspaceClick,
                size = keySize,
                textColor = if (colors.isDark) Color(0xFF38BDF8) else colors.primarySaffron,
                enabled = enabled
            )
        }

        // Row 5: Radiant Saffron SUBMIT ANSWER Action
        Spacer(modifier = Modifier.height(2.dp))
        Button(
            onClick = onSubmitClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .semantics { contentDescription = "Submit Answer" },
            shape = RoundedCornerShape(VedicTheme.radii.button),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primarySaffron,
                contentColor = if (colors.isDark) Color(0xFF0F172A) else Color.White
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            enabled = enabled
        ) {
            Text(
                text = "SUBMIT ANSWER ⚡",
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

/**
 * Minimalist Circular Key with Micro-Glow / Ripple on Press
 */
@Composable
private fun NumpadCircleKey(
    label: String,
    contentDesc: String,
    onClick: () -> Unit,
    size: Dp,
    enabled: Boolean
) {
    val colors = VedicTheme.colors
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Smooth press scaling & glow intensity
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1.0f,
        animationSpec = tween(durationMillis = 100),
        label = "KeyScale"
    )

    val borderGlowColor = if (isPressed) {
        if (colors.isDark) colors.secondaryGold else colors.primarySaffron
    } else {
        colors.borderSubtle
    }

    val backgroundBrush = if (colors.isDark) {
        if (isPressed) {
            Brush.radialGradient(listOf(Color(0xFF334155), Color(0xFF1E293B)))
        } else {
            Brush.radialGradient(listOf(Color(0xFF1E293B), Color(0xFF0F172A)))
        }
    } else {
        if (isPressed) {
            Brush.radialGradient(listOf(Color(0xFFFFF3E0), Color(0xFFFFFFFF)))
        } else {
            Brush.radialGradient(listOf(Color(0xFFFFFFFF), Color(0xFFFDFBF7)))
        }
    }

    Box(
        modifier = Modifier
            .size(size)
            .scale(scale)
            .shadow(
                elevation = if (isPressed) 1.dp else 3.dp,
                shape = CircleShape,
                ambientColor = colors.shadowColor,
                spotColor = colors.glowColor
            )
            .clip(CircleShape)
            .background(backgroundBrush)
            .border(
                width = if (isPressed) 1.8.dp else 1.dp,
                color = borderGlowColor,
                shape = CircleShape
            )
            .semantics { contentDescription = contentDesc }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = (size.value * 0.40f).sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = colors.textPrimary
        )
    }
}

/**
 * Functional Action Key (CLR, ⌫, Secondary Action)
 */
@Composable
private fun NumpadActionKey(
    label: String,
    contentDesc: String,
    onClick: () -> Unit,
    size: Dp,
    textColor: Color,
    enabled: Boolean
) {
    val colors = VedicTheme.colors
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1.0f,
        animationSpec = tween(durationMillis = 100),
        label = "ActionKeyScale"
    )

    Box(
        modifier = Modifier
            .size(size)
            .scale(scale)
            .clip(CircleShape)
            .background(
                if (colors.isDark) Color(0xFF161E2E) else Color(0xFFF5F5F4)
            )
            .border(
                width = 1.dp,
                color = if (isPressed) textColor else colors.borderSubtle,
                shape = CircleShape
            )
            .semantics { contentDescription = contentDesc }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = if (label.length > 2) 11.sp else (size.value * 0.35f).sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.Monospace,
            color = textColor
        )
    }
}
