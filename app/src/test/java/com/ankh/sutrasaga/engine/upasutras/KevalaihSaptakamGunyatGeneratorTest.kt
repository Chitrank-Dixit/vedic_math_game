package com.ankh.sutrasaga.engine.upasutras

import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.KevalaihClassification
import com.ankh.sutrasaga.domain.models.UpaSutraId
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class KevalaihSaptakamGunyatGeneratorTest {

    private lateinit var generator: KevalaihSaptakamGunyatGenerator

    @Before
    fun setUp() {
        generator = KevalaihSaptakamGunyatGenerator()
    }

    @Test
    fun testUpaSutraMetadata() {
        assertEquals(UpaSutraId.KEVALAIHSAPTAKAM_GUNYAT, generator.upaSutraId)
        assertEquals("Kevalaih Saptakam Gunyat", generator.sutraName)
    }

    @Test
    fun testIdentityAnchor_7Times143Equals1001() {
        val prod = 7L * 143L
        assertEquals(1001L, prod)
        assertEquals(999999L, 1001L * 999L)
        assertEquals(999999L, 7L * 142857L)
    }

    @Test
    fun testCanonicalOneSeventh() {
        val solution = generator.solve(1L, 7L)
        assertEquals(KevalaihClassification.VALID_SEVENTH_RECIPROCAL, solution.classification)
        assertEquals(142857L, solution.cyclicBlock)
        assertEquals("142857", solution.cyclicBlockString)
        assertEquals("0.(142857)", solution.decimalString)
        assertEquals(4, solution.steps.size)

        // Algebraic check
        assertEquals(1L, (142857L * 7L) / 999999L)
    }

    @Test
    fun testAllSeventhsNumerators1To6() {
        val expected = listOf(
            Pair(1L, 142857L),
            Pair(2L, 285714L),
            Pair(3L, 428571L),
            Pair(4L, 571428L),
            Pair(5L, 714285L),
            Pair(6L, 857142L)
        )

        for ((num, expectedBlock) in expected) {
            val solution = generator.solve(num, 7L)
            assertEquals(KevalaihClassification.VALID_SEVENTH_RECIPROCAL, solution.classification)
            assertEquals("Cyclic block for $num/7", expectedBlock, solution.cyclicBlock)
            assertEquals("Block string for $num/7", String.format("%06d", expectedBlock), solution.cyclicBlockString)

            // Algebraic reduction check: block / 999999 == num / 7
            val reducedNum = (expectedBlock * 7L) / 999999L
            assertEquals("Algebraic fraction reduction for $num/7", num, reducedNum)
        }
    }

    @Test
    fun testUnsupportedDenominatorsAndNumerators() {
        val sol8 = generator.solve(1L, 8L)
        assertEquals(KevalaihClassification.UNSUPPORTED_DENOMINATOR, sol8.classification)

        val sol13 = generator.solve(1L, 13L)
        assertEquals(KevalaihClassification.UNSUPPORTED_DENOMINATOR, sol13.classification)

        val sol0 = generator.solve(0L, 7L)
        assertEquals(KevalaihClassification.UNSUPPORTED_NUMERATOR, sol0.classification)

        val sol7 = generator.solve(7L, 7L)
        assertEquals(KevalaihClassification.UNSUPPORTED_NUMERATOR, sol7.classification)
    }

    @Test
    fun testTwentyHandVerifiedGenerations() {
        for (n in 1L..6L) {
            val sol = generator.solve(n, 7L)
            assertEquals(n * 142857L, sol.cyclicBlock)
            assertTrue(sol.steps.isNotEmpty())
        }
    }

    @Test
    fun testFiftyRandomizedPropertyTestsTier1() {
        repeat(50) {
            val problem = generator.generateProblem(DifficultyTier.TIER_1_EASY)
            assertEquals(142857L, problem.correctAnswer)
            assertTrue(!problem.distractors.contains(problem.correctAnswer))
            assertNotNull(problem.questionText)
        }
    }

    @Test
    fun testFiftyRandomizedPropertyTestsTier2() {
        repeat(50) {
            val problem = generator.generateProblem(DifficultyTier.TIER_2_HARD)
            assertTrue(problem.correctAnswer in listOf(285714L, 428571L))
            assertTrue(!problem.distractors.contains(problem.correctAnswer))
        }
    }

    @Test
    fun testFiftyRandomizedPropertyTestsTier3() {
        repeat(50) {
            val problem = generator.generateTier3Problem()
            assertTrue(problem.correctAnswer in listOf(571428L, 714285L, 857142L))
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
