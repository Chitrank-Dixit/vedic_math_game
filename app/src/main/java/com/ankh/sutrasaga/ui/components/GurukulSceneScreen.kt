package com.ankh.sutrasaga.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankh.sutrasaga.R
import com.ankh.sutrasaga.data.repository.RiveDialogueRepository
import com.ankh.sutrasaga.domain.models.GurukulScript
import com.ankh.sutrasaga.domain.models.RiveEmotion
import com.ankh.sutrasaga.domain.models.RiveSpeaker
import com.ankh.sutrasaga.domain.models.RiveSutraDialogueTree
import com.ankh.sutrasaga.domain.models.SutraProblem
import com.ankh.sutrasaga.domain.validation.VedicMathValidator
import com.ankh.sutrasaga.engine.rive.RiveDialogueController
import com.ankh.sutrasaga.ui.theme.CyberCyanLight
import com.ankh.sutrasaga.ui.theme.TextLightSecondary
import com.ankh.sutrasaga.ui.theme.TextMuted
import com.ankh.sutrasaga.ui.theme.TextWhitePrimary
import com.ankh.sutrasaga.ui.theme.VedicGold
import com.ankh.sutrasaga.ui.theme.VedicGoldLight
import com.ankh.sutrasaga.ui.util.MathFormatter

/**
 * GurukulSceneScreen — Duolingo-style interactive pre-quiz tutorial scene for Vedic Mathematics.
 *
 * Implements:
 * 1. Clean decoupling via RiveDialogueController and RiveAdapter.
 * 2. 4-Node dynamic Guru-Shishya dialogue tree (The Hook -> Sutra Reveal -> Spark of Insight -> Active Handshake).
 * 3. Clean mathematical overlay slate with token highlights.
 * 4. Interactive tap handshake on step 4 with retry and corrective feedback.
 */
