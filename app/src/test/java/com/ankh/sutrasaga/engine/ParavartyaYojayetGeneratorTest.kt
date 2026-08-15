package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.domain.models.DifficultyTier
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ParavartyaYojayetGeneratorTest {

    private lateinit var generator: ParavartyaYojayetGenerator

    @Before
    fun setUp() {
        generator = ParavartyaYojayetGenerator()
    }

    @Test
    fun testSeedExample1_1225_DividedBy_12() {
        val result = generator.solveParavartya(1225L, 12L)
        assertEquals(102L, result.quotient)
        assertEquals(1L, result.normalizedRemainder)
        assertEquals("1225 = 12 × 102 + 1", result.verificationIdentity)
    }

    @Test
    fun testSeedExample2_432_DividedBy_11() {
        val result = generator.solveParavartya(432L, 11L)
        assertEquals(39L, result.quotient)
        assertEquals(3L, result.normalizedRemainder)
        assertEquals("432 = 11 × 39 + 3", result.verificationIdentity)
    }

    @Test
    fun testSeedExample3_97_DividedBy_12() {
        val result = generator.solveParavartya(97L, 12L)
        assertEquals(8L, result.quotient)
        assertEquals(1L, result.normalizedRemainder)
        assertEquals("97 = 12 × 8 + 1", result.verificationIdentity)
    }

    @Test
    fun testHandVerifiedCases() {
        val testCases = listOf(
            Triple(144L, 12L, Pair(12L, 0L)),
            Triple(500L, 11L, Pair(45L, 5L)),
            Triple(1000L, 13L, Pair(76L, 12L)),
            Triple(2500L, 14L, Pair(178L, 8L)),
            Triple(9999L, 19L, Pair(526L, 5L)),
            Triple(81L, 12L, Pair(6L, 9L)),
            Triple(121L, 11L, Pair(11L, 0L)),
            Triple(169L, 13L, Pair(13L, 0L)),
            Triple(1050L, 15L, Pair(70L, 0L)),
            Triple(8888L, 16L, Pair(555L, 8L)),
            Triple(7654L, 17L, Pair(450L, 4L)),
            Triple(4321L, 18L, Pair(240L, 1L)),
            Triple(3000L, 11L, Pair(272L, 8L)),
            Triple(777L, 12L, Pair(64L, 9L)),
            Triple(850L, 13L, Pair(65L, 5L)),
            Triple(12345L, 101L, Pair(122L, 23L)),
            Triple(54321L, 102L, Pair(532L, 57L)),
            Triple(99999L, 105L, Pair(952L, 39L)),
            Triple(10000L, 109L, Pair(91L, 81L)),
            Triple(25000L, 103L, Pair(242L, 74L))
        )

        for ((n, d, expected) in testCases) {
            val result = generator.solveParavartya(n, d)
            assertEquals("Quotient mismatch for $n ÷ $d", expected.first, result.quotient)
            assertEquals("Remainder mismatch for $n ÷ $d", expected.second, result.normalizedRemainder)
            assertEquals(n, d * result.quotient + result.normalizedRemainder)
            assertTrue(result.normalizedRemainder in 0 until d)
        }
    }

    @Test
    fun testBoundaryCases() {
        // Dividend smaller than divisor
        val resultSmaller = generator.solveParavartya(9L, 12L)
        assertEquals(0L, resultSmaller.quotient)
        assertEquals(9L, resultSmaller.normalizedRemainder)

        // Exact multiple
        val resultMultiple = generator.solveParavartya(132L, 12L)
        assertEquals(11L, resultMultiple.quotient)
        assertEquals(0L, resultMultiple.normalizedRemainder)

        // Divisor 11
        val res11 = generator.solveParavartya(100L, 11L)
        assertEquals(9L, res11.quotient)
        assertEquals(1L, res11.normalizedRemainder)

        // Divisor 19
        val res19 = generator.solveParavartya(100L, 19L)
        assertEquals(5L, res19.quotient)
        assertEquals(5L, res19.normalizedRemainder)

        // Divisor 101
        val res101 = generator.solveParavartya(1000L, 101L)
        assertEquals(9L, res101.quotient)
        assertEquals(91L, res101.normalizedRemainder)
    }

    @Test
    fun testRandomizedPropertyTestTier1() {
        repeat(50) {
            val problem = generator.generateProblem(DifficultyTier.TIER_1_EASY)
            val n = problem.operand
            val d = problem.prefixPart
            val q = problem.correctAnswer
            val r = problem.prefixProduct

            assertEquals("Identity invariant violated", n, d * q + r)
            assertTrue("Remainder bounds invariant violated: $r for divisor $d", r in 0 until d)
            assertTrue("Distractors must be 4", problem.distractors.size == 4)
        }
    }

    @Test
    fun testRandomizedPropertyTestTier2() {
        repeat(50) {
            val problem = generator.generateProblem(DifficultyTier.TIER_2_HARD)
            val n = problem.operand
            val d = problem.prefixPart
            val q = problem.correctAnswer
            val r = problem.prefixProduct

            assertEquals("Identity invariant violated", n, d * q + r)
            assertTrue("Remainder bounds invariant violated: $r for divisor $d", r in 0 until d)
            assertTrue("Distractors must be 4", problem.distractors.size == 4)
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun testUnsupportedDivisorRejection() {
        generator.solveParavartya(100L, 25L)
    }
}
