package com.ankh.sutrasaga.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import com.ankh.sutrasaga.ui.components.GuruGuidanceBanner
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.ankh.sutrasaga.domain.models.DecompositionStep
import com.ankh.sutrasaga.ui.components.MentalMathNumpad
import com.ankh.sutrasaga.ui.components.VedicSutraCard
import com.ankh.sutrasaga.ui.components.YantraArrowPattern
import com.ankh.sutrasaga.ui.components.YantraProgressRing
import com.ankh.sutrasaga.ui.theme.VedicTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

data class SutraLesson(
    val id: String,
    val sanskritTitle: String,
    val englishTitle: String,
    val shloka: String,
    val literalMeaning: String,
    val shortcutRule: String,
    val primaryEquation: String,
    val targetAnswer: String,
    val steps: List<DecompositionStep>,
    val carries: List<Int> = emptyList()
)

val SampleUrdhvaLesson = SutraLesson(
    id = "urdhva_23x14",
    sanskritTitle = "ऊर्ध्व तिर्यग्भ्याम्",
    englishTitle = "Vertically and Crosswise",
    shloka = "ऊर्ध्वतिर्यग्भ्यां गुणयेत्।",
    literalMeaning = "Vertically and crosswise multiply.",
    shortcutRule = "Multiply vertical units digits -> Cross-multiply and sum -> Multiply vertical tens digits and add carries.",
    primaryEquation = "23 × 14",
    targetAnswer = "322",
    steps = listOf(
        DecompositionStep(
            stepNumber = 1,
            label = "Units × Units",
            formulaDisplay = "3 × 4",
            stepResult = "12 (write 2, carry 1)",
            explanation = "Multiply vertical right digits"
        ),
        DecompositionStep(
            stepNumber = 2,
            label = "Crosswise Multiplication",
            formulaDisplay = "2(4) + 3(1) + 1",
            stepResult = "12 (write 2, carry 1)",
            explanation = "Multiply crosswise diagonals and add carry"
        ),
        DecompositionStep(
            stepNumber = 3,
            label = "Tens × Tens",
            formulaDisplay = "2(1) + 1",
            stepResult = "3",
            explanation = "Multiply vertical left digits and add carry"
        )
    ),
    carries = listOf(1, 1)
)

/**
 * Validates whether the student's input matches the expected answer for the current
 * solver step or the overall lesson target answer.
 *
 * Rules:
 * 1. Blank or empty input is always rejected (fail-closed).
 * 2. On the final step, matches if userInput equals lesson.targetAnswer (exact or numeric).
 * 3. Matches if userInput equals the current step's stepResult (exact or numeric).
 * 4. Matches if userInput matches any numeric token in stepResult (e.g. "12" or "2" for "12 (write 2, carry 1)").
 */
fun validateSolverStep(
    lesson: SutraLesson,
    stepIndex: Int,
    userInput: String
): Boolean {
    val trimmedInput = userInput.trim()
    if (trimmedInput.isEmpty()) return false

    val isFinalStep = stepIndex >= lesson.steps.size - 1
    val currentStep = lesson.steps.getOrNull(stepIndex)

    // 1. Check final target answer if on the final step
    if (isFinalStep) {
        val target = lesson.targetAnswer.trim()
        if (trimmedInput.equals(target, ignoreCase = true)) return true
        val targetInt = target.toIntOrNull()
        val inputInt = trimmedInput.toIntOrNull()
        if (targetInt != null && inputInt != null && targetInt == inputInt) return true
    }

    // 2. Check current step result
    if (currentStep != null) {
        val stepRes = currentStep.stepResult.trim()
        if (trimmedInput.equals(stepRes, ignoreCase = true)) return true

        val stepInt = stepRes.toIntOrNull()
        val inputInt = trimmedInput.toIntOrNull()
        if (stepInt != null && inputInt != null && stepInt == inputInt) return true

        // 3. Check extracted numeric tokens from stepResult (e.g. "12 (write 2, carry 1)" -> ["12", "2", "1"])
        val numericTokens = Regex("[0-9]+").findAll(stepRes).map { it.value }.toList()
        if (numericTokens.contains(trimmedInput)) return true

        // Also check integer equivalence for numeric tokens (e.g. "04" vs "4")
        if (inputInt != null && numericTokens.any { it.toIntOrNull() == inputInt }) return true
    }

    return false
}

