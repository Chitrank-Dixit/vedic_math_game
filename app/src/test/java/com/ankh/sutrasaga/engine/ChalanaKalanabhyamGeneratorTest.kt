package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.domain.models.ChalanaClassification
import com.ankh.sutrasaga.domain.models.DifficultyTier
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ChalanaKalanabhyamGeneratorTest {

    private lateinit var generator: ChalanaKalanabhyamGenerator

    @Before
    fun setUp() {
        generator = ChalanaKalanabhyamGenerator()
    }

    @Test
    fun testCanonicalExample1_x_sq_minus_5x_plus_6() {
        val sol = generator.solveQuadratic(1, -5, 6)

        assertEquals(ChalanaClassification.TWO_REAL_ROOTS, sol.classification)
        assertEquals(2L, sol.derivative.coef)
        assertEquals(-5L, sol.derivative.const)
        assertEquals(1L, sol.discriminant.value)
        assertEquals(true, sol.discriminant.isPerfectSquare)
        assertEquals(1L, sol.discriminant.squareRoot)
        assertEquals(setOf(3L, 2L), sol.roots.toSet())

        // Verification of derivative-discriminant invariant f'(r)² == D
        for (r in sol.roots) {
            val fPrimeR = sol.derivative.eval(r)
            assertEquals("f'(r)² must equal D for root $r", sol.discriminant.value, fPrimeR * fPrimeR)
            assertEquals("f(r) must equal 0 for root $r", 0L, sol.quadratic.eval(r))
        }
    }

    @Test
    fun testExactNonMonicExample2_2x_sq_minus_2x_minus_12() {
        val sol = generator.solveQuadratic(2, -2, -12)

        assertEquals(ChalanaClassification.TWO_REAL_ROOTS, sol.classification)
        assertEquals(4L, sol.derivative.coef)
        assertEquals(-2L, sol.derivative.const)
        assertEquals(100L, sol.discriminant.value)
        assertEquals(10L, sol.discriminant.squareRoot)
        assertEquals(setOf(3L, -2L), sol.roots.toSet())

        for (r in sol.roots) {
            val fPrimeR = sol.derivative.eval(r)
            assertEquals("f'(r)² must equal D for root $r", sol.discriminant.value, fPrimeR * fPrimeR)
            assertEquals("f(r) must equal 0 for root $r", 0L, sol.quadratic.eval(r))
        }
    }

    @Test
    fun testRepeatedRootExample3_x_sq_minus_6x_plus_9() {
        val sol = generator.solveQuadratic(1, -6, 9)

        assertEquals(ChalanaClassification.REPEATED_REAL_ROOT, sol.classification)
        assertEquals(0L, sol.discriminant.value)
        assertEquals(listOf(3L, 3L), sol.roots)

        for (r in sol.roots) {
            val fPrimeR = sol.derivative.eval(r)
            assertEquals(0L, fPrimeR * fPrimeR)
            assertEquals(0L, sol.quadratic.eval(r))
        }
    }

    @Test
    fun testNoRealRootsExample4_x_sq_plus_2x_plus_5() {
        val sol = generator.solveQuadratic(1, 2, 5)

        assertEquals(ChalanaClassification.NO_REAL_ROOTS, sol.classification)
        assertEquals(-16L, sol.discriminant.value)
        assertTrue(sol.roots.isEmpty())
    }

    @Test
    fun testIrrationalRootsDeferredExample5_2x_sq_minus_x_minus_12() {
        val sol = generator.solveQuadratic(2, -1, -12)

        assertEquals(ChalanaClassification.IRRATIONAL_ROOTS_DEFERRED, sol.classification)
        assertEquals(97L, sol.discriminant.value)
        assertEquals(false, sol.discriminant.isPerfectSquare)
        assertTrue(sol.roots.isEmpty())
    }

    @Test
    fun testNotQuadraticCase_A_is_0() {
        val sol = generator.solveQuadratic(0, 3, 5)

        assertEquals(ChalanaClassification.NOT_QUADRATIC, sol.classification)
        assertTrue(sol.roots.isEmpty())
    }

    @Test
    fun test20HandVerifiedSupportedQuadratics() {
        val cases = listOf(
            Triple(1L, -7L, 12L) to setOf(4L, 3L),
            Triple(1L, 7L, 10L) to setOf(-2L, -5L),
            Triple(1L, 1L, -6L) to setOf(-3L, 2L),
            Triple(1L, -9L, 20L) to setOf(4L, 5L),
            Triple(1L, 0L, -16L) to setOf(4L, -4L),
            Triple(1L, 0L, -25L) to setOf(5L, -5L),
            Triple(1L, -8L, 15L) to setOf(3L, 5L),
            Triple(1L, 10L, 25L) to setOf(-5L),
            Triple(1L, -12L, 36L) to setOf(6L),
            Triple(1L, -1L, -20L) to setOf(5L, -4L),

            Triple(2L, -10L, 12L) to setOf(3L, 2L),
            Triple(2L, 4L, -30L) to setOf(-5L, 3L),
            Triple(3L, -15L, 18L) to setOf(3L, 2L),
            Triple(2L, -14L, 24L) to setOf(4L, 3L),
            Triple(3L, 3L, -18L) to setOf(-3L, 2L),
            Triple(4L, -16L, 16L) to setOf(2L),
            Triple(2L, -16L, 30L) to setOf(5L, 3L),
            Triple(3L, -27L, 60L) to setOf(5L, 4L),
            Triple(2L, -8L, -24L) to setOf(6L, -2L),
            Triple(3L, 6L, -24L) to setOf(-4L, 2L)
        )

        for ((input, expectedRoots) in cases) {
            val sol = generator.solveQuadratic(input.first, input.second, input.third)
            assertTrue("Must be classified as TWO_REAL_ROOTS or REPEATED_REAL_ROOT for $input", sol.classification == ChalanaClassification.TWO_REAL_ROOTS || sol.classification == ChalanaClassification.REPEATED_REAL_ROOT)
            assertEquals("Roots for $input must match expected", expectedRoots, sol.roots.toSet())
            for (r in sol.roots) {
                val fPrimeR = sol.derivative.eval(r)
                assertEquals("f'(r)² must equal D", sol.discriminant.value, fPrimeR * fPrimeR)
                assertEquals("f(r) must equal 0", 0L, sol.quadratic.eval(r))
            }
        }
    }

    @Test
    fun test10NonMonicCases() {
        val cases = listOf(
            Triple(2L, -10L, 12L), Triple(2L, 4L, -30L), Triple(3L, -15L, 18L),
            Triple(2L, -14L, 24L), Triple(3L, 3L, -18L), Triple(4L, -16L, 16L),
            Triple(2L, -16L, 30L), Triple(3L, -27L, 60L), Triple(2L, -8L, -24L),
            Triple(3L, 6L, -24L)
        )

        for (input in cases) {
            val sol = generator.solveQuadratic(input.first, input.second, input.third)
            assertTrue(sol.classification == ChalanaClassification.TWO_REAL_ROOTS || sol.classification == ChalanaClassification.REPEATED_REAL_ROOT)
            for (r in sol.roots) {
                val fPrimeR = sol.derivative.eval(r)
                assertEquals(sol.discriminant.value, fPrimeR * fPrimeR)
                assertEquals(0L, sol.quadratic.eval(r))
            }
        }
    }

    @Test
    fun testRandomizedPropertyTestTier1() {
        repeat(50) {
            val problem = generator.generateChalanaProblem(DifficultyTier.TIER_1_EASY)
            val sol = problem.solution
            if (sol.classification == ChalanaClassification.TWO_REAL_ROOTS || sol.classification == ChalanaClassification.REPEATED_REAL_ROOT) {
                for (r in sol.roots) {
                    val fPrimeR = sol.derivative.eval(r)
                    assertEquals(sol.discriminant.value, fPrimeR * fPrimeR)
                    assertEquals(0L, sol.quadratic.eval(r))
                }
            }
        }
    }

    @Test
    fun testRandomizedPropertyTestTier2() {
        repeat(50) {
            val problem = generator.generateChalanaProblem(DifficultyTier.TIER_2_HARD)
            val sol = problem.solution
            if (sol.classification == ChalanaClassification.TWO_REAL_ROOTS || sol.classification == ChalanaClassification.REPEATED_REAL_ROOT) {
                for (r in sol.roots) {
                    val fPrimeR = sol.derivative.eval(r)
                    assertEquals(sol.discriminant.value, fPrimeR * fPrimeR)
                    assertEquals(0L, sol.quadratic.eval(r))
                }
            }
        }
    }
}
