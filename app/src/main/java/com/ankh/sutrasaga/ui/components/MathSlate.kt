package com.ankh.sutrasaga.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankh.sutrasaga.domain.models.AnswerFormat
import com.ankh.sutrasaga.domain.models.DecompositionStep
import com.ankh.sutrasaga.domain.models.SutraProblem
import com.ankh.sutrasaga.ui.theme.CyberCyan
import com.ankh.sutrasaga.ui.theme.CyberCyanLight
import com.ankh.sutrasaga.ui.theme.HologramBorderBrush
import com.ankh.sutrasaga.ui.theme.TextLightSecondary
import com.ankh.sutrasaga.ui.theme.TextWhitePrimary
import com.ankh.sutrasaga.ui.theme.VedicGold
import com.ankh.sutrasaga.ui.theme.VedicGoldLight

@Composable
fun MathSlate(
    problem: SutraProblem,
    stepIndex: Int,
    interactiveMode: Boolean = false,
    onInteractiveStepAction: ((Int) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val chalkWhite = TextWhitePrimary
    val amberHighlight = VedicGold
    val cyanAccent = CyberCyanLight
    val goldSuccess = VedicGoldLight

    // Dynamic problem parameters
    val operand = problem.operand
    val prefix = problem.prefixPart
    val incrementedPrefix = problem.incrementedPrefix.takeIf { it > 0 } ?: (prefix + 1)
    val prefixProduct = problem.prefixProduct.takeIf { it > 0 } ?: (prefix * incrementedPrefix)
    val suffix = problem.appendedSuffix.ifEmpty { "25" }
    val finalAnswer = problem.correctAnswer
    val isEkadhikenaProblem = problem.sutraName == "Ekadhikena Purvena"
    val currentDecompositionStep = problem.decompositionSteps.getOrNull(stepIndex - 1)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xF20F172A)),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = HologramBorderBrush,
            width = 1.5.dp
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Slate Header Label with glowing ornament
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "✦",
                    color = CyberCyanLight,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "VEDIC MATH SLATE",
                    style = MaterialTheme.typography.labelMedium,
                    letterSpacing = 2.sp,
                    color = CyberCyanLight,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "✦",
                    color = CyberCyanLight,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Animated Math Stage Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(135.dp)
                    .background(Color(0xD90B1120), RoundedCornerShape(14.dp))
                    .border(1.dp, Color(0x3338BDF8), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (!isEkadhikenaProblem) {
                    GenericSutraStage(
                        problem = problem,
                        step = currentDecompositionStep,
                        isInitialStage = stepIndex == 0,
                        chalkWhite = chalkWhite,
                        cyanAccent = cyanAccent,
                        goldSuccess = goldSuccess
                    )
                } else when (stepIndex) {
                    0 -> {
                        // Step 0: Display initial problem (e.g. 65²)
                        Text(
                            text = "$operand² = ?",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Monospace,
                            color = chalkWhite
                        )
                    }
                    1 -> {
                        // Step 1: Tens Prefix Isolation
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Prefix n = ",
                                fontSize = 22.sp,
                                color = TextLightSecondary
                            )
                            val scaleAnim by animateFloatAsState(
                                targetValue = 1.25f,
                                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                                label = "scalePrefix"
                            )
                            Text(
                                text = "$prefix",
                                fontSize = 38.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = amberHighlight,
                                modifier = Modifier.scale(scaleAnim)
                            )
                            Text(
                                text = " (dim 5)",
                                fontSize = 15.sp,
                                color = TextLightSecondary.copy(alpha = 0.6f),
                                modifier = Modifier.padding(start = 6.dp)
                            )
                        }
                    }
                    2 -> {
                        // Step 2: "+1" Increment & Prefix Product Expression
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0x33FFB300))
                                    .border(1.dp, Color(0x66FFB300), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Ekadhikena: +1",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = VedicGoldLight
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "$prefix × ($prefix + 1) = $prefix × $incrementedPrefix",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = FontFamily.Monospace,
                                color = goldSuccess
                            )
                        }
                    }
                    3 -> {
                        // Step 3: Prefix Product Resolution
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Multiply Prefixes",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = cyanAccent
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$prefix × $incrementedPrefix = $prefixProduct",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = FontFamily.Monospace,
                                color = goldSuccess
                            )
                        }
                    }
                    4 -> {
                        // Step 4: Suffix Attachment (25)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "$prefixProduct",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = FontFamily.Monospace,
                                color = goldSuccess
                            )
                            Text(
                                text = " || ",
                                fontSize = 26.sp,
                                color = TextLightSecondary.copy(alpha = 0.5f)
                            )
                            val suffixScale by animateFloatAsState(
                                targetValue = 1.2f,
                                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                                label = "suffixScale"
                            )
                            Text(
                                text = suffix,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = FontFamily.Monospace,
                                color = amberHighlight,
                                modifier = Modifier.scale(suffixScale)
                            )
                        }
                    }
                    else -> {
                        // Step 5+: Final Result & Victory Flourish
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "★ FINAL RESULT ★",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp,
                                color = cyanAccent
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$operand² = $finalAnswer",
                                fontSize = 34.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = FontFamily.Monospace,
                                color = goldSuccess
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Caption / Interactive Controls
            if (interactiveMode) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { onInteractiveStepAction?.invoke(stepIndex) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        if (!isEkadhikenaProblem) {
                            Text(
                                text = currentDecompositionStep?.let { "🔍 Reveal: ${it.label}" } ?: "✓ Complete",
                                color = TextWhitePrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        } else {
                            Text(
                                text = when (stepIndex) {
                                    0 -> "🔍 1. Select Tens Digit"
                                    1 -> "🔍 2. Increment (+1)"
                                    2 -> "🔍 3. Multiply Prefixes"
                                    3 -> "🔍 4. Append $suffix"
                                    else -> "✓ Step Complete"
                                },
                                color = TextWhitePrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            } else {
                val stepDesc = if (!isEkadhikenaProblem) {
                    currentDecompositionStep?.explanation ?: "Final Answer: $finalAnswer"
                } else when (stepIndex) {
                    0 -> "Initial Problem: Calculate $operand²"
                    1 -> "Step 1: Extract prefix n = $prefix"
                    2 -> "Step 2: Apply Ekadhikena (+1) ⟶ $prefix + 1 = $incrementedPrefix"
                    3 -> "Step 3: Multiply prefix: $prefix × $incrementedPrefix = $prefixProduct"
                    4 -> "Step 4: Append suffix $suffix ⟶ $prefixProduct || $suffix"
                    else -> "Final Answer: $finalAnswer"
                }
                Text(
                    text = stepDesc,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextLightSecondary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun GenericSutraStage(
    problem: SutraProblem,
    step: DecompositionStep?,
    isInitialStage: Boolean,
    chalkWhite: Color,
    cyanAccent: Color,
    goldSuccess: Color
) {
    val answerText = when (problem.answerFormat) {
        AnswerFormat.QUOTIENT_AND_REMAINDER -> "Q=${problem.correctAnswer}, R=${problem.expectedRemainder}"
        AnswerFormat.ORDERED_PAIR -> "x=${problem.correctAnswer}, y=${problem.expectedSecondaryAnswer}"
        AnswerFormat.INTEGER -> problem.correctAnswer.toString()
    }

    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when {
            isInitialStage -> Text(
                text = problem.questionText,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace,
                color = chalkWhite,
                textAlign = TextAlign.Center
            )
            step != null -> {
                Text(
                    text = step.label,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = cyanAccent,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "${step.formulaDisplay} = ${step.stepResult}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace,
                    color = goldSuccess,
                    textAlign = TextAlign.Center
                )
            }
            else -> {
                Text(
                    text = "★ FINAL RESULT ★",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp,
                    color = cyanAccent
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Answer = $answerText",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace,
                    color = goldSuccess
                )
            }
        }
    }
}
