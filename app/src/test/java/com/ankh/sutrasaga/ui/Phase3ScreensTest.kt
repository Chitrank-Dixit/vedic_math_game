package com.ankh.sutrasaga.ui

import com.ankh.sutrasaga.ui.navigation.VedicAppRoute
import com.ankh.sutrasaga.ui.screens.SampleArenaProblems
import com.ankh.sutrasaga.ui.screens.SampleSutraModules
import com.ankh.sutrasaga.ui.screens.SampleUrdhvaLesson
import com.ankh.sutrasaga.ui.screens.SutraLessonsRepository
import com.ankh.sutrasaga.ui.screens.validateSolverStep
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
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

    @Test
    fun testVedicAppRouteDefinitions() {
        val expectedRoutes = setOf(
            VedicAppRoute.HOME,
            VedicAppRoute.GURUKUL_TUTORIAL,
            VedicAppRoute.SUTRA_SOLVER,
            VedicAppRoute.PRACTICE_ARENA,
            VedicAppRoute.UPA_SUTRA_TREASURY,
            VedicAppRoute.UPA_SUTRA_CODEX,
            VedicAppRoute.UPA_SUTRA_QUEST
        )
        assertEquals(expectedRoutes, VedicAppRoute.values().toSet())
    }

    @Test
    fun testValidateSolverStepRejectsIncorrectAndEmptySubmissions() {
        val lesson = SampleUrdhvaLesson // primaryEquation = "23 × 14", targetAnswer = "322"
        // Steps:
        // 0: "3 × 4" -> stepResult = "12 (write 2, carry 1)"
        // 1: "2(4) + 3(1) + 1" -> stepResult = "12 (write 2, carry 1)"
        // 2: "2(1) + 1" -> stepResult = "3"

        // Empty / blank input must always be rejected
        assertFalse("Blank input must be rejected", validateSolverStep(lesson, 0, ""))
        assertFalse("Whitespace input must be rejected", validateSolverStep(lesson, 0, "   "))

        // Step 0
        assertTrue("Step 0 accepts 12", validateSolverStep(lesson, 0, "12"))
        assertTrue("Step 0 accepts units digit 2", validateSolverStep(lesson, 0, "2"))
        assertFalse("Step 0 rejects arbitrary digit 9", validateSolverStep(lesson, 0, "9"))
        assertFalse("Step 0 rejects final target answer 322", validateSolverStep(lesson, 0, "322"))

        // Step 1
        assertTrue("Step 1 accepts 12", validateSolverStep(lesson, 1, "12"))
        assertTrue("Step 1 accepts units digit 2", validateSolverStep(lesson, 1, "2"))
        assertFalse("Step 1 rejects arbitrary digit 5", validateSolverStep(lesson, 1, "5"))
        assertFalse("Step 1 rejects wrong answer 42", validateSolverStep(lesson, 1, "42"))

        // Step 2 (Final step)
        assertTrue("Final step accepts step result 3", validateSolverStep(lesson, 2, "3"))
        assertTrue("Final step accepts full targetAnswer 322", validateSolverStep(lesson, 2, "322"))
        assertFalse("Final step rejects arbitrary single digit 7", validateSolverStep(lesson, 2, "7"))
        assertFalse("Final step rejects wrong number 999", validateSolverStep(lesson, 2, "999"))
    }

    @Test
    fun testValidateSolverStepWithAll16Lessons() {
        for ((_, lesson) in SutraLessonsRepository.allLessons) {
            val finalIndex = lesson.steps.size - 1

            // Blank input must fail on every step
            for (i in lesson.steps.indices) {
                assertFalse("Lesson ${lesson.id} step $i must reject blank", validateSolverStep(lesson, i, ""))
                assertFalse("Lesson ${lesson.id} step $i must reject arbitrary wrong answer 99999", validateSolverStep(lesson, i, "99999"))
            }

            // Final step must accept targetAnswer
            assertTrue(
                "Lesson ${lesson.id} final step must accept targetAnswer '${lesson.targetAnswer}'",
                validateSolverStep(lesson, finalIndex, lesson.targetAnswer)
            )

            // Final step must reject an incorrect answer
            val wrongTarget = if (lesson.targetAnswer == "0") "9" else "0"
            assertFalse(
                "Lesson ${lesson.id} final step must reject wrong answer '$wrongTarget'",
                validateSolverStep(lesson, finalIndex, wrongTarget)
            )
        }
    }
}

