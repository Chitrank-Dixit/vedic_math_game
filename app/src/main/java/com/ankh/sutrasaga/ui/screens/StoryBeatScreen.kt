package com.ankh.sutrasaga.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ankh.sutrasaga.data.repository.GurukulContentRepository
import com.ankh.sutrasaga.domain.models.SutraProblem
import com.ankh.sutrasaga.engine.AnurupyeShunyamanyatGenerator
import com.ankh.sutrasaga.engine.ChalanaKalanabhyamGenerator
import com.ankh.sutrasaga.engine.EkadhikenaPurvenaGenerator
import com.ankh.sutrasaga.engine.EkanyunenaGenerator
import com.ankh.sutrasaga.engine.GunakasamuccayahGenerator
import com.ankh.sutrasaga.engine.GunitasamuccayahGenerator
import com.ankh.sutrasaga.engine.NikhilamGenerator
import com.ankh.sutrasaga.engine.ParavartyaYojayetGenerator
import com.ankh.sutrasaga.engine.PuranapuranabhyamGenerator
import com.ankh.sutrasaga.engine.SankalanaVyavakalanabhyamGenerator
import com.ankh.sutrasaga.engine.ShesanyankenaCharamenaGenerator
import com.ankh.sutrasaga.engine.ShunyamSamyasamuccayeGenerator
import com.ankh.sutrasaga.engine.SopantyadvayamantyamGenerator
import com.ankh.sutrasaga.engine.UrdhvaTiryagbhyamGenerator
import com.ankh.sutrasaga.engine.VyashtisamashtihGenerator
import com.ankh.sutrasaga.engine.YavadunamGenerator
import com.ankh.sutrasaga.ui.components.GurukulSceneScreen

@Composable
fun StoryBeatScreen(
    worldId: Int,
    onContinueClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val script = GurukulContentRepository.getStoryScript(worldId)
    val exampleProblem = canonicalOnboardingProblem(worldId)

    GurukulSceneScreen(
        script = script,
        problem = exampleProblem,
        onComplete = onContinueClick,
        onBack = onBackClick,
        modifier = modifier
    )
}

internal fun canonicalOnboardingProblem(worldId: Int): SutraProblem = when (worldId) {
    1 -> EkadhikenaPurvenaGenerator().generateCanonicalTutorialProblem()
    2 -> NikhilamGenerator().generateCanonicalTutorialProblem()
    3 -> EkanyunenaGenerator().generateCanonicalTutorialProblem()
    4 -> YavadunamGenerator().generateCanonicalTutorialProblem()
    5 -> UrdhvaTiryagbhyamGenerator().generateCanonicalTutorialProblem()
    6 -> ParavartyaYojayetGenerator().generateCanonicalTutorialProblem()
    7 -> AnurupyeShunyamanyatGenerator().generateCanonicalTutorialProblem()
    8 -> SankalanaVyavakalanabhyamGenerator().generateSpecificProblem(0L)
    9 -> ShunyamSamyasamuccayeGenerator().generateSpecificProblem(0L)
    10 -> PuranapuranabhyamGenerator().generateSpecificProblem(0L)
    11 -> VyashtisamashtihGenerator().generateSpecificProblem(0L)
    12 -> ShesanyankenaCharamenaGenerator().generateSpecificProblem(0L)
    13 -> SopantyadvayamantyamGenerator().generateSpecificProblem(0L)
    14 -> GunitasamuccayahGenerator().generateSpecificProblem(0L)
    15 -> GunakasamuccayahGenerator().generateSpecificProblem(0L)
    16 -> ChalanaKalanabhyamGenerator().generateSpecificProblem(0L)
    else -> EkadhikenaPurvenaGenerator().generateSpecificProblem(65L)
}
