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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankh.sutrasaga.domain.models.UpaSutraCompletionState
import com.ankh.sutrasaga.domain.models.UpaSutraDefinition
import com.ankh.sutrasaga.domain.models.UpaSutraId
import com.ankh.sutrasaga.domain.models.UpaSutraProgress
import com.ankh.sutrasaga.domain.models.UpaSutraRegistry
import com.ankh.sutrasaga.ui.theme.CosmicBackground
import com.ankh.sutrasaga.ui.theme.CyberCyan
import com.ankh.sutrasaga.ui.theme.CyberCyanLight
import com.ankh.sutrasaga.ui.theme.HologramBorderBrush
import com.ankh.sutrasaga.ui.theme.SuccessEmerald
import com.ankh.sutrasaga.ui.theme.TextLightSecondary
import com.ankh.sutrasaga.ui.theme.TextMuted
import com.ankh.sutrasaga.ui.theme.TextWhitePrimary
import com.ankh.sutrasaga.ui.theme.VedicGold
import com.ankh.sutrasaga.ui.theme.VedicGoldLight
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
            .background(CosmicBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Navigation Action Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onBackClick,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = Brush.horizontalGradient(listOf(VedicGold, VedicGoldLight)),
                    width = 1.dp
                ),
                contentPadding = ButtonDefaults.TextButtonContentPadding
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = VedicGold,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Worlds", color = VedicGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = onCodexClick,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4338CA)),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = Brush.horizontalGradient(listOf(CyberCyan, CyberCyanLight)),
                    width = 1.dp
                ),
                contentPadding = ButtonDefaults.TextButtonContentPadding
            ) {
                Text("📜 Codex ➔", color = TextWhitePrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Title and Subtitle Banner
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🏛️ Upa-Sutra Treasury",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp,
                color = VedicGoldLight
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Master the 13 sacred sub-sutras to unlock ancient computational secrets.",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = TextLightSecondary,
                textAlign = TextAlign.Center
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
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
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) Color(0xF01E293B) else Color(0x800F172A)
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = when (completionState) {
                UpaSutraCompletionState.MASTERED -> Brush.horizontalGradient(listOf(SuccessEmerald, Color(0xFF34D399)))
                UpaSutraCompletionState.PRACTICED -> Brush.horizontalGradient(listOf(CyberCyan, CyberCyanLight))
                UpaSutraCompletionState.AVAILABLE, UpaSutraCompletionState.LEARNING -> Brush.horizontalGradient(listOf(Color(0x66FFB300), Color(0x3338BDF8)))
                UpaSutraCompletionState.LOCKED -> Brush.horizontalGradient(listOf(Color(0x20FFFFFF), Color(0x10FFFFFF)))
            },
            width = 1.dp
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isUnlocked) 4.dp else 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = definition.displayName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isUnlocked) TextWhitePrimary else TextMuted
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = definition.sanskritName,
                        fontSize = 12.sp,
                        color = if (isUnlocked) VedicGoldLight else TextMuted
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "\"${definition.meaning}\"",
                    fontSize = 12.sp,
                    color = TextLightSecondary
                )

                Spacer(modifier = Modifier.height(6.dp))

                when (completionState) {
                    UpaSutraCompletionState.MASTERED -> {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0x3310B981))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "👑 MASTERED",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = SuccessEmerald,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }
                    UpaSutraCompletionState.PRACTICED -> {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0x3300E5FF))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "✓ PRACTICED",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = CyberCyanLight,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }
                    UpaSutraCompletionState.AVAILABLE, UpaSutraCompletionState.LEARNING -> {
                        Text(
                            text = "✦ Unlocked from World ${definition.parentWorldId}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = VedicGoldLight
                        )
                    }
                    UpaSutraCompletionState.LOCKED -> {
                        Text(
                            text = "🔒 Unlocks after World ${definition.unlockAfterWorldId}",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            if (isUnlocked) {
                Button(
                    onClick = onClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when (completionState) {
                            UpaSutraCompletionState.MASTERED -> Color(0xFF065F46)
                            UpaSutraCompletionState.PRACTICED -> Color(0xFF0369A1)
                            else -> Color(0xFF4338CA)
                        }
                    ),
                    contentPadding = ButtonDefaults.TextButtonContentPadding
                ) {
                    Text(
                        text = when (completionState) {
                            UpaSutraCompletionState.MASTERED -> "Replay"
                            UpaSutraCompletionState.PRACTICED -> "Challenge"
                            else -> "Quest"
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhitePrimary
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .background(Color(0xFF334155), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text("LOCKED", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
