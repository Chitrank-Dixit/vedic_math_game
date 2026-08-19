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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import com.ankh.sutrasaga.domain.models.UpaSutraId
import com.ankh.sutrasaga.domain.models.UpaSutraRegistry
import com.ankh.sutrasaga.ui.components.NumericKeypad
import com.ankh.sutrasaga.ui.viewmodel.GameUiState
import com.ankh.sutrasaga.ui.viewmodel.UpaSutraQuestStage

@Composable
fun UpaSutraQuestScreen(
    state: GameUiState,
    onContinueClick: () -> Unit,
    onDigitClick: (Char) -> Unit,
    onBackspaceClick: () -> Unit,
    onClearClick: () -> Unit,
    onSubmitClick: () -> Unit,
    onNextProblemClick: () -> Unit,
    onRevealStepClick: () -> Unit,
    onReturnToTreasuryClick: () -> Unit
) {
    val upaSutraId = state.selectedUpaSutraId ?: UpaSutraId.ANTYAYORDASHAKEPI
    val definition = UpaSutraRegistry.getById(upaSutraId)

    val isImplemented = upaSutraId == UpaSutraId.ANTYAYORDASHAKEPI ||
            upaSutraId == UpaSutraId.ANURUPYENA ||
            upaSutraId == UpaSutraId.YAVADUNAM_TAVADUNIKRTYA_VARGANCHA_YOJAYET ||
            upaSutraId == UpaSutraId.ADYAMADYENANTYAMANTYENA ||
            upaSutraId == UpaSutraId.VESHTANAM ||
            upaSutraId == UpaSutraId.SHISYATE_SHESAMAJNA ||
            upaSutraId == UpaSutraId.KEVALAIHSAPTAKAM_GUNYAT ||
            upaSutraId == UpaSutraId.LOPANA_STHAPANABHYAM ||
            upaSutraId == UpaSutraId.VILOKANAM

    // For Upa-Sutras that are not yet implemented (coming soon placeholder)
    if (!isImplemented) {
        ComingSoonScreen(
            definition = definition,
            onBackClick = onReturnToTreasuryClick
        )
        return
    }

    when (state.questStage) {
        UpaSutraQuestStage.STORY_BEAT -> {
            QuestStoryBeatStage(
                definition = definition,
                onContinueClick = onContinueClick,
                onBackClick = onReturnToTreasuryClick
            )
        }
        UpaSutraQuestStage.GUIDED_EXAMPLE -> {
            QuestGuidedExampleStage(
                definition = definition,
                onStartPracticeClick = onContinueClick,
                onBackClick = onReturnToTreasuryClick
            )
        }
        UpaSutraQuestStage.PRACTICE -> {
            QuestPracticeStage(
                definition = definition,
                state = state,
                onDigitClick = onDigitClick,
                onBackspaceClick = onBackspaceClick,
                onClearClick = onClearClick,
                onSubmitClick = onSubmitClick,
                onNextProblemClick = onNextProblemClick,
                onRevealStepClick = onRevealStepClick,
                onReturnToTreasuryClick = onReturnToTreasuryClick
            )
        }
        UpaSutraQuestStage.CHALLENGE -> {
            QuestChallengeStage(
                definition = definition,
                state = state,
                onDigitClick = onDigitClick,
                onBackspaceClick = onBackspaceClick,
                onClearClick = onClearClick,
                onSubmitClick = onSubmitClick,
                onNextProblemClick = onNextProblemClick,
                onReturnToTreasuryClick = onReturnToTreasuryClick
            )
        }
        UpaSutraQuestStage.REWARD -> {
            QuestRewardStage(
                definition = definition,
                state = state,
                onReturnToTreasuryClick = onReturnToTreasuryClick
            )
        }
    }
}

@Composable
private fun ComingSoonScreen(
    definition: UpaSutraDefinition,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🏛️", fontSize = 48.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = definition.displayName,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = definition.sanskritName,
            fontSize = 18.sp,
            color = Color(0xFFFFD700)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "\"${definition.meaning}\"",
            fontSize = 15.sp,
            color = Color(0xFF38BDF8)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .background(Color(0xFF1E293B), RoundedCornerShape(8.dp))
                .border(1.dp, Color(0xFF334155), RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Text(
                text = "Quest scroll for this sub-sutra is currently being transcribed in the Gurukul. Check the Codex for reference worked examples!",
                fontSize = 14.sp,
                color = Color(0xFF94A3B8)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onBackClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5))
        ) {
            Text("Return to Treasury")
        }
    }
}

