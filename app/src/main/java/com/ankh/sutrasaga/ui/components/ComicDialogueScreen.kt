package com.ankh.sutrasaga.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankh.sutrasaga.domain.models.DialoguePanel
import com.ankh.sutrasaga.domain.models.DialogueScript
import com.ankh.sutrasaga.domain.models.Speaker

enum class TailPosition {
    LEFT, RIGHT, NONE
}

class SpeechBubbleShape(
    private val tailPosition: TailPosition,
    private val cornerRadiusPx: Float = 32f,
    private val tailWidthPx: Float = 30f,
    private val tailHeightPx: Float = 30f,
    private val tailTopOffsetPx: Float = 50f
) : Shape {
    override fun createOutline(
        size: androidx.compose.ui.geometry.Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            when (tailPosition) {
                TailPosition.LEFT -> {
                    val bodyLeft = tailWidthPx
                    val bodyRight = size.width
                    val bodyTop = 0f
                    val bodyBottom = size.height

                    // Main rounded box
                    moveTo(bodyLeft + cornerRadiusPx, bodyTop)
                    lineTo(bodyRight - cornerRadiusPx, bodyTop)
                    quadraticBezierTo(bodyRight, bodyTop, bodyRight, bodyTop + cornerRadiusPx)
                    lineTo(bodyRight, bodyBottom - cornerRadiusPx)
                    quadraticBezierTo(bodyRight, bodyBottom, bodyRight - cornerRadiusPx, bodyBottom)
                    lineTo(bodyLeft + cornerRadiusPx, bodyBottom)
                    quadraticBezierTo(bodyLeft, bodyBottom, bodyLeft, bodyBottom - cornerRadiusPx)

                    // Tail pointing left
                    lineTo(bodyLeft, tailTopOffsetPx + tailHeightPx)
                    lineTo(0f, tailTopOffsetPx + (tailHeightPx / 2f))
                    lineTo(bodyLeft, tailTopOffsetPx)

                    lineTo(bodyLeft, bodyTop + cornerRadiusPx)
                    quadraticBezierTo(bodyLeft, bodyTop, bodyLeft + cornerRadiusPx, bodyTop)
                    close()
                }
                TailPosition.RIGHT -> {
                    val bodyLeft = 0f
                    val bodyRight = size.width - tailWidthPx
                    val bodyTop = 0f
                    val bodyBottom = size.height

                    moveTo(bodyLeft + cornerRadiusPx, bodyTop)
                    lineTo(bodyRight - cornerRadiusPx, bodyTop)
                    quadraticBezierTo(bodyRight, bodyTop, bodyRight, bodyTop + cornerRadiusPx)

                    // Tail pointing right
                    lineTo(bodyRight, tailTopOffsetPx)
                    lineTo(size.width, tailTopOffsetPx + (tailHeightPx / 2f))
                    lineTo(bodyRight, tailTopOffsetPx + tailHeightPx)

                    lineTo(bodyRight, bodyBottom - cornerRadiusPx)
                    quadraticBezierTo(bodyRight, bodyBottom, bodyRight - cornerRadiusPx, bodyBottom)
                    lineTo(bodyLeft + cornerRadiusPx, bodyBottom)
                    quadraticBezierTo(bodyLeft, bodyBottom, bodyLeft, bodyBottom - cornerRadiusPx)
                    lineTo(bodyLeft, bodyTop + cornerRadiusPx)
                    quadraticBezierTo(bodyLeft, bodyTop, bodyLeft + cornerRadiusPx, bodyTop)
                    close()
                }
                TailPosition.NONE -> {
                    // Standard rounded rectangle for Narrator
                    moveTo(cornerRadiusPx, 0f)
                    lineTo(size.width - cornerRadiusPx, 0f)
                    quadraticBezierTo(size.width, 0f, size.width, cornerRadiusPx)
                    lineTo(size.width, size.height - cornerRadiusPx)
                    quadraticBezierTo(size.width, size.height, size.width - cornerRadiusPx, size.height)
                    lineTo(cornerRadiusPx, size.height)
                    quadraticBezierTo(0f, size.height, 0f, size.height - cornerRadiusPx)
                    lineTo(0f, cornerRadiusPx)
                    quadraticBezierTo(0f, 0f, cornerRadiusPx, 0f)
                    close()
                }
            }
        }
        return Outline.Generic(path)
    }
}

