package com.ankh.sutrasaga.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.ankh.sutrasaga.ui.components.MentalMathNumpad
import com.ankh.sutrasaga.ui.components.YantraProgressRing
import com.ankh.sutrasaga.ui.theme.VedicTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

data class ArenaProblem(
    val equation: String,
    val targetAnswer: String,
    val sutraHint: String
)

val SampleArenaProblems = listOf(
    ArenaProblem("65²", "4225", "Ekadhikena: 6×7=42 | 25"),
    ArenaProblem("98 × 97", "9506", "Nikhilam: -2,-3 -> 95 | 06"),
    ArenaProblem("25 × 25", "625", "Ekadhikena: 2×3=6 | 25"),
    ArenaProblem("104 × 106", "11024", "Nikhilam: +4,+6 -> 110 | 24"),
    ArenaProblem("35²", "1225", "Ekadhikena: 3×4=12 | 25")
)

/**
 * Screen 03: VedicPracticeArenaScreen — Timed Mental Speed Challenge
 */
@Composable
fun VedicPracticeArenaScreen(
    totalDurationSeconds: Int = 60,
    problems: List<ArenaProblem> = SampleArenaProblems,
    onBackClick: () -> Unit,
    onFinishArena: (score: Int, opsPerMin: Double) -> Unit
) {
    val colors = VedicTheme.colors
    val typography = VedicTheme.typography
    val radii = VedicTheme.radii
    val scope = rememberCoroutineScope()

    var secondsRemaining by remember { mutableIntStateOf(totalDurationSeconds) }
    var currentProblemIdx by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var inputBuffer by remember { mutableStateOf("") }
    var isFinished by remember { mutableStateOf(false) }

    val currentProblem = problems.getOrNull(currentProblemIdx % problems.size) ?: problems.first()

    // 60-second Timer Loop
    LaunchedEffect(key1 = isFinished) {
        if (!isFinished) {
            while (secondsRemaining > 0) {
                delay(1000L)
                secondsRemaining--
            }
            isFinished = true
        }
    }

    // Mandala Timer Pulse when under 10s
    val isUrgent = secondsRemaining <= 10 && secondsRemaining > 0
    val infiniteTransition = rememberInfiniteTransition(label = "TimerPulse")
    val timerScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = if (isUrgent) 1.15f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "MandalaScale"
    )

    // Shake on error
    val shakeOffset = remember { Animatable(0f) }

    // Instant Keypress Verification Engine
    fun handleDigitInput(digit: Char) {
        if (isFinished) return
        val nextInput = inputBuffer + digit
        inputBuffer = nextInput

        if (nextInput == currentProblem.targetAnswer) {
            // Correct Answer! Instant Advance
            score += 100
            inputBuffer = ""
            currentProblemIdx++
        } else if (!currentProblem.targetAnswer.startsWith(nextInput)) {
            // Instant Error Trigger
            scope.launch {
                shakeOffset.animateTo(8f, tween(40))
                shakeOffset.animateTo(-8f, tween(40))
                shakeOffset.animateTo(0f, tween(40))
            }
        }
    }

    fun handleBackspace() {
        if (inputBuffer.isNotEmpty()) {
            inputBuffer = inputBuffer.dropLast(1)
        }
    }

    fun handleClear() {
        inputBuffer = ""
    }

    val elapsedSeconds = (totalDurationSeconds - secondsRemaining).coerceAtLeast(1)
    val opsPerMin = (score / 100.0) / (elapsedSeconds / 60.0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.backgroundPrimary)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 1. Header Area: Score, Speed (Ops/min), and Mandala Timer Ring
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onBackClick,
                shape = RoundedCornerShape(radii.button),
                colors = ButtonDefaults.buttonColors(containerColor = colors.surfaceCard),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = Brush.horizontalGradient(listOf(colors.borderSubtle, colors.borderSubtle)),
                    width = 1.dp
                ),
                contentPadding = ButtonDefaults.TextButtonContentPadding
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Exit",
                    tint = colors.textPrimary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Exit", color = colors.textPrimary, fontSize = 12.sp)
            }

            // Central Rotating Geometric Mandala Timer
            Box(
                modifier = Modifier
                    .scale(timerScale)
                    .size(54.dp),
                contentAlignment = Alignment.Center
            ) {
                YantraProgressRing(
                    progress = secondsRemaining.toFloat() / totalDurationSeconds.toFloat(),
                    size = 54.dp,
                    strokeWidth = 3.dp
                )
                Text(
                    text = "${secondsRemaining}s",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace,
                    color = if (isUrgent) colors.primarySaffron else colors.textPrimary
                )
            }

            // Speed & Score Badge
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$score PTS",
                    style = typography.badge.copy(fontSize = 14.sp),
                    color = colors.secondaryGold
                )
                Text(
                    text = "%.1f ops/min".format(opsPerMin),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.textSecondary
                )
            }
        }

        // 2. Central Focus Zone: Minimalist High-Contrast Tabular Equation Display
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(shakeOffset.value.roundToInt(), 0) }
                .padding(vertical = 10.dp),
            shape = RoundedCornerShape(radii.card),
            colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.horizontalGradient(listOf(colors.borderSubtle, colors.borderSubtle)),
                width = 1.2.dp
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = currentProblem.sutraHint,
                    style = typography.subtitle.copy(fontSize = 13.sp),
                    color = colors.secondaryGold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${currentProblem.equation} = ?",
                    style = typography.mathDisplay.copy(fontSize = 38.sp),
                    color = if (colors.isDark) colors.textPrimary else colors.primarySaffron
                )
                Spacer(modifier = Modifier.height(14.dp))

                // Real-time Input Display Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (colors.isDark) Color(0xFF0B1120) else Color(0xFFF7F5F0))
                        .border(1.dp, colors.primarySaffron, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (inputBuffer.isEmpty()) "Tap digits..." else inputBuffer,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace,
                        color = if (inputBuffer.isEmpty()) colors.textSecondary else colors.primarySaffron
                    )
                }
            }
        }

        // 3. Bottom Streamlined Mental Math Numpad
        MentalMathNumpad(
            onDigitClick = { handleDigitInput(it) },
            onBackspaceClick = { handleBackspace() },
            onClearClick = { handleClear() },
            onSubmitClick = {
                if (inputBuffer == currentProblem.targetAnswer) {
                    score += 100
                    inputBuffer = ""
                    currentProblemIdx++
                }
            },
            enabled = !isFinished
        )
    }

    // 4. Time Up / Results Modal Dialog
    if (isFinished) {
        Dialog(onDismissRequest = {}) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(radii.card),
                colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(listOf(colors.secondaryGold, colors.primarySaffron)),
                    width = 2.dp
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "⚡", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "TIME UP — PRACTICE COMPLETE!",
                        style = typography.h1.copy(fontSize = 18.sp),
                        color = colors.primarySaffron
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Final Score: $score PTS",
                        style = typography.mathDisplay.copy(fontSize = 28.sp),
                        color = colors.secondaryGold
                    )
                    Text(
                        text = "Speed: %.1f ops/min".format(opsPerMin),
                        style = typography.bodySmall,
                        color = colors.textSecondary
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { onFinishArena(score, opsPerMin) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(radii.button),
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primarySaffron)
                    ) {
                        Text(
                            text = "CLAIM SCORE & EXIT ➔",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
