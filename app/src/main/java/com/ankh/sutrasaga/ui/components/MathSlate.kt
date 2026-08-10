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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankh.sutrasaga.domain.models.SutraProblem

@Composable
fun MathSlate(
    problem: SutraProblem,
    stepIndex: Int,
    interactiveMode: Boolean = false,
    onInteractiveStepAction: ((Int) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val slateBg = Color(0xFF1B262C) // Chalkboard charcoal
    val frameBorder = Color(0xFF8D6E63) // Wooden frame
    val chalkWhite = Color(0xFFECEFF1)
    val amberHighlight = Color(0xFFFFB300)
    val cyanAccent = Color(0xFF00E5FF)
    val goldSuccess = Color(0xFFFFD54F)

    // Dynamic problem parameters
    val operand = problem.operand
    val prefix = problem.prefixPart
    val incrementedPrefix = problem.incrementedPrefix.takeIf { it > 0 } ?: (prefix + 1)
    val prefixProduct = problem.prefixProduct.takeIf { it > 0 } ?: (prefix * incrementedPrefix)
    val suffix = problem.appendedSuffix.ifEmpty { "25" }
    val finalAnswer = problem.correctAnswer

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = slateBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .border(6.dp, frameBorder, RoundedCornerShape(16.dp))
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Slate Header Label
            Text(
                text = "❖ VEDIC MATH SLATE ❖",
                style = MaterialTheme.typography.labelMedium,
                letterSpacing = 2.sp,
                color = cyanAccent,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Animated Math Stage Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(Color(0xFF0F171E), RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFF37474F), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                when (stepIndex) {
                    0 -> {
                        // Step 0: Display initial problem (e.g. 65²)
                        Text(
                            text = "$operand² = ?",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
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
                                color = chalkWhite.copy(alpha = 0.8f)
                            )
                            val scaleAnim by animateFloatAsState(
                                targetValue = 1.3f,
                                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                                label = "scalePrefix"
                            )
                            Text(
                                text = "$prefix",
                                fontSize = 38.sp,
                                fontWeight = FontWeight.Bold,
                                color = amberHighlight,
                                modifier = Modifier.scale(scaleAnim)
                            )
                            Text(
                                text = " (digit 5 dimmed)",
                                fontSize = 16.sp,
                                color = chalkWhite.copy(alpha = 0.4f),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                    2 -> {
                        // Step 2: "+1" Increment & Prefix Product Expression
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(amberHighlight)
                                    .padding(horizontal = 10.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Ekadhikena: +1",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "$prefix × ($prefix + 1) = $prefix × $incrementedPrefix",
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
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
                                fontSize = 14.sp,
                                color = cyanAccent
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$prefix × $incrementedPrefix = $prefixProduct",
                                fontSize = 34.sp,
                                fontWeight = FontWeight.Bold,
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
                                fontSize = 34.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = goldSuccess
                            )
                            Text(
                                text = " || ",
                                fontSize = 28.sp,
                                color = chalkWhite.copy(alpha = 0.5f)
                            )
                            val suffixScale by animateFloatAsState(
                                targetValue = 1.2f,
                                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                                label = "suffixScale"
                            )
                            Text(
                                text = suffix,
                                fontSize = 34.sp,
                                fontWeight = FontWeight.Bold,
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
                                text = "★ RESULT ★",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = goldSuccess
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$operand² = $finalAnswer",
                                fontSize = 38.sp,
                                fontWeight = FontWeight.Bold,
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
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { onInteractiveStepAction?.invoke(stepIndex) },
                        colors = ButtonDefaults.buttonColors(containerColor = cyanAccent)
                    ) {
                        Text(
                            text = when (stepIndex) {
                                0 -> "1. Select Tens Digit"
                                1 -> "2. Increment (+1)"
                                2 -> "3. Multiply Prefixes"
                                3 -> "4. Append $suffix"
                                else -> "✓ Complete"
                            },
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else {
                val stepDesc = when (stepIndex) {
                    0 -> "Initial Problem: Calculate $operand²"
                    1 -> "Step 1: Extract prefix n = $prefix"
                    2 -> "Step 2: Apply Ekadhikena (+1) $\\rightarrow$ $prefix + 1 = $incrementedPrefix"
                    3 -> "Step 3: Multiply prefix: $prefix × $incrementedPrefix = $prefixProduct"
                    4 -> "Step 4: Append suffix $suffix $\\rightarrow$ $prefixProduct || $suffix"
                    else -> "Final Answer: $finalAnswer"
                }
                Text(
                    text = stepDesc,
                    style = MaterialTheme.typography.bodyMedium,
                    color = chalkWhite.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
