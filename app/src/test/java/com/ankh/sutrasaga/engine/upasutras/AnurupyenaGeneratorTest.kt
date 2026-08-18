package com.ankh.sutrasaga.engine.upasutras

import com.ankh.sutrasaga.domain.models.AnurupyenaClassification
import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.UpaSutraId
import kotlin.math.abs
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AnurupyenaGeneratorTest {

    private lateinit var generator: AnurupyenaGenerator

    @Before
    fun setUp() {
        generator = AnurupyenaGenerator()
    }

    @Test
    fun testUpaSutraMetadata() {
        assertEquals(UpaSutraId.ANURUPYENA, generator.upaSutraId)
        assertEquals("Anurupyena", generator.sutraName)
    }

    @Test
    fun testCanonicalExampleBase50_48Times46() {
        val solution = generator.solve(48, 46, 50)
        assertEquals(AnurupyenaClassification.VALID_BASE_50, solution.classification)
        assertEquals(-2L, solution.dev1)
        assertEquals(-4L, solution.dev2)
        assertEquals(44L, solution.crossAddResult)
        assertEquals(22L, solution.scaledLeftPart)
        assertEquals(8L, solution.rawRightPart)
        assertEquals("08", solution.finalRightPartString)
        assertEquals(2208L, solution.product)
        assertEquals(48L * 46L, solution.product)
        assertEquals(4, solution.steps.size)
    }

    @Test
    fun testCanonicalExampleBase200_196Times204_WithBorrow() {
        val solution = generator.solve(196, 204, 200)
        assertEquals(AnurupyenaClassification.VALID_BASE_200, solution.classification)
        assertEquals(-4L, solution.dev1)
        assertEquals(4L, solution.dev2)
        assertEquals(200L, solution.crossAddResult)
        assertEquals(400L, solution.scaledLeftPart)
        assertEquals(-16L, solution.rawRightPart)
        assertEquals("84", solution.finalRightPartString)
        assertEquals(39984L, solution.product)
        assertEquals(196L * 204L, solution.product)
    }

    @Test
    fun testNonIntegerScaleExcluded_54Times47() {
        val solution = generator.solve(54, 47, 50)
        assertEquals(AnurupyenaClassification.NON_INTEGER_SCALE_EXCLUDED, solution.classification)
        assertEquals(51L, solution.crossAddResult)
    }

    @Test
    fun testTwentyHandVerifiedBase50Cases() {
        val base50Pairs = listOf(
            // Both positive deviations (same parity)
            Pair(52L, 54L), Pair(53L, 57L), Pair(54L, 56L), Pair(51L, 55L), Pair(56L, 58L),
            Pair(52L, 56L), Pair(53L, 55L), Pair(55L, 59L), Pair(58L, 62L), Pair(51L, 57L),
            // Both negative or mixed deviations (same parity)
            Pair(48L, 46L), Pair(44L, 46L), Pair(42L, 48L), Pair(45L, 47L), Pair(43L, 49L),
            Pair(56L, 48L), Pair(54L, 46L), Pair(52L, 48L), Pair(58L, 44L), Pair(48L, 48L)
        )

        for ((n1, n2) in base50Pairs) {
            val solution = generator.solve(n1, n2, 50)
            assertEquals("Base 50 classification for $n1 × $n2", AnurupyenaClassification.VALID_BASE_50, solution.classification)
            assertEquals("Product mismatch for $n1 × $n2", n1 * n2, solution.product)
        }
    }

    @Test
    fun testTenHandVerifiedBase200Cases() {
        val base200Pairs = listOf(
            Pair(203L, 205L), Pair(195L, 197L), Pair(198L, 204L), Pair(192L, 196L), Pair(206L, 208L),
            Pair(194L, 206L), Pair(201L, 207L), Pair(197L, 199L), Pair(190L, 210L), Pair(202L, 204L)
        )

        for ((n1, n2) in base200Pairs) {
            val solution = generator.solve(n1, n2, 200)
            assertEquals("Base 200 classification for $n1 × $n2", AnurupyenaClassification.VALID_BASE_200, solution.classification)
            assertEquals("Product mismatch for $n1 × $n2", n1 * n2, solution.product)
        }
    }

    @Test
    fun testFiftyRandomizedPropertyTestsTier1() {
        repeat(50) {
            val problem = generator.generateProblem(DifficultyTier.TIER_1_EASY)
            assertTrue("Operand near 50", abs(problem.operand - 50L) <= 15L)
            assertTrue(problem.correctAnswer > 0)
            assertTrue(problem.decompositionSteps.isNotEmpty())
            assertTrue(!problem.distractors.contains(problem.correctAnswer))

            val match = Regex("Calculate (\\d+) × (\\d+)").find(problem.questionText)
            assertNotNull("Question text format match", match)
            val num1 = match!!.groupValues[1].toLong()
            val num2 = match.groupValues[2].toLong()
            assertEquals("Direct product match", num1 * num2, problem.correctAnswer)
        }
    }

    @Test
    fun testFiftyRandomizedPropertyTestsTier2() {
        repeat(50) {
            val problem = generator.generateProblem(DifficultyTier.TIER_2_HARD)
            assertTrue(problem.correctAnswer > 0)
            assertTrue(problem.decompositionSteps.isNotEmpty())
            assertTrue(!problem.distractors.contains(problem.correctAnswer))

            val match = Regex("Calculate (\\d+) × (\\d+)").find(problem.questionText)
            assertNotNull("Question text format match", match)
            val num1 = match!!.groupValues[1].toLong()
            val num2 = match.groupValues[2].toLong()
            assertEquals("Direct product match", num1 * num2, problem.correctAnswer)
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
