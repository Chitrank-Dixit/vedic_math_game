package com.ankh.sutrasaga.ui

import com.ankh.sutrasaga.ui.navigation.VedicAppRoute
import com.ankh.sutrasaga.ui.screens.SampleArenaProblems
import com.ankh.sutrasaga.ui.screens.SampleSutraModules
import com.ankh.sutrasaga.ui.screens.SampleUrdhvaLesson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class Phase3ScreensTest {

    @Test
    fun testSampleSutraModulesCategorization() {
        val modules = SampleSutraModules
        assertEquals(16, modules.size)

        val categories = modules.groupBy { it.category }
        assertEquals(5, categories.keys.size)
        assertTrue(categories.containsKey("Multiplication Shortcuts"))
        assertTrue(categories.containsKey("Squaring & Cubing"))
        assertTrue(categories.containsKey("Division & Reciprocals"))
        assertTrue(categories.containsKey("Algebra & Linear Systems"))
        assertTrue(categories.containsKey("Factorization & Verification"))

        val urdhva = modules.first { it.id == "urdhva" }
        assertEquals("ऊर्ध्व तिर्यग्भ्याम्", urdhva.sanskritTitle)
        assertEquals(1.0f, urdhva.progress, 0.01f)
    }

    @Test
    fun testSampleUrdhvaLessonModeling() {
        val lesson = SampleUrdhvaLesson
        assertEquals("ऊर्ध्व तिर्यग्भ्याम्", lesson.sanskritTitle)
        assertEquals("23 × 14", lesson.primaryEquation)
        assertEquals("322", lesson.targetAnswer)
        assertEquals(3, lesson.steps.size)
        assertEquals(2, lesson.carries.size)
    }

    @Test
    fun testSampleArenaProblems() {
        val problems = SampleArenaProblems
        assertTrue(problems.size >= 5)
        assertEquals("65²", problems[0].equation)
        assertEquals("4225", problems[0].targetAnswer)
    }

    @Test
    fun testVedicAppRouteEnum() {
        assertEquals(5, VedicAppRoute.entries.size)
        assertNotNull(VedicAppRoute.HOME)
        assertNotNull(VedicAppRoute.SUTRA_SOLVER)
        assertNotNull(VedicAppRoute.PRACTICE_ARENA)
        assertNotNull(VedicAppRoute.UPA_SUTRA_TREASURY)
        assertNotNull(VedicAppRoute.UPA_SUTRA_CODEX)
    }
}
