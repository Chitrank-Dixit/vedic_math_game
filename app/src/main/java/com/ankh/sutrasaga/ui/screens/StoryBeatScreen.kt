package com.ankh.sutrasaga.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ankh.sutrasaga.data.repository.GurukulContentRepository
import com.ankh.sutrasaga.engine.EkadhikenaPurvenaGenerator
import com.ankh.sutrasaga.ui.components.GurukulSceneScreen

@Composable
fun StoryBeatScreen(
    worldId: Int,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val script = GurukulContentRepository.getStoryScript(worldId)
    val exampleProblem = EkadhikenaPurvenaGenerator().generateSpecificProblem(65L)

    GurukulSceneScreen(
        script = script,
        problem = exampleProblem,
        onComplete = onContinueClick,
        modifier = modifier
    )
}