/**
 * Screen 02: SutraSolverScreen — Interactive Step-by-Step Learning Workspace
 */
@Composable
fun SutraSolverScreen(
    lesson: SutraLesson = SampleUrdhvaLesson,
    onBackClick: () -> Unit,
    onLessonComplete: () -> Unit,
    onReplayTutorial: (() -> Unit)? = null
) {
    val colors = VedicTheme.colors
    val typography = VedicTheme.typography
    val radii = VedicTheme.radii
    val scope = rememberCoroutineScope()

    var isBlueprintOpen by remember { mutableStateOf(false) }
    var activeStepIndex by remember { mutableIntStateOf(0) }
    var userInput by remember { mutableStateOf("") }
    var isCompleted by remember { mutableStateOf(false) }
    var showCompletionDialog by remember { mutableStateOf(false) }

    // Error shake animation
    val shakeOffset = remember { Animatable(0f) }

    androidx.compose.runtime.LaunchedEffect(lesson.id) {
        activeStepIndex = 0
        userInput = ""
        isCompleted = false
        showCompletionDialog = false
        isBlueprintOpen = false
    }

    val currentArrowPattern = when {
        lesson.id == "urdhva" && activeStepIndex == 0 -> YantraArrowPattern.VERTICAL_RIGHT
        lesson.id == "urdhva" && activeStepIndex == 1 -> YantraArrowPattern.CROSSWISE
        lesson.id == "urdhva" && activeStepIndex == 2 -> YantraArrowPattern.VERTICAL_LEFT
        activeStepIndex % 2 == 1 -> YantraArrowPattern.CROSSWISE
        else -> YantraArrowPattern.NONE
    }

    fun handleDigit(digit: Char) {
        if (isCompleted) return
        userInput += digit
    }

    fun handleBackspace() {
        if (userInput.isNotEmpty() && !isCompleted) {
            userInput = userInput.dropLast(1)
        }
    }

    fun handleClear() {
        if (!isCompleted) {
            userInput = ""
        }
    }

    fun handleSubmit() {
        if (isCompleted) return

        if (validateSolverStep(lesson, activeStepIndex, userInput)) {
            if (activeStepIndex + 1 < lesson.steps.size) {
                activeStepIndex++
                userInput = ""
            } else {
                isCompleted = true
                showCompletionDialog = true
            }
        } else {
            // Trigger error shake animation
            scope.launch {
                shakeOffset.animateTo(12f, tween(50))
                shakeOffset.animateTo(-12f, tween(50))
                shakeOffset.animateTo(8f, tween(50))
                shakeOffset.animateTo(-8f, tween(50))
                shakeOffset.animateTo(0f, tween(50))
            }
        }
    }

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
        // 1. Navigation Top Header & Blueprint Toggle
        Column(modifier = Modifier.fillMaxWidth()) {
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
                        brush = Brush.horizontalGradient(listOf(colors.secondaryGold, colors.primarySaffron)),
                        width = 1.dp
                    ),
                    contentPadding = ButtonDefaults.TextButtonContentPadding
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = colors.primarySaffron,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Home", color = colors.primarySaffron, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (onReplayTutorial != null) {
                        OutlinedButton(
                            onClick = onReplayTutorial,
                            shape = RoundedCornerShape(radii.button),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = if (colors.isDark) colors.secondaryGold else colors.primarySaffron
                            ),
                            contentPadding = ButtonDefaults.TextButtonContentPadding
                        ) {
                            Text(
                                text = "👑 Guru",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Collapsible Formula Blueprint Button
                    OutlinedButton(
                        onClick = { isBlueprintOpen = !isBlueprintOpen },
                        shape = RoundedCornerShape(radii.button),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (colors.isDark) colors.secondaryGold else colors.primarySaffron
                        ),
                        contentPadding = ButtonDefaults.TextButtonContentPadding
                    ) {
                        Text(
                            text = if (isBlueprintOpen) "Hide ✕" else "📜 Blueprint ▾",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // 2. Collapsible Formula Blueprint Drawer
            AnimatedVisibility(
                visible = isBlueprintOpen,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    shape = RoundedCornerShape(radii.card),
                    colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(listOf(colors.secondaryGold, colors.borderSubtle)),
                        width = 1.dp
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = lesson.shloka,
                            style = typography.h2.copy(fontSize = 15.sp),
                            color = if (colors.isDark) colors.secondaryGold else colors.primarySaffron
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "\"${lesson.literalMeaning}\"",
                            style = typography.subtitle.copy(fontSize = 12.sp),
                            color = colors.textSecondary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Shortcut Rule: ${lesson.shortcutRule}",
                            style = typography.bodySmall.copy(fontSize = 12.sp),
                            color = colors.textPrimary
                        )
                    }
                }
            }
        }

        // 3. Main Workspace: VedicSutraCard with Shake Animation
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .offset { IntOffset(shakeOffset.value.roundToInt(), 0) },
            contentAlignment = Alignment.Center
        ) {
            VedicSutraCard(
                sutraSanskritName = lesson.sanskritTitle,
                sutraEnglishName = lesson.englishTitle,
                primaryEquation = lesson.primaryEquation,
                steps = lesson.steps,
                activeStepIndex = activeStepIndex,
                isCompleted = isCompleted,
                arrowPattern = currentArrowPattern,
                carries = lesson.carries,
                userInputDisplay = userInput
            )
        }

        // 4. Pedagogical Scaffolding: Character Guidance Banner
        val currentStep = lesson.steps.getOrNull(activeStepIndex)
        val guidanceHint = when {
            isCompleted -> "👑 Mastery achieved! You solved ${lesson.primaryEquation} = ${lesson.targetAnswer}."
            currentStep != null -> "Step ${activeStepIndex + 1}: ${currentStep.explanation}"
            else -> "Apply ${lesson.shortcutRule}"
        }

        GuruGuidanceBanner(
            hintText = guidanceHint,
            isEncouragement = isCompleted,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        // 5. Input Area: Embedded MentalMathNumpad Pinned to Bottom Thumb Zone
        MentalMathNumpad(
            onDigitClick = { handleDigit(it) },
            onBackspaceClick = { handleBackspace() },
            onClearClick = { handleClear() },
            onSubmitClick = { handleSubmit() },
            enabled = !isCompleted
        )
    }

    // 6. Completion Modal Dialog
    if (showCompletionDialog) {
        Dialog(onDismissRequest = { showCompletionDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(radii.card),
                colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(listOf(colors.statusSuccess, Color(0xFF86EFAC))),
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
                    YantraProgressRing(
                        progress = 1.0f,
                        size = 72.dp,
                        strokeWidth = 4.dp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "SUTRA MASTERED! 👑",
                        style = typography.h1.copy(fontSize = 22.sp),
                        color = colors.statusSuccess
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "You successfully solved ${lesson.primaryEquation} = ${lesson.targetAnswer} with mental arithmetic!",
                        style = typography.bodySmall,
                        textAlign = TextAlign.Center,
                        color = colors.textSecondary
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            showCompletionDialog = false
                            onLessonComplete()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(radii.button),
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primarySaffron)
                    ) {
                        Text(
                            text = "CLAIM MASTERY & RETURN ➔",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
