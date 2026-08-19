package com.ankh.sutrasaga.ui.screens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankh.sutrasaga.domain.models.AnswerFormat
import com.ankh.sutrasaga.ui.components.NumericKeypad
import com.ankh.sutrasaga.ui.theme.BossGradientBrush
import com.ankh.sutrasaga.ui.theme.CosmicBackground
import com.ankh.sutrasaga.ui.theme.CyberCyan
import com.ankh.sutrasaga.ui.theme.CyberCyanLight
import com.ankh.sutrasaga.ui.theme.ErrorCrimson
import com.ankh.sutrasaga.ui.theme.HologramBorderBrush
import com.ankh.sutrasaga.ui.theme.SuccessEmerald
import com.ankh.sutrasaga.ui.theme.TextLightSecondary
import com.ankh.sutrasaga.ui.theme.TextMuted
import com.ankh.sutrasaga.ui.theme.TextWhitePrimary
import com.ankh.sutrasaga.ui.theme.VedicGold
import com.ankh.sutrasaga.ui.theme.VedicGoldLight
import com.ankh.sutrasaga.ui.viewmodel.GameUiState

@Composable
fun BossBattleScreen(
    state: GameUiState,
    onBackClick: () -> Unit,
    onRevealStepClick: () -> Unit,
    onDigitClick: (Char) -> Unit,
    onBackspaceClick: () -> Unit,
    onClearClick: () -> Unit,
    onRemainderClick: () -> Unit,
    onOrderedPairClick: () -> Unit,
    onSubmitClick: () -> Unit,
    onNextProblemClick: () -> Unit
) {
    val problem = state.currentProblem ?: return
    val revealedCount = state.revealedStepsCount

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CosmicBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Boss Battle Header Banner with Fire Crimson Gradient
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xF21E1B4B)),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = BossGradientBrush,
                    width = 1.5.dp
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onBackClick,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A)),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = Brush.horizontalGradient(listOf(Color(0xFFEF4444), Color(0xFFF97316))),
                            width = 1.dp
                        ),
                        contentPadding = ButtonDefaults.TextButtonContentPadding
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFFFCA5A5),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Worlds", color = Color(0xFFFCA5A5), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "⚔️ BOSS BATTLE ⚔️",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp,
                            color = Color(0xFFF87171)
                        )
                        Text(
                            text = "Challenge ${state.currentProblemIndex + 1} of ${state.totalProblemsInMode} | Score: ${state.score}",
                            fontSize = 12.sp,
                            color = TextLightSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Boss Question Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xF20F172A)),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = HologramBorderBrush,
                    width = 1.5.dp
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = problem.sutraName.uppercase(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.5.sp,
                        color = CyberCyanLight
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = problem.questionText,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace,
                        color = VedicGoldLight
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Intermediate Decomposition Steps Hint Area
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xD91E293B)),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(listOf(Color(0x33FFB300), Color(0x3338BDF8))),
                    width = 1.dp
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "💡 Decomposition Hints",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = VedicGoldLight
                        )
                        if (revealedCount < problem.decompositionSteps.size && !state.isAnswerSubmitted) {
                            Button(
                                onClick = onRevealStepClick,
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)),
                                contentPadding = ButtonDefaults.TextButtonContentPadding
                            ) {
                                Text("Reveal Hint", fontSize = 11.sp, color = CyberCyanLight, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    if (revealedCount > 0) {
                        Spacer(modifier = Modifier.height(4.dp))
                        for (i in 0 until revealedCount) {
                            val step = problem.decompositionSteps[i]
                            Text(
                                text = "• Step ${step.stepNumber} (${step.label}): ${step.formulaDisplay} = ${step.stepResult}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextLightSecondary,
                                modifier = Modifier.padding(vertical = 1.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // User Answer Input Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF0F172A))
                    .border(
                        width = 1.5.dp,
                        brush = HologramBorderBrush,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = if (state.userInput.isEmpty()) {
                        when (problem.answerFormat) {
                            AnswerFormat.QUOTIENT_AND_REMAINDER -> "Enter quotient R remainder..."
                            AnswerFormat.ORDERED_PAIR -> "Enter x, y..."
                            AnswerFormat.INTEGER -> "Enter final answer..."
                        }
                    } else state.userInput,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace,
                    color = if (state.userInput.isEmpty()) TextMuted else VedicGoldLight
                )
            }

            // Feedback Banner
            if (state.isAnswerSubmitted) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (state.isAnswerCorrect == true) Color(0xF0064E3B) else Color(0xF07F1D1D)
                    ),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(
                            if (state.isAnswerCorrect == true) listOf(SuccessEmerald, Color(0xFF34D399)) else listOf(ErrorCrimson, Color(0xFFF87171))
                        ),
                        width = 1.dp
                    )
                ) {
                    Text(
                        text = state.feedbackMessage,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhitePrimary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    )
                }
            }
        }

        // Bottom Controls: Keypad or Next Button
        if (state.isAnswerSubmitted) {
            Button(
                onClick = onNextProblemClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VedicGold),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Text(
                    text = if (state.currentProblemIndex + 1 < state.totalProblemsInMode) "NEXT CHALLENGE ➔" else "CLAIM VICTORY 🏆",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0F172A)
                )
            }
        } else {
            NumericKeypad(
                onDigitClick = onDigitClick,
                onBackspaceClick = onBackspaceClick,
                onClearClick = onClearClick,
                onSecondaryValueClick = when (problem.answerFormat) {
                    AnswerFormat.QUOTIENT_AND_REMAINDER -> onRemainderClick
                    AnswerFormat.ORDERED_PAIR -> onOrderedPairClick
                    AnswerFormat.INTEGER -> null
                },
                secondaryValueLabel = when (problem.answerFormat) {
                    AnswerFormat.QUOTIENT_AND_REMAINDER -> "ADD REMAINDER (R)"
                    AnswerFormat.ORDERED_PAIR -> "ADD Y VALUE (,)"
                    AnswerFormat.INTEGER -> ""
                },
                onSubmitClick = onSubmitClick,
                enabled = !state.isAnswerSubmitted
            )
        }
    }
}
