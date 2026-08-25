package com.ankh.sutrasaga.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankh.sutrasaga.R
import com.ankh.sutrasaga.domain.models.GuruPose
import com.ankh.sutrasaga.domain.models.GurukulDialogueBeat
import com.ankh.sutrasaga.ui.theme.CyberCyan
import com.ankh.sutrasaga.ui.theme.CyberCyanLight
import com.ankh.sutrasaga.ui.theme.VedicGold
import com.ankh.sutrasaga.ui.theme.VedicGoldLight

/**
 * GurukulAvatarStage — Animated character presentation stage for Guru & Disciple (Shishya).
 *
 * Features:
 * 1. Procedural breathing and floating bob animations for life-like presence.
 * 2. Active speaker aura glow indicators (Gold for Guru, Cyan for Shishya).
 * 3. Procedural speech-to-viseme mouth shape oscillation for dynamic lip-syncing.
 * 4. Dynamic layered pose & facial expression switching (mouths, eyes).
 * 5. Prepared interface contract for future 3D glTF avatar drop-in via Google Filament / Sceneview.
 */
@Composable
fun GurukulAvatarStage(
    beat: GurukulDialogueBeat,
    isThinking: Boolean = false,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "AvatarBreathingTransition")

    // Guru breathing elevation animation
    val guruBreathOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (isThinking) -2f else -5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "GuruBreath"
    )

    // Disciple levitation / bobbing animation
    val discipleBobOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "DiscipleBob"
    )

    // Speaking aura pulse animation
    val auraPulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "AuraPulse"
    )

    // Procedural viseme lip-sync cycle during active speech
    val visemeCycle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(220, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "VisemeLipSync"
    )

    val isGuruSpeaking = beat.guruText.isNotBlank() && !isThinking
    val isDiscipleSpeaking = !beat.discipleText.isNullOrBlank() && !isThinking

    // Determine dynamic mouth drawable for Guru
    val activeGuruMouthRes = if (isGuruSpeaking && visemeCycle > 0.45f) {
        beat.guruMouth.drawableResId
    } else {
        R.drawable.guru_mouth_neutral
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        // --- Left Avatar: Guru Sage ---
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .offset(y = guruBreathOffset.dp),
                contentAlignment = Alignment.Center
            ) {
                // Speaker Aura Ring
                if (isGuruSpeaking) {
                    Box(
                        modifier = Modifier
                            .size(92.dp)
                            .scale(auraPulse)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(Color(0x66FFB300), Color(0x11FFB300), Color.Transparent)
                                )
                            )
                            .border(1.5.dp, VedicGold.copy(alpha = 0.6f), CircleShape)
                    )
                }

                // Layered Character (Base Pose + Mouth Overlay)
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(0xCC0F172A))
                        .border(1.5.dp, if (isGuruSpeaking) VedicGoldLight else Color(0x33FFB300), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Crossfade(targetState = beat.guruPose.drawableResId, label = "GuruPoseCrossfade") { resId ->
                        Image(
                            painter = painterResource(id = resId),
                            contentDescription = "Guru Sage",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // Dynamic Procedural Viseme Mouth Overlay
                    Image(
                        painter = painterResource(id = activeGuruMouthRes),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Speaker Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isGuruSpeaking) Color(0x4DFFB300) else Color(0x221E293B))
                    .border(1.dp, if (isGuruSpeaking) VedicGoldLight else Color(0x3364748B), RoundedCornerShape(6.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "👑 GURU",
                    fontWeight = FontWeight.Bold,
                    color = if (isGuruSpeaking) VedicGoldLight else Color(0xFF94A3B8),
                    fontSize = 10.sp
                )
            }
        }

        // --- Center Holographic Mandala Emblem ---
        Box(
            modifier = Modifier
                .size(42.dp)
                .padding(bottom = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "⚡",
                fontSize = 20.sp,
                color = CyberCyanLight
            )
        }

        // --- Right Avatar: Disciple (Shishya) ---
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .offset(y = discipleBobOffset.dp),
                contentAlignment = Alignment.Center
            ) {
                // Speaker Aura Ring
                if (isDiscipleSpeaking) {
                    Box(
                        modifier = Modifier
                            .size(92.dp)
                            .scale(auraPulse)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(Color(0x6600E5FF), Color(0x1100E5FF), Color.Transparent)
                                )
                            )
                            .border(1.5.dp, CyberCyan.copy(alpha = 0.6f), CircleShape)
                    )
                }

                // Layered Character (State Pose + Eye Overlay)
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(0xCC0F172A))
                        .border(1.5.dp, if (isDiscipleSpeaking) CyberCyanLight else Color(0x3300E5FF), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Crossfade(targetState = beat.discipleState.drawableResId, label = "DisciplePoseCrossfade") { resId ->
                        Image(
                            painter = painterResource(id = resId),
                            contentDescription = "Disciple Shishya",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // Eyes Expression Overlay
                    Image(
                        painter = painterResource(id = beat.discipleEyes.drawableResId),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Speaker Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isDiscipleSpeaking) Color(0x4D00E5FF) else Color(0x221E293B))
                    .border(1.dp, if (isDiscipleSpeaking) CyberCyanLight else Color(0x3364748B), RoundedCornerShape(6.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "🙏 SHISHYA",
                    fontWeight = FontWeight.Bold,
                    color = if (isDiscipleSpeaking) CyberCyanLight else Color(0xFF94A3B8),
                    fontSize = 10.sp
                )
            }
        }
    }
}

/**
 * GuruGuidanceBanner — Non-punitive, supportive character feedback pill for active problem solving.
 *
 * Implements psychological safety & Peak-End habit formation:
 * - Employs supportive character hints instead of jarring error buzzers.
 * - Displays active sub-step scaffolding and cheerful reinforcement.
 */
@Composable
fun GuruGuidanceBanner(
    hintText: String,
    isEncouragement: Boolean = false,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isEncouragement) Color(0xF0064E3B) else Color(0xF00F172A)
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                if (isEncouragement) listOf(Color(0xFF34D399), Color(0xFF10B981))
                else listOf(Color(0x80FFB300), Color(0x4038BDF8))
            ),
            width = 1.dp
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0x33FFB300))
                    .border(1.dp, VedicGold, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.guru_base_pose),
                    contentDescription = "Guru Guide",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = hintText,
                color = if (isEncouragement) Color(0xFFD1FAE5) else Color(0xFFF1F5F9),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 16.sp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
