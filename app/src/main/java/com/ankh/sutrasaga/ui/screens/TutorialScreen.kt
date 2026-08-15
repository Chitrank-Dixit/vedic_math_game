package com.ankh.sutrasaga.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ankh.sutrasaga.data.repository.GurukulContentRepository
import com.ankh.sutrasaga.ui.components.GurukulSceneScreen

@Composable
fun TutorialScreen(
    worldId: Int,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val script = GurukulContentRepository.getTutorialScript(worldId)
    val canonicalExampleProblem = canonicalOnboardingProblem(worldId)

    GurukulSceneScreen(
        script = script,
        problem = canonicalExampleProblem,
        onComplete = onComplete,
        modifier = modifier
    )
}
