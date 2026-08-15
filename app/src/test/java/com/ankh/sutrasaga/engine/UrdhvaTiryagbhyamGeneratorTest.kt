package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.domain.models.DifficultyTier
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UrdhvaTiryagbhyamGeneratorTest {

    private lateinit var generator: UrdhvaTiryagbhyamGenerator

    @Before
    fun setUp() {
        generator = UrdhvaTiryagbhyamGenerator()
    }

    @Test
    fun testCanonicalWorkedExample23Times41() {
        val problem = generator.generateSpecificProblemPair(23L, 41L)
        assertEquals(23L, problem.operand)
        assertEquals(943L, problem.correctAnswer)

        val steps = problem.decompositionSteps
        assertEquals(4, steps.size)
        assertEquals("3", steps[0].stepResult) // 3x1 = 3 (carry 0)
        assertEquals("4 (carry 1)", steps[1].stepResult) // 2x1 + 3x4 = 14 -> 4 carry 1
        assertEquals("9", steps[2].stepResult) // 2x4 + 1 = 9
        assertEquals("943", steps[3].stepResult)
    }

    @Test
    fun testHandVerifiedCarryCaseUnitsCarry27Times38() {
        // Units: 7x8 = 56 -> 6 (carry 5)
        // Cross: 2x8 + 7x3 + 5 = 16 + 21 + 5 = 42 -> 2 (carry 4)
        // Tens: 2x3 + 4 = 10 -> 1026
        val problem = generator.generateSpecificProblemPair(27L, 38L)
        assertEquals(1026L, problem.correctAnswer)
    }

    @Test
    fun testHandVerifiedCompoundHeavyCarry89Times76() {
        // Units: 9x6 = 54 -> 4 (carry 5)
        // Cross: 8x6 + 9x7 + 5 = 48 + 63 + 5 = 116 -> 6 (carry 11)
        // Tens: 8x7 + 11 = 56 + 11 = 67 -> 6764
        val problem = generator.generateSpecificProblemPair(89L, 76L)
        assertEquals(6764L, problem.correctAnswer)
    }

    @Test
    fun testHandVerifiedCarryCase98Times89() {
        val problem = generator.generateSpecificProblemPair(98L, 89L)
        assertEquals(8722L, problem.correctAnswer)
    }

    @Test
    fun testHandVerifiedCarryCase64Times57() {
        val problem = generator.generateSpecificProblemPair(64L, 57L)
        assertEquals(3648L, problem.correctAnswer)
    }

    @Test
    fun testHandVerifiedCarryCase73Times49() {
        val problem = generator.generateSpecificProblemPair(73L, 49L)
        assertEquals(3577L, problem.correctAnswer)
    }

    @Test
    fun testHandVerifiedCarryCase55Times55() {
        val problem = generator.generateSpecificProblemPair(55L, 55L)
        assertEquals(3025L, problem.correctAnswer)
    }

    @Test
    fun testHandVerifiedCarryCase48Times39() {
        val problem = generator.generateSpecificProblemPair(48L, 39L)
        assertEquals(1872L, problem.correctAnswer)
    }

    @Test
    fun testHandVerifiedTier23DigitBy2Digit123Times45() {
        // 123 x 45 = 5535
        val problem = generator.generateSpecificProblemPair(123L, 45L)
        assertEquals(5535L, problem.correctAnswer)
    }

    @Test
    fun testHandVerifiedTier23DigitBy2Digit246Times35() {
        // 246 x 35 = 8610
        val problem = generator.generateSpecificProblemPair(246L, 35L)
        assertEquals(8610L, problem.correctAnswer)
    }

    @Test
    fun testHandVerifiedTier23DigitBy2Digit987Times65() {
        // 987 x 65 = 64155
        val problem = generator.generateSpecificProblemPair(987L, 65L)
        assertEquals(64155L, problem.correctAnswer)
    }

    @Test
    fun testTier1GeneratedProblemsMathematicalCorrectness() {
        repeat(30) {
            val problem = generator.generateProblem(DifficultyTier.TIER_1_EASY)
            val expected = problem.prefixPart * problem.incrementedPrefix
            assertEquals(
                "Mathematical mismatch for ${problem.prefixPart} x ${problem.incrementedPrefix}",
                expected,
                problem.correctAnswer
            )
            assertTrue("Distractors count must be 4", problem.distractors.size == 4)
        }
    }

    @Test
    fun testTier2GeneratedProblemsMathematicalCorrectness() {
        repeat(30) {
            val problem = generator.generateProblem(DifficultyTier.TIER_2_HARD)
            val expected = problem.prefixPart * problem.incrementedPrefix
            assertEquals(
                "Mathematical mismatch for ${problem.prefixPart} x ${problem.incrementedPrefix}",
                expected,
                problem.correctAnswer
            )
            assertTrue("Distractors count must be 4", problem.distractors.size == 4)
        }
    }
}
