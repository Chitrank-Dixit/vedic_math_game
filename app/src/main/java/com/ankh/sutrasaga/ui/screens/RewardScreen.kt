package com.ankh.sutrasaga.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
        badgeLabel = "[PLACEHOLDER BADGE: Master of Fives]"
    )
    2 -> WorldCompletionContent(
        title = "WORLD 2 COMPLETE!",
        masteryMessage = "You have mastered Nikhilam Navatashcaramam Dashatah!",
        badgeLabel = "[PLACEHOLDER BADGE: Master of Complements]"
    )
    3 -> WorldCompletionContent(
        title = "WORLD 3 COMPLETE!",
        masteryMessage = "You have mastered Ekanyunena Purvena!",
        badgeLabel = "[PLACEHOLDER BADGE: Master of Nines]"
    )
    else -> WorldCompletionContent(
        title = "WORLD $worldId COMPLETE!",
        masteryMessage = "You have completed this world!",
        badgeLabel = "[PLACEHOLDER BADGE: World $worldId Master]"
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
            .background(Color(0xFFE8EAF6))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = completionContent.title,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = completionContent.masteryMessage,
                fontSize = 15.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Placeholder Badge Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color(0xFFFFD54F), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "🏆",
                        fontSize = 54.sp
                    )
                    Text(
                        text = completionContent.badgeLabel,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5D4037)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "FINAL SCORE",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                    Text(
                        text = "${state.score} PTS",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Progress saved to Room DB ✓",
                        fontSize = 12.sp,
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Button(
            onClick = onReturnHomeClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(text = "RETURN TO WORLD SELECT", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
