package com.ankh.sutrasaga.ui.components

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankh.sutrasaga.ui.theme.CyberCyan
import com.ankh.sutrasaga.ui.theme.CyberCyanLight
import com.ankh.sutrasaga.ui.theme.GoldGradientBrush
import com.ankh.sutrasaga.ui.theme.TextLightSecondary
import com.ankh.sutrasaga.ui.theme.TextWhitePrimary
import com.ankh.sutrasaga.ui.theme.VedicGold
import com.ankh.sutrasaga.ui.theme.VedicGoldLight

@Composable
fun NumericKeypad(
    onDigitClick: (Char) -> Unit,
    onBackspaceClick: () -> Unit,
    onClearClick: () -> Unit,
    onSecondaryValueClick: (() -> Unit)? = null,
    secondaryValueLabel: String = "",
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val buttonShape = RoundedCornerShape(12.dp)
    val buttonHeight = 46.dp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        val keys = listOf(
            listOf('1', '2', '3'),
            listOf('4', '5', '6'),
            listOf('7', '8', '9')
        )

        for (row in keys) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                for (digit in row) {
                    Button(
                        onClick = { onDigitClick(digit) },
                        modifier = Modifier
                            .weight(1f)
                            .height(buttonHeight),
                        shape = buttonShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1E293B),
                            disabledContainerColor = Color(0xFF0F172A)
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = Brush.verticalGradient(
                                listOf(Color(0x4038BDF8), Color(0x1538BDF8))
                            ),
                            width = 1.dp
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                        enabled = enabled
                    ) {
                        Text(
                            text = digit.toString(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = TextWhitePrimary
                        )
                    }
                }
            }
        }

        // Bottom Row: CLR, 0, ⌫
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Clear Button (Amber/Red tint)
            Button(
                onClick = onClearClick,
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight),
                shape = buttonShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF261C1C),
                    disabledContainerColor = Color(0xFF0F172A)
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = Brush.verticalGradient(
                        listOf(Color(0x66EF4444), Color(0x20EF4444))
                    ),
                    width = 1.dp
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                enabled = enabled
            ) {
                Text(
                    text = "CLR",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFF87171)
                )
            }

            // Zero Button
            Button(
                onClick = { onDigitClick('0') },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight),
                shape = buttonShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E293B),
                    disabledContainerColor = Color(0xFF0F172A)
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = Brush.verticalGradient(
                        listOf(Color(0x4038BDF8), Color(0x1538BDF8))
                    ),
                    width = 1.dp
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                enabled = enabled
            ) {
                Text(
                    text = "0",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = TextWhitePrimary
                )
            }

            // Backspace Button (Cyan tint)
            Button(
                onClick = onBackspaceClick,
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight),
                shape = buttonShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF17253B),
                    disabledContainerColor = Color(0xFF0F172A)
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = Brush.verticalGradient(
                        listOf(Color(0x6638BDF8), Color(0x2038BDF8))
                    ),
                    width = 1.dp
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                enabled = enabled
            ) {
                Text(
                    text = "⌫",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyberCyanLight
                )
            }
        }

        // Secondary Value Separator Button (e.g. "R" for Remainder or "," for Pairs)
        if (onSecondaryValueClick != null) {
            Spacer(modifier = Modifier.height(2.dp))
            Button(
                onClick = onSecondaryValueClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
                shape = buttonShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E1B4B)
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = Brush.horizontalGradient(
                        listOf(Color(0x80818CF8), Color(0x80C084FC))
                    ),
                    width = 1.dp
                ),
                enabled = enabled
            ) {
                Text(
                    text = secondaryValueLabel,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFA5B4FC)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Large Radiant Submit Answer Button
        Button(
            onClick = onSubmitClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = VedicGold,
                disabledContainerColor = Color(0xFF334155)
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
            enabled = enabled
        ) {
            Text(
                text = "SUBMIT ANSWER ⚡",
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp,
                color = Color(0xFF0F172A)
            )
        }
    }
}
