package com.ankh.sutrasaga.engine.upasutras

import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.SimplePolynomial
import com.ankh.sutrasaga.domain.models.SisyateClassification
import com.ankh.sutrasaga.domain.models.UpaSutraId
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SisyateSesasamjnahGeneratorTest {

    private lateinit var generator: SisyateSesasamjnahGenerator

    @Before
    fun setUp() {
        generator = SisyateSesasamjnahGenerator()
    }

    @Test
    fun testUpaSutraMetadata() {
        assertEquals(UpaSutraId.SHISYATE_SHESAMAJNA, generator.upaSutraId)
        assertEquals("Sisyate Sesasamjnah", generator.sutraName)
    }

    @Test
    fun testCanonicalExample_Cubic_x3Minus3x2Plus4xMinus5_DividedBy_xMinus2() {
        val poly = SimplePolynomial(listOf(1L, -3L, 4L, -5L))
        val solution = generator.solve(poly, 2L)
        assertEquals(SisyateClassification.NON_ZERO_REMAINDER, solution.classification)
        assertEquals(-1L, solution.remainder)
        assertEquals(listOf(1L, -1L, 2L), solution.quotientCoefficients) // Q(x) = x^2 - x + 2
        assertEquals(3, solution.steps.size)

        // Independent oracle check
        val (q, r) = generator.polynomialLongDivision(poly, 2L)
        assertEquals(-1L, r)
        assertEquals(listOf(1L, -1L, 2L), q)
    }

    @Test
    fun testZeroRemainderExample_x3Minus6x2Plus11xMinus6_DividedBy_xMinus1() {
        val poly = SimplePolynomial(listOf(1L, -6L, 11L, -6L))
        val solution = generator.solve(poly, 1L)
        assertEquals(SisyateClassification.ZERO_REMAINDER_FACTOR_CONFIRMED, solution.classification)
        assertEquals(0L, solution.remainder)
        assertEquals(listOf(1L, -5L, 6L), solution.quotientCoefficients) // Q(x) = x^2 - 5x + 6

        // Independent oracle check
        val (q, r) = generator.polynomialLongDivision(poly, 1L)
        assertEquals(0L, r)
        assertEquals(listOf(1L, -5L, 6L), q)
    }

    @Test
    fun testNegativeKExample_2x2Plus3xMinus5_DividedBy_xPlus2() {
        val poly = SimplePolynomial(listOf(2L, 3L, -5L))
        val solution = generator.solve(poly, -2L)
        assertEquals(SisyateClassification.NON_ZERO_REMAINDER, solution.classification)
        assertEquals(-3L, solution.remainder)
        assertEquals(listOf(2L, -1L), solution.quotientCoefficients) // Q(x) = 2x - 1

        // Independent oracle check
        val (q, r) = generator.polynomialLongDivision(poly, -2L)
        assertEquals(-3L, r)
        assertEquals(listOf(2L, -1L), q)
    }

    @Test
    fun testTwentyHandVerifiedCases() {
        val cases = listOf(
            // Poly, k, expectedRemainder
            Triple(SimplePolynomial(listOf(1L, 2L, 3L)), 1L, 6L),           // x^2 + 2x + 3 at x=1 -> 6
            Triple(SimplePolynomial(listOf(1L, -4L, 4L)), 2L, 0L),          // (x-2)^2 at x=2 -> 0
            Triple(SimplePolynomial(listOf(2L, -5L, 2L)), 3L, 5L),          // 2(9) - 15 + 2 = 5
            Triple(SimplePolynomial(listOf(3L, 2L, -1L)), 2L, 15L),         // 3(4) + 4 - 1 = 15
            Triple(SimplePolynomial(listOf(1L, 0L, -4L)), 2L, 0L),          // x^2 - 4 at x=2 -> 0
            Triple(SimplePolynomial(listOf(1L, 0L, -4L)), 3L, 5L),          // x^2 - 4 at x=3 -> 5
            Triple(SimplePolynomial(listOf(1L, -1L, -6L)), -2L, 0L),        // (x-3)(x+2) at x=-2 -> 0
            Triple(SimplePolynomial(listOf(2L, 1L, -3L)), -1L, -2L),        // 2 - 1 - 3 = -2
            Triple(SimplePolynomial(listOf(1L, 1L, 1L, 1L)), 1L, 4L),       // 1 + 1 + 1 + 1 = 4
            Triple(SimplePolynomial(listOf(1L, -1L, 1L, -1L)), 1L, 0L),     // 1 - 1 + 1 - 1 = 0
            Triple(SimplePolynomial(listOf(1L, -1L, 1L, -1L)), -1L, -4L),   // -1 - 1 - 1 - 1 = -4
            Triple(SimplePolynomial(listOf(2L, 0L, 0L, -8L)), 2L, 8L),      // 2(8) - 8 = 8
            Triple(SimplePolynomial(listOf(1L, -2L, 0L, 3L)), 3L, 12L),     // 27 - 18 + 3 = 12
            Triple(SimplePolynomial(listOf(3L, -4L, 2L)), -1L, 9L),         // 3 + 4 + 2 = 9
            Triple(SimplePolynomial(listOf(1L, 5L, 6L)), -2L, 0L),          // (x+2)(x+3) at x=-2 -> 0
            Triple(SimplePolynomial(listOf(1L, 5L, 6L)), -3L, 0L),          // (x+2)(x+3) at x=-3 -> 0
            Triple(SimplePolynomial(listOf(1L, 5L, 6L)), 1L, 12L),          // 1 + 5 + 6 = 12
            Triple(SimplePolynomial(listOf(2L, -3L, 4L)), 0L, 4L),          // at x=0 -> 4
            Triple(SimplePolynomial(listOf(1L, -7L, 10L)), 2L, 0L),         // (x-2)(x-5) at x=2 -> 0
            Triple(SimplePolynomial(listOf(1L, -7L, 10L)), 5L, 0L)          // (x-2)(x-5) at x=5 -> 0
        )

        for ((poly, k, expectedR) in cases) {
            val solution = generator.solve(poly, k)
            assertEquals("Remainder for ${poly.toFormattedString()} at k=$k", expectedR, solution.remainder)

            val (q, r) = generator.polynomialLongDivision(poly, k)
            assertEquals("Oracle remainder for ${poly.toFormattedString()} at k=$k", expectedR, r)
        }
    }

    @Test
    fun testTenFactorConfirmationCases() {
        val factorCases = listOf(
            Pair(SimplePolynomial(listOf(1L, -5L, 6L)), 2L),         // (x-2)(x-3)
            Pair(SimplePolynomial(listOf(1L, -5L, 6L)), 3L),         // (x-2)(x-3)
            Pair(SimplePolynomial(listOf(1L, 4L, 3L)), -1L),         // (x+1)(x+3)
            Pair(SimplePolynomial(listOf(1L, 4L, 3L)), -3L),         // (x+1)(x+3)
            Pair(SimplePolynomial(listOf(2L, -4L)), 2L),             // 2x - 4 at x=2
            Pair(SimplePolynomial(listOf(1L, 0L, -9L)), 3L),         // x^2 - 9 at x=3
            Pair(SimplePolynomial(listOf(1L, 0L, -9L)), -3L),        // x^2 - 9 at x=-3
            Pair(SimplePolynomial(listOf(1L, -2L, -3L)), 3L),        // (x-3)(x+1) at x=3
            Pair(SimplePolynomial(listOf(1L, -2L, -3L)), -1L),       // (x-3)(x+1) at x=-1
            Pair(SimplePolynomial(listOf(1L, -6L, 11L, -6L)), 2L)    // (x-1)(x-2)(x-3) at x=2
        )

        for ((poly, k) in factorCases) {
            val solution = generator.solve(poly, k)
            assertEquals(SisyateClassification.ZERO_REMAINDER_FACTOR_CONFIRMED, solution.classification)
            assertEquals(0L, solution.remainder)
        }
    }

    @Test
    fun testFiftyRandomizedPropertyTestsTier1() {
        repeat(50) {
            val problem = generator.generateProblem(DifficultyTier.TIER_1_EASY)
            val k = problem.prefixPart
            assertNotNull(problem.questionText)
            assertTrue(!problem.distractors.contains(problem.correctAnswer))
        }
    }

    @Test
    fun testFiftyRandomizedPropertyTestsTier2() {
        repeat(50) {
            val problem = generator.generateProblem(DifficultyTier.TIER_2_HARD)
            val k = problem.prefixPart
            assertNotNull(problem.questionText)
            assertTrue(!problem.distractors.contains(problem.correctAnswer))
        }
    }

    @Test
    fun testPracticeAndChallengeSets() {
        val practiceSet = generator.generateQuestPracticeSet()
        assertEquals(5, practiceSet.size)

        val challengeSet = generator.generateQuestChallengeSet()
        assertEquals(3, challengeSet.size)
    }
}
