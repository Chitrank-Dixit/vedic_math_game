package com.ankh.sutrasaga.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ankh.sutrasaga.ui.theme.VedicTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * VedicAppLogo — Scalable Vector Brand Emblem
 *
 * Design Concept:
 * - Outer squircle frame (#0F172A in Dark, #FFFFFF in Light)
 * - Central Emblem: Abstract 8-pointed Sri Yantra star merged with an Infinity Loop (∞)
 * - Linear gradient transitioning from Saffron (#E65100) to Deep Vedic Gold (#D4AF37)
 * - 2px vector stroke width with soft rounded caps
 */
@Composable
fun VedicAppLogo(
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
    showBackground: Boolean = true
) {
    val colors = VedicTheme.colors
    val radii = VedicTheme.radii

    Box(
        modifier = modifier
            .size(size)
            .then(
                if (showBackground) {
                    Modifier
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(size * 0.25f),
                            ambientColor = colors.shadowColor,
                            spotColor = colors.glowColor
                        )
                        .clip(RoundedCornerShape(size * 0.25f))
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val w = this.size.width
            val h = this.size.height
            val center = Offset(w / 2f, h / 2f)

            // 1. Draw Squircle Background if enabled
            if (showBackground) {
                drawRoundRect(
                    color = colors.surfaceCard,
                    size = Size(w, h),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.25f, h * 0.25f)
                )
                drawRoundRect(
                    brush = Brush.linearGradient(
                        listOf(colors.primarySaffron.copy(alpha = 0.5f), colors.secondaryGold.copy(alpha = 0.5f))
                    ),
                    size = Size(w, h),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.25f, h * 0.25f),
                    style = Stroke(width = 1.5.dp.toPx())
                )
            }

            val emblemRadius = w * 0.32f
            val strokePx = 2.dp.toPx()

            val logoBrush = Brush.linearGradient(
                colors = listOf(colors.primarySaffron, colors.secondaryGold),
                start = Offset(center.x - emblemRadius, center.y - emblemRadius),
                end = Offset(center.x + emblemRadius, center.y + emblemRadius)
            )

            // 2. Draw 8-Pointed Mandala Star
            draw8PointedStar(
                center = center,
                outerR = emblemRadius,
                innerR = emblemRadius * 0.55f,
                brush = logoBrush,
                strokeWidth = strokePx
            )

            // 3. Draw Intersecting Infinity Loop (∞)
            drawInfinitySymbol(
                center = center,
                width = emblemRadius * 1.35f,
                height = emblemRadius * 0.65f,
                brush = logoBrush,
                strokeWidth = strokePx
            )

            // 4. Central Bindu Point (Sacred Origin)
            drawCircle(
                brush = logoBrush,
                radius = strokePx * 1.2f,
                center = center
            )
        }
    }
}

private fun DrawScope.draw8PointedStar(
    center: Offset,
    outerR: Float,
    innerR: Float,
    brush: Brush,
    strokeWidth: Float
) {
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
        brush = brush,
        style = Stroke(
            width = strokeWidth,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )
}

private fun DrawScope.drawInfinitySymbol(
    center: Offset,
    width: Float,
    height: Float,
    brush: Brush,
    strokeWidth: Float
) {
    val path = Path()
    val halfW = width / 2f
    val halfH = height / 2f

    val steps = 60
    for (i in 0..steps) {
        val t = (i.toFloat() / steps.toFloat()) * (2 * PI).toFloat()
        // Lemniscate of Bernoulli parametric curve
        val denom = 1 + sin(t) * sin(t)
        val x = center.x + (halfW * cos(t)) / denom
        val y = center.y + (halfH * sin(t) * cos(t)) / denom

        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()

    drawPath(
        path = path,
        brush = brush,
        style = Stroke(
            width = strokeWidth * 0.9f,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )
}
