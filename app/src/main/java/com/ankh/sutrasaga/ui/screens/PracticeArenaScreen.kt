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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankh.sutrasaga.ui.components.MathSlate
import com.ankh.sutrasaga.ui.components.NumericKeypad
import com.ankh.sutrasaga.ui.viewmodel.GameUiState

@Composable
fun PracticeArenaScreen(
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
            .background(Color(0xFF2B1F17))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Header stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PRACTICE ARENA (${state.currentProblemIndex + 1}/${state.totalProblemsInMode})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFB300)
                )
                Text(
                    text = "SCORE: ${state.score}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00E5FF)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Embedded Interactive MathSlate
            MathSlate(
                problem = problem,
                stepIndex = if (state.isAnswerSubmitted) 5 else revealedCount,
                interactiveMode = true,
                onInteractiveStepAction = { onRevealStepClick() }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // User Answer Input Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(Color(0xFF1B262C), RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = if (state.userInput.isEmpty()) "Enter final answer..." else state.userInput,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (state.userInput.isEmpty()) Color.Gray else Color(0xFFFFD54F)
                )
            }

            // Feedback Banner
            if (state.isAnswerSubmitted) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (state.isAnswerCorrect == true) Color(0xFF1B5E20) else Color(0xFFB71C1C)
                    )
                ) {
                    Text(
                        text = state.feedbackMessage,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
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
                    .height(56.dp)
            ) {
                Text(
                    text = if (state.currentProblemIndex + 1 < state.totalProblemsInMode) "NEXT PROBLEM ➔" else "GO TO BOSS BATTLE ⚔️",
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
