package com.ankh.sutrasaga.ui

import com.ankh.sutrasaga.domain.models.DiscipleState
import com.ankh.sutrasaga.domain.models.GuruPose
import com.ankh.sutrasaga.ui.navigation.VedicAppRoute
import com.ankh.sutrasaga.ui.screens.GurukulScriptsRepository
import com.ankh.sutrasaga.ui.screens.SampleSutraModules
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GurukulNarrativeTutorialTest {

    @Test
    fun testAll16SutraGurukulScriptsExistAndAreComplete() {
        val modules = SampleSutraModules
        assertEquals(16, modules.size)

        for (module in modules) {
            val script = GurukulScriptsRepository.getScriptForModule(module)
            assertNotNull("Script for module ${module.id} should not be null", script)
            assertTrue("Script for ${module.id} should have title", script.title.isNotEmpty())
            assertTrue("Script for ${module.id} should have subtitle", script.subtitle.isNotEmpty())
            assertTrue("Script for ${module.id} must contain dialogue beats", script.beats.isNotEmpty())
            assertTrue("Script for ${module.id} must have at least 3 beats", script.beats.size >= 3)
        }
    }

    @Test
    fun testGurukulScriptBeatsHaveValidDialogueAndCharacterStates() {
        for ((title, script) in GurukulScriptsRepository.allScripts) {
            assertTrue("Script title should be non-empty", title.isNotEmpty())
            assertTrue("World ID should be between 1 and 16", script.worldId in 1..16)

            script.beats.forEachIndexed { index, beat ->
                assertTrue(
                    "Guru text in $title beat $index must not be blank",
                    beat.guruText.isNotBlank()
                )
                assertNotNull("Guru pose should be valid in $title beat $index", beat.guruPose)
                assertNotNull("Guru mouth should be valid in $title beat $index", beat.guruMouth)
                assertNotNull("Disciple state should be valid in $title beat $index", beat.discipleState)
                assertNotNull("Disciple eyes should be valid in $title beat $index", beat.discipleEyes)
                assertTrue("Slate step index should be non-negative", beat.slateStepIndex >= 0)
            }
        }
    }

    @Test
    fun testProblemGenerationForGurukulTutorial() {
        val modules = SampleSutraModules
        for (module in modules) {
            val problem = GurukulScriptsRepository.getProblemForModule(module)
            assertNotNull("Problem for ${module.id} must not be null", problem)
            assertTrue("Problem for ${module.id} must have question text", problem.questionText.isNotEmpty())
            assertTrue("Problem for ${module.id} must have non-empty decomposition steps", problem.decompositionSteps.isNotEmpty())
        }
    }

    @Test
    fun testVedicAppRoutesIncludeGurukulTutorial() {
        val routes = VedicAppRoute.values()
        assertTrue(routes.contains(VedicAppRoute.GURUKUL_TUTORIAL))
        assertTrue(routes.contains(VedicAppRoute.HOME))
        assertTrue(routes.contains(VedicAppRoute.SUTRA_SOLVER))
        assertTrue(routes.contains(VedicAppRoute.PRACTICE_ARENA))
        assertTrue(routes.contains(VedicAppRoute.UPA_SUTRA_TREASURY))
        assertTrue(routes.contains(VedicAppRoute.UPA_SUTRA_CODEX))
        assertTrue(routes.contains(VedicAppRoute.UPA_SUTRA_QUEST))
    }
}
