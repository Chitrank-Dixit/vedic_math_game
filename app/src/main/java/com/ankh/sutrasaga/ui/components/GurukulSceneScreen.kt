package com.ankh.sutrasaga.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankh.sutrasaga.R
import com.ankh.sutrasaga.data.repository.RiveDialogueRepository
import com.ankh.sutrasaga.domain.models.GurukulScript
import com.ankh.sutrasaga.domain.models.RiveDialogueNode
import com.ankh.sutrasaga.domain.models.RiveSutraDialogueTree
import com.ankh.sutrasaga.domain.models.SutraProblem
import com.ankh.sutrasaga.domain.validation.VedicMathValidator
import com.ankh.sutrasaga.engine.rive.RiveDialogueController
import com.ankh.sutrasaga.ui.theme.VedicParchmentTokens

/**
 * GurukulSceneScreen — Redesigned Indian parchment educational scene for Vedic Mathematics.
 * Inspired by modern character-driven learning apps (Duolingo) and the approved visual design.
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
    var isHandshakeSuccessful by remember { mutableStateOf(false) }

    val totalNodes = controller.totalNodes
    val isHandshakeStep = currentNode.interactiveHandshake.requiresUserTap

    val infiniteTransition = rememberInfiniteTransition(label = "GurukulParchmentTransition")
    val tapPromptAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "TapPromptAlpha"
    )

    Surface(
        modifier = modifier
            .fillMaxSize()
            .clickable {
                if (!isHandshakeStep) {
                    controller.advanceNode()
                }
            },
        color = VedicParchmentTokens.ParchmentBase
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // 1. Full-Screen Indian Manuscript Parchment Background
            Image(
                painter = painterResource(id = R.drawable.bg_parchment_manuscript),
                contentDescription = "Indian Learning Manuscript Parchment",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // 2. Main Scene Layout
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Header Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = VedicParchmentTokens.ParchmentCardBg
                    ),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE2D6C3)),
                        width = 1.dp
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (onBack != null) {
                                Button(
                                    onClick = onBack,
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFF3E8D6)
                                    ),
                                    border = CardDefaults.outlinedCardBorder().copy(
                                        brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFD4C3A3)),
                                        width = 1.dp
                                    ),
                                    contentPadding = ButtonDefaults.TextButtonContentPadding,
                                    modifier = Modifier.padding(end = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back to Worlds",
                                        tint = VedicParchmentTokens.InkDark,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Worlds",
                                        color = VedicParchmentTokens.InkDark,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = activeTree.sutraName,
                                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.sp),
                                    fontWeight = FontWeight.ExtraBold,
                                    color = VedicParchmentTokens.InkDark,
                                    maxLines = 1,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "World ${activeTree.worldNumber} • ${activeTree.englishMeaning}",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, lineHeight = 13.sp),
                                    color = VedicParchmentTokens.InkLight,
                                    maxLines = 2,
                                    textAlign = TextAlign.Center,
                                    softWrap = true
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                IconButton(
                                    onClick = {
                                        isHandshakeSuccessful = false
                                        controller.restart()
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Restart Scene",
                                        tint = VedicParchmentTokens.InkMuted,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Button(
                                    onClick = { onComplete() },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFF3E8D6)
                                    ),
                                    border = CardDefaults.outlinedCardBorder().copy(
                                        brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFD4C3A3)),
                                        width = 1.dp
                                    ),
                                    contentPadding = ButtonDefaults.TextButtonContentPadding
                                ) {
                                    Text(
                                        text = "Skip ⏭",
                                        color = VedicParchmentTokens.InkDark,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // 4-Step Milestone Progress Bar
                        ParchmentStepMilestones(
                            currentStepIndex = currentNodeIndex,
                            totalSteps = totalNodes
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Wooden Mathematical Slate Demonstration
                WoodenMathSlate(
                    expression = currentNode.mathOverlay.expression,
                    highlightTokens = currentNode.mathOverlay.highlightTokens
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Character Stage (Shishya on Left, Speech Bubble Center, Guru on Right)
                GurukulCharacterStage(
                    currentNode = currentNode,
                    isHandshakeSuccessful = isHandshakeSuccessful
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Bottom Handshake Mini-Game ("Let's try it!") or Tap Prompt
                if (isHandshakeStep) {
                    val expectedAnswer = currentNode.interactiveHandshake.expectedAnswer
                        ?: VedicMathValidator.inferExpectedAnswer(currentNode.interactiveHandshake)

                    InteractiveHandshakeTileGroup(
                        handshake = currentNode.interactiveHandshake,
                        expectedAnswer = expectedAnswer,
                        onAnswerSelected = { userAnswer ->
                            val success = controller.submitHandshake(userAnswer)
                            isHandshakeSuccessful = success
                            success
                        },
                        onContinueClick = { onComplete() }
                    )
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tap anywhere to continue ▹",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = VedicParchmentTokens.SaffronDark.copy(alpha = tapPromptAlpha),
                            letterSpacing = 0.5.sp
                        )

                        Text(
                            text = "Step ${currentNodeIndex + 1} of $totalNodes",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = VedicParchmentTokens.InkMuted
                        )
                    }
                }
            }
        }
    }
}

/**
 * Fallback Legacy Gurukul Scene Composable.
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
        color = VedicParchmentTokens.ParchmentBase
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
                color = VedicParchmentTokens.InkDark
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = VedicParchmentTokens.ParchmentCardBg)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Practice lesson for ${script.title}",
                        color = VedicParchmentTokens.InkDark,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
