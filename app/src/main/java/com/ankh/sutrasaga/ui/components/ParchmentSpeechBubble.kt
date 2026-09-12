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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankh.sutrasaga.domain.models.RiveSpeaker
import com.ankh.sutrasaga.ui.theme.VedicParchmentTokens

/**
 * Clean character-anchored speech bubble on warm parchment.
 * Left-aligned for Shishya, Right-aligned for Guru.
 */
@Composable
fun ParchmentSpeechBubble(
    speaker: RiveSpeaker,
    text: String,
    modifier: Modifier = Modifier
) {
    val isGuru = speaker == RiveSpeaker.GURU

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = VedicParchmentTokens.ParchmentCardBg
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(
                if (isGuru) VedicParchmentTokens.ForestGreenBorder else VedicParchmentTokens.ShishyaOrangeBorder
            ),
            width = 1.5.dp
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Speaker Tag Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = if (isGuru) Arrangement.End else Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (isGuru) VedicParchmentTokens.ForestGreenBg else VedicParchmentTokens.ShishyaOrangeBg
                        )
                        .border(
                            1.dp,
                            if (isGuru) VedicParchmentTokens.ForestGreenBorder else VedicParchmentTokens.ShishyaOrangeBorder,
                            RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = if (isGuru) "GURU" else "SHISHYA",
                        fontWeight = FontWeight.Bold,
                        color = if (isGuru) VedicParchmentTokens.ForestGreenDark else VedicParchmentTokens.ShishyaOrange,
                        fontSize = 11.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Speech Text
            Crossfade(
                targetState = text,
                label = "ParchmentSpeechTextCrossfade"
            ) { speechText ->
                Text(
                    text = speechText,
                    color = VedicParchmentTokens.InkDark,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}