@Composable
private fun QuestStoryBeatStage(
    definition: UpaSutraDefinition,
    onContinueClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val storyText = when (definition.id) {
        UpaSutraId.VILOKANAM -> {
            "Guru: \"A wise mathematician does not guess. Observation is not staring at a number until it panics and gives up its answer — first, we notice what the numbers are telling us!\"\n\nDisciple: \"We inspect the structure! 85² has a 5 at the end, 97² sits close to 100, 43 × 47 has matching tens and units totaling 10, and 58 × 62 centers around 60! The moment we see the pattern, the solution method becomes crystal clear!\""
        }
        UpaSutraId.LOPANA_STHAPANABHYAM -> {
            "Guru: \"Sometimes a busy multivariable expression reveals itself when we temporarily hide one variable — but we must remember what we learned before bringing it back! We didn't erase y forever — y went behind the curtain so x could introduce itself.\"\n\nDisciple: \"To factor 2x² + 5xy + 2y² + 4x + 5y + 2, we set y = 0 to get (2x + 2)(x + 1), then set x = 0 to get (y + 2)(2y + 1). Matching constant terms 2 and 1 yields (2x + y + 2)(x + 2y + 1)!\""
        }
        UpaSutraId.KEVALAIHSAPTAKAM_GUNYAT -> {
            "Guru: \"Behold the 143 wheel! Seven times 143 is 1001, and 1001 times 999 is 999999 — unlocking the six-digit cycle 142857. 143 is the tiny key, but the decimal wheel has six seats — don't try to squeeze it into three!\"\n\nDisciple: \"1/7 is 0.(142857)! And to find 2/7 or 3/7, we just turn the wheel by multiplying the 6-digit block! 2 × 142857 = 285714!\""
        }
        UpaSutraId.SHISYATE_SHESAMAJNA -> {
            "Guru: \"When you divide a polynomial by a simple factor, you don't always need the full division — a single substitution reveals what's left over! The polynomial doesn't need to show its full work — just ask it what it equals at the right spot, and it'll confess the remainder.\"\n\nDisciple: \"To find the remainder of (x³ - 3x² + 4x - 5) ÷ (x - 2), we simply set x = 2! P(2) = 8 - 12 + 8 - 5 = -1! If the remainder is 0, we've discovered a confirmed factor!\""
        }
        UpaSutraId.VESHTANAM -> {
            "Guru: \"Some numbers guard a secret divisor, and osculation is how we coax it out — one digit at a time! Osculation means 'a little kiss' — the last digit gives the rest of the number a kiss goodbye before revealing its secret.\"\n\nDisciple: \"For 19, the osculator is +2! We take last digit 7 of 247, multiply by 2 to get 14, add to 24 to get 38! Since 38 is 19 × 2, 247 is divisible by 19!\""
        }
        UpaSutraId.ADYAMADYENANTYAMANTYENA -> {
            "Guru: \"You learned to multiply crosswise in World 5 — now let's walk that path backward and discover the factors hiding inside a non-monic quadratic! The first digit and the last digit are old friends from opposite ends of the number — cross paths, and see if they agree!\"\n\nDisciple: \"First term 2 factors into (2, 1) and last term 5 factors into (5, 1). Testing candidate (2x+5)(x+1) gives cross-sum 2(1) + 1(5) = 7! It matches B=7!\""
        }
        UpaSutraId.YAVADUNAM_TAVADUNIKRTYA_VARGANCHA_YOJAYET -> {
            "Guru: \"You've already tamed the deficiency once in my grove — now let's see if you can handle it when the deficiency itself grows too big for its space! Twelve squared is too big for its own room — so it knocks on the neighbor's door and hands over the extra hundred.\"\n\nDisciple: \"88² has deficiency 12, giving 12² = 144! The 44 stays in the 2-digit room, and the +1 carries over to 76 to make 7744!\""
        }
        UpaSutraId.ANURUPYENA -> {
            "Guru: \"Welcome, seeker! In World 2 you measured distance from 100 with Nikhilam. But what if numbers cluster near 50 or 200? Fifty is halfway to a hundred — so whatever cross-add we find, we split right down the middle too!\"\n\nDisciple: \"A sub-base camp! We measure deviations from 50, cross-add, halve the left side, and multiply the deviations!\""
        }
        UpaSutraId.ANTYAYORDASHAKEPI -> {
            "Guru: \"Welcome, seeker! You have proven your skills in the main worlds. Now learn the sub-sutra Antyayordashake'pi: when multiplying two numbers sharing the same tens digit whose units digits sum to 10, multiply the tens by one more (Ekadhika) and the units digits together!\"\n\nDisciple: \"Matching tens, units summing to 10! The answer forms in two mental strokes!\""
        }
        else -> "Guru: \"Let us explore the wisdom of ${definition.displayName}.\""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            OutlinedButton(
                onClick = onBackClick,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF94A3B8))
            ) {
                Text("← Treasury")
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "UPA-SUTRA QUEST INTRODUCTION",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700),
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = definition.displayName,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "${definition.sanskritName} — \"${definition.meaning}\"",
                    fontSize = 15.sp,
                    color = Color(0xFF38BDF8)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = storyText,
                    fontSize = 15.sp,
                    color = Color(0xFFE2E8F0),
                    lineHeight = 22.sp
                )
            }
        }

        Button(
            onClick = onContinueClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5))
        ) {
            Text("Inspect Guided Example →", fontSize = 16.sp, modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}

@Composable
private fun QuestGuidedExampleStage(
    definition: UpaSutraDefinition,
    onStartPracticeClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                OutlinedButton(
                    onClick = onBackClick,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF94A3B8))
                ) {
                    Text("← Treasury")
                }
            }

            Text(
                text = "Guided Example",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
            )
            Text(
                text = definition.displayName,
                fontSize = 14.sp,
                color = Color(0xFFFFD700)
            )

            Spacer(modifier = Modifier.height(20.dp))

            when (definition.id) {
                UpaSutraId.VILOKANAM -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Pattern Sight Library",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "1. Ends in 5 Square: 85²",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                            Text(
                                text = "8 × 9 || 25 = 7225",
                                fontSize = 14.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF94A3B8)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "2. Near Base 100: 97²",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                            Text(
                                text = "(97 - 3) || 3² = 9409",
                                fontSize = 14.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF94A3B8)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "3. Same Tens, Units Sum 10: 43 × 47",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                            Text(
                                text = "4 × 5 || 3 × 7 = 2021",
                                fontSize = 14.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF94A3B8)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "4. Symmetric Product: 58 × 62",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                            Text(
                                text = "60² - 2² = 3596",
                                fontSize = 14.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFFFFD700)
                            )
                        }
                    }
                }
                UpaSutraId.LOPANA_STHAPANABHYAM -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "2x² + 5xy + 2y² + 4x + 5y + 2",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Step 1: Eliminate y (y = 0)",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                            Text(
                                text = "2x² + 4x + 2 = (2x + 2)(x + 1)",
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF94A3B8)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Step 2: Eliminate x (x = 0)",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                            Text(
                                text = "2y² + 5y + 2 = (y + 2)(2y + 1)",
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF94A3B8)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Step 3: Retain & Pair Constants",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                            Text(
                                text = "Constant 2: (2x + y + 2)\nConstant 1: (x + 2y + 1)",
                                fontSize = 15.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF34D399)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Step 4: Check Cross-Term",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                            Text(
                                text = "(2x)(2y) + (y)(x) = 5xy ✓",
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFFFFD700)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Factors: (2x + y + 2)(x + 2y + 1)",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFFFFD700)
                            )
                        }
                    }
                }
                UpaSutraId.KEVALAIHSAPTAKAM_GUNYAT -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "1/7 & 2/7 (The 143 Wheel)",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Step 1: Foundational Identity",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                            Text(
                                text = "7 × 143 = 1001",
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF94A3B8)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Step 2: Scale to 999999",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                            Text(
                                text = "1001 × 999 = 999999 ⟹ 7 × 142857 = 999999",
                                fontSize = 14.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF94A3B8)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Step 3: Canonical 6-Digit Repetend",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                            Text(
                                text = "1/7 = 0.(142857)",
                                fontSize = 18.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF34D399)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Step 4: Rotate Wheel for 2/7",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                            Text(
                                text = "2 × 142857 = 285714 ⟹ 2/7 = 0.(285714)",
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFFFFD700)
                            )
                        }
                    }
                }
                UpaSutraId.SHISYATE_SHESAMAJNA -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "(x³ - 3x² + 4x - 5) ÷ (x - 2)",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Step 1: Set Divisor Root",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                            Text(
                                text = "x - 2 = 0 ⟹ x = 2",
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF94A3B8)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Step 2: Substitute x = 2 into P(x)",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                            Text(
                                text = "P(2) = 2³ - 3(2²) + 4(2) - 5",
                                fontSize = 15.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF94A3B8)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Step 3: Evaluate & Sum Terms",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                            Text(
                                text = "8 - 12 + 8 - 5 = -1",
                                fontSize = 18.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF34D399)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Remainder = -1",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFFFFD700)
                            )
                        }
                    }
                }
                UpaSutraId.VESHTANAM -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "247 ÷ 19 (Osculator +2)",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Step 1: Split Rest & Last Digit",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                            Text(
                                text = "Rest = 24,  Last Digit = 7",
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF94A3B8)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Step 2: Multiply by Osculator 2",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                            Text(
                                text = "7 × 2 = 14",
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF94A3B8)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Step 3: Add to Rest",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                            Text(
                                text = "24 + 14 = 38",
                                fontSize = 18.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF34D399)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Verdict: 38 = 19 × 2 ⟹ DIVISIBLE ✓",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFFFFD700)
                            )
                        }
                    }
                }
                UpaSutraId.ADYAMADYENANTYAMANTYENA -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "2x² + 7x + 5",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Step 1: First-Term Pairs (A = 2)",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                            Text(
                                text = "2 = 2 × 1 ⟹ (2, 1)",
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF94A3B8)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Step 2: Last-Term Pairs (C = 5)",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                            Text(
                                text = "5 = 5 × 1 or 1 × 5",
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF94A3B8)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Step 3: Test Candidate (2x + 5)(x + 1)",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                            Text(
                                text = "Cross-Term: 2(1) + 1(5) = 2 + 5 = 7 ✓",
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF34D399)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Confirmed Factors: (2x + 5)(x + 1)",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFFFFD700)
                            )
                        }
                    }
                }
                UpaSutraId.YAVADUNAM_TAVADUNIKRTYA_VARGANCHA_YOJAYET -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "88² (Base 100, Deficiency 12)",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Step 1: Reduce by Deficiency",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                            Text(
                                text = "88 - 12 = 76",
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF94A3B8)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Step 2: Square Deficiency (12² = 144)",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                            Text(
                                text = "Block width is 2 digits ⟹ Keep \"44\", carry +1",
                                fontSize = 15.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFFF59E0B)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Step 3: Combine with Carry",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                            Text(
                                text = "(76 + 1) || 44 = 7744",
                                fontSize = 18.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF34D399)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Final Result: 7744",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFFFFD700)
                            )
                        }
                    }
                }
                UpaSutraId.ANURUPYENA -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "48 × 46 (Working Base 50)",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Step 1: Deviations from 50 (100 ÷ 2)",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                            Text(
                                text = "48 - 50 = -2,  46 - 50 = -4",
                                fontSize = 14.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF94A3B8)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Step 2: Cross-Add & Scale (Halve)",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                            Text(
                                text = "48 + (-4) = 44 ⟹ 44 ÷ 2 = 22",
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF34D399)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Step 3: Multiply Deviations (2 Digits)",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                            Text(
                                text = "(-2) × (-4) = 8 ⟹ \"08\"",
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF34D399)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Combined Result: 22 || 08 = 2208",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFFFFD700)
                            )
                        }
                    }
                }
                else -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "43 × 47",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Step 1: Check Condition",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                            Text(
                                text = "Tens match (4 = 4) and Units sum to 10 (3 + 7 = 10)",
                                fontSize = 13.sp,
                                color = Color(0xFF94A3B8)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Step 2: LHS = Tens × (Tens + 1)",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                            Text(
                                text = "4 × (4 + 1) = 4 × 5 = 20",
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF34D399)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Step 3: RHS = Units × Units",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                            Text(
                                text = "3 × 7 = 21",
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF34D399)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Combined Result: 20 || 21 = 2021",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFFFFD700)
                            )
                        }
                    }
                }
            }
        }

        Button(
            onClick = onStartPracticeClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669))
        ) {
            Text("Begin Guided Practice (5 Problems) →", fontSize = 16.sp, modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}

@Composable
private fun QuestPracticeStage(
    definition: UpaSutraDefinition,
    state: GameUiState,
    onDigitClick: (Char) -> Unit,
    onBackspaceClick: () -> Unit,
    onClearClick: () -> Unit,
    onSubmitClick: () -> Unit,
    onNextProblemClick: () -> Unit,
    onRevealStepClick: () -> Unit,
    onReturnToTreasuryClick: () -> Unit
) {
    val problem = state.currentProblem

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onReturnToTreasuryClick,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF94A3B8))
                ) {
                    Text("← Treasury", fontSize = 12.sp)
                }

                Text(
                    text = "PRACTICE (${state.currentProblemIndex + 1}/${state.totalProblemsInMode})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF38BDF8)
                )
                Text(
                    text = "Score: ${state.score}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = problem?.questionText ?: "Calculating...",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Input: ${state.userInput.ifEmpty { "..." }}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFF34D399)
                    )

                    if (state.feedbackMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = state.feedbackMessage,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (state.isAnswerCorrect == true) Color(0xFF34D399) else Color(0xFFF87171)
                        )
                    }

                    // Step reveals
                    if (state.revealedStepsCount > 0 && problem != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        problem.decompositionSteps.take(state.revealedStepsCount).forEach { step ->
                            Text(
                                text = "• ${step.label}: ${step.formulaDisplay} = ${step.stepResult}",
                                fontSize = 12.sp,
                                color = Color(0xFFCBD5E1)
                            )
                        }
                    }
                }
            }
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            if (!state.isAnswerSubmitted) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onRevealStepClick,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF38BDF8))
                    ) {
                        Text("Hint / Reveal Step")
                    }
                }
            } else {
                Button(
                    onClick = onNextProblemClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669))
                ) {
                    Text(
                        text = if (state.currentProblemIndex + 1 >= state.totalProblemsInMode) "Proceed to Challenge →" else "Next Problem →",
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            NumericKeypad(
                onDigitClick = onDigitClick,
                onBackspaceClick = onBackspaceClick,
                onClearClick = onClearClick,
                onSubmitClick = onSubmitClick
            )
        }
    }
}

