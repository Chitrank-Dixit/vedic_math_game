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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankh.sutrasaga.domain.models.InteractiveHandshake
import com.ankh.sutrasaga.ui.theme.VedicParchmentTokens

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
    val targetNum = expectedAnswer?.toIntOrNull()
    val options = remember(expectedAnswer) {
        if (targetNum != null) {
            val start = (targetNum - 2).coerceAtLeast(1)
            (start..(start + 4)).map { it.toString() }
        } else {
            listOf("1", "2", "3", "4", "5")
        }
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
                            .clickable(enabled = isAnswerCorrect != true) {
                                selectedOption = option
                                val success = onAnswerSelected(option)
                                isAnswerCorrect = success
                                if (success) {
                                    feedbackMessage = "✦ Excellent! You found it! 👏 ✦\nGreat job, my child! You understood the pattern beautifully."
                                } else {
                                    feedbackMessage = "Almost! Give it another try."
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
