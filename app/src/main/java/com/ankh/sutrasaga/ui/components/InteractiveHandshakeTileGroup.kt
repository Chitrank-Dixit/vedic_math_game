package com.ankh.sutrasaga.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankh.sutrasaga.domain.models.InteractiveHandshake
import com.ankh.sutrasaga.ui.theme.VedicParchmentTokens

/**
 * Generates dynamic selectable choices around the expected answer, ensuring
 * that the target answer (including zero and negative values) is always included.
 */
fun generateHandshakeChoices(expectedAnswer: String?): List<String> {
    val targetNum = expectedAnswer?.toIntOrNull()
    return if (targetNum != null) {
        val start = targetNum - 2
        (start..(start + 4)).map { it.toString() }
    } else if (!expectedAnswer.isNullOrBlank()) {
        listOf(expectedAnswer)
    } else {
        listOf("0", "1", "2", "3", "4")
    }
}

/**
 * Interactive Handshake ("Let's try it!") Mini-Game component.
 * Allows the student to select from dynamic number tiles, providing immediate visual feedback
 * and allowing retries without restarting the lesson.
 */
@Composable
fun InteractiveHandshakeTileGroup(
    handshake: InteractiveHandshake,
    expectedAnswer: String?,
    onAnswerSelected: (String) -> Boolean,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var isAnswerCorrect by remember { mutableStateOf<Boolean?>(null) }
    var feedbackMessage by remember { mutableStateOf<String?>(null) }

    // Generate dynamic selectable choices around the expected answer
    val options = remember(expectedAnswer) {
        generateHandshakeChoices(expectedAnswer)
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF7EEDB)
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFD4C3A3)),
            width = 1.5.dp
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Card Title Tag
            Text(
                text = "🌿  Let's try it!  🌿",
                color = VedicParchmentTokens.ForestGreenDark,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Subtitle Prompt
            Text(
                text = handshake.promptText ?: "Tap the correct missing number:",
                color = VedicParchmentTokens.InkDark,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Choice Tiles Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                options.forEach { option ->
                    val isSelected = selectedOption == option
                    val isThisOptionCorrect = isSelected && isAnswerCorrect == true
                    val isThisOptionWrong = isSelected && isAnswerCorrect == false

                    val tileBg by animateColorAsState(
                        targetValue = when {
                            isThisOptionCorrect -> VedicParchmentTokens.TileCorrectBg
                            isThisOptionWrong -> VedicParchmentTokens.TileIncorrectBg
                            else -> VedicParchmentTokens.TileDefaultBg
                        },
                        animationSpec = tween(250, easing = FastOutSlowInEasing),
                        label = "TileBg_$option"
                    )

                    val tileBorder by animateColorAsState(
                        targetValue = when {
                            isThisOptionCorrect -> VedicParchmentTokens.TileCorrectBorder
                            isThisOptionWrong -> VedicParchmentTokens.TileIncorrectBorder
                            isSelected -> VedicParchmentTokens.TileSelectedBorder
                            else -> VedicParchmentTokens.TileDefaultBorder
                        },
                        animationSpec = tween(250, easing = FastOutSlowInEasing),
                        label = "TileBorder_$option"
                    )

                    val tileTextColor by animateColorAsState(
                        targetValue = when {
                            isThisOptionCorrect -> VedicParchmentTokens.TileCorrectText
                            isThisOptionWrong -> VedicParchmentTokens.TileIncorrectText
                            else -> VedicParchmentTokens.TileDefaultText
                        },
                        label = "TileTextColor_$option"
                    )

                    Box(
                        modifier = Modifier
                            .size(width = 52.dp, height = 48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(tileBg)
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = tileBorder,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .semantics {
                                role = Role.Button
                                contentDescription = "Option $option"
                            }
                            .clickable(
                                enabled = isAnswerCorrect != true,
                                role = Role.Button,
                                onClickLabel = "Select option $option"
                            ) {
                                selectedOption = option
                                val success = onAnswerSelected(option)
                                isAnswerCorrect = success
                                if (success) {
                                    feedbackMessage = when (expectedAnswer) {
                                        "20" -> "✦ Splendid, Shishya! 👏 ✦\n4 × 5 = 20. Append 25 to get 45² = 2025! You are ready for the arena."
                                        "3" -> "✦ Splendid, Shishya! 👏 ✦\n10 − 7 = 3. Together with 5 and 7, 1000 − 427 = 573! Zero borrowing conquered!"
                                        "52" -> "✦ Splendid, Shishya! 👏 ✦\n53 − 1 = 52 for the left part! Coupled with 47 (99 − 52), 53 × 99 = 5247! Lightning fast!"
                                        "94" -> "✦ Splendid, Shishya! 👏 ✦\n97 − 3 = 94 for the left part! Coupled with 3² = 09, 97² = 9409! Flawless precision!"
                                        "5" -> "✦ Splendid, Shishya! 👏 ✦\n(3 × 1) + (1 × 2) = 5 for the middle digit! Combined: 6 | 5 | 1 = 651! Universal multiplication unlocked!"
                                        "2" -> "✦ Splendid, Shishya! 👏 ✦\n4 + (−2) = 2 for the second quotient digit! Combined with remainder 4 + (−4) = 0, 144 ÷ 12 = 12! Synthetic division mastered!"
                                        "4" -> "✦ Splendid, Shishya! 👏 ✦\nSince x-ratio matches the constants (1/2), y = 0! Then 2x = 8 gives x = 4. Simultaneous equations solved in seconds!"
                                        "1" -> "✦ Splendid, Shishya! 👏 ✦\n22 ÷ 22 = 1, so x + y = 1! Combined with x − y = 3, adding gives 2x = 4 (x = 2, y = −1)! Simultaneous equations conquered!"
                                        "-1" -> "✦ Splendid, Shishya! 👏 ✦\nx + 1 = 0 yields x = −1! Since 7 ≠ 8, the shared factor vanishes to zero. Shunyam Samyasamuccaye mastered!"
                                        "9" -> "✦ Splendid, Shishya! 👏 ✦\n(6 ÷ 2)² = 3² = 9! Adding 9 to both sides gives (x + 3)² = 1 (roots −2 and −4)! Pūraṇāpūraṇābhyām mastered!"
                                        "60" -> "✦ Splendid, Shishya! 👏 ✦\n(58 + 62) ÷ 2 = 60! With deviation d = 2, 58 × 62 = 60² − 2² = 3600 − 4 = 3596! Vyaṣṭisamaṣṭiḥ mastered!"
                                        "6" -> "✦ Splendid, Shishya! 👏 ✦\n20 = (2 × 7) + 6, so the next remainder is 6! Tracking remainders reveals 0.(142857) with cycle length 6! Śeṣāṇyaṅkena Carameṇa mastered!"
                                        "11" -> "✦ Splendid, Shishya! 👏 ✦\n3 + (2 × 4) = 11! Resolving carry with 6 gives 716, and leading 1 gives 143 × 12 = 1716! Sopāntyadvayamantyam mastered!"
                                        "12" -> "✦ Splendid, Shishya! 👏 ✦\n4 × 3 = 12! Both the factor sums (4 × 3) and polynomial sum (1 + 5 + 6) equal 12! Guṇitasamuccayaḥ mastered!"
                                        "-2" -> "✦ Splendid, Shishya! 👏 ✦\nx + 2 = 0 yields x = −2! Together with x + 5 = 0 (x = −5), the roots are −2 and −5! Guṇakasamuccayaḥ mastered!"
                                        "7" -> "✦ Splendid, Shishya! 👏 ✦\n2x = 14 yields x = 7! Combined with the minus branch 2x = 2 (x = 1), the roots are 7 and 1! Calana-Kalanābhyām mastered!"
                                        else -> "✦ Excellent! You found it! 👏 ✦\nGreat job, my child! You understood the pattern beautifully."
                                    }
                                } else {
                                    feedbackMessage = when (expectedAnswer) {
                                        "20" -> "Almost! Remember: multiply the preceding part 4 by one more than itself (4 × 5). Try again!"
                                        "3" -> "Almost! Remember: subtract the final units digit from 10 (10 − 7). Try again!"
                                        "52" -> "Almost! Remember: apply Ekanyunena by subtracting 1 from 53 (53 − 1). Try again!"
                                        "94" -> "Almost! Remember: lessen the number by its deficiency from 100 (97 − 3). Try again!"
                                        "5" -> "Almost! Cross-multiply and sum: (3 × 1) + (1 × 2). Try again!"
                                        "2" -> "Almost! Multiply the previous quotient digit by −2 and add: 4 + (1 × −2). Try again!"
                                        "4" -> "Almost! With y = 0, solve 2x = 8 by dividing 8 by 2. Try again!"
                                        "1" -> "Almost! Divide 22 by 22 to find the sum (x + y). Try again!"
                                        "-1" -> "Almost! Solve x + 1 = 0 by subtracting 1 from both sides. Try again!"
                                        "9" -> "Almost! Take half the coefficient of x (6 ÷ 2 = 3) and square it (3²). Try again!"
                                        "60" -> "Almost! Find the central midpoint by calculating (58 + 62) ÷ 2. Try again!"
                                        "6" -> "Almost! Divide 20 by 7: 20 = (2 × 7) + remainder. Find the remainder. Try again!"
                                        "11" -> "Almost! Multiply the previous neighbor 4 by 2 (2 × 4 = 8) and add 3 (3 + 8). Try again!"
                                        "12" -> "Almost! Multiply the sum of the first factor (1 + 3 = 4) by the second factor (1 + 2 = 3): 4 × 3. Try again!"
                                        "-2" -> "Almost! Subtract 2 from both sides of x + 2 = 0 to isolate x. Try again!"
                                        "7" -> "Almost! Divide 14 by 2 in 2x = 14 to isolate root x. Try again!"
                                        else -> "Almost! Give it another try."
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = option,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = tileTextColor
                        )
                    }
                }
            }

            // Success / Feedback Banner & Continue Button
            AnimatedVisibility(
                visible = isAnswerCorrect != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    feedbackMessage?.let { msg ->
                        Text(
                            text = msg,
                            color = if (isAnswerCorrect == true) VedicParchmentTokens.ForestGreenDark else VedicParchmentTokens.TileIncorrectText,
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }

                    if (isAnswerCorrect == true) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = onContinueClick,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = VedicParchmentTokens.ForestGreenPrimary
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .height(40.dp)
                        ) {
                            Text(
                                text = "Continue →",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
