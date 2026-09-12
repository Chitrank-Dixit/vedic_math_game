package com.ankh.sutrasaga.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ankh.sutrasaga.R
import com.ankh.sutrasaga.domain.models.RiveDialogueNode
import com.ankh.sutrasaga.domain.models.RiveSpeaker
import com.ankh.sutrasaga.ui.theme.VedicParchmentTokens

/**
 * Character Stage displaying stylized Shishya (Left) and Guru (Right)
 * reacting dynamically to the dialogue progression and Rive state machine.
 */
@Composable
fun GurukulCharacterStage(
    currentNode: RiveDialogueNode,
    isHandshakeSuccessful: Boolean = false,
    modifier: Modifier = Modifier
) {
    val isGuru = currentNode.speaker == RiveSpeaker.GURU
    val animName = currentNode.riveState.animation

    val infiniteTransition = rememberInfiniteTransition(label = "CharacterIdleBreathing")
    val idleFloatOffset by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "IdleFloatOffset"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Character Avatars & Central Speech Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Shishya Avatar (Left)
            val shishyaImageRes = when {
                isHandshakeSuccessful || animName == "shishya_aha" || animName == "shishya_celebrate" -> R.drawable.shishya_avatar_eureka
                else -> R.drawable.shishya_avatar_puzzled
            }

            Box(
                modifier = Modifier
                    .offset(y = if (!isGuru) idleFloatOffset.dp else 0.dp)
                    .size(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = shishyaImageRes),
                    contentDescription = "Shishya Character",
                    modifier = Modifier
                        .size(116.dp)
                        .clip(CircleShape)
                        .shadow(elevation = if (!isGuru) 8.dp else 2.dp, shape = CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            // 2. Center Speech Bubble
            ParchmentSpeechBubble(
                speaker = currentNode.speaker,
                text = currentNode.text,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )

            // 3. Guru Avatar (Right)
            // STRICT REQUIREMENT: Guru must NEVER wink!
            val guruImageRes = when {
                isHandshakeSuccessful || animName == "guru_nod" || animName == "guru_bless" -> R.drawable.guru_avatar_proud_nod
                else -> R.drawable.guru_avatar_explaining
            }

            Box(
                modifier = Modifier
                    .offset(y = if (isGuru) idleFloatOffset.dp else 0.dp)
                    .size(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = guruImageRes),
                    contentDescription = "Guru Character",
                    modifier = Modifier
                        .size(116.dp)
                        .clip(CircleShape)
                        .shadow(elevation = if (isGuru) 8.dp else 2.dp, shape = CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
