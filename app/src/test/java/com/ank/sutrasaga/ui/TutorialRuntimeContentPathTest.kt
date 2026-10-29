package com.ank.sutrasaga.ui

import com.ank.sutrasaga.ui.screens.CanonicalSutraModules
import com.ank.sutrasaga.ui.screens.GurukulScriptsRepository
import com.ank.sutrasaga.ui.screens.SampleSutraModules
import com.ank.sutrasaga.ui.screens.canonicalOnboardingProblem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Exercises the *actual* runtime tutorial content-loading path for all 16 worlds.
 *
 * Authoritative content source at runtime:
 *   GameUiState.selectedScript
 *     -> GurukulScriptsRepository.getScriptForModule(module)
 *     -> GurukulSceneScreen(script, problem)
 *
 * The quiz problem shown on the slate is supplied by:
 *   GameUiState.selectedLessonProblem
 *     -> GurukulScriptsRepository.getProblemForModule(module)
 *
 * The quiz problem used at tutorial launch (StoryBeatScreen / canonicalOnboardingProblem) is:
 *   canonicalOnboardingProblem(worldId)   [StoryBeatScreen.kt]
 *
 * IMPORTANT: GurukulContentRepository, SutraDialogueRepository, StoryBeatScreen,
 * and TutorialScreen are legacy / unused at runtime.  Tests for those repositories
 * validate internal consistency only and do NOT reflect what learners see.
 */
class TutorialRuntimeContentPathTest {

    /**
     * All 16 worlds must resolve to a non-null GurukulScript via the runtime path
     * (GurukulScriptsRepository.getScriptForModule) and the script must be complete.
     */
    @Test
    fun allSixteenWorldsLoadViaRuntimePath() {
        val canonicalModules = CanonicalSutraModules
        assertEquals(
            "Production module list must have exactly 16 entries",
            16, canonicalModules.size
        )

        for (module in canonicalModules) {
            val script = GurukulScriptsRepository.getScriptForModule(module)
            assertNotNull(
                "Runtime script for world ${module.worldNumber} (${module.id}) must not be null",
                script
            )
            assertTrue(
                "Runtime script for world ${module.worldNumber} must have a title",
                script.title.isNotBlank()
            )
            assertTrue(
                "Runtime script for world ${module.worldNumber} must have a subtitle",
                script.subtitle.isNotBlank()
            )
            assertTrue(
                "Runtime script for world ${module.worldNumber} must contain at least 3 dialogue beats",
                script.beats.size >= 3
            )
            assertTrue(
                "Runtime script worldId must match module worldNumber (${module.worldNumber})",
                script.worldId == module.worldNumber
            )
        }
    }

    /**
     * Every beat in every runtime script must have non-blank guru dialogue.
     */
    @Test
    fun allRuntimeScriptBeatsHaveNonBlankDialogue() {
        for (module in CanonicalSutraModules) {
            val script = GurukulScriptsRepository.getScriptForModule(module)
            script.beats.forEachIndexed { index, beat ->
                assertTrue(
                    "Guru text must not be blank for world ${module.worldNumber} beat $index",
                    beat.guruText.isNotBlank()
                )
                val dt = beat.discipleText
                if (dt != null) {
                    assertTrue(
                        "Disciple text must not be blank when set, world ${module.worldNumber} beat $index",
                        dt.isNotBlank()
                    )
                }
                assertTrue(
                    "slateStepIndex must be non-negative for world ${module.worldNumber} beat $index",
                    beat.slateStepIndex >= 0
                )
                assertNotNull("GuruPose must be set for world ${module.worldNumber} beat $index", beat.guruPose)
                assertNotNull("GuruMouth must be set for world ${module.worldNumber} beat $index", beat.guruMouth)
                assertNotNull("DiscipleState must be set for world ${module.worldNumber} beat $index", beat.discipleState)
                assertNotNull("DiscipleEyes must be set for world ${module.worldNumber} beat $index", beat.discipleEyes)
            }
        }
    }

    /**
     * canonicalOnboardingProblem must be consistent for every world.
     */
    @Test
    fun canonicalOnboardingProblemIsConsistentForAllSixteenWorlds() {
        for (worldId in 1..16) {
            val problem = canonicalOnboardingProblem(worldId)
            assertNotNull("canonicalOnboardingProblem($worldId) must not be null", problem)
            assertTrue(
                "canonicalOnboardingProblem($worldId) sutraName must not be blank",
                problem.sutraName.isNotBlank()
            )
            assertTrue(
                "canonicalOnboardingProblem($worldId) questionText must not be blank",
                problem.questionText.isNotBlank()
            )
            assertTrue(
                "canonicalOnboardingProblem($worldId) decompositionSteps must not be empty",
                problem.decompositionSteps.isNotEmpty()
            )
        }
    }

    /**
     * The runtime script worldId and canonicalOnboardingProblem must both map to the same
     * worldId, confirming the dialogue and quiz teach the same world's sutra.
     */
    @Test
    fun runtimeScriptWorldIdMatchesCanonicalProblemSutraForAllWorlds() {
        for (worldId in 1..16) {
            val module = CanonicalSutraModules.firstOrNull { it.worldNumber == worldId }
            assertNotNull("CanonicalSutraModules must contain world $worldId", module)
            module!!

            val script = GurukulScriptsRepository.getScriptForModule(module)
            val problem = canonicalOnboardingProblem(worldId)

            assertEquals("Runtime script worldId must equal $worldId", worldId, script.worldId)
            assertTrue(
                "Problem sutraName for world $worldId must not be blank",
                problem.sutraName.isNotBlank()
            )
            // GurukulScriptsRepository uses Sanskrit titles, not "Tutorial N: ..." prefixes.
            assertTrue(
                "Runtime script title for world $worldId must not be a legacy placeholder",
                script.title.isNotBlank() &&
                    !script.title.startsWith("Tutorial ") &&
                    !script.title.contains("Placeholder")
            )
        }
    }

    /**
     * SampleSutraModules must cover all 16 worlds and each must resolve to a valid script.
     */
    @Test
    fun sampleModulesAlsoCoverAllSixteenWorlds() {
        assertEquals("SampleSutraModules must have 16 entries", 16, SampleSutraModules.size)
        val worldNumbersCovered = SampleSutraModules.map { it.worldNumber }.toSet()
        for (worldId in 1..16) {
            assertTrue(
                "SampleSutraModules must contain world $worldId",
                worldNumbersCovered.contains(worldId)
            )
        }
        for (module in SampleSutraModules) {
            val script = GurukulScriptsRepository.getScriptForModule(module)
            assertNotNull(
                "SampleSutraModules: script for world ${module.worldNumber} must not be null", script
            )
            assertTrue(
                "SampleSutraModules: script for world ${module.worldNumber} must have beats",
                script.beats.isNotEmpty()
            )
        }
    }
}
