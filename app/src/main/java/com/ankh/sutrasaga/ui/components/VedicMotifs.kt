package com.ankh.sutrasaga.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ankh.sutrasaga.ui.theme.VedicTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * VedicCornerBorder — 1px Gold-Line Flourishes for Card Corners
 */
@Composable
fun VedicCornerBorder(
    modifier: Modifier = Modifier,
    cornerLength: Dp = 16.dp,
    strokeWidth: Dp = 1.2.dp,
    color: Color? = null
) {
    val colors = VedicTheme.colors
    val resolvedColor = color ?: colors.secondaryGold.copy(alpha = 0.45f)

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val len = cornerLength.toPx()
        val sw = strokeWidth.toPx()

        // Top-Left Corner
        drawLine(resolvedColor, Offset(0f, 0f), Offset(len, 0f), sw, StrokeCap.Round)
        drawLine(resolvedColor, Offset(0f, 0f), Offset(0f, len), sw, StrokeCap.Round)

        // Top-Right Corner
        drawLine(resolvedColor, Offset(w, 0f), Offset(w - len, 0f), sw, StrokeCap.Round)
        drawLine(resolvedColor, Offset(w, 0f), Offset(w, len), sw, StrokeCap.Round)

        // Bottom-Left Corner
        drawLine(resolvedColor, Offset(0f, h), Offset(len, h), sw, StrokeCap.Round)
        drawLine(resolvedColor, Offset(0f, h), Offset(0f, h - len), sw, StrokeCap.Round)

        // Bottom-Right Corner
        drawLine(resolvedColor, Offset(w, h), Offset(w - len, h), sw, StrokeCap.Round)
        drawLine(resolvedColor, Offset(w, h), Offset(w, h - len), sw, StrokeCap.Round)
    }
}

/**
 * YantraWatermark — 8-Ring Concentric Geometric Yantra Background Pattern (3% - 5% Opacity)
 */
@Composable
fun YantraWatermark(
    modifier: Modifier = Modifier,
    size: Dp = 320.dp,
    opacity: Float = 0.04f
) {
    val colors = VedicTheme.colors
    val infiniteTransition = rememberInfiniteTransition(label = "YantraWatermarkRot")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 60000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "WatermarkRot"
    )

    Canvas(modifier = modifier.size(size)) {
        val center = Offset(this.size.width / 2f, this.size.height / 2f)
        val maxR = this.size.width / 2f
        val strokeColor = colors.secondaryGold.copy(alpha = opacity)

        rotate(rotation, pivot = center) {
            // 8 Concentric Geometric Circles
            for (i in 1..8) {
                val r = maxR * (i.toFloat() / 8f)
                drawCircle(
                    color = strokeColor,
                    radius = r,
                    center = center,
                    style = Stroke(width = 1.dp.toPx())
                )
            }

            // Radiating 16 Geometric Ray Lines
            val rayCount = 16
            for (i in 0 until rayCount) {
                val angle = (i * 360f / rayCount) * (PI / 180f)
                val startX = center.x + (maxR * 0.2f) * cos(angle).toFloat()
                val startY = center.y + (maxR * 0.2f) * sin(angle).toFloat()
                val endX = center.x + maxR * cos(angle).toFloat()
                val endY = center.y + maxR * sin(angle).toFloat()

                drawLine(
                    color = strokeColor,
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = 0.8.dp.toPx()
                )
            }
        }
    }
}

/**
 * DynamicCalculationArrows — Animated Glowing Crosswise Vector Arrows
 */
@Composable
fun DynamicCalculationArrows(
    pattern: YantraArrowPattern,
    modifier: Modifier = Modifier
) {
    val colors = VedicTheme.colors
    val infiniteTransition = rememberInfiniteTransition(label = "ArrowGlowPulse")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ArrowPulse"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val arrowColor = colors.primarySaffron.copy(alpha = pulse)
        val sw = 2.dp.toPx()

        when (pattern) {
            YantraArrowPattern.VERTICAL_RIGHT -> {
                drawLine(arrowColor, Offset(w * 0.75f, h * 0.1f), Offset(w * 0.75f, h * 0.9f), sw, StrokeCap.Round)
            }
            YantraArrowPattern.CROSSWISE -> {
                drawLine(arrowColor, Offset(w * 0.25f, h * 0.1f), Offset(w * 0.75f, h * 0.9f), sw, StrokeCap.Round)
                drawLine(arrowColor, Offset(w * 0.75f, h * 0.1f), Offset(w * 0.25f, h * 0.9f), sw, StrokeCap.Round)
            }
            YantraArrowPattern.VERTICAL_LEFT -> {
                drawLine(arrowColor, Offset(w * 0.25f, h * 0.1f), Offset(w * 0.25f, h * 0.9f), sw, StrokeCap.Round)
            }
            else -> {}
        }
    }
}
