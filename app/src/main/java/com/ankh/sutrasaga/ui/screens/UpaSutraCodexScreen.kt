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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankh.sutrasaga.domain.models.UpaSutraCompletionState
import com.ankh.sutrasaga.domain.models.UpaSutraDefinition
import com.ankh.sutrasaga.domain.models.UpaSutraRegistry
import com.ankh.sutrasaga.ui.viewmodel.GameUiState

@Composable
fun UpaSutraCodexScreen(
    state: GameUiState,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onBackClick,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF94A3B8))
            ) {
                Text("← Back")
            }

            Text(
                text = "📜 The Upa-Sutra Codex",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700)
            )

            Spacer(modifier = Modifier.width(60.dp))
        }

        Text(
            text = "Ancient reference scrolls and verified worked examples for all 13 sub-sutras.",
            fontSize = 13.sp,
            color = Color(0xFF94A3B8),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(UpaSutraRegistry.entries) { definition ->
                val progress = state.upaSutraProgressMap[definition.id]
                val completionState = progress?.state ?: UpaSutraCompletionState.LOCKED

                CodexCard(
                    definition = definition,
                    completionState = completionState
                )
            }
        }
    }
}

@Composable
private fun CodexCard(
    definition: UpaSutraDefinition,
    completionState: UpaSutraCompletionState
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = definition.displayName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "${definition.sanskritName} • ${definition.verificationTier}",
                        fontSize = 13.sp,
                        color = Color(0xFFFFD700)
                    )
                }

                Box(
                    modifier = Modifier
                        .background(
                            when (completionState) {
                                UpaSutraCompletionState.MASTERED -> Color(0xFF065F46)
                                UpaSutraCompletionState.PRACTICED -> Color(0xFF0369A1)
                                else -> Color(0xFF334155)
                            },
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = when (completionState) {
                            UpaSutraCompletionState.MASTERED -> "MASTERED"
                            UpaSutraCompletionState.PRACTICED -> "PRACTICED"
                            else -> "REFERENCE"
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (completionState) {
                            UpaSutraCompletionState.MASTERED -> Color(0xFF6EE7B7)
                            UpaSutraCompletionState.PRACTICED -> Color(0xFF7DD3FC)
                            else -> Color(0xFF94A3B8)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Meaning: \"${definition.meaning}\"",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF38BDF8)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = definition.description,
                fontSize = 13.sp,
                color = Color(0xFFCBD5E1)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Canonical Worked Example Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0F172A), shape = RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFF334155), shape = RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = "CANONICAL WORKED EXAMPLE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700),
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = definition.workedExample,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFF34D399)
                    )
                }
            }
        }
    }
}
