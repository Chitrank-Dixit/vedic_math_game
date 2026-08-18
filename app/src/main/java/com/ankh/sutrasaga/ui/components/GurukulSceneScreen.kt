package com.ankh.sutrasaga.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankh.sutrasaga.R
import com.ankh.sutrasaga.domain.models.GurukulScript
import com.ankh.sutrasaga.domain.models.SutraProblem

@Composable
fun GurukulSceneScreen(
    script: GurukulScript,
    problem: SutraProblem,
    onComplete: () -> Unit,
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var currentBeatIndex by remember(script) { mutableIntStateOf(0) }
    val totalBeats = script.beats.size
    val currentBeat = script.beats.getOrElse(currentBeatIndex) { script.beats.last() }

    // Ambient Hologram Glow Pulsing Animation
    val hologramAlpha = remember { Animatable(0.3f) }
    LaunchedEffect(Unit) {
        hologramAlpha.animateTo(
            targetValue = 0.7f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    val guruGold = Color(0xFFFFB300)
    val discipleCyan = Color(0xFF00E5FF)

    Surface(
        modifier = modifier
            .fillMaxSize()
            .clickable {
                if (currentBeatIndex < totalBeats - 1) {
                    currentBeatIndex++
                } else {
                    onComplete()
                }
            },
        color = Color(0xFF1F140E)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // 1. Full-Screen Morning Gurukul Scene Background
            // Contains Master Purva (Left), Disciple (Right), Morning Sunbeams, Banyan Tree & Temple
            // TODO: replace with final art — bg_gurukul_scene.jpg
            Image(
                painter = painterResource(id = R.drawable.bg_gurukul_scene),
                contentDescription = "Gurukul Morning Scene Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // 2. Main Scene Column Layout
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Navigation Bar & Progress Indicator
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (onBack != null) {
                            Button(
                                onClick = onBack,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3E2723)),
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Text("← Worlds", color = Color(0xFFFFD700), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = script.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = guruGold
                            )
                            Text(
                                text = script.subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { currentBeatIndex = 0 }) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Restart Scene",
                                    tint = Color.White
                                )
                            }
                            Button(
                                onClick = onComplete,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E342E))
                            ) {
                                Text("Skip", color = Color.White)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    LinearProgressIndicator(
                        progress = { (currentBeatIndex + 1).toFloat() / totalBeats.toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = guruGold,
                        trackColor = Color(0xFF4E342E)
                    )
                }

                // Center Stage Area: Holographic Scroll Frame & Embedded MathSlate
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.72f)
                            .align(Alignment.Center),
                        contentAlignment = Alignment.Center
                    ) {
                        // Ambient Slate Hologram Glow FX
                        // TODO: replace with final art — fx_hologram_glow.xml
                        Image(
                            painter = painterResource(id = R.drawable.fx_hologram_glow),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(230.dp)
                                .alpha(hologramAlpha.value),
                            contentScale = ContentScale.FillBounds
                        )

                        // Slate Frame Prop Container
                        // TODO: replace with final art — prop_holographic_slate.jpg
                        Image(
                            painter = painterResource(id = R.drawable.prop_holographic_slate),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.FillBounds
                        )

                        // Embedded Animated MathSlate
                        MathSlate(
                            problem = problem,
                            stepIndex = currentBeat.slateStepIndex,
                            interactiveMode = false,
                            modifier = Modifier.padding(12.dp)
                        )
                    }

                    // Victory Particle Burst FX (Triggered on Final Step)
                    GurukulParticleEffect(
                        trigger = currentBeat.slateStepIndex == 5,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Anchored Dialogue Caption Box
                Column(modifier = Modifier.fillMaxWidth()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xDD3E2723)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Guru:",
                                    fontWeight = FontWeight.Bold,
                                    color = guruGold,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = currentBeat.guruText,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    lineHeight = 19.sp
                                )
                            }

                            currentBeat.discipleText?.let { discipleLine ->
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Ghost:",
                                        fontWeight = FontWeight.Bold,
                                        color = discipleCyan,
                                        fontSize = 14.sp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = discipleLine,
                                        color = Color(0xFFE0F7FA),
                                        fontSize = 14.sp,
                                        lineHeight = 19.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tap anywhere to advance",
                            fontSize = 12.sp,
                            color = Color(0xFFEEEEEE)
                        )

                        if (currentBeatIndex == totalBeats - 1) {
                            Button(
                                onClick = onComplete,
                                colors = ButtonDefaults.buttonColors(containerColor = guruGold)
                            ) {
                                Text("Start Guided Practice", color = Color.Black, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
