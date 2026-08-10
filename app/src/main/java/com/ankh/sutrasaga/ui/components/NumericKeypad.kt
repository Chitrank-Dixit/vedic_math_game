package com.ankh.sutrasaga.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NumericKeypad(
    onDigitClick: (Char) -> Unit,
    onBackspaceClick: () -> Unit,
    onClearClick: () -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val buttonModifier = Modifier
        .padding(4.dp)
        .fillMaxWidth()

    Column(modifier = modifier.padding(8.dp)) {
        val keys = listOf(
            listOf('1', '2', '3'),
            listOf('4', '5', '6'),
            listOf('7', '8', '9')
        )

        for (row in keys) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (digit in row) {
                    OutlinedButton(
                        onClick = { onDigitClick(digit) },
                        modifier = buttonModifier.weight(1f),
                        enabled = enabled
                    ) {
                        Text(text = digit.toString(), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(
                onClick = onClearClick,
                modifier = buttonModifier.weight(1f),
                enabled = enabled
            ) {
                Text(text = "CLR", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            OutlinedButton(
                onClick = { onDigitClick('0') },
                modifier = buttonModifier.weight(1f),
                enabled = enabled
            ) {
                Text(text = "0", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            OutlinedButton(
                onClick = onBackspaceClick,
                modifier = buttonModifier.weight(1f),
                enabled = enabled
            ) {
                Text(text = "⌫", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }

        Button(
            onClick = onSubmitClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
            enabled = enabled
        ) {
            Text(text = "SUBMIT ANSWER", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}
