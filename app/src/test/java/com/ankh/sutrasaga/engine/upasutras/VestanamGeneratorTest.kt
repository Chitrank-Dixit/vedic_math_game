package com.ankh.sutrasaga.engine.upasutras

import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.SupportedDivisor
import com.ankh.sutrasaga.domain.models.UpaSutraId
import com.ankh.sutrasaga.domain.models.VestanamClassification
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class VestanamGeneratorTest {

    private lateinit var generator: VestanamGenerator

    @Before
    fun setUp() {
        generator = VestanamGenerator()
    }

    @Test
    fun testUpaSutraMetadata() {
        assertEquals(UpaSutraId.VESHTANAM, generator.upaSutraId)
        assertEquals("Vestanam", generator.sutraName)
    }

    @Test
    fun testExample1_Divisor19_247() {
        val solution = generator.solve(247L, SupportedDivisor.NINETEEN)
        assertEquals(VestanamClassification.DIVISIBLE, solution.classification)
        assertTrue(solution.isDivisible)
        assertEquals(1, solution.steps.size)
        val s1 = solution.steps[0]
        assertEquals(24L, s1.restValue)
        assertEquals(7L, s1.lastDigit)
        assertEquals(14L, s1.osculatedProduct)
        assertEquals(38L, s1.resultValue)
        assertEquals(38L, solution.finalValue)
        assertEquals(0L, 247L % 19L)
    }

    @Test
    fun testExample2_Divisor13_143() {
        val solution = generator.solve(143L, SupportedDivisor.THIRTEEN)
        assertEquals(VestanamClassification.DIVISIBLE, solution.classification)
        assertTrue(solution.isDivisible)
        assertEquals(1, solution.steps.size)
        val s1 = solution.steps[0]
        assertEquals(14L, s1.restValue)
        assertEquals(3L, s1.lastDigit)
        assertEquals(12L, s1.osculatedProduct)
        assertEquals(26L, s1.resultValue)
        assertEquals(26L, solution.finalValue)
        assertEquals(0L, 143L % 13L)
    }

    @Test
    fun testExample3_Divisor7_133() {
        val solution = generator.solve(133L, SupportedDivisor.SEVEN)
        assertEquals(VestanamClassification.DIVISIBLE, solution.classification)
        assertTrue(solution.isDivisible)
        assertEquals(1, solution.steps.size)
        val s1 = solution.steps[0]
        assertEquals(13L, s1.restValue)
        assertEquals(3L, s1.lastDigit)
        assertEquals(6L, s1.osculatedProduct)
        assertEquals(7L, s1.resultValue)
        assertEquals(7L, solution.finalValue)
        assertEquals(0L, 133L % 7L)
    }

    @Test
    fun testExample4_Divisor19_MultiStep_2223() {
        val solution = generator.solve(2223L, SupportedDivisor.NINETEEN)
        assertEquals(VestanamClassification.DIVISIBLE, solution.classification)
        assertTrue(solution.isDivisible)
        assertEquals(2, solution.steps.size)

        val s1 = solution.steps[0]
        assertEquals(222L, s1.restValue)
        assertEquals(3L, s1.lastDigit)
        assertEquals(228L, s1.resultValue)

        val s2 = solution.steps[1]
        assertEquals(22L, s2.restValue)
        assertEquals(8L, s2.lastDigit)
        assertEquals(38L, s2.resultValue)

        assertEquals(38L, solution.finalValue)
        assertEquals(0L, 2223L % 19L)
    }

    @Test
    fun testExample5_Divisor7_NonDivisible_2223() {
        val solution = generator.solve(2223L, SupportedDivisor.SEVEN)
        assertEquals(VestanamClassification.NOT_DIVISIBLE, solution.classification)
        assertFalse(solution.isDivisible)
        assertEquals(2, solution.steps.size)

        val s1 = solution.steps[0]
        assertEquals(222L, s1.restValue)
        assertEquals(3L, s1.lastDigit)
        assertEquals(216L, s1.resultValue)

        val s2 = solution.steps[1]
        assertEquals(21L, s2.restValue)
        assertEquals(6L, s2.lastDigit)
        assertEquals(9L, s2.resultValue)

        assertEquals(9L, solution.finalValue)
        assertEquals(4L, 2223L % 7L)
    }

    @Test
    fun testFifteenHandVerifiedCasesDivisor19() {
        val cases = listOf(
            Pair(190L, true),
            Pair(209L, true),
            Pair(361L, true), // 19^2
            Pair(399L, true),
            Pair(570L, true),
            Pair(741L, true),
            Pair(988L, true),
            Pair(1159L, true),
            Pair(191L, false),
            Pair(248L, false),
            Pair(362L, false),
            Pair(400L, false),
            Pair(573L, false),
            Pair(745L, false),
            Pair(990L, false)
        )

        for ((num, expectedDiv) in cases) {
            val solution = generator.solve(num, SupportedDivisor.NINETEEN)
            assertEquals("Divisibility for $num on d=19", expectedDiv, solution.isDivisible)
            assertEquals("Agreement with modulo for $num on d=19", num % 19L == 0L, solution.isDivisible)
        }
    }

    @Test
    fun testFifteenHandVerifiedCasesDivisor13() {
        val cases = listOf(
            Pair(130L, true),
            Pair(169L, true), // 13^2
            Pair(221L, true),
            Pair(286L, true),
            Pair(351L, true),
            Pair(507L, true),
            Pair(663L, true),
            Pair(819L, true),
            Pair(131L, false),
            Pair(170L, false),
            Pair(225L, false),
            Pair(290L, false),
            Pair(355L, false),
            Pair(510L, false),
            Pair(665L, false)
        )

        for ((num, expectedDiv) in cases) {
            val solution = generator.solve(num, SupportedDivisor.THIRTEEN)
            assertEquals("Divisibility for $num on d=13", expectedDiv, solution.isDivisible)
            assertEquals("Agreement with modulo for $num on d=13", num % 13L == 0L, solution.isDivisible)
        }
    }

    @Test
    fun testFifteenHandVerifiedCasesDivisor7() {
        val cases = listOf(
            Pair(112L, true),
            Pair(147L, true),
            Pair(203L, true),
            Pair(259L, true),
            Pair(343L, true), // 7^3
            Pair(441L, true),
            Pair(567L, true),
            Pair(721L, true),
            Pair(113L, false),
            Pair(148L, false),
            Pair(205L, false),
            Pair(260L, false),
            Pair(345L, false),
            Pair(445L, false),
            Pair(570L, false)
        )

        for ((num, expectedDiv) in cases) {
            val solution = generator.solve(num, SupportedDivisor.SEVEN)
            assertEquals("Divisibility for $num on d=7", expectedDiv, solution.isDivisible)
            assertEquals("Agreement with modulo for $num on d=7", num % 7L == 0L, solution.isDivisible)
        }
    }

    @Test
    fun testFiftyRandomizedPropertyTestsDivisor19() {
        repeat(50) {
            val problem = generator.generateProblem(DifficultyTier.TIER_1_EASY)
            val num = problem.operand
            val solution = generator.solve(num, SupportedDivisor.NINETEEN)
            assertEquals("Solution equals modulo for $num", num % 19L == 0L, solution.isDivisible)
            assertEquals(solution.finalValue, problem.correctAnswer)
            assertTrue(!problem.distractors.contains(problem.correctAnswer))
        }
    }

    @Test
    fun testFiftyRandomizedPropertyTestsDivisor13() {
        repeat(50) {
            val problem = generator.generateProblem(DifficultyTier.TIER_2_HARD)
            val num = problem.operand
            val solution = generator.solve(num, SupportedDivisor.THIRTEEN)
            assertEquals("Solution equals modulo for $num", num % 13L == 0L, solution.isDivisible)
            assertEquals(solution.finalValue, problem.correctAnswer)
            assertTrue(!problem.distractors.contains(problem.correctAnswer))
        }
    }

    @Test
    fun testFiftyRandomizedPropertyTestsDivisor7() {
        repeat(50) {
            val problem = generator.generateTier3Problem()
            val num = problem.operand
            val solution = generator.solve(num, SupportedDivisor.SEVEN)
            assertEquals("Solution equals modulo for $num", num % 7L == 0L, solution.isDivisible)
            assertEquals(solution.finalValue, problem.correctAnswer)
            assertTrue(!problem.distractors.contains(problem.correctAnswer))
        }
    }

    @Test
    fun testUnsupportedDivisorsAreRejected() {
        val solution11 = generator.solve(121L, 11L)
        assertEquals(VestanamClassification.UNSUPPORTED_DIVISOR, solution11.classification)

        val solution17 = generator.solve(289L, 17L)
        assertEquals(VestanamClassification.UNSUPPORTED_DIVISOR, solution17.classification)
    }

    @Test
    fun testPracticeAndChallengeSets() {
        val practiceSet = generator.generateQuestPracticeSet()
        assertEquals(5, practiceSet.size)

        val challengeSet = generator.generateQuestChallengeSet()
        assertEquals(3, challengeSet.size)
    }
}