@Composable
fun GurukulSceneScreen(
    script: GurukulScript? = null,
    problem: SutraProblem? = null,
    worldId: Int = script?.worldId ?: 1,
    dialogueTree: RiveSutraDialogueTree? = null,
    onComplete: () -> Unit,
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val repository = remember { RiveDialogueRepository(context) }
    val activeTree = dialogueTree ?: repository.getDialogueForWorld(worldId)

    // Fallback if asset tree isn't loaded
    if (activeTree == null) {
        LegacyGurukulSceneScreen(
            script = script ?: com.ankh.sutrasaga.ui.screens.GurukulScriptsRepository.allScripts.values.first(),
            problem = problem ?: com.ankh.sutrasaga.ui.screens.GurukulScriptsRepository.getProblemForModule(com.ankh.sutrasaga.ui.screens.SampleSutraModules.first()),
            onComplete = onComplete,
            onBack = onBack,
            modifier = modifier
        )
        return
    }

    val controller = remember(activeTree) { RiveDialogueController(activeTree) }

    val currentNodeIndex by controller.currentNodeIndex.collectAsState()
    val currentNode by controller.currentNode.collectAsState()
    val isCompleted by controller.isCompleted.collectAsState()
    val handshakeError by controller.handshakeError.collectAsState()
    val particleTriggered by controller.riveAdapter.particleTriggered.collectAsState()

    LaunchedEffect(isCompleted) {
        if (isCompleted) {
            onComplete()
        }
    }

    val totalNodes = controller.totalNodes

    val infiniteTransition = rememberInfiniteTransition(label = "GurukulSceneTransition")

    // Hint Tap Pulsing Animation
    val tapPromptAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "TapPrompt"
    )

    val isGuru = currentNode.speaker == RiveSpeaker.GURU
    val isHandshakeStep = currentNode.interactiveHandshake.requiresUserTap

    Surface(
        modifier = modifier
            .fillMaxSize()
            .clickable {
                if (!isHandshakeStep) {
                    controller.advanceNode()
                }
            },
        color = Color(0xFF0B1120)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // 1. Full-Screen Gurukul Background Artwork
            Image(
                painter = painterResource(id = R.drawable.bg_gurukul_scene),
                contentDescription = "Gurukul Scene Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Dark vignette overlay for readable contrast
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xCC0B1120),
                                Color(0x660B1120),
                                Color(0x550B1120),
                                Color(0xE60B1120)
                            )
                        )
                    )
            )

            // 2. Main Scene Layout
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Header Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xEA0F172A)),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(
                            listOf(Color(0x80FFB300), Color(0x4038BDF8), Color(0x80FFB300))
                        ),
                        width = 1.dp
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (onBack != null) {
                                Button(
                                    onClick = onBack,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                                    border = CardDefaults.outlinedCardBorder().copy(
                                        brush = Brush.horizontalGradient(listOf(VedicGold, VedicGoldLight)),
                                        width = 1.dp
                                    ),
                                    contentPadding = ButtonDefaults.TextButtonContentPadding,
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back",
                                        tint = VedicGold,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Worlds",
                                        color = VedicGold,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 6.dp)
                            ) {
                                Text(
                                    text = activeTree.sutraName,
                                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.sp),
                                    fontWeight = FontWeight.ExtraBold,
                                    color = VedicGoldLight,
                                    maxLines = 1
                                )
                                Text(
                                    text = "World ${activeTree.worldNumber} · ${activeTree.englishMeaning}",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, lineHeight = 14.sp),
                                    color = TextLightSecondary,
                                    maxLines = 2,
                                    softWrap = true
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                IconButton(
                                    onClick = { controller.restart() },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Restart Scene",
                                        tint = TextMuted,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Button(
                                    onClick = { controller.skip() },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                                    contentPadding = ButtonDefaults.TextButtonContentPadding
                                ) {
                                    Text(
                                        text = "Skip ⏭",
                                        color = TextWhitePrimary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = { (currentNodeIndex + 1).toFloat() / totalNodes.toFloat() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = VedicGold,
                            trackColor = Color(0xFF334155)
                        )
                    }
                }

                // Center Stage: Holographic Math Slate Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.95f),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xF00A0F1D)),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.radialGradient(
                                listOf(
                                    Color(0xFF38BDF8),
                                    Color(0xFF0284C7),
                                    Color(0xFF0F172A)
                                )
                            ),
                            width = 1.5.dp
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            // Slate Header Tag
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "✦ VEDIC MATH SLATE ✦",
                                    color = CyberCyanLight,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Dynamic Clean Math Expression
                            val cleanExpression = MathFormatter.format(currentNode.mathOverlay.expression)
                            Crossfade(
                                targetState = cleanExpression,
                                label = "MathOverlayExpressionCrossfade"
                            ) { expression ->
                                Text(
                                    text = expression,
                                    color = VedicGoldLight,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Default,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                            }

                            // Highlighted Tokens (Cleanly Formatted)
                            if (currentNode.mathOverlay.highlightTokens.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    currentNode.mathOverlay.highlightTokens.forEach { rawToken ->
                                        val cleanToken = MathFormatter.format(rawToken)
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color(0x33FFB300))
                                                .border(1.dp, VedicGold.copy(alpha = 0.7f), RoundedCornerShape(8.dp))
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = cleanToken,
                                                color = VedicGoldLight,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Victory particle FX on eureka/handshake node
                    GurukulParticleEffect(
                        trigger = particleTriggered,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Bottom Section: Dialogue Speech Card & Interactive Handshake
                Column(modifier = Modifier.fillMaxWidth()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isGuru) Color(0xF2131C31) else Color(0xF20F243A)
                        ),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.horizontalGradient(
                                if (isGuru) listOf(Color(0xFFFFB300), Color(0x66FFB300))
                                else listOf(Color(0xFF00E5FF), Color(0x6600E5FF))
                            ),
                            width = 1.5.dp
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            // Speaker Badge & Emotion Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isGuru) Color(0x4DFFB300) else Color(0x4D00E5FF))
                                        .border(
                                            1.dp,
                                            if (isGuru) VedicGoldLight else CyberCyanLight,
                                            RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = if (isGuru) "👑 GURU" else "🙏 SHISHYA",
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (isGuru) VedicGoldLight else CyberCyanLight,
                                        fontSize = 12.sp,
                                        letterSpacing = 0.5.sp
                                    )
                                }

                                // Emotion & Audio Cue Pill
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    val emotionLabel = when (currentNode.riveState.emotion) {
                                        RiveEmotion.HAPPY -> "✨ Joy"
                                        RiveEmotion.THINKING -> "🤔 Ponder"
                                        RiveEmotion.SURPRISED -> "💡 Eureka"
                                        RiveEmotion.NEUTRAL -> "🧘 Calm"
                                    }
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Color(0x33334155))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = emotionLabel,
                                            color = Color(0xFF94A3B8),
                                            fontSize = 10.sp
                                        )
                                    }
                                    Text(
                                        text = "🔊",
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Dialogue Text
                            Crossfade(targetState = currentNode.text, label = "DialogueTextCrossfade") { text ->
                                Text(
                                    text = text,
                                    color = TextWhitePrimary,
                                    fontSize = 15.sp,
                                    lineHeight = 22.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            }

                            // Corrective feedback error banner (if any)
                            AnimatedVisibility(visible = handshakeError != null, enter = fadeIn(), exit = fadeOut()) {
                                handshakeError?.let { err ->
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "⚠️ $err",
                                        color = Color(0xFFFBBF24),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            // Interactive Handshake Tap Section (Node 4)
                            if (isHandshakeStep) {
                                Spacer(modifier = Modifier.height(14.dp))
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                                    border = CardDefaults.outlinedCardBorder().copy(
                                        brush = Brush.horizontalGradient(listOf(Color(0xFF34D399), Color(0xFF10B981))),
                                        width = 1.dp
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "👉 ${currentNode.interactiveHandshake.promptText ?: "Tap to proceed"}",
                                            color = Color(0xFFD1FAE5),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            modifier = Modifier.weight(1f)
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))

                                        val expectedAnswer = currentNode.interactiveHandshake.expectedAnswer
                                            ?: VedicMathValidator.inferExpectedAnswer(currentNode.interactiveHandshake)
                                            ?: "5"

                                        Button(
                                            onClick = { controller.submitHandshake(expectedAnswer) },
                                            shape = RoundedCornerShape(12.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                                        ) {
                                            Text(
                                                text = "Begin ⚡",
                                                color = Color(0xFF064E3B),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Bottom Navigation Indicator Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (!isHandshakeStep) {
                            Text(
                                text = "Tap anywhere to continue ▹",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = VedicGoldLight.copy(alpha = tapPromptAlpha),
                                letterSpacing = 0.5.sp
                            )
                        } else {
                            Text(
                                text = "Complete the interactive handshake above 👆",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF34D399).copy(alpha = tapPromptAlpha),
                                letterSpacing = 0.5.sp
                            )
                        }

                        Text(
                            text = "Node ${currentNodeIndex + 1} of $totalNodes",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Fallback Legacy Gurukul Scene Composable if assets cannot be resolved.
 */
@Composable
private fun LegacyGurukulSceneScreen(
    script: GurukulScript,
    problem: SutraProblem,
    onComplete: () -> Unit,
    onBack: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .clickable { onComplete() },
        color = Color(0xFF0B1120)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = script.title,
                style = MaterialTheme.typography.titleLarge,
                color = VedicGoldLight
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xF20F172A))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Practice lesson for ${script.title}",
                        color = TextWhitePrimary,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
