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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankh.sutrasaga.ui.viewmodel.GameUiState

@Composable
fun WorldSelectScreen(
    state: GameUiState,
    onWorldClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4F8))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Ankh: The Sutra Saga",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        Text(
            text = "Select a World to Begin",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                WorldCard(
                    title = "World 1: Ekadhikena Purvena",
                    subtitle = "Squaring numbers ending in 5",
                    isUnlocked = true,
                    isCompleted = state.isWorld1Completed,
                    bestScore = state.world1BestScore,
                    onClick = { onWorldClick(1) }
                )
            }
            item {
                WorldCard(
                    title = "World 2: Nikhilam Navatashcaramam",
                    subtitle = "All from 9 and last from 10 (Base Subtraction)",
                    isUnlocked = true,
                    isCompleted = state.isWorld2Completed,
                    bestScore = state.world2BestScore,
                    onClick = { onWorldClick(2) }
                )
            }
            item {
                WorldCard(
                    title = "World 3: Ekanyunena Purvena",
                    subtitle = "Multiplication by 9s",
                    isUnlocked = true,
                    isCompleted = state.isWorld3Completed,
                    bestScore = state.world3BestScore,
                    onClick = { onWorldClick(3) }
                )
            }
            item {
                WorldCard(
                    title = "World 4: Yavadunam",
                    subtitle = "Squaring near a base (Deficiency Squaring)",
                    isUnlocked = true,
                    isCompleted = state.isWorld4Completed,
                    bestScore = state.world4BestScore,
                    onClick = { onWorldClick(4) }
                )
            }
            item {
                WorldCard(
                    title = "World 5: Urdhva-Tiryagbhyam",
                    subtitle = "Vertically and Crosswise (MVP Finale)",
                    isUnlocked = true,
                    isCompleted = state.isWorld5Completed,
                    bestScore = state.world5BestScore,
                    onClick = { onWorldClick(5) }
                )
            }
            item {
                WorldCard(
                    title = "World 6: Paravartya Yojayet",
                    subtitle = "Transpose and Apply Division (Post-MVP)",
                    isUnlocked = true,
                    isCompleted = state.isWorld6Completed,
                    bestScore = state.world6BestScore,
                    onClick = { onWorldClick(6) }
                )
            }
            item {
                WorldCard(
                    title = "World 7: Anurupye Shunyamanyat",
                    subtitle = "Simultaneous Linear Equations (Post-MVP)",
                    isUnlocked = true,
                    isCompleted = state.isWorld7Completed,
                    bestScore = state.world7BestScore,
                    onClick = { onWorldClick(7) }
                )
            }
            item {
                WorldCard(
                    title = "World 8: Sankalana-Vyavakalanabhyam",
                    subtitle = "Swapped Simultaneous Equations (Post-MVP)",
                    isUnlocked = true,
                    isCompleted = state.isWorld8Completed,
                    bestScore = state.world8BestScore,
                    onClick = { onWorldClick(8) }
                )
            }
            item {
                WorldCard(
                    title = "World 9: Shunyam Samyasamuccaye",
                    subtitle = "Common Sum Factor Equations (Post-MVP)",
                    isUnlocked = true,
                    isCompleted = state.isWorld9Completed,
                    bestScore = state.world9BestScore,
                    onClick = { onWorldClick(9) }
                )
            }
            item {
                WorldCard(
                    title = "World 10: Puranapuranabhyam",
                    subtitle = "Completing the Square Quadratic Equations (Post-MVP)",
                    isUnlocked = true,
                    isCompleted = state.isWorld10Completed,
                    bestScore = state.world10BestScore,
                    onClick = { onWorldClick(10) }
                )
            }
            item {
                WorldCard(
                    title = "World 11: Vyashtisamashtih",
                    subtitle = "Symmetric Products Around an Average (Post-MVP)",
                    isUnlocked = true,
                    isCompleted = state.isWorld11Completed,
                    bestScore = state.world11BestScore,
                    onClick = { onWorldClick(11) }
                )
            }
            item {
                WorldCard(
                    title = "World 12: Shesanyankena Charamena",
                    subtitle = "Recurring Decimal Expansions via Remainder Cycles (Post-MVP)",
                    isUnlocked = true,
                    isCompleted = state.isWorld12Completed,
                    bestScore = state.world12BestScore,
                    onClick = { onWorldClick(12) }
                )
            }
            item {
                WorldCard(
                    title = "World 13: Sopantyadvayamantyam",
                    subtitle = "Multiplication by 12–19 via Ultimate & Penultimate Digits (Post-MVP)",
                    isUnlocked = true,
                    isCompleted = state.isWorld13Completed,
                    bestScore = state.world13BestScore,
                    onClick = { onWorldClick(13) }
                )
            }
            item {
                WorldCard(
                    title = "World 14: Gunitasamuccayah",
                    subtitle = "Polynomial Factorization Verification via Coefficient Sums (Post-MVP)",
                    isUnlocked = true,
                    isCompleted = state.isWorld14Completed,
                    bestScore = state.world14BestScore,
                    onClick = { onWorldClick(14) }
                )
            }
            item {
                WorldCard(
                    title = "World 15: Gunakasamuccayah",
                    subtitle = "Monic Quadratic Factorization by Factor Pairs (Post-MVP)",
                    isUnlocked = true,
                    isCompleted = state.isWorld15Completed,
                    bestScore = state.world15BestScore,
                    onClick = { onWorldClick(15) }
                )
            }
            item {
                WorldCard(
                    title = "World 16: Chalana-Kalanabhyam",
                    subtitle = "Derivative-Discriminant Root Relation (Campaign Finale)",
                    isUnlocked = true,
                    isCompleted = state.isWorld16Completed,
                    bestScore = state.world16BestScore,
                    onClick = { onWorldClick(16) }
                )
            }
        }
    }
}

@Composable
private fun WorldCard(
    title: String,
    subtitle: String,
    isUnlocked: Boolean,
    isCompleted: Boolean,
    bestScore: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) Color.White else Color(0xFFE0E0E0)
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
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isUnlocked) Color.Black else Color.Gray
                )
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                if (isCompleted) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "✓ COMPLETED | Best Score: $bestScore",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                }
            }

            if (isUnlocked) {
                Button(onClick = onClick) {
                    Text(if (isCompleted) "Replay" else "Play")
                }
            } else {
                Box(
                    modifier = Modifier
                        .background(Color.Gray, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("LOCKED", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
