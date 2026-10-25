package com.ankh.sutrasaga.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ankh.sutrasaga.data.repository.GurukulContentRepository
import com.ankh.sutrasaga.ui.components.GurukulSceneScreen

/**
 * LEGACY COMPOSABLE — NOT WIRED INTO PRODUCTION NAVIGATION.
 *
 * TutorialScreen consumes GurukulContentRepository (also legacy) and is NOT
 * reachable via VedicAppNavigator. Learners never see this screen at runtime.
 *
 * Authoritative runtime tutorial composable: GurukulSceneScreen, invoked from
 * VedicAppNavigator (GURUKUL_TUTORIAL route) with content from GurukulScriptsRepository.
 */
@Composable
fun TutorialScreen(
    worldId: Int,
    onComplete: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val script = GurukulContentRepository.getTutorialScript(worldId)
    val canonicalExampleProblem = canonicalOnboardingProblem(worldId)

    GurukulSceneScreen(
        script = script,
        problem = canonicalExampleProblem,
        onComplete = onComplete,
        onBack = onBackClick,
        modifier = modifier
    )
}
