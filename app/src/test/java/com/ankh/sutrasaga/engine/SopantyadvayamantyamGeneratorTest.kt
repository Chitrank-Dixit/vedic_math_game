package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.domain.models.DifficultyTier
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SopantyadvayamantyamGeneratorTest {

    private lateinit var generator: SopantyadvayamantyamGenerator

    @Before
    fun setUp() {
        generator = SopantyadvayamantyamGenerator()
    }

    @Test
    fun testCanonicalExample1_143_times_12() {
        val sol = generator.solveMultiplication(143, 12)

        assertEquals(143L, sol.multiplicand)
        assertEquals(12, sol.multiplier)
        assertEquals(2, sol.n)
        assertEquals(listOf(0, 1, 4, 3, 0), sol.sandwichedDigits.sandwiched)
        assertEquals(listOf(1, 6, 11, 6), sol.rawValues.map { it.rawValue })
        assertEquals(listOf(1, 7, 1, 6), sol.finalDigits)
        assertEquals(1716L, sol.product)
        assertEquals(143L * 12L, sol.product)
    }

    @Test
    fun testSeedExample2_124_times_14() {
        val sol = generator.solveMultiplication(124, 14)

        assertEquals(4, sol.n)
        assertEquals(listOf(0, 1, 2, 4, 0), sol.sandwichedDigits.sandwiched)
        assertEquals(listOf(1, 6, 12, 16), sol.rawValues.map { it.rawValue })
        assertEquals(listOf(1, 7, 3, 6), sol.finalDigits)
        assertEquals(1736L, sol.product)
        assertEquals(124L * 14L, sol.product)
    }

    @Test
    fun testSeedExample3_253_times_19_compounding_carries() {
        val sol = generator.solveMultiplication(253, 19)

        assertEquals(9, sol.n)
        assertEquals(listOf(0, 2, 5, 3, 0), sol.sandwichedDigits.sandwiched)
        assertEquals(listOf(2, 23, 48, 27), sol.rawValues.map { it.rawValue })
        assertEquals(listOf(4, 8, 0, 7), sol.finalDigits)
        assertEquals(4807L, sol.product)
        assertEquals(253L * 19L, sol.product)
    }

    @Test
    fun test20HandVerifiedCasesMultipliers12To19() {
        val cases = listOf(
            Pair(12L, 12) to 144L,
            Pair(25L, 12) to 300L,
            Pair(34L, 13) to 442L,
            Pair(51L, 13) to 663L,
            Pair(62L, 14) to 868L,
            Pair(111L, 14) to 1554L,
            Pair(43L, 15) to 645L,
            Pair(123L, 15) to 1845L,
            Pair(72L, 16) to 1152L,
            Pair(214L, 16) to 3424L,
            Pair(31L, 17) to 527L,
            Pair(105L, 17) to 1785L,
            Pair(44L, 18) to 792L,
            Pair(222L, 18) to 3996L,
            Pair(15L, 19) to 285L,
            Pair(101L, 19) to 1919L,
            Pair(502L, 12) to 6024L,
            Pair(313L, 13) to 4069L,
            Pair(404L, 14) to 5656L,
            Pair(707L, 15) to 10605L
        )

        for ((input, expected) in cases) {
            val sol = generator.solveMultiplication(input.first, input.second)
            assertEquals("Product mismatch for ${input.first} × ${input.second}", expected, sol.product)
            assertEquals("Algorithm result must match direct multiplication", input.first * input.second, sol.product)
        }
    }

    @Test
    fun testCompoundingChainedCarryCases() {
        val chainedCases = listOf(
            Pair(999L, 19),
            Pair(888L, 18),
            Pair(777L, 17),
            Pair(987L, 19),
            Pair(899L, 18)
        )

        for ((m, mult) in chainedCases) {
            val sol = generator.solveMultiplication(m, mult)
            assertEquals(m * mult, sol.product)
            assertTrue("Must contain multiple carries >= 1", sol.carrySteps.count { it.outgoingCarry > 0 } >= 2)
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun testRejectsMultiplierUnder12() {
        generator.buildProblem(100, 11, DifficultyTier.TIER_1_EASY)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testRejectsMultiplierOver19() {
        generator.buildProblem(100, 20, DifficultyTier.TIER_1_EASY)
    }

    @Test
    fun testRandomizedPropertyTestTier1() {
        repeat(50) {
            val problem = generator.generateSopantyaProblem(DifficultyTier.TIER_1_EASY)
            val m = problem.multiplicand
            val mult = problem.multiplier
            val sol = generator.solveMultiplication(m, mult)

            assertEquals(m * mult, sol.product)
            assertEquals(sol.product, problem.solution.product)
        }
    }

    @Test
    fun testRandomizedPropertyTestTier2() {
        repeat(50) {
            val problem = generator.generateSopantyaProblem(DifficultyTier.TIER_2_HARD)
            val m = problem.multiplicand
            val mult = problem.multiplier
            val sol = generator.solveMultiplication(m, mult)

            assertEquals(m * mult, sol.product)
            assertEquals(sol.product, problem.solution.product)
        }
    }
}
