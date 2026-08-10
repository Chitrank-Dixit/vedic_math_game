package com.ankh.sutrasaga.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

private data class Particle(
    val x: Float,
    val y: Float,
    val radius: Float,
    val color: Color,
    val velocityX: Float,
    val velocityY: Float
)

@Composable
fun GurukulParticleEffect(
    trigger: Boolean,
    modifier: Modifier = Modifier
) {
    if (!trigger) return

    val progress = remember { Animatable(0f) }

    LaunchedEffect(trigger) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
        )
    }

    val particles = remember {
        List(30) {
            Particle(
                x = 0.5f,
                y = 0.5f,
                radius = Random.nextFloat() * 8f + 4f,
                color = listOf(Color(0xFFFFD54F), Color(0xFF00E5FF), Color(0xFFFFB300), Color(0xFF81C784)).random(),
                velocityX = (Random.nextFloat() - 0.5f) * 400f,
                velocityY = (Random.nextFloat() - 0.8f) * 400f
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val alpha = 1f - progress.value

        particles.forEach { particle ->
            val px = width * particle.x + particle.velocityX * progress.value
            val py = height * particle.y + particle.velocityY * progress.value + 100f * progress.value * progress.value
            drawCircle(
                color = particle.color.copy(alpha = alpha.coerceIn(0f, 1f)),
                radius = particle.radius,
                center = Offset(px, py)
            )
        }
    }
}
