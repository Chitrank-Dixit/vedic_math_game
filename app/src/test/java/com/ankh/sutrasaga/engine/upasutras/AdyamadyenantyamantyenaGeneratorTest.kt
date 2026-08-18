package com.ankh.sutrasaga.engine.upasutras

import com.ankh.sutrasaga.domain.models.AdyamadyaClassification
import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.UpaSutraId
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AdyamadyenantyamantyenaGeneratorTest {

    private lateinit var generator: AdyamadyenantyamantyenaGenerator

    @Before
    fun setUp() {
        generator = AdyamadyenantyamantyenaGenerator()
    }

    @Test
    fun testUpaSutraMetadata() {
        assertEquals(UpaSutraId.ADYAMADYENANTYAMANTYENA, generator.upaSutraId)
        assertEquals("Adyamadyenantyamantyena", generator.sutraName)
    }

    @Test
    fun testCanonicalExample_2xSquaredPlus7xPlus5() {
        val solution = generator.solve(2, 7, 5)
        assertEquals(AdyamadyaClassification.FACTORABLE_NON_MONIC, solution.classification)
        assertNotNull(solution.confirmedCandidate)
        val cand = solution.confirmedCandidate!!
        assertEquals(2L, cand.a1)
        assertEquals(1L, cand.a2)
        assertEquals(5L, cand.c1)
        assertEquals(1L, cand.c2)
        assertEquals(7L, cand.crossTerm)
        assertEquals("(2x + 5)(x + 1)", cand.toFactorString())
        assertEquals(4, solution.steps.size)

        // Expansion check
        assertEquals(2L, cand.a1 * cand.a2)
        assertEquals(7L, cand.a1 * cand.c2 + cand.a2 * cand.c1)
        assertEquals(5L, cand.c1 * cand.c2)
    }

    @Test
    fun testNegativeCoefficients_3xSquaredMinus5xMinus2() {
        val solution = generator.solve(3, -5, -2)
        assertEquals(AdyamadyaClassification.FACTORABLE_NON_MONIC, solution.classification)
        assertNotNull(solution.confirmedCandidate)
        val cand = solution.confirmedCandidate!!
        assertEquals(3L, cand.a1)
        assertEquals(1L, cand.a2)
        assertEquals(1L, cand.c1)
        assertEquals(-2L, cand.c2)
        assertEquals(-5L, cand.crossTerm)
        assertEquals("(3x + 1)(x - 2)", cand.toFactorString())

        // Expansion check
        assertEquals(3L, cand.a1 * cand.a2)
        assertEquals(-5L, cand.a1 * cand.c2 + cand.a2 * cand.c1)
        assertEquals(-2L, cand.c1 * cand.c2)
    }

    @Test
    fun testUnfactorable_2xSquaredPlus3xPlus4_DiscriminantCheck() {
        val solution = generator.solve(2, 3, 4)
        assertEquals(AdyamadyaClassification.NO_INTEGER_FACTORIZATION, solution.classification)
        assertTrue("Discriminant is negative", solution.quadratic.discriminant < 0L)
        assertEquals(-23L, solution.quadratic.discriminant)
    }

    @Test
    fun testMonicQuadraticDeferredToWorld15() {
        val solution = generator.solve(1, 5, 6)
        assertEquals(AdyamadyaClassification.MONIC_DEFERRED_TO_WORLD_15, solution.classification)
    }

    @Test
    fun testTwentyHandVerifiedFactorizableCases() {
        val cases = listOf(
            Triple(2L, 5L, 2L),   // (2x+1)(x+2)
            Triple(2L, 9L, 4L),   // (2x+1)(x+4)
            Triple(3L, 8L, 5L),   // (3x+5)(x+1)
            Triple(3L, 7L, 2L),   // (3x+1)(x+2)
            Triple(4L, 8L, 3L),   // (2x+1)(2x+3) or (4x+?)(x+?)
            Triple(2L, 11L, 12L), // (2x+3)(x+4)
            Triple(3L, 10L, 8L),  // (3x+4)(x+2)
            Triple(5L, 12L, 7L),  // (5x+7)(x+1)
            Triple(2L, 13L, 15L), // (2x+3)(x+5)
            Triple(4L, 12L, 5L),  // (2x+1)(2x+5)
            Triple(2L, 7L, 3L),   // (2x+1)(x+3)
            Triple(3L, 11L, 6L),  // (3x+2)(x+3)
            Triple(2L, 9L, 9L),   // (2x+3)(x+3)
            Triple(3L, 14L, 8L),  // (3x+2)(x+4)
            Triple(2L, 15L, 18L), // (2x+3)(x+6)
            Triple(4L, 11L, 6L),  // (4x+3)(x+2)
            Triple(5L, 16L, 3L),  // (5x+1)(x+3)
            Triple(2L, 17L, 21L), // (2x+3)(x+7)
            Triple(3L, 13L, 4L),  // (3x+1)(x+4)
            Triple(2L, 19L, 24L)  // (2x+3)(x+8)
        )

        for ((a, b, c) in cases) {
            val solution = generator.solve(a, b, c)
            assertEquals("Factorable for $a x² + $b x + $c", AdyamadyaClassification.FACTORABLE_NON_MONIC, solution.classification)
            assertNotNull("Confirmed candidate for $a x² + $b x + $c", solution.confirmedCandidate)
            val cand = solution.confirmedCandidate!!
            assertEquals("A match", a, cand.a1 * cand.a2)
            assertEquals("B match", b, cand.crossTerm)
            assertEquals("C match", c, cand.c1 * cand.c2)
        }
    }

    @Test
    fun testTenHandVerifiedNegativeCoefficientCases() {
        val cases = listOf(
            Triple(2L, -5L, 2L),   // (2x-1)(x-2)
            Triple(3L, -7L, 2L),   // (3x-1)(x-2)
            Triple(2L, 1L, -6L),   // (2x-3)(x+2)
            Triple(3L, -1L, -2L),  // (3x+2)(x-1)
            Triple(2L, -7L, -4L),  // (2x+1)(x-4)
            Triple(3L, -4L, -4L),  // (3x+2)(x-2)
            Triple(5L, -8L, -4L),  // (5x+2)(x-2)
            Triple(2L, -9L, 4L),   // (2x-1)(x-4)
            Triple(4L, -4L, -3L),  // (2x-3)(2x+1)
            Triple(2L, -3L, -5L)   // (2x-5)(x+1)
        )

        for ((a, b, c) in cases) {
            val solution = generator.solve(a, b, c)
            assertEquals("Factorable for $a x² + $b x + $c", AdyamadyaClassification.FACTORABLE_NON_MONIC, solution.classification)
            assertNotNull("Confirmed candidate for $a x² + $b x + $c", solution.confirmedCandidate)
            val cand = solution.confirmedCandidate!!
            assertEquals("A match", a, cand.a1 * cand.a2)
            assertEquals("B match", b, cand.crossTerm)
            assertEquals("C match", c, cand.c1 * cand.c2)
        }
    }

    @Test
    fun testTenHandVerifiedUnfactorableCases() {
        val cases = listOf(
            Triple(2L, 1L, 3L),   // D = 1 - 24 = -23
            Triple(3L, 2L, 5L),   // D = 4 - 60 = -56
            Triple(2L, 4L, 5L),   // D = 16 - 40 = -24
            Triple(4L, 1L, 2L),   // D = 1 - 32 = -31
            Triple(2L, 3L, 7L),   // D = 9 - 56 = -47
            Triple(3L, -2L, 4L),  // D = 4 - 48 = -44
            Triple(5L, 1L, 1L),   // D = 1 - 20 = -19
            Triple(2L, 2L, 3L),   // D = 4 - 24 = -20
            Triple(3L, 5L, 7L),   // D = 25 - 84 = -59
            Triple(2L, -1L, 4L)   // D = 1 - 32 = -31
        )

        for ((a, b, c) in cases) {
            val solution = generator.solve(a, b, c)
            assertEquals("Unfactorable for $a x² + $b x + $c", AdyamadyaClassification.NO_INTEGER_FACTORIZATION, solution.classification)
            assertTrue("Discriminant must be negative or non-square", solution.quadratic.discriminant < 0L)
        }
    }

    @Test
    fun testFiftyRandomizedPropertyTestsTier1() {
        repeat(50) {
            val problem = generator.generateProblem(DifficultyTier.TIER_1_EASY)
            assertTrue(problem.decompositionSteps.isNotEmpty())
            assertTrue(!problem.distractors.contains(problem.correctAnswer))
            val match = Regex("Factor (\\d+)x² \\+ (\\d+)x \\+ (\\d+)").find(problem.questionText)
            assertNotNull("Question text format match", match)
            val a = match!!.groupValues[1].toLong()
            val b = match.groupValues[2].toLong()
            val c = match.groupValues[3].toLong()
            val solution = generator.solve(a, b, c)
            assertEquals(AdyamadyaClassification.FACTORABLE_NON_MONIC, solution.classification)
            assertEquals(b, problem.correctAnswer)
        }
    }

    @Test
    fun testFiftyRandomizedPropertyTestsTier2() {
        repeat(50) {
            val problem = generator.generateProblem(DifficultyTier.TIER_2_HARD)
            assertTrue(problem.decompositionSteps.isNotEmpty())
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
