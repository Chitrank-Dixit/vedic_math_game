package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.domain.models.DifficultyTier
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class YavadunamGeneratorTest {

    private lateinit var generator: YavadunamGenerator

    @Before
    fun setUp() {
        generator = YavadunamGenerator()
    }

    @Test
    fun testCanonicalWorkedExample94Squared() {
        val problem = generator.generateSpecificProblem(94L)
        assertEquals(94L, problem.operand)
        assertEquals(8836L, problem.correctAnswer)

        val steps = problem.decompositionSteps
        assertEquals(4, steps.size)
        assertEquals("6", steps[0].stepResult) // Deficiency D = 6
        assertEquals("88", steps[1].stepResult) // LHS = 94 - 6 = 88
        assertEquals("36", steps[2].stepResult) // RHS = 6² = 36
        assertEquals("8836", steps[3].stepResult)
    }

    @Test
    fun testCanonicalWorkedExample97Squared() {
        val problem = generator.generateSpecificProblem(97L)
        assertEquals(97L, problem.operand)
        assertEquals(9409L, problem.correctAnswer)
    }

    @Test
    fun testWorkedExample88SquaredWithCarry() {
        val problem = generator.generateSpecificProblem(88L)
        assertEquals(88L, problem.operand)
        assertEquals(7744L, problem.correctAnswer)
    }

    @Test
    fun testBase1000WorkedExample994Squared() {
        val problem = generator.generateSpecificProblem(994L)
        assertEquals(994L, problem.operand)
        assertEquals(988036L, problem.correctAnswer)
    }

    @Test
    fun testTier1GeneratedProblemsMathematicalCorrectness() {
        repeat(30) {
            val problem = generator.generateProblem(DifficultyTier.TIER_1_EASY)
            val expected = problem.operand * problem.operand
            assertEquals(
                "Mathematical mismatch for operand ${problem.operand}",
                expected,
                problem.correctAnswer
            )
            assertTrue("Distractor count must be 4", problem.distractors.size == 4)
        }
    }

    @Test
    fun testTier2GeneratedProblemsMathematicalCorrectness() {
        repeat(30) {
            val problem = generator.generateProblem(DifficultyTier.TIER_2_HARD)
            val expected = problem.operand * problem.operand
            assertEquals(
                "Mathematical mismatch for operand ${problem.operand}",
                expected,
                problem.correctAnswer
            )
        }
    }
}
