package com.ank.sutrasaga.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ank.sutrasaga.data.repository.GurukulContentRepository
import com.ank.sutrasaga.domain.models.SutraProblem
import com.ank.sutrasaga.engine.AnurupyeShunyamanyatGenerator
import com.ank.sutrasaga.engine.ChalanaKalanabhyamGenerator
import com.ank.sutrasaga.engine.EkadhikenaPurvenaGenerator
import com.ank.sutrasaga.engine.EkanyunenaGenerator
import com.ank.sutrasaga.engine.GunakasamuccayahGenerator
import com.ank.sutrasaga.engine.GunitasamuccayahGenerator
import com.ank.sutrasaga.engine.NikhilamGenerator
import com.ank.sutrasaga.engine.ParavartyaYojayetGenerator
import com.ank.sutrasaga.engine.PuranapuranabhyamGenerator
import com.ank.sutrasaga.engine.SankalanaVyavakalanabhyamGenerator
import com.ank.sutrasaga.engine.ShesanyankenaCharamenaGenerator
import com.ank.sutrasaga.engine.ShunyamSamyasamuccayeGenerator
import com.ank.sutrasaga.engine.SopantyadvayamantyamGenerator
import com.ank.sutrasaga.engine.UrdhvaTiryagbhyamGenerator
import com.ank.sutrasaga.engine.VyashtisamashtihGenerator
import com.ank.sutrasaga.engine.YavadunamGenerator
import com.ank.sutrasaga.ui.components.GurukulSceneScreen

/**
 * LEGACY COMPOSABLE — NOT WIRED INTO PRODUCTION NAVIGATION.
 *
 * StoryBeatScreen consumes GurukulContentRepository (also legacy) and is NOT
 * reachable via VedicAppNavigator. Learners never see this screen at runtime.
 *
 * Authoritative runtime tutorial composable: GurukulSceneScreen, invoked from
 * VedicAppNavigator (GURUKUL_TUTORIAL route) with content from GurukulScriptsRepository.
 */
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

/**
 * AUTHORITATIVE QUIZ PROBLEM FACTORY — shared utility used by tests and tutorial screens.
 *
 * Returns the canonical SutraProblem for a given worldId by delegating to each
 * world's dedicated engine generator. This is the source of truth for expected
 * quiz answers and decomposition steps used in tests such as WorldOnboardingProblemTest
 * and TutorialRuntimeContentPathTest.
 *
 * Note: GurukulScriptsRepository.getProblemForModule() delegates to SutraLessonsRepository
 * rather than this function. For tutorial-to-quiz cross-verification, compare both.
 */
internal fun canonicalOnboardingProblem(worldId: Int): SutraProblem = when (worldId) {
    1 -> EkadhikenaPurvenaGenerator().generateCanonicalTutorialProblem()
    2 -> NikhilamGenerator().generateCanonicalTutorialProblem()
    3 -> EkanyunenaGenerator().generateCanonicalTutorialProblem()
    4 -> YavadunamGenerator().generateCanonicalTutorialProblem()
    5 -> UrdhvaTiryagbhyamGenerator().generateCanonicalTutorialProblem()
    6 -> ParavartyaYojayetGenerator().generateCanonicalTutorialProblem()
    7 -> AnurupyeShunyamanyatGenerator().generateCanonicalTutorialProblem()
    8 -> SankalanaVyavakalanabhyamGenerator().generateCanonicalTutorialProblem()
    9 -> ShunyamSamyasamuccayeGenerator().generateCanonicalTutorialProblem()
    10 -> PuranapuranabhyamGenerator().generateCanonicalTutorialProblem()
    11 -> VyashtisamashtihGenerator().generateCanonicalTutorialProblem()
    12 -> ShesanyankenaCharamenaGenerator().generateCanonicalTutorialProblem()
    13 -> SopantyadvayamantyamGenerator().generateCanonicalTutorialProblem()
    14 -> GunitasamuccayahGenerator().generateCanonicalTutorialProblem()
    15 -> GunakasamuccayahGenerator().generateCanonicalTutorialProblem()
    16 -> ChalanaKalanabhyamGenerator().generateCanonicalTutorialProblem()
    else -> EkadhikenaPurvenaGenerator().generateSpecificProblem(65L)
}
