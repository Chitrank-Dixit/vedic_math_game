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
    fun testAll16SutraLessonsAreDistinctAndComplete() {
        val modules = SampleSutraModules
        assertEquals(16, modules.size)

        val retrievedLessons = modules.map { module ->
            val lesson = com.ankh.sutrasaga.ui.screens.SutraLessonsRepository.getLessonForModule(module)
            assertEquals(module.id, lesson.id)
            assertTrue(lesson.sanskritTitle.isNotEmpty())
            assertTrue(lesson.englishTitle.isNotEmpty())
            assertTrue(lesson.shloka.isNotEmpty())
            assertTrue(lesson.shortcutRule.isNotEmpty())
            assertTrue(lesson.primaryEquation.isNotEmpty())
            assertTrue(lesson.targetAnswer.isNotEmpty())
            assertTrue("Lesson ${lesson.id} must have decomposition steps", lesson.steps.isNotEmpty())
            lesson
        }

        // Assert all 16 lessons have unique equations and titles
        val uniqueLessonIds = retrievedLessons.map { it.id }.toSet()
        assertEquals(16, uniqueLessonIds.size)

        val uniqueEquations = retrievedLessons.map { it.primaryEquation }.toSet()
        assertEquals(16, uniqueEquations.size)
    }
}
