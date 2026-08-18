package com.ankh.sutrasaga.engine.upasutras

import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.UpaSutraId
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AntyayordashakepiGeneratorTest {

    private lateinit var generator: AntyayordashakepiGenerator

    @Before
    fun setUp() {
        generator = AntyayordashakepiGenerator()
    }

    @Test
    fun testUpaSutraMetadata() {
        assertEquals(UpaSutraId.ANTYAYORDASHAKEPI, generator.upaSutraId)
        assertEquals("Antyayordashake'pi", generator.sutraName)
    }

    @Test
    fun testCanonicalExample43Times47() {
        val problem = generator.generateSpecificProblem(43)
        assertEquals(43L, problem.operand)
        assertEquals(2021L, problem.correctAnswer)
        assertEquals("Calculate 43 × 47", problem.questionText)
        assertTrue(problem.decompositionSteps.any { it.formulaDisplay.contains("4 × (4 + 1)") })
        assertTrue(problem.decompositionSteps.any { it.formulaDisplay.contains("3 × 7") })
    }

    @Test
    fun testCanonicalExample64Times66() {
        val problem = generator.generateSpecificProblem(64)
        assertEquals(64L, problem.operand)
        assertEquals(4224L, problem.correctAnswer)
    }

    @Test
    fun testCanonicalExample72Times78() {
        val problem = generator.generateSpecificProblem(72)
        assertEquals(72L, problem.operand)
        assertEquals(5616L, problem.correctAnswer)
    }

    @Test
    fun testCanonicalExample91Times99() {
        val problem = generator.generateSpecificProblem(91)
        assertEquals(91L, problem.operand)
        assertEquals(9009L, problem.correctAnswer)
        assertTrue(problem.decompositionSteps.any { it.stepResult == "09" })
    }

    @Test
    fun testCanonicalExample85Times85() {
        val problem = generator.generateSpecificProblem(85)
        assertEquals(85L, problem.operand)
        assertEquals(7225L, problem.correctAnswer)
    }

    @Test
    fun testTwentyHandVerifiedCases() {
        val testPairs = listOf(
            Pair(23L, 27L), Pair(31L, 39L), Pair(32L, 38L), Pair(34L, 36L), Pair(35L, 35L),
            Pair(41L, 49L), Pair(42L, 48L), Pair(44L, 46L), Pair(52L, 58L), Pair(53L, 57L),
            Pair(61L, 69L), Pair(63L, 67L), Pair(71L, 79L), Pair(74L, 76L), Pair(82L, 88L),
            Pair(83L, 87L), Pair(92L, 98L), Pair(93L, 97L), Pair(94L, 96L), Pair(95L, 95L)
        )

        for ((n1, n2) in testPairs) {
            val t = n1 / 10
            val u1 = n1 % 10
            val u2 = n2 % 10

            assertEquals(t, n2 / 10)
            assertEquals(10L, u1 + u2)

            val expectedProduct = n1 * n2
            val lhs = t * (t + 1)
            val rhs = u1 * u2
            val calculatedFormula = lhs * 100 + rhs

            assertEquals("Direct vs formula mismatch for $n1 × $n2", expectedProduct, calculatedFormula)

            val prob = generator.generateSpecificProblem(n1)
            assertEquals(expectedProduct, prob.correctAnswer)
        }
    }

    @Test
    fun testHardTierThreeDigitPrefixMultiplication() {
        // e.g. 123 × 127 = (12 × 13) || (3 × 7) = 15621
        val prob = generator.generateSpecificProblem(123)
        assertEquals(123L, prob.operand)
        assertEquals(15621L, prob.correctAnswer)
    }

    @Test
    fun testFiftyRandomizedPropertyTests() {
        repeat(50) {
            val tier = if (it % 2 == 0) DifficultyTier.TIER_1_EASY else DifficultyTier.TIER_2_HARD
            val problem = generator.generateProblem(tier)

            val match = Regex("Calculate (\\d+) × (\\d+)").find(problem.questionText)
            assertNotNull("Question text format match", match)

            val num1 = match!!.groupValues[1].toLong()
            val num2 = match.groupValues[2].toLong()
            val t1 = num1 / 10
            val t2 = num2 / 10
            val u1 = num1 % 10
            val u2 = num2 % 10

            assertEquals("Tens prefixes must match", t1, t2)
            assertEquals("Units must sum to 10", 10L, u1 + u2)
            assertEquals("Product must equal exact multiplication", num1 * num2, problem.correctAnswer)
            assertTrue("Decomposition steps must not be empty", problem.decompositionSteps.isNotEmpty())
            assertTrue("Distractors must not contain correct answer", !problem.distractors.contains(problem.correctAnswer))
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