@Composable
private fun QuestChallengeStage(
    definition: UpaSutraDefinition,
    state: GameUiState,
    onDigitClick: (Char) -> Unit,
    onBackspaceClick: () -> Unit,
    onClearClick: () -> Unit,
    onSubmitClick: () -> Unit,
    onNextProblemClick: () -> Unit,
    onReturnToTreasuryClick: () -> Unit
) {
    val problem = state.currentProblem

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onReturnToTreasuryClick,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF94A3B8))
                ) {
                    Text("← Treasury", fontSize = 12.sp)
                }

                Text(
                    text = "⚔️ CHALLENGE (${state.currentProblemIndex + 1}/${state.totalProblemsInMode})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF59E0B)
                )
                Text(
                    text = "Score: ${state.score}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = problem?.questionText ?: "Calculating...",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Input: ${state.userInput.ifEmpty { "..." }}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFF34D399)
                    )

                    if (state.feedbackMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = state.feedbackMessage,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (state.isAnswerCorrect == true) Color(0xFF34D399) else Color(0xFFF87171)
                        )
                    }
                }
            }
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            if (state.isAnswerSubmitted) {
                Button(
                    onClick = onNextProblemClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669))
                ) {
                    Text(
                        text = if (state.currentProblemIndex + 1 >= state.totalProblemsInMode) "Complete Quest →" else "Next Challenge Problem →",
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            NumericKeypad(
                onDigitClick = onDigitClick,
                onBackspaceClick = onBackspaceClick,
                onClearClick = onClearClick,
                onSubmitClick = onSubmitClick
            )
        }
    }
}

@Composable
private fun QuestRewardStage(
    definition: UpaSutraDefinition,
    state: GameUiState,
    onReturnToTreasuryClick: () -> Unit
) {
    val isMastered = state.questChallengeCorrectCount >= 2

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (isMastered) "👑" else "📜",
            fontSize = 56.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isMastered) "QUEST MASTERED!" else "QUEST PRACTICED!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = if (isMastered) Color(0xFF10B981) else Color(0xFF38BDF8)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = definition.displayName,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = "${definition.sanskritName} — \"${definition.meaning}\"",
            fontSize = 14.sp,
            color = Color(0xFFFFD700)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Performance Summary",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF94A3B8)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Practice Correct: ${state.questPracticeCorrectCount} / 5",
                    fontSize = 15.sp,
                    color = Color.White
                )
                Text(
                    text = "Challenge Correct: ${state.questChallengeCorrectCount} / 3",
                    fontSize = 15.sp,
                    color = Color.White
                )
                Text(
                    text = "Total Score: ${state.score} pts",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onReturnToTreasuryClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5))
        ) {
            Text("Return to Upa-Sutra Treasury", fontSize = 16.sp, modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}
