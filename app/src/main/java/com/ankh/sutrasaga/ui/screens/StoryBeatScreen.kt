package com.ankh.sutrasaga.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ankh.sutrasaga.data.repository.GurukulContentRepository
import com.ankh.sutrasaga.domain.models.SutraProblem
import com.ankh.sutrasaga.engine.AnurupyeShunyamanyatGenerator
import com.ankh.sutrasaga.engine.EkadhikenaPurvenaGenerator
import com.ankh.sutrasaga.engine.EkanyunenaGenerator
import com.ankh.sutrasaga.engine.NikhilamGenerator
import com.ankh.sutrasaga.engine.ParavartyaYojayetGenerator
import com.ankh.sutrasaga.engine.UrdhvaTiryagbhyamGenerator
import com.ankh.sutrasaga.engine.YavadunamGenerator
import com.ankh.sutrasaga.ui.components.GurukulSceneScreen

@Composable
fun StoryBeatScreen(
    worldId: Int,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val script = GurukulContentRepository.getStoryScript(worldId)
    val exampleProblem = canonicalOnboardingProblem(worldId)

    GurukulSceneScreen(
        script = script,
        problem = exampleProblem,
        onComplete = onContinueClick,
        modifier = modifier
    )
}

internal fun canonicalOnboardingProblem(worldId: Int): SutraProblem = when (worldId) {
    1 -> EkadhikenaPurvenaGenerator().generateSpecificProblem(65L)
    2 -> NikhilamGenerator().generateSpecificProblem(37L)
    3 -> EkanyunenaGenerator().generateSpecificProblem(47L)
    4 -> YavadunamGenerator().generateSpecificProblem(94L)
    5 -> UrdhvaTiryagbhyamGenerator().generateSpecificProblemPair(23L, 41L)
    6 -> ParavartyaYojayetGenerator().generateSpecificProblem(1225L)
    7 -> AnurupyeShunyamanyatGenerator().generateSpecificProblem(0L)
    else -> EkadhikenaPurvenaGenerator().generateSpecificProblem(65L)
}
