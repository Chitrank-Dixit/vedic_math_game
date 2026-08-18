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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankh.sutrasaga.domain.models.UpaSutraCompletionState
import com.ankh.sutrasaga.domain.models.UpaSutraDefinition
import com.ankh.sutrasaga.domain.models.UpaSutraId
import com.ankh.sutrasaga.domain.models.UpaSutraProgress
import com.ankh.sutrasaga.domain.models.UpaSutraRegistry
import com.ankh.sutrasaga.ui.viewmodel.GameUiState

@Composable
fun UpaSutraTreasuryScreen(
    state: GameUiState,
    onQuestClick: (UpaSutraId) -> Unit,
    onCodexClick: () -> Unit,
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
                Text("← World Select")
            }

            Text(
                text = "🏛️ Upa-Sutra Treasury",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700)
            )

            Button(
                onClick = onCodexClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5))
            ) {
                Text("📜 Codex", color = Color.White)
            }
        }

        Text(
            text = "Master the 13 sacred sub-sutras to unlock ancient computational secrets.",
            fontSize = 14.sp,
            color = Color(0xFF94A3B8),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(UpaSutraRegistry.entries) { definition ->
                val progress = state.upaSutraProgressMap[definition.id]
                val isPrerequisiteMet = isWorldCompleted(state, definition.unlockAfterWorldId)
                val effectiveState = when {
                    progress?.state == UpaSutraCompletionState.MASTERED -> UpaSutraCompletionState.MASTERED
                    progress?.state == UpaSutraCompletionState.PRACTICED -> UpaSutraCompletionState.PRACTICED
                    progress?.state == UpaSutraCompletionState.LEARNING -> UpaSutraCompletionState.LEARNING
                    isPrerequisiteMet -> UpaSutraCompletionState.AVAILABLE
                    else -> UpaSutraCompletionState.LOCKED
                }

                UpaSutraCard(
                    definition = definition,
                    completionState = effectiveState,
                    onClick = { onQuestClick(definition.id) }
                )
            }
        }
    }
}

private fun isWorldCompleted(state: GameUiState, worldId: Int): Boolean {
    return when (worldId) {
        1 -> state.isWorld1Completed
        2 -> state.isWorld2Completed
        3 -> state.isWorld3Completed
        4 -> state.isWorld4Completed
        5 -> state.isWorld5Completed
        6 -> state.isWorld6Completed
        7 -> state.isWorld7Completed
        8 -> state.isWorld8Completed
        9 -> state.isWorld9Completed
        10 -> state.isWorld10Completed
        11 -> state.isWorld11Completed
        12 -> state.isWorld12Completed
        13 -> state.isWorld13Completed
        14 -> state.isWorld14Completed
        15 -> state.isWorld15Completed
        16 -> state.isWorld16Completed
        else -> false
    }
}

@Composable
private fun UpaSutraCard(
    definition: UpaSutraDefinition,
    completionState: UpaSutraCompletionState,
    onClick: () -> Unit
) {
    val isUnlocked = completionState != UpaSutraCompletionState.LOCKED

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (completionState) {
                UpaSutraCompletionState.MASTERED -> Color(0xFF1E293B)
                UpaSutraCompletionState.PRACTICED -> Color(0xFF1E293B)
                UpaSutraCompletionState.AVAILABLE -> Color(0xFF1E293B)
                UpaSutraCompletionState.LEARNING -> Color(0xFF1E293B)
                UpaSutraCompletionState.LOCKED -> Color(0xFF161E2E)
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isUnlocked) 4.dp else 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = definition.displayName,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isUnlocked) Color.White else Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = definition.sanskritName,
                        fontSize = 13.sp,
                        color = if (isUnlocked) Color(0xFFFFD700) else Color(0xFF475569)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "\"${definition.meaning}\"",
                    fontSize = 13.sp,
                    color = if (isUnlocked) Color(0xFF94A3B8) else Color(0xFF475569)
                )

                Spacer(modifier = Modifier.height(6.dp))

                when (completionState) {
                    UpaSutraCompletionState.MASTERED -> {
                        Text(
                            text = "👑 MASTERED | Badge Earned",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF10B981)
                        )
                    }
                    UpaSutraCompletionState.PRACTICED -> {
                        Text(
                            text = "✓ PRACTICED | Ready for Challenge",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF38BDF8)
                        )
                    }
                    UpaSutraCompletionState.AVAILABLE, UpaSutraCompletionState.LEARNING -> {
                        Text(
                            text = "✦ AVAILABLE (Attached to World ${definition.parentWorldId})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFBBF24)
                        )
                    }
                    UpaSutraCompletionState.LOCKED -> {
                        Text(
                            text = "🔒 Unlocks after World ${definition.unlockAfterWorldId}",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            if (isUnlocked) {
                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when (completionState) {
                            UpaSutraCompletionState.MASTERED -> Color(0xFF059669)
                            UpaSutraCompletionState.PRACTICED -> Color(0xFF0284C7)
                            else -> Color(0xFF4F46E5)
                        }
                    )
                ) {
                    Text(
                        text = when (completionState) {
                            UpaSutraCompletionState.MASTERED -> "Replay"
                            UpaSutraCompletionState.PRACTICED -> "Challenge"
                            else -> "Start Quest"
                        },
                        fontSize = 13.sp,
                        color = Color.White
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .background(Color(0xFF334155), RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text("LOCKED", color = Color(0xFF94A3B8), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
