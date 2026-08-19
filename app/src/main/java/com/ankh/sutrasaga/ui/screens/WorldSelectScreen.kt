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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun WorldSelectScreen(
    state: GameUiState,
    onWorldClick: (Int) -> Unit,
    onTreasuryClick: () -> Unit = {},
    onCodexClick: () -> Unit = {}
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
        // App Header Banner
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Ankh: The Sutra Saga",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp,
                color = VedicGoldLight
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Select a World to Begin the Ancient Journey",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = TextLightSecondary
            )
        }

        // Upa-Sutra Treasury & Codex Hub Entry Point Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xF21E293B)),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = HologramBorderBrush,
                width = 1.5.dp
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(14.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "🏛️ Upa-Sutra Treasury",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = VedicGoldLight
                    )
                    Text(
                        text = "13 Ancient Sub-Sutras & Side Quests",
                        fontSize = 12.sp,
                        color = TextLightSecondary
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedButton(
                        onClick = onCodexClick,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberCyanLight),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = Brush.horizontalGradient(listOf(CyberCyan, CyberCyanLight)),
                            width = 1.dp
                        ),
                        contentPadding = ButtonDefaults.TextButtonContentPadding
                    ) {
                        Text("📜 Codex", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = onTreasuryClick,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4338CA)),
                        contentPadding = ButtonDefaults.TextButtonContentPadding
                    ) {
                        Text("Treasury ➔", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhitePrimary)
                    }
                }
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
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
                    subtitle = "Vertically and Crosswise (2x2 Multiplication)",
                    isUnlocked = true,
                    isCompleted = state.isWorld5Completed,
                    bestScore = state.world5BestScore,
                    onClick = { onWorldClick(5) }
                )
            }
            item {
                WorldCard(
                    title = "World 6: Paravartya Yojayet",
                    subtitle = "Transpose and Apply (Synthetic Polynomial Division)",
                    isUnlocked = true,
                    isCompleted = state.isWorld6Completed,
                    bestScore = state.world6BestScore,
                    onClick = { onWorldClick(6) }
                )
            }
            item {
                WorldCard(
                    title = "World 7: Anurupye Shunyamanyat",
                    subtitle = "If one is in ratio, the other is zero (Ratio Systems)",
                    isUnlocked = true,
                    isCompleted = state.isWorld7Completed,
                    bestScore = state.world7BestScore,
                    onClick = { onWorldClick(7) }
                )
            }
            item {
                WorldCard(
                    title = "World 8: Sankalana-Vyavakalanabhyam",
                    subtitle = "By Addition and Subtraction (Simultaneous 2-Var Systems)",
                    isUnlocked = true,
                    isCompleted = state.isWorld8Completed,
                    bestScore = state.world8BestScore,
                    onClick = { onWorldClick(8) }
                )
            }
            item {
                WorldCard(
                    title = "World 9: Puranapuranabhyam",
                    subtitle = "By the Completion or Non-Completion (Completing the Square)",
                    isUnlocked = true,
                    isCompleted = state.isWorld9Completed,
                    bestScore = state.world9BestScore,
                    onClick = { onWorldClick(9) }
                )
            }
            item {
                WorldCard(
                    title = "World 10: Calana-Kalanabhyam",
                    subtitle = "Sequential Difference & Factoring Quadratics",
                    isUnlocked = true,
                    isCompleted = state.isWorld10Completed,
                    bestScore = state.world10BestScore,
                    onClick = { onWorldClick(10) }
                )
            }
            item {
                WorldCard(
                    title = "World 11: Yavadunam Tavadunam",
                    subtitle = "Multi-Digit Deficiency Squaring & Base Cubing",
                    isUnlocked = true,
                    isCompleted = state.isWorld11Completed,
                    bestScore = state.world11BestScore,
                    onClick = { onWorldClick(11) }
                )
            }
            item {
                WorldCard(
                    title = "World 12: Vyashtisamashtih",
                    subtitle = "Part and Whole: Average & Weighted Sum Decomposition",
                    isUnlocked = true,
                    isCompleted = state.isWorld12Completed,
                    bestScore = state.world12BestScore,
                    onClick = { onWorldClick(12) }
                )
            }
            item {
                WorldCard(
                    title = "World 13: Sheshanyankena Charamena",
                    subtitle = "The Remainders by the Last Digit (Single-Line Division)",
                    isUnlocked = true,
                    isCompleted = state.isWorld13Completed,
                    bestScore = state.world13BestScore,
                    onClick = { onWorldClick(13) }
                )
            }
            item {
                WorldCard(
                    title = "World 14: Sopantyadvayamantyam",
                    subtitle = "The Ultimate and Twice the Penultimate (3-Var Determinants)",
                    isUnlocked = true,
                    isCompleted = state.isWorld14Completed,
                    bestScore = state.world14BestScore,
                    onClick = { onWorldClick(14) }
                )
            }
            item {
                WorldCard(
                    title = "World 15: Ekanyunena Charamena",
                    subtitle = "One Less Than the Previous for Repeating Decimals (1/19, 1/29)",
                    isUnlocked = true,
                    isCompleted = state.isWorld15Completed,
                    bestScore = state.world15BestScore,
                    onClick = { onWorldClick(15) }
                )
            }
            item {
                WorldCard(
                    title = "World 16: Gunitasamuccayah",
                    subtitle = "The Product of the Sum is the Sum of the Products (Factor Check)",
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
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) Color(0xF01E293B) else Color(0x800F172A)
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = if (isCompleted) {
                Brush.horizontalGradient(listOf(SuccessEmerald, Color(0xFF34D399)))
            } else if (isUnlocked) {
                Brush.horizontalGradient(listOf(Color(0x4038BDF8), Color(0x20FFB300)))
            } else {
                Brush.horizontalGradient(listOf(Color(0x20FFFFFF), Color(0x10FFFFFF)))
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
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isUnlocked) TextWhitePrimary else TextMuted
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = TextLightSecondary
                )
                if (isCompleted) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0x3310B981))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "✓ MASTERED",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = SuccessEmerald,
                                letterSpacing = 0.5.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Best: $bestScore pts",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = VedicGoldLight
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
                        containerColor = if (isCompleted) Color(0xFF334155) else VedicGold
                    ),
                    contentPadding = ButtonDefaults.TextButtonContentPadding
                ) {
                    Text(
                        text = if (isCompleted) "Replay" else "Play",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isCompleted) TextWhitePrimary else Color(0xFF0F172A)
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
