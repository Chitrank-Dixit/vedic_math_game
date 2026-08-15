package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.GunakasamuccayahClassification
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GunakasamuccayahGeneratorTest {

    private lateinit var generator: GunakasamuccayahGenerator

    @Before
    fun setUp() {
        generator = GunakasamuccayahGenerator()
    }

    @Test
    fun testCanonicalExample1_x_sq_plus_7x_plus_10() {
        val sol = generator.solveQuadratic(7, 10)

        assertEquals(GunakasamuccayahClassification.FACTORED, sol.classification)
        assertNotNull(sol.matchingPair)
        assertEquals(10L, sol.matchingPair!!.product())
        assertEquals(7L, sol.matchingPair!!.sum())
        assertEquals(setOf(-2L, -5L), sol.roots.toSet())

        // Root substitution verification
        for (root in sol.roots) {
            assertEquals("Root $root must satisfy x² + 7x + 10 = 0", 0L, sol.quadratic.eval(root))
        }
    }

    @Test
    fun testNegativeConstantExample2_x_sq_plus_x_minus_6() {
        val sol = generator.solveQuadratic(1, -6)

        assertEquals(GunakasamuccayahClassification.FACTORED, sol.classification)
        assertNotNull(sol.matchingPair)
        assertEquals(-6L, sol.matchingPair!!.product())
        assertEquals(1L, sol.matchingPair!!.sum())
        assertEquals(setOf(-3L, 2L), sol.roots.toSet())

        for (root in sol.roots) {
            assertEquals("Root $root must satisfy x² + x - 6 = 0", 0L, sol.quadratic.eval(root))
        }
    }

    @Test
    fun testNegativeMiddleTermExample3_x_sq_minus_9x_plus_20() {
        val sol = generator.solveQuadratic(-9, 20)

        assertEquals(GunakasamuccayahClassification.FACTORED, sol.classification)
        assertNotNull(sol.matchingPair)
        assertEquals(20L, sol.matchingPair!!.product())
        assertEquals(-9L, sol.matchingPair!!.sum())
        assertEquals(setOf(4L, 5L), sol.roots.toSet())

        for (root in sol.roots) {
            assertEquals("Root $root must satisfy x² - 9x + 20 = 0", 0L, sol.quadratic.eval(root))
        }
    }

    @Test
    fun testUnsupportedNoIntegerFactorPairExample4_x_sq_plus_x_plus_1() {
        val sol = generator.solveQuadratic(1, 1)

        assertEquals(GunakasamuccayahClassification.NO_INTEGER_FACTOR_PAIR, sol.classification)
        assertNull(sol.matchingPair)
        assertTrue(sol.roots.isEmpty())
    }

    @Test
    fun testRepeatedFactorCase_x_sq_plus_6x_plus_9() {
        val sol = generator.solveQuadratic(6, 9)

        assertEquals(GunakasamuccayahClassification.REPEATED_FACTOR, sol.classification)
        assertNotNull(sol.matchingPair)
        assertEquals(3L, sol.matchingPair!!.p)
        assertEquals(3L, sol.matchingPair!!.q)
        assertEquals(listOf(-3L, -3L), sol.roots)
    }

    @Test
    fun testZeroConstantCase_x_sq_minus_4x() {
        val sol = generator.solveQuadratic(-4, 0)

        assertEquals(GunakasamuccayahClassification.FACTORED, sol.classification)
        assertNotNull(sol.matchingPair)
        assertEquals(0L, sol.matchingPair!!.product())
        assertEquals(-4L, sol.matchingPair!!.sum())
        assertEquals(setOf(4L, 0L), sol.roots.toSet())
    }

    @Test
    fun test20HandVerifiedFactorizableQuadratics() {
        val cases = listOf(
            Pair(5L, 6L) to listOf(-2L, -3L),
            Pair(6L, 8L) to listOf(-2L, -4L),
            Pair(7L, 12L) to listOf(-3L, -4L),
            Pair(-5L, 6L) to listOf(2L, 3L),
            Pair(-7L, 12L) to listOf(3L, 4L),
            Pair(-1L, -12L) to listOf(4L, -3L),
            Pair(2L, -15L) to listOf(-5L, 3L),
            Pair(-2L, -15L) to listOf(5L, -3L),
            Pair(0L, -16L) to listOf(4L, -4L),
            Pair(0L, -25L) to listOf(5L, -5L),

            Pair(8L, 15L) to listOf(-3L, -5L),
            Pair(9L, 18L) to listOf(-3L, -6L),
            Pair(-10L, 21L) to listOf(3L, 7L),
            Pair(-11L, 24L) to listOf(3L, 8L),
            Pair(10L, 25L) to listOf(-5L, -5L),
            Pair(-12L, 36L) to listOf(6L, 6L),
            Pair(1L, -20L) to listOf(-5L, 4L),
            Pair(-1L, -20L) to listOf(5L, -4L),
            Pair(3L, -28L) to listOf(-7L, 4L),
            Pair(-3L, -28L) to listOf(7L, -4L)
        )

        for ((input, expectedRoots) in cases) {
            val sol = generator.solveQuadratic(input.first, input.second)
            assertTrue("Factorization must be successful for B=${input.first}, C=${input.second}", sol.classification == GunakasamuccayahClassification.FACTORED || sol.classification == GunakasamuccayahClassification.REPEATED_FACTOR)
            for (root in sol.roots) {
                assertEquals("Root substitution verification for x² + ${input.first}x + ${input.second} = 0", 0L, sol.quadratic.eval(root))
            }
        }
    }

    @Test
    fun test20UnsupportedNoIntegerFactorPairCases() {
        val cases = listOf(
            Pair(1L, 1L), Pair(1L, 2L), Pair(1L, 3L), Pair(1L, 4L), Pair(1L, 5L),
            Pair(2L, 2L), Pair(2L, 4L), Pair(3L, 1L), Pair(3L, 3L), Pair(3L, 5L),
            Pair(4L, 1L), Pair(4L, 2L), Pair(4L, 7L), Pair(4L, 5L), Pair(5L, 1L),
            Pair(5L, 2L), Pair(5L, 3L), Pair(5L, 8L), Pair(5L, 5L), Pair(1L, 7L)
        )

        for (input in cases) {
            val sol = generator.solveQuadratic(input.first, input.second)
            assertEquals("Must be classified as NO_INTEGER_FACTOR_PAIR for B=${input.first}, C=${input.second}", GunakasamuccayahClassification.NO_INTEGER_FACTOR_PAIR, sol.classification)
            assertNull(sol.matchingPair)
        }
    }

    @Test
    fun testRandomizedPropertyTestTier1() {
        repeat(50) {
            val problem = generator.generateMonicProblem(DifficultyTier.TIER_1_EASY)
            val sol = problem.solution
            if (sol.matchingPair != null) {
                for (root in sol.roots) {
                    assertEquals(0L, sol.quadratic.eval(root))
                }
            }
        }
    }

    @Test
    fun testRandomizedPropertyTestTier2() {
        repeat(50) {
            val problem = generator.generateMonicProblem(DifficultyTier.TIER_2_HARD)
            val sol = problem.solution
            if (sol.matchingPair != null) {
                for (root in sol.roots) {
                    assertEquals(0L, sol.quadratic.eval(root))
                }
            }
        }
    }
}
