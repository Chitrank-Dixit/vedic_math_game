package com.ankh.sutrasaga.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import com.ankh.sutrasaga.ui.theme.CosmicBackground
import com.ankh.sutrasaga.ui.theme.CyberCyan
import com.ankh.sutrasaga.ui.theme.CyberCyanLight
import com.ankh.sutrasaga.ui.theme.GoldGradientBrush
import com.ankh.sutrasaga.ui.theme.HologramBorderBrush
import com.ankh.sutrasaga.ui.theme.SuccessEmerald
import com.ankh.sutrasaga.ui.theme.TextLightSecondary
import com.ankh.sutrasaga.ui.theme.TextMuted
import com.ankh.sutrasaga.ui.theme.TextWhitePrimary
import com.ankh.sutrasaga.ui.theme.VedicGold
import com.ankh.sutrasaga.ui.theme.VedicGoldLight
import com.ankh.sutrasaga.ui.viewmodel.GameUiState

internal data class WorldCompletionContent(
    val title: String,
    val masteryMessage: String,
    val badgeLabel: String
)

internal fun worldCompletionContent(worldId: Int): WorldCompletionContent = when (worldId) {
    1 -> WorldCompletionContent(
        title = "WORLD 1 COMPLETE!",
        masteryMessage = "You have mastered Ekadhikena Purvena!",
        badgeLabel = "Master of Fives"
    )
    2 -> WorldCompletionContent(
        title = "WORLD 2 COMPLETE!",
        masteryMessage = "You have mastered Nikhilam Navatashcaramam Dashatah!",
        badgeLabel = "Master of Complements"
    )
    3 -> WorldCompletionContent(
        title = "WORLD 3 COMPLETE!",
        masteryMessage = "You have mastered Ekanyunena Purvena!",
        badgeLabel = "Master of Nines"
    )
    else -> WorldCompletionContent(
        title = "WORLD $worldId COMPLETE!",
        masteryMessage = "You have completed this ancient world!",
        badgeLabel = "World $worldId Master"
    )
}

@Composable
fun RewardScreen(
    state: GameUiState,
    onReturnHomeClick: () -> Unit
) {
    val completionContent = worldCompletionContent(state.selectedWorldId)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CosmicBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = completionContent.title,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp,
                color = VedicGoldLight
            )

            Text(
                text = completionContent.masteryMessage,
                fontSize = 15.sp,
                color = TextLightSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 6.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Luminous Victory Badge Box
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xF21E293B)),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = HologramBorderBrush,
                    width = 2.dp
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "🏆",
                        fontSize = 52.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = completionContent.badgeLabel,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp,
                        color = VedicGoldLight
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Score Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xF20F172A)),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(listOf(Color(0x4038BDF8), Color(0x20FFB300))),
                    width = 1.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "FINAL SCORE",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.5.sp,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${state.score} PTS",
                        fontSize = 38.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace,
                        color = CyberCyanLight
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Progress saved to Room DB ✓",
                        fontSize = 12.sp,
                        color = SuccessEmerald,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Button(
            onClick = onReturnHomeClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VedicGold),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
        ) {
            Text(
                text = "RETURN TO WORLD SELECT",
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF0F172A)
            )
        }
    }
}
