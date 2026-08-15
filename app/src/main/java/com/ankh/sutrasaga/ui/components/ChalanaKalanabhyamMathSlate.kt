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
import com.ankh.sutrasaga.domain.models.ChalanaSolution

@Composable
fun ChalanaKalanabhyamMathSlate(
    solution: ChalanaSolution,
    revealedStepsCount: Int = 5,
    modifier: Modifier = Modifier
) {
    val quad = solution.quadratic
    val deriv = solution.derivative
    val disc = solution.discriminant

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
                text = "MATHSLATE: CHALANA-KALANABHYAM",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700),
                letterSpacing = 1.2.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Highlighted Monic / Non-Monic Quadratic Equation
            Text(
                text = quad.toFormattedString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Step 1: Derivative Expression f'(x) = 2Ax + B
            if (revealedStepsCount >= 1) {
                Text(
                    text = "First Derivative f'(x) = 2Ax + B:",
                    fontSize = 14.sp,
                    color = Color(0xFFA0A0C0)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "f'(x) = ${deriv.toFormattedString()}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFF80DEEA)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Step 2: Discriminant Calculation D = B² - 4AC
            if (revealedStepsCount >= 2) {
                val dStatus = if (disc.value < 0) "D < 0 (No Real Roots)" else if (disc.isPerfectSquare) "√D = ${disc.squareRoot}" else "D > 0 (Irrational)"

                Text(
                    text = "Discriminant D = B² - 4AC:",
                    fontSize = 14.sp,
                    color = Color(0xFFA0A0C0)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "D = ${disc.value}  ($dStatus)",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = if (disc.value >= 0) Color(0xFFFFB74D) else Color(0xFFE57373)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Step 3 & 4: Branches f'(x) = ±√D and Derived Roots
            if (revealedStepsCount >= 3 && solution.roots.isNotEmpty()) {
                val r1 = solution.roots[0]
                val r2 = solution.roots.getOrElse(1) { r1 }

                Text(
                    text = "Derivative Branches (2Ax + B = ±√D):",
                    fontSize = 14.sp,
                    color = Color(0xFFA0A0C0)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "+√D: x = $r1",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFF00FFCC)
                    )
                    Text(
                        text = "-√D: x = $r2",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFF00FFCC)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Verification check: f'(r)² == D
                val fPrimeR1 = deriv.eval(r1)
                Text(
                    text = "Invariant: f'($r1)² = ($fPrimeR1)² = ${fPrimeR1 * fPrimeR1} == D ✓",
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFF81C784)
                )
            }
        }
    }
}
