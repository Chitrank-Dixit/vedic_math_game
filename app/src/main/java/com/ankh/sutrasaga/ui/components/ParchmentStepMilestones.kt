package com.ankh.sutrasaga.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankh.sutrasaga.ui.theme.VedicParchmentTokens

/**
 * 4-Step Milestone progression indicator for the Vedic Math Gurukul lesson.
 * Steps: 1: Hook, 2: Sutra Reveal, 3: Insight, 4: Handshake
 */
@Composable
fun ParchmentStepMilestones(
    currentStepIndex: Int,
    totalSteps: Int = 4,
    modifier: Modifier = Modifier
) {
    val stepLabels = listOf("Hook", "Sutra Reveal", "Insight", "Handshake")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Milestone Dots & Connecting Line Row
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            // Background track line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(RoundedCornerShape(1.5.dp))
                    .background(Color(0xFFE2D6C3))
            )

            // Active progress fill line
            val progressFraction by animateFloatAsState(
                targetValue = (currentStepIndex.toFloat() / (totalSteps - 1).coerceAtLeast(1).toFloat()).coerceIn(0f, 1f),
                animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
                label = "ProgressFillFraction"
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = progressFraction)
                    .height(3.dp)
                    .align(Alignment.CenterStart)
                    .clip(RoundedCornerShape(1.5.dp))
                    .background(VedicParchmentTokens.SaffronPrimary)
            )

            // Step Indicator Nodes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0 until totalSteps) {
                    val isPast = i < currentStepIndex
                    val isCurrent = i == currentStepIndex
                    val isFuture = i > currentStepIndex

                    val dotBg by animateColorAsState(
                        targetValue = when {
                            isPast -> VedicParchmentTokens.SaffronPrimary
                            isCurrent -> VedicParchmentTokens.SaffronPrimary
                            else -> Color(0xFFFAF5EC)
                        },
                        label = "StepDotBg_$i"
                    )

                    val dotBorder by animateColorAsState(
                        targetValue = when {
                            isPast || isCurrent -> VedicParchmentTokens.SaffronDark
                            else -> Color(0xFFD1C2AC)
                        },
                        label = "StepDotBorder_$i"
                    )

                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(dotBg)
                            .border(if (isCurrent) 2.5.dp else 1.5.dp, dotBorder, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCurrent) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                            )
                        } else if (isPast) {
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .clip(CircleShape)
                                    .background(VedicParchmentTokens.SaffronGlow)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Step Labels Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            for (i in 0 until totalSteps) {
                val isCurrent = i == currentStepIndex
                val isPast = i < currentStepIndex
                val labelText = stepLabels.getOrElse(i) { "Step ${i + 1}" }

                Text(
                    text = labelText,
                    fontSize = 11.sp,
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                    color = when {
                        isCurrent -> VedicParchmentTokens.SaffronDark
                        isPast -> VedicParchmentTokens.InkMedium
                        else -> VedicParchmentTokens.InkMuted
                    }
                )
            }
        }
    }
}
