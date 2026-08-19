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
import com.ankh.sutrasaga.domain.models.UpaSutraCompletionState
import com.ankh.sutrasaga.domain.models.UpaSutraDefinition
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
fun UpaSutraCodexScreen(
    state: GameUiState,
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
        // Top App Bar Navigation Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.Start,
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
                Text("Treasury", color = VedicGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
                text = "📜 The Upa-Sutra Codex",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp,
                color = VedicGoldLight
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Ancient reference scrolls and verified worked examples for all 13 sub-sutras.",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = TextLightSecondary,
                textAlign = TextAlign.Center
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
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
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xF21E293B)),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                when (completionState) {
                    UpaSutraCompletionState.MASTERED -> listOf(SuccessEmerald, Color(0xFF34D399))
                    UpaSutraCompletionState.PRACTICED -> listOf(CyberCyan, CyberCyanLight)
                    else -> listOf(Color(0x4038BDF8), Color(0x20FFB300))
                }
            ),
            width = 1.dp
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextWhitePrimary
                    )
                    Text(
                        text = "${definition.sanskritName} • ${definition.verificationTier}",
                        fontSize = 12.sp,
                        color = VedicGoldLight
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            when (completionState) {
                                UpaSutraCompletionState.MASTERED -> Color(0x3310B981)
                                UpaSutraCompletionState.PRACTICED -> Color(0x3300E5FF)
                                else -> Color(0x3364748B)
                            }
                        )
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = when (completionState) {
                            UpaSutraCompletionState.MASTERED -> "👑 MASTERED"
                            UpaSutraCompletionState.PRACTICED -> "✓ PRACTICED"
                            else -> "REFERENCE"
                        },
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp,
                        color = when (completionState) {
                            UpaSutraCompletionState.MASTERED -> SuccessEmerald
                            UpaSutraCompletionState.PRACTICED -> CyberCyanLight
                            else -> TextMuted
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Meaning: \"${definition.meaning}\"",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = CyberCyanLight
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = definition.description,
                fontSize = 12.sp,
                color = TextLightSecondary,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Canonical Worked Example Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xD90B1120))
                    .border(1.dp, Color(0x3338BDF8), RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = "CANONICAL WORKED EXAMPLE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = VedicGoldLight,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = definition.workedExample,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = SuccessEmerald
                    )
                }
            }
        }
    }
}
