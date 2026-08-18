package com.ankh.sutrasaga.engine.upasutras

import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.UpaSutraId
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class YavadunamRemixGeneratorTest {

    private lateinit var generator: YavadunamRemixGenerator

    @Before
    fun setUp() {
        generator = YavadunamRemixGenerator()
    }

    @Test
    fun testUpaSutraMetadata() {
        assertEquals(UpaSutraId.YAVADUNAM_TAVADUNIKRTYA_VARGANCHA_YOJAYET, generator.upaSutraId)
        assertEquals("Yavadunam Tavadunikrtya Varganca Yojayet", generator.sutraName)
    }

    @Test
    fun testCanonicalExampleBase100_97Squared_NoCarry() {
        val solution = generator.solve(97, 100)
        assertEquals(3L, solution.deficiency)
        assertEquals(94L, solution.rawLhs)
        assertEquals(9L, solution.rawRhs)
        assertEquals(0L, solution.carry)
        assertEquals(94L, solution.finalLhs)
        assertEquals("09", solution.finalRhsString)
        assertEquals(9409L, solution.product)
        assertEquals(97L * 97L, solution.product)
        assertEquals(4, solution.steps.size)
    }

    @Test
    fun testCanonicalExampleBase1000_994Squared_ThreeDigitPad() {
        val solution = generator.solve(994, 1000)
        assertEquals(6L, solution.deficiency)
        assertEquals(988L, solution.rawLhs)
        assertEquals(36L, solution.rawRhs)
        assertEquals(0L, solution.carry)
        assertEquals(988L, solution.finalLhs)
        assertEquals("036", solution.finalRhsString)
        assertEquals(988036L, solution.product)
        assertEquals(994L * 994L, solution.product)
    }

    @Test
    fun testCanonicalExampleBase100_88Squared_WithCarry() {
        val solution = generator.solve(88, 100)
        assertEquals(12L, solution.deficiency)
        assertEquals(76L, solution.rawLhs)
        assertEquals(144L, solution.rawRhs)
        assertEquals(1L, solution.carry)
        assertEquals(77L, solution.finalLhs)
        assertEquals("44", solution.finalRhsString)
        assertEquals(7744L, solution.product)
        assertEquals(88L * 88L, solution.product)
    }

    @Test
    fun testFifteenHandVerifiedNoCarryCases() {
        val cases = listOf(
            Pair(99L, 100L), Pair(98L, 100L), Pair(96L, 100L), Pair(95L, 100L), Pair(94L, 100L),
            Pair(93L, 100L), Pair(92L, 100L), Pair(91L, 100L),
            Pair(999L, 1000L), Pair(998L, 1000L), Pair(997L, 1000L), Pair(996L, 1000L),
            Pair(995L, 1000L), Pair(993L, 1000L), Pair(991L, 1000L)
        )

        for ((operand, base) in cases) {
            val solution = generator.solve(operand, base)
            assertEquals("No carry expected for $operand²", 0L, solution.carry)
            assertEquals("Product mismatch for $operand²", operand * operand, solution.product)
        }
    }

    @Test
    fun testFifteenHandVerifiedCarryForcingCases() {
        val cases = listOf(
            Pair(89L, 100L), Pair(87L, 100L), Pair(86L, 100L), Pair(85L, 100L), Pair(84L, 100L),
            Pair(83L, 100L), Pair(82L, 100L), Pair(81L, 100L), Pair(80L, 100L), Pair(79L, 100L),
            Pair(78L, 100L), Pair(77L, 100L), Pair(76L, 100L), Pair(75L, 100L), Pair(74L, 100L)
        )

        for ((operand, base) in cases) {
            val solution = generator.solve(operand, base)
            assertTrue("Carry expected for $operand²", solution.carry > 0L)
            assertEquals("Product mismatch for $operand²", operand * operand, solution.product)
        }
    }

    @Test
    fun testFiftyRandomizedPropertyTestsTier1() {
        repeat(50) {
            val problem = generator.generateProblem(DifficultyTier.TIER_1_EASY)
            assertTrue(problem.operand in 90L..99L)
            assertTrue(problem.correctAnswer > 0)
            assertEquals("Direct square verification", problem.operand * problem.operand, problem.correctAnswer)
            assertTrue(problem.decompositionSteps.isNotEmpty())
            assertTrue(!problem.distractors.contains(problem.correctAnswer))
        }
    }

    @Test
    fun testFiftyRandomizedPropertyTestsTier2() {
        repeat(50) {
            val problem = generator.generateProblem(DifficultyTier.TIER_2_HARD)
            assertTrue(problem.correctAnswer > 0)
            assertEquals("Direct square verification", problem.operand * problem.operand, problem.correctAnswer)
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
