package com.ankh.sutrasaga.engine.upasutras

import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.LinearBivariateFactor
import com.ankh.sutrasaga.domain.models.LopanaClassification
import com.ankh.sutrasaga.domain.models.UpaSutraId
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LopanasthapanabhyamGeneratorTest {

    private lateinit var generator: LopanasthapanabhyamGenerator

    @Before
    fun setUp() {
        generator = LopanasthapanabhyamGenerator()
    }

    @Test
    fun testUpaSutraMetadata() {
        assertEquals(UpaSutraId.LOPANA_STHAPANABHYAM, generator.upaSutraId)
        assertEquals("Lopanasthapanabhyam", generator.sutraName)
    }

    @Test
    fun testCanonicalExample_2xPlusYPlus2_Times_xPlus2yPlus1() {
        val f1 = LinearBivariateFactor(2, 1, 2)
        val f2 = LinearBivariateFactor(1, 2, 1)
        val sol = generator.solve(f1, f2)

        // Target: 2x² + 5xy + 2y² + 4x + 5y + 2
        assertEquals(2L, sol.target.a)
        assertEquals(5L, sol.target.b)
        assertEquals(2L, sol.target.c)
        assertEquals(4L, sol.target.d)
        assertEquals(5L, sol.target.e)
        assertEquals(2L, sol.target.f)

        assertEquals(4, sol.steps.size)
        assertEquals("(2x + y + 2)(x + 2y + 1)", sol.factoredFormDisplay)
        assertEquals(LopanaClassification.AMBIGUOUS_RECOMBINATION, sol.classification)
        assertNotNull(sol.rejectedCandidate)
    }

    @Test
    fun testSimplerMonicExample_xPlusYPlus1_Times_xPlus2yPlus2() {
        val f1 = LinearBivariateFactor(1, 1, 1)
        val f2 = LinearBivariateFactor(1, 2, 2)
        val sol = generator.solve(f1, f2)

        // Target: x² + 3xy + 2y² + 3x + 4y + 2
        assertEquals(1L, sol.target.a)
        assertEquals(3L, sol.target.b)
        assertEquals(2L, sol.target.c)
        assertEquals(3L, sol.target.d)
        assertEquals(4L, sol.target.e)
        assertEquals(2L, sol.target.f)

        assertEquals("(x + y + 1)(x + 2y + 2)", sol.factoredFormDisplay)
    }

    @Test
    fun testNegativeCoefficientExample_xPlusYPlus1_Times_xMinus2yMinus2() {
        val f1 = LinearBivariateFactor(1, 1, 1)
        val f2 = LinearBivariateFactor(1, -2, -2)
        val sol = generator.solve(f1, f2)

        // Target: x² - xy - 2y² - x - 4y - 2
        assertEquals(1L, sol.target.a)
        assertEquals(-1L, sol.target.b)
        assertEquals(-2L, sol.target.c)
        assertEquals(-1L, sol.target.d)
        assertEquals(-4L, sol.target.e)
        assertEquals(-2L, sol.target.f)

        assertEquals("(x + y + 1)(x - 2y - 2)", sol.factoredFormDisplay)
    }

    @Test
    fun testTwentyHandVerifiedConstructedCases() {
        for (i in 1..20) {
            val f1 = LinearBivariateFactor(1, i.toLong() % 3 + 1, (i % 4) + 1L)
            val f2 = LinearBivariateFactor(2, 1, (i % 3) + 1L)
            val sol = generator.solve(f1, f2)

            // Verify exact 6-coefficient algebraic identity
            val a = f1.px * f2.px
            val b = f1.px * f2.py + f1.py * f2.px
            val c = f1.py * f2.py
            val d = f1.px * f2.constant + f1.constant * f2.px
            val e = f1.py * f2.constant + f1.constant * f2.py
            val f = f1.constant * f2.constant

            assertEquals(a, sol.target.a)
            assertEquals(b, sol.target.b)
            assertEquals(c, sol.target.c)
            assertEquals(d, sol.target.d)
            assertEquals(e, sol.target.e)
            assertEquals(f, sol.target.f)
        }
    }

    @Test
    fun testTenMixedSignCases() {
        val signCases = listOf(
            Pair(LinearBivariateFactor(1, -1, 1), LinearBivariateFactor(1, 2, -2)),
            Pair(LinearBivariateFactor(2, -1, -1), LinearBivariateFactor(1, 1, 2)),
            Pair(LinearBivariateFactor(1, 2, -3), LinearBivariateFactor(2, -1, 1)),
            Pair(LinearBivariateFactor(1, -2, 2), LinearBivariateFactor(1, -1, -1)),
            Pair(LinearBivariateFactor(2, 1, -2), LinearBivariateFactor(1, -2, 3)),
            Pair(LinearBivariateFactor(1, 1, -1), LinearBivariateFactor(1, -1, -2)),
            Pair(LinearBivariateFactor(2, -2, 1), LinearBivariateFactor(1, 1, -3)),
            Pair(LinearBivariateFactor(1, -1, 2), LinearBivariateFactor(2, 1, -1)),
            Pair(LinearBivariateFactor(1, 2, -1), LinearBivariateFactor(1, -1, 2)),
            Pair(LinearBivariateFactor(2, -1, 3), LinearBivariateFactor(1, 2, -2))
        )

        for ((f1, f2) in signCases) {
            val sol = generator.solve(f1, f2)
            val expectedB = f1.px * f2.py + f1.py * f2.px
            assertEquals(expectedB, sol.target.b)
            assertTrue(sol.steps.isNotEmpty())
        }
    }

    @Test
    fun testTenPlausibleRejectedCandidateCases() {
        for (i in 1..10) {
            val f1 = LinearBivariateFactor(2, 1, (i + 1).toLong())
            val f2 = LinearBivariateFactor(1, 2, 1L)
            val sol = generator.solve(f1, f2)

            assertEquals(LopanaClassification.AMBIGUOUS_RECOMBINATION, sol.classification)
            assertNotNull(sol.rejectedCandidate)
            assertTrue(sol.rejectedCandidate!!.contains("≠"))
        }
    }

    @Test
    fun testFiftyRandomizedPropertyTestsTier1() {
        repeat(50) {
            val problem = generator.generateProblem(DifficultyTier.TIER_1_EASY)
            assertTrue(problem.correctAnswer in 1L..3L)
            assertTrue(!problem.distractors.contains(problem.correctAnswer))
            assertNotNull(problem.questionText)
        }
    }

    @Test
    fun testFiftyRandomizedPropertyTestsTier2() {
        repeat(50) {
            val problem = generator.generateProblem(DifficultyTier.TIER_2_HARD)
            assertTrue(problem.correctAnswer != 0L)
            assertTrue(!problem.distractors.contains(problem.correctAnswer))
        }
    }

    @Test
    fun testFiftyRandomizedPropertyTestsTier3() {
        repeat(50) {
            val problem = generator.generateTier3Problem()
            assertTrue(problem.correctAnswer in 2L..4L)
            assertTrue(!problem.distractors.contains(problem.correctAnswer))
        }
    }

    @Test
    fun testQuestPracticeAndChallengeSets() {
        val practiceSet = generator.generateQuestPracticeSet()
        assertEquals(5, practiceSet.size)

        val challengeSet = generator.generateQuestChallengeSet()
        assertEquals(3, challengeSet.size)
    }
}
