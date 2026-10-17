package com.ankh.sutrasaga.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankh.sutrasaga.ui.theme.VedicTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * YantraProgressRing — Animated Sacred Geometry Progress Indicator
 *
 * Visualizes mental arithmetic accuracy and mastery using concentric radiating Yantra
 * geometric lines rather than basic solid bars:
 * - Idle: Muted geometric concentric circles.
 * - Active: Radiant saffron glowing sweep arc.
 * - 100% Completion: Brilliant Emerald Green glow with radiating 8-pointed mandala star.
 */
@Composable
fun YantraProgressRing(
    progress: Float, // 0.0f to 1.0f
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    strokeWidth: Dp = 3.dp,
    showPercentageText: Boolean = false
) {
    val colors = VedicTheme.colors
    val clampedProgress = progress.coerceIn(0f, 1f)
    val isCompleted = clampedProgress >= 0.999f

    // Smooth progress animation
    val animatedProgress by animateFloatAsState(
        targetValue = clampedProgress,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "YantraProgressAnim"
    )

    // Continuous subtle sacred rotation for ambient meditation feel
    val infiniteTransition = rememberInfiniteTransition(label = "YantraRotation")
    val ambientRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 24000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "YantraAmbientRotation"
    )

    // Pulse glow animation on 100% completion
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "MandalaPulse"
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val strokePx = strokeWidth.toPx()
            val center = Offset(this.size.width / 2f, this.size.height / 2f)
            val radius = (this.size.width - strokePx) / 2f

            // 1. Draw Background Concentric Yantra Rings (Sacred Geometry Grid)
            drawCircle(
                color = colors.borderSubtle,
                radius = radius,
                style = Stroke(width = strokePx * 0.5f)
            )
            drawCircle(
                color = colors.borderSubtle.copy(alpha = 0.15f),
                radius = radius * 0.7f,
                style = Stroke(width = strokePx * 0.35f)
            )
            drawCircle(
                color = colors.borderSubtle.copy(alpha = 0.1f),
                radius = radius * 0.4f,
                style = Stroke(width = strokePx * 0.25f)
            )

            // 2. Draw Radiating Geometric Rays / Ray-Ticks
            val rayCount = 8
            rotate(ambientRotation, pivot = center) {
                for (i in 0 until rayCount) {
                    val angle = (i * 360f / rayCount) * (PI / 180f)
                    val innerR = radius * 0.72f
                    val outerR = radius * 0.94f
                    val startX = center.x + innerR * cos(angle).toFloat()
                    val startY = center.y + innerR * sin(angle).toFloat()
                    val endX = center.x + outerR * cos(angle).toFloat()
                    val endY = center.y + outerR * sin(angle).toFloat()

                    drawLine(
                        color = if (isCompleted) colors.statusSuccess.copy(alpha = 0.4f * pulseGlow)
                        else colors.borderSubtle.copy(alpha = 0.2f),
                        start = Offset(startX, startY),
                        end = Offset(endX, endY),
                        strokeWidth = strokePx * 0.4f,
                        cap = StrokeCap.Round
                    )
                }
            }

            // 3. Draw Active Glowing Progress Arc
            if (animatedProgress > 0f) {
                val arcBrush = if (isCompleted) {
                    Brush.sweepGradient(
                        colors = listOf(
                            colors.statusSuccess,
                            Color(0xFF86EFAC),
                            colors.statusSuccess
                        ),
                        center = center
                    )
                } else {
                    Brush.sweepGradient(
                        colors = listOf(
                            colors.primarySaffron,
                            colors.secondaryGold,
                            colors.primarySaffron
                        ),
                        center = center
                    )
                }

                drawArc(
                    brush = arcBrush,
                    startAngle = -90f,
                    sweepAngle = animatedProgress * 360f,
                    useCenter = false,
                    style = Stroke(width = strokePx, cap = StrokeCap.Round),
                    size = Size(radius * 2, radius * 2),
                    topLeft = Offset(center.x - radius, center.y - radius)
                )
            }

            // 4. If Completed: Draw 8-Pointed Mandala Star Motif in Center
            if (isCompleted) {
                drawMandalaStar(
                    center = center,
                    outerRadius = radius * 0.45f * pulseGlow,
                    innerRadius = radius * 0.22f,
                    color = colors.statusSuccess
                )
            }
        }

        if (showPercentageText && !isCompleted) {
            Text(
                text = "${(clampedProgress * 100).toInt()}%",
                fontSize = (size.value * 0.22f).sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = colors.textPrimary
            )
        } else if (showPercentageText && isCompleted) {
            Text(
                text = "✓",
                fontSize = (size.value * 0.28f).sp,
                fontWeight = FontWeight.ExtraBold,
                color = colors.statusSuccess
            )
        }
    }
}

/**
 * Draws an 8-pointed sacred geometry star mandala
 */
private fun DrawScope.drawMandalaStar(
    center: Offset,
    outerRadius: Float,
    innerRadius: Float,
    color: Color
) {
    val path = Path()
    val points = 8
    val angleStep = (2 * PI / (points * 2)).toFloat()

    for (i in 0 until (points * 2)) {
        val r = if (i % 2 == 0) outerRadius else innerRadius
        val angle = i * angleStep - (PI / 2).toFloat()
        val x = center.x + r * cos(angle)
        val y = center.y + r * sin(angle)
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()

    drawPath(
        path = path,
        color = color,
        style = Stroke(width = 1.8f, cap = StrokeCap.Round)
    )
}
