package com.ankh.sutrasaga.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankh.sutrasaga.ui.components.NumericKeypad
import com.ankh.sutrasaga.ui.viewmodel.GameUiState

@Composable
fun BossBattleScreen(
    state: GameUiState,
    onRevealStepClick: () -> Unit,
    onDigitClick: (Char) -> Unit,
    onBackspaceClick: () -> Unit,
    onClearClick: () -> Unit,
    onSubmitClick: () -> Unit,
    onNextProblemClick: () -> Unit
) {
    val problem = state.currentProblem ?: return
    val revealedCount = state.revealedStepsCount

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBE9E7))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Boss Battle Header Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFD84315)),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "⚔️ BOSS BATTLE: GUARDIAN OF FIVES ⚔️",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Challenge ${state.currentProblemIndex + 1} of ${state.totalProblemsInMode} (Untimed Mode) | Score: ${state.score}",
                        fontSize = 13.sp,
                        color = Color(0xFFFFCCBC)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Boss Question Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "3-DIGIT SQUARING CHALLENGE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                    Text(
                        text = "${problem.operand}² = ?",
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFBF360C)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Intermediate Decomposition Steps Hint Area
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE0B2)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Decomposition Steps",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        if (revealedCount < problem.decompositionSteps.size && !state.isAnswerSubmitted) {
                            OutlinedButton(onClick = onRevealStepClick) {
                                Text("💡 Reveal Hint", fontSize = 12.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    if (revealedCount > 0) {
                        for (i in 0 until revealedCount) {
                            val step = problem.decompositionSteps[i]
                            Text(
                                text = "• Step ${step.stepNumber} (${step.label}): ${step.formulaDisplay} = ${step.stepResult}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFD84315),
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // User Answer Input Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = if (state.userInput.isEmpty()) "Enter answer..." else state.userInput,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (state.userInput.isEmpty()) Color.LightGray else Color.Black
                )
            }

            // Feedback Banner
            if (state.isAnswerSubmitted) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (state.isAnswerCorrect == true) Color(0xFFC8E6C9) else Color(0xFFFFCDD2)
                    )
                ) {
                    Text(
                        text = state.feedbackMessage,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    )
                }
            }
        }

        // Bottom Controls
        if (state.isAnswerSubmitted) {
            Button(
                onClick = onNextProblemClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = if (state.currentProblemIndex + 1 < state.totalProblemsInMode) "NEXT BOSS STAGE ➔" else "CLAIM VICTORY 🏆",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            NumericKeypad(
                onDigitClick = onDigitClick,
                onBackspaceClick = onBackspaceClick,
                onClearClick = onClearClick,
                onSubmitClick = onSubmitClick,
                enabled = !state.isAnswerSubmitted
            )
        }
    }
}
