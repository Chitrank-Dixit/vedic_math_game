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
