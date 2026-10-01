package com.ankh.sutrasaga.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import com.ankh.sutrasaga.ui.theme.VedicTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * MandalaBurst — Level Clear Victory Celebration Overlay
 *
 * Emits an 8-pointed sacred geometry mandala burst radiating outward:
 * - Color transitions from Saffron (#FF9800) to Tulsi Emerald (#4ADE80)
 * - Concentric expansion rings + 8 radiating golden star petals
 */
@Composable
fun MandalaBurst(
    modifier: Modifier = Modifier,
    onAnimationEnd: () -> Unit = {}
) {
    val colors = VedicTheme.colors
    val progress = remember { Animatable(0f) }
    val rotation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Fast radiant expansion (1000ms)
        progress.animateTo(
            targetValue = 1.0f,
            animationSpec = tween(durationMillis = 1100, easing = FastOutSlowInEasing)
        )
        onAnimationEnd()
    }

    LaunchedEffect(Unit) {
        rotation.animateTo(
            targetValue = 90f,
            animationSpec = tween(durationMillis = 1100, easing = LinearEasing)
        )
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val p = progress.value
            val center = Offset(size.width / 2f, size.height / 2f)
            val maxR = size.minDimension * 0.45f
            val currentR = maxR * p
            val alpha = (1f - p).coerceIn(0f, 1f)

            // Transition color from Saffron -> Gold -> Tulsi Emerald
            val currentColor = if (p < 0.5f) {
                colors.primarySaffron.copy(alpha = alpha)
            } else {
                colors.statusSuccess.copy(alpha = alpha)
            }

            // 1. Expanding Concentric Geometric Rings
            drawCircle(
                color = currentColor,
                radius = currentR,
                center = center,
                style = Stroke(width = 3.dp.toPx() * (1f - p * 0.5f))
            )
            drawCircle(
                color = colors.secondaryGold.copy(alpha = alpha * 0.7f),
                radius = currentR * 0.65f,
                center = center,
                style = Stroke(width = 2.dp.toPx())
            )

            // 2. Radiating 8-Pointed Sacred Geometry Mandala Star Burst
            rotate(rotation.value, pivot = center) {
                drawBurstMandala(
                    center = center,
                    outerR = currentR * 0.85f,
                    innerR = currentR * 0.40f,
                    color = currentColor,
                    strokeWidth = 2.5.dp.toPx()
                )

                // 8 Secondary Star Ray Lines
                val rays = 8
                for (i in 0 until rays) {
                    val angle = (i * 360f / rays) * (PI / 180f)
                    val startR = currentR * 0.3f
                    val endR = currentR * 1.15f
                    val x1 = center.x + startR * cos(angle).toFloat()
                    val y1 = center.y + startR * sin(angle).toFloat()
                    val x2 = center.x + endR * cos(angle).toFloat()
                    val y2 = center.y + endR * sin(angle).toFloat()

                    drawLine(
                        color = colors.secondaryGold.copy(alpha = alpha * 0.9f),
                        start = Offset(x1, y1),
                        end = Offset(x2, y2),
                        strokeWidth = 2.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawBurstMandala(
    center: Offset,
    outerR: Float,
    innerR: Float,
    color: Color,
    strokeWidth: Float
) {
    if (outerR <= 0f) return
    val path = Path()
    val points = 8
    val step = (2 * PI / (points * 2)).toFloat()

    for (i in 0 until (points * 2)) {
        val r = if (i % 2 == 0) outerR else innerR
        val angle = i * step - (PI / 2).toFloat()
        val x = center.x + r * cos(angle)
        val y = center.y + r * sin(angle)
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()

    drawPath(
        path = path,
        color = color,
        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
    )
}
