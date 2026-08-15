package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.Fraction
import com.ankh.sutrasaga.domain.models.QuadraticClassification
import com.ankh.sutrasaga.domain.models.QuadraticEquation
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PuranapuranabhyamGeneratorTest {

    private lateinit var generator: PuranapuranabhyamGenerator

    @Before
    fun setUp() {
        generator = PuranapuranabhyamGenerator()
    }

    @Test
    fun testMonicBeginnerExample1_x2_plus_6x_plus_8_eq_0() {
        val eq = QuadraticEquation(1, 6, 8)
        val sol = generator.solveQuadratic(eq)

        assertEquals(QuadraticClassification.TWO_REAL_ROOTS, sol.classification)
        assertEquals(Fraction(-2, 1), sol.root1.reduced)
        assertEquals(Fraction(-4, 1), sol.root2.reduced)

        // Verification in original equation: A*r^2 + B*r + C == 0
        val r1 = sol.root1.reduced.numerator
        val r2 = sol.root2.reduced.numerator
        assertEquals(0L, eq.a * r1 * r1 + eq.b * r1 + eq.c)
        assertEquals(0L, eq.a * r2 * r2 + eq.b * r2 + eq.c)
    }

    @Test
    fun testNonMonicExample2_2x2_plus_5x_minus_3_eq_0() {
        val eq = QuadraticEquation(2, 5, -3)
        val sol = generator.solveQuadratic(eq)

        assertEquals(QuadraticClassification.TWO_REAL_ROOTS, sol.classification)
        assertEquals(Fraction(1, 2), sol.root1.reduced)
        assertEquals(Fraction(-3, 1), sol.root2.reduced)

        // Exact substitution: 2(1/2)^2 + 5(1/2) - 3 = 2/4 + 5/2 - 3 = 0
        val r1 = sol.root1.reduced
        val lhs1Num = eq.a * r1.numerator * r1.numerator + eq.b * r1.numerator * r1.denominator + eq.c * r1.denominator * r1.denominator
        assertEquals(0L, lhs1Num)

        val r2 = sol.root2.reduced
        val lhs2Num = eq.a * r2.numerator * r2.numerator + eq.b * r2.numerator * r2.denominator + eq.c * r2.denominator * r2.denominator
        assertEquals(0L, lhs2Num)
    }

    @Test
    fun testRepeatedRootExample3_x2_minus_6x_plus_9_eq_0() {
        val eq = QuadraticEquation(1, -6, 9)
        val sol = generator.solveQuadratic(eq)

        assertEquals(QuadraticClassification.REPEATED_REAL_ROOT, sol.classification)
        assertTrue(sol.isRepeated)
        assertEquals(Fraction(3, 1), sol.root1.reduced)
        assertEquals(Fraction(3, 1), sol.root2.reduced)
    }

    @Test
    fun testNoRealRootsExample4_x2_plus_2x_plus_5_eq_0() {
        val eq = QuadraticEquation(1, 2, 5)
        val sol = generator.solveQuadratic(eq)

        assertEquals(QuadraticClassification.NO_REAL_ROOTS, sol.classification)
        assertTrue("Discriminant must be negative", sol.discriminant < 0L)
    }

    @Test
    fun test20HandVerifiedQuadratics() {
        val cases = listOf(
            // Monic cases
            Triple(QuadraticEquation(1, 5, 6), Pair(Fraction(-2, 1), Fraction(-3, 1)), QuadraticClassification.TWO_REAL_ROOTS),
            Triple(QuadraticEquation(1, -5, 6), Pair(Fraction(3, 1), Fraction(2, 1)), QuadraticClassification.TWO_REAL_ROOTS),
            Triple(QuadraticEquation(1, 2, -8), Pair(Fraction(2, 1), Fraction(-4, 1)), QuadraticClassification.TWO_REAL_ROOTS),
            Triple(QuadraticEquation(1, -2, -15), Pair(Fraction(5, 1), Fraction(-3, 1)), QuadraticClassification.TWO_REAL_ROOTS),
            Triple(QuadraticEquation(1, 7, 12), Pair(Fraction(-3, 1), Fraction(-4, 1)), QuadraticClassification.TWO_REAL_ROOTS),
            Triple(QuadraticEquation(1, -7, 10), Pair(Fraction(5, 1), Fraction(2, 1)), QuadraticClassification.TWO_REAL_ROOTS),
            Triple(QuadraticEquation(1, 0, -9), Pair(Fraction(3, 1), Fraction(-3, 1)), QuadraticClassification.TWO_REAL_ROOTS),
            Triple(QuadraticEquation(1, 0, -16), Pair(Fraction(4, 1), Fraction(-4, 1)), QuadraticClassification.TWO_REAL_ROOTS),
            Triple(QuadraticEquation(1, 4, 4), Pair(Fraction(-2, 1), Fraction(-2, 1)), QuadraticClassification.REPEATED_REAL_ROOT),
            Triple(QuadraticEquation(1, -4, 4), Pair(Fraction(2, 1), Fraction(2, 1)), QuadraticClassification.REPEATED_REAL_ROOT),

            // Non-monic cases (10 non-monic)
            Triple(QuadraticEquation(2, 7, 3), Pair(Fraction(-1, 2), Fraction(-3, 1)), QuadraticClassification.TWO_REAL_ROOTS),
            Triple(QuadraticEquation(3, 10, 3), Pair(Fraction(-1, 3), Fraction(-3, 1)), QuadraticClassification.TWO_REAL_ROOTS),
            Triple(QuadraticEquation(2, -7, 3), Pair(Fraction(3, 1), Fraction(1, 2)), QuadraticClassification.TWO_REAL_ROOTS),
            Triple(QuadraticEquation(3, -10, 3), Pair(Fraction(3, 1), Fraction(1, 3)), QuadraticClassification.TWO_REAL_ROOTS),
            Triple(QuadraticEquation(2, 3, -2), Pair(Fraction(1, 2), Fraction(-2, 1)), QuadraticClassification.TWO_REAL_ROOTS),

            Triple(QuadraticEquation(3, 5, -2), Pair(Fraction(1, 3), Fraction(-2, 1)), QuadraticClassification.TWO_REAL_ROOTS),
            Triple(QuadraticEquation(4, 4, 1), Pair(Fraction(-1, 2), Fraction(-1, 2)), QuadraticClassification.REPEATED_REAL_ROOT),
            Triple(QuadraticEquation(2, -5, 2), Pair(Fraction(2, 1), Fraction(1, 2)), QuadraticClassification.TWO_REAL_ROOTS),
            Triple(QuadraticEquation(3, -7, 2), Pair(Fraction(2, 1), Fraction(1, 3)), QuadraticClassification.TWO_REAL_ROOTS),
            Triple(QuadraticEquation(2, 1, -6), Pair(Fraction(3, 2), Fraction(-2, 1)), QuadraticClassification.TWO_REAL_ROOTS)
        )

        for ((eq, expectedRoots, expectedClassif) in cases) {
            val sol = generator.solveQuadratic(eq)
            assertEquals("Classification mismatch for ${eq.toFormattedString()}", expectedClassif, sol.classification)
            assertEquals("Root 1 mismatch for ${eq.toFormattedString()}", expectedRoots.first.reduced, sol.root1.reduced)
            assertEquals("Root 2 mismatch for ${eq.toFormattedString()}", expectedRoots.second.reduced, sol.root2.reduced)
        }
    }

    @Test
    fun testRandomizedPropertyTestTier1() {
        repeat(50) {
            val pProblem = generator.generateQuadraticProblem(DifficultyTier.TIER_1_EASY)
            assertTrue(
                "Classification must be TWO_REAL_ROOTS or REPEATED_REAL_ROOT",
                pProblem.classification == QuadraticClassification.TWO_REAL_ROOTS || pProblem.classification == QuadraticClassification.REPEATED_REAL_ROOT
            )

            val eq = pProblem.equation
            val r1 = pProblem.root1.reduced.numerator
            val r2 = pProblem.root2.reduced.numerator

            assertEquals("Root 1 must satisfy equation", 0L, eq.a * r1 * r1 + eq.b * r1 + eq.c)
            assertEquals("Root 2 must satisfy equation", 0L, eq.a * r2 * r2 + eq.b * r2 + eq.c)
        }
    }

    @Test
    fun testRandomizedPropertyTestTier2() {
        repeat(50) {
            val pProblem = generator.generateQuadraticProblem(DifficultyTier.TIER_2_HARD)
            val eq = pProblem.equation

            val r1 = pProblem.root1.reduced
            val lhs1Num = eq.a * r1.numerator * r1.numerator + eq.b * r1.numerator * r1.denominator + eq.c * r1.denominator * r1.denominator
            assertEquals("Root 1 substitution into A*x^2 + B*x + C must be 0", 0L, lhs1Num)

            val r2 = pProblem.root2.reduced
            val lhs2Num = eq.a * r2.numerator * r2.numerator + eq.b * r2.numerator * r2.denominator + eq.c * r2.denominator * r2.denominator
            assertEquals("Root 2 substitution into A*x^2 + B*x + C must be 0", 0L, lhs2Num)
        }
    }
}
