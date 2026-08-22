package com.ankh.sutrasaga.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankh.sutrasaga.domain.models.DecompositionStep
import com.ankh.sutrasaga.ui.theme.VedicTheme

/**
 * Geometric Arrow Visualization Type for Sutra step navigation
 */
enum class YantraArrowPattern {
    VERTICAL_RIGHT,     // Urdhva Tiryagbhyam: Step 1 (Units × Units)
    CROSSWISE,          // Urdhva Tiryagbhyam: Step 2 (Cross Multiply & Add)
    VERTICAL_LEFT,      // Urdhva Tiryagbhyam: Step 3 (Tens × Tens)
    DEFICIENCY_SQUARE,  // Yavadunam: (Base - Deviation) | Deviation²
    COMPLEMENT_SUBTRACT,// Nikhilam: All from 9, last from 10
    NONE
}

/**
 * VedicSutraCard — Sacred Manuscript Math Visualization Slate
 *
 * Implements the core interactive visualization area for Vedic Sutras:
 * - Elevated manuscript aesthetics with gold-foil micro-border
 * - Classical Serif header (Sanskrit + English)
 * - Monospace tabular alignment ('tnum') with carry numbers
 * - Animated Yantra geometric rays and multiplication pathways
 * - Saffron active highlights & Emerald completion validation
 */
