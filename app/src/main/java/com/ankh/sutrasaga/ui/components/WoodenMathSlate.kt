package com.ankh.sutrasaga.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankh.sutrasaga.ui.theme.VedicParchmentTokens
import com.ankh.sutrasaga.ui.util.MathFormatter

/**
 * Wooden chalkboard mathematical slate representing the Vedic calculation demonstration.
 */
@Composable
fun WoodenMathSlate(
    expression: String,
    highlightTokens: List<String> = emptyList(),
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = VedicParchmentTokens.ChalkSlateWood
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.verticalGradient(
                listOf(
                    Color(0xFF92400E),
                    Color(0xFF78350F),
                    Color(0xFF451A03)
                )
            ),
            width = 3.dp
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        // Inner Dark Chalkboard Surface
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF2D3748),
                            Color(0xFF1A202C),
                            Color(0xFF0F172A)
                        )
                    )
                )
                .border(1.dp, Color(0x33CA8A04), RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Slate Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "🌿 ✦ VEDIC MATH SLATE ✦ 🌿",
                        color = VedicParchmentTokens.ChalkTextGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Formatted Chalk Math Expression
                val cleanExpression = MathFormatter.format(expression)
                Crossfade(
                    targetState = cleanExpression,
                    label = "WoodenSlateExpressionCrossfade"
                ) { formattedExpr ->
                    Text(
                        text = formattedExpr,
                        color = VedicParchmentTokens.ChalkTextWhite,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center,
                        lineHeight = 28.sp,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }

                // Highlighted Token Chips
                if (highlightTokens.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        highlightTokens.forEach { token ->
                            val cleanToken = MathFormatter.format(token)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(VedicParchmentTokens.ChalkTextHighlightBg)
                                    .border(
                                        1.dp,
                                        VedicParchmentTokens.ChalkTextGold.copy(alpha = 0.7f),
                                        RoundedCornerShape(6.dp)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = cleanToken,
                                    color = VedicParchmentTokens.ChalkTextGold,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