@Composable
fun ComicDialogueScreen(
    script: DialogueScript,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentPanelIndex by remember(script) { mutableIntStateOf(0) }
    val totalPanels = script.panels.size
    val currentPanel = script.panels.getOrElse(currentPanelIndex) { script.panels.last() }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .clickable {
                if (currentPanelIndex < totalPanels - 1) {
                    currentPanelIndex++
                } else {
                    onComplete()
                }
            },
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = script.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    script.subtitle?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { currentPanelIndex = 0 }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Restart Dialogue",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Button(
                        onClick = onComplete,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text("Skip", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            // Progress Bar
            Column(modifier = Modifier.fillMaxWidth()) {
                LinearProgressIndicator(
                    progress = { (currentPanelIndex + 1).toFloat() / totalPanels.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Panel ${currentPanelIndex + 1} of $totalPanels",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.End)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Comic Stage Area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                ComicPanelStage(panel = currentPanel)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom Action Area / Guidance
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tap anywhere to continue",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )

                if (currentPanelIndex == totalPanels - 1) {
                    Button(
                        onClick = onComplete,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Continue", color = MaterialTheme.colorScheme.onPrimary)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ComicPanelStage(panel: DialoguePanel) {
    val masterColor = Color(0xFFFFB300) // Amber / Gold
    val sidekickColor = Color(0xFF00E5FF) // Cyan / Teal
    val narratorColor = Color(0xFF7C4DFF) // Purple / Indigo

    when (panel.speaker) {
        Speaker.NARRATOR -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                CharacterAvatar(
                    speakerName = panel.speakerNameOverride ?: "Narrator",
                    baseEmoji = "📜",
                    expressionEmoji = panel.expression.emoji,
                    accentColor = narratorColor
                )
                Spacer(modifier = Modifier.height(16.dp))
                SpeechBubble(
                    panel = panel,
                    tailPosition = TailPosition.NONE,
                    borderColor = narratorColor
                )
            }
        }
        Speaker.SUTRA_MASTER -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                CharacterAvatar(
                    speakerName = panel.speakerNameOverride ?: "Sutra-Master",
                    baseEmoji = "🧙‍♂️",
                    expressionEmoji = panel.expression.emoji,
                    accentColor = masterColor
                )
                Spacer(modifier = Modifier.width(8.dp))
                SpeechBubble(
                    panel = panel,
                    tailPosition = TailPosition.LEFT,
                    borderColor = masterColor,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Speaker.SIDEKICK -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.End
            ) {
                SpeechBubble(
                    panel = panel,
                    tailPosition = TailPosition.RIGHT,
                    borderColor = sidekickColor,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                CharacterAvatar(
                    speakerName = panel.speakerNameOverride ?: "Calcu-Ghost",
                    baseEmoji = "👻",
                    expressionEmoji = panel.expression.emoji,
                    accentColor = sidekickColor
                )
            }
        }
    }
}

@Composable
private fun CharacterAvatar(
    speakerName: String,
    baseEmoji: String,
    expressionEmoji: String,
    accentColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(72.dp)
    ) {
        Box(contentAlignment = Alignment.BottomEnd) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.2f))
                    .border(2.dp, accentColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = baseEmoji, fontSize = 30.sp)
            }
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, accentColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = expressionEmoji, fontSize = 14.sp)
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = speakerName,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = accentColor,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

@Composable
private fun SpeechBubble(
    panel: DialoguePanel,
    tailPosition: TailPosition,
    borderColor: Color,
    modifier: Modifier = Modifier
) {
    val bubbleShape = remember(tailPosition) { SpeechBubbleShape(tailPosition = tailPosition) }
    val formattedText = remember(panel.text, panel.highlightMathToken) {
        buildAnnotatedStringWithMathHighlight(panel.text, panel.highlightMathToken)
    }

    Surface(
        modifier = modifier
            .clip(bubbleShape)
            .border(2.dp, borderColor, bubbleShape),
        shape = bubbleShape,
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 4.dp
    ) {
        Box(
            modifier = Modifier.padding(
                start = if (tailPosition == TailPosition.LEFT) 22.dp else 16.dp,
                end = if (tailPosition == TailPosition.RIGHT) 22.dp else 16.dp,
                top = 16.dp,
                bottom = 16.dp
            )
        ) {
            Text(
                text = formattedText,
                style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 24.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun buildAnnotatedStringWithMathHighlight(
    text: String,
    highlightToken: String?
): AnnotatedString {
    if (highlightToken.isNullOrEmpty() || !text.contains(highlightToken)) {
        return buildAnnotatedString { append(text) }
    }

    val token = highlightToken

    return buildAnnotatedString {
        var startIdx = 0
        while (startIdx < text.length) {
            val matchIdx = text.indexOf(token, startIdx)
            if (matchIdx == -1) {
                append(text.substring(startIdx))
                break
            } else {
                append(text.substring(startIdx, matchIdx))
                withStyle(
                    style = SpanStyle(
                        color = Color(0xFFFFD54F), // Vibrant gold accent
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp
                    )
                ) {
                    append(token)
                }
                startIdx = matchIdx + token.length
            }
        }
    }
}