@Composable
fun VedicSutraCard(
    sutraSanskritName: String,
    sutraEnglishName: String,
    primaryEquation: String,
    steps: List<DecompositionStep>,
    activeStepIndex: Int,
    isCompleted: Boolean,
    modifier: Modifier = Modifier,
    arrowPattern: YantraArrowPattern = YantraArrowPattern.CROSSWISE,
    carries: List<Int>? = null,
    userInputDisplay: String = ""
) {
    val colors = VedicTheme.colors
    val typography = VedicTheme.typography
    val radii = VedicTheme.radii

    // Animated ray glow for Yantra arrow lines
    val infiniteTransition = rememberInfiniteTransition(label = "YantraArrowRay")
    val rayPulse by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ArrowPulse"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (isCompleted) 6.dp else 4.dp,
                shape = RoundedCornerShape(radii.card),
                ambientColor = if (isCompleted) colors.statusSuccess.copy(alpha = 0.2f) else colors.shadowColor,
                spotColor = if (isCompleted) colors.statusSuccess.copy(alpha = 0.3f) else colors.glowColor
            ),
        shape = RoundedCornerShape(radii.card),
        colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = if (isCompleted) {
                Brush.horizontalGradient(listOf(colors.statusSuccess, Color(0xFF86EFAC)))
            } else {
                Brush.horizontalGradient(
                    if (colors.isDark) listOf(Color(0x40FFD700), Color(0x2038BDF8), Color(0x40FFD700))
                    else listOf(Color(0x33D4AF37), Color(0x20E65100), Color(0x33D4AF37))
                )
            },
            width = 1.2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Header: Sanskrit & English Badges with YantraProgressRing
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = sutraSanskritName,
                        style = typography.h2.copy(fontSize = 18.sp),
                        color = if (colors.isDark) colors.secondaryGold else colors.primarySaffron
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = sutraEnglishName,
                        style = typography.subtitle.copy(fontSize = 13.sp),
                        color = colors.textSecondary
                    )
                }

                // Mini YantraProgressRing indicator
                val progressFraction = if (steps.isEmpty()) 0f
                else ((activeStepIndex + 1).coerceAtMost(steps.size)).toFloat() / steps.size.toFloat()

                YantraProgressRing(
                    progress = if (isCompleted) 1.0f else progressFraction,
                    size = 40.dp,
                    strokeWidth = 2.5.dp,
                    showPercentageText = false
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. Primary Mathematical Visualization Area with Geometric Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (colors.isDark) Color(0xD90B1120) else Color(0xFFF7F5F0)
                    )
                    .border(
                        1.dp,
                        if (isCompleted) colors.statusSuccess.copy(alpha = 0.4f) else colors.borderSubtle,
                        RoundedCornerShape(10.dp)
                    )
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                // Background Geometric Yantra Arrow Rays Overlay
                Canvas(
                    modifier = Modifier
                        .matchParentSize()
                        .padding(horizontal = 24.dp, vertical = 6.dp)
                ) {
                    val w = size.width
                    val h = size.height
                    val arrowColor = if (isCompleted) colors.statusSuccess.copy(alpha = 0.5f)
                    else colors.primarySaffron.copy(alpha = 0.7f * rayPulse)

                    when (arrowPattern) {
                        YantraArrowPattern.VERTICAL_RIGHT -> {
                            // Straight vertical ray through right-most digits
                            drawLine(
                                color = arrowColor,
                                start = Offset(w * 0.75f, h * 0.15f),
                                end = Offset(w * 0.75f, h * 0.85f),
                                strokeWidth = 2.5.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                        }
                        YantraArrowPattern.CROSSWISE -> {
                            // Crosswise X-rays connecting opposing diagonal digits
                            drawLine(
                                color = arrowColor,
                                start = Offset(w * 0.30f, h * 0.15f),
                                end = Offset(w * 0.70f, h * 0.85f),
                                strokeWidth = 2.2.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                            drawLine(
                                color = arrowColor,
                                start = Offset(w * 0.70f, h * 0.15f),
                                end = Offset(w * 0.30f, h * 0.85f),
                                strokeWidth = 2.2.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                        }
                        YantraArrowPattern.VERTICAL_LEFT -> {
                            // Straight vertical ray through left-most digits
                            drawLine(
                                color = arrowColor,
                                start = Offset(w * 0.25f, h * 0.15f),
                                end = Offset(w * 0.25f, h * 0.85f),
                                strokeWidth = 2.5.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                        }
                        YantraArrowPattern.DEFICIENCY_SQUARE -> {
                            // Divider bar ray
                            drawLine(
                                color = arrowColor,
                                start = Offset(w * 0.50f, h * 0.10f),
                                end = Offset(w * 0.50f, h * 0.90f),
                                strokeWidth = 1.8.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f))
                            )
                        }
                        YantraArrowPattern.COMPLEMENT_SUBTRACT -> {
                            // Arch arrow from top to bottom
                            drawLine(
                                color = arrowColor,
                                start = Offset(w * 0.35f, h * 0.20f),
                                end = Offset(w * 0.65f, h * 0.80f),
                                strokeWidth = 2.dp.toPx()
                            )
                        }
                        YantraArrowPattern.NONE -> { /* No arrow drawn */ }
                    }
                }

                // Foreground Monospace Math Digits & Carries
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Optional carry digits row
                    if (carries != null && carries.isNotEmpty()) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.padding(bottom = 2.dp)
                        ) {
                            for (carry in carries) {
                                Text(
                                    text = if (carry > 0) "+$carry" else "  ",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = colors.secondaryGold
                                )
                            }
                        }
                    }

                    // Main Math Expression (e.g. 23 × 14)
                    Text(
                        text = primaryEquation,
                        style = typography.mathDisplay.copy(fontSize = 32.sp),
                        color = if (isCompleted) colors.statusSuccess
                        else if (colors.isDark) colors.secondaryGold
                        else colors.primarySaffron
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 3. Step-by-Step Working Area with Active Saffron Highlight
            if (steps.isNotEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    steps.forEachIndexed { index, step ->
                        val isActive = index == activeStepIndex && !isCompleted
                        val isPast = index < activeStepIndex || isCompleted

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    if (isActive) colors.primarySaffron.copy(alpha = 0.12f)
                                    else Color.Transparent
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "• Step ${step.stepNumber} (${step.label}): ${step.formulaDisplay}",
                                style = typography.bodySmall,
                                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                                color = when {
                                    isActive -> colors.primarySaffron
                                    isPast -> colors.textPrimary
                                    else -> colors.textSecondary
                                }
                            )

                            Text(
                                text = if (isPast) "= ${step.stepResult}" else if (isActive) "…" else " ",
                                style = typography.mathStep.copy(fontSize = 14.sp),
                                fontWeight = FontWeight.Bold,
                                color = if (isPast) colors.statusSuccess else colors.primarySaffron
                            )
                        }
                    }
                }
            }

            // 4. Live User Input Display Box
            if (userInputDisplay.isNotEmpty() || isCompleted) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (isCompleted) colors.statusSuccess.copy(alpha = 0.12f)
                            else if (colors.isDark) Color(0xFF0F172A)
                            else Color(0xFFFFFFFF)
                        )
                        .border(
                            1.dp,
                            if (isCompleted) colors.statusSuccess else colors.borderSubtle,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isCompleted) "✓ MASTERED CALCULATION" else userInputDisplay,
                        style = typography.mathStep,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isCompleted) colors.statusSuccess else colors.textPrimary
                    )
                }
            }
        }
    }
}
