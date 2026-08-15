package com.ankh.sutrasaga.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankh.sutrasaga.domain.models.FactorPair
import com.ankh.sutrasaga.domain.models.FactorizationCandidate
import com.ankh.sutrasaga.domain.models.GunakasamuccayahSolution

@Composable
fun GunakasamuccayahMathSlate(
    solution: GunakasamuccayahSolution,
    revealedStepsCount: Int = 5,
    modifier: Modifier = Modifier
) {
    val quad = solution.quadratic
    val matchingPair = solution.matchingPair

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E2C)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "MATHSLATE: GUNAKASAMUCCAYAH",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700),
                letterSpacing = 1.2.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Highlighted Monic Quadratic Equation
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = quad.toFormattedString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Step 1: Candidate Factor Pairs Grid
            if (revealedStepsCount >= 1 && solution.candidates.isNotEmpty()) {
                Text(
                    text = "Candidate Factor Pairs of C = ${quad.c}:",
                    fontSize = 14.sp,
                    color = Color(0xFFA0A0C0)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    solution.candidates.take(4).forEach { candidate ->
                        CandidateRow(candidate = candidate, targetB = quad.b, targetC = quad.c)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Step 2 & 3: Matching Binomial Factors
            if (revealedStepsCount >= 2 && matchingPair != null) {
                val pStr = if (matchingPair.p >= 0) "+ ${matchingPair.p}" else "- ${-matchingPair.p}"
                val qStr = if (matchingPair.q >= 0) "+ ${matchingPair.q}" else "- ${-matchingPair.q}"

                Text(
                    text = "Factored Expression:",
                    fontSize = 14.sp,
                    color = Color(0xFFA0A0C0)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "(x $pStr)(x $qStr) = 0",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFF00FFCC)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Step 4 & 5: Roots & Verification
            if (revealedStepsCount >= 3 && solution.roots.isNotEmpty()) {
                val root1 = solution.roots[0]
                val root2 = solution.roots.getOrElse(1) { root1 }

                Text(
                    text = "Derived Roots (x + p = 0 ⟹ x = -p):",
                    fontSize = 14.sp,
                    color = Color(0xFFA0A0C0)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "x = $root1,  x = $root2",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFF76FF03)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Verification check
                val val1 = quad.eval(root1)
                Text(
                    text = "Verification: ($root1)² + (${quad.b})($root1) + (${quad.c}) = $val1 ✓",
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFF81C784)
                )
            }
        }
    }
}

@Composable
private fun CandidateRow(
    candidate: FactorizationCandidate,
    targetB: Long,
    targetC: Long
) {
    val isMatch = candidate.productMatches && candidate.sumMatches
    val pair = candidate.pair
    val bg = if (isMatch) Color(0xFF1B5E20) else Color(0xFF2A2A3D)
    val border = if (isMatch) Color(0xFF76FF03) else Color(0xFF444466)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bg, RoundedCornerShape(8.dp))
            .border(1.dp, border, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "(${pair.p}, ${pair.q})",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = Color.White
        )
        Text(
            text = "Prod: ${pair.product()} ${if (candidate.productMatches) "✓" else "✗"}",
            fontSize = 13.sp,
            color = if (candidate.productMatches) Color(0xFF81C784) else Color(0xFFE57373)
        )
        Text(
            text = "Sum: ${pair.sum()} ${if (candidate.sumMatches) "✓" else "✗"}",
            fontSize = 13.sp,
            color = if (candidate.sumMatches) Color(0xFF81C784) else Color(0xFFE57373)
        )
    }
}
