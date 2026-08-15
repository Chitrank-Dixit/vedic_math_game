package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.Fraction
import com.ankh.sutrasaga.domain.models.LinearEquation
import com.ankh.sutrasaga.domain.models.SankalanaClassification
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SankalanaVyavakalanabhyamGeneratorTest {

    private lateinit var generator: SankalanaVyavakalanabhyamGenerator

    @Before
    fun setUp() {
        generator = SankalanaVyavakalanabhyamGenerator()
    }

    @Test
    fun testCanonicalExample1_45x_minus_23y_eq_113() {
        val eq1 = LinearEquation(45, -23, 113)
        val eq2 = LinearEquation(23, -45, 91)
        val sol = generator.solveSystem(eq1, eq2)

        assertEquals(SankalanaClassification.UNIQUE_SOLUTION, sol.classification)
        assertEquals(Fraction(1, 1), sol.xPlusY.reduced)
        assertEquals(Fraction(3, 1), sol.xMinusY.reduced)
        assertEquals(Fraction(2, 1), sol.xSolution.reduced)
        assertEquals(Fraction(-1, 1), sol.ySolution.reduced)

        // Exact verification in original equations
        assertEquals(113L, eq1.a * sol.xSolution.numerator + eq1.b * sol.ySolution.numerator)
        assertEquals(91L, eq2.a * sol.xSolution.numerator + eq2.b * sol.ySolution.numerator)
    }

    @Test
    fun testPositiveBeginnerExample2_4x_plus_7y_eq_5() {
        val eq1 = LinearEquation(4, 7, 5)
        val eq2 = LinearEquation(7, 4, 17)
        val sol = generator.solveSystem(eq1, eq2)

        assertEquals(SankalanaClassification.UNIQUE_SOLUTION, sol.classification)
        assertEquals(Fraction(2, 1), sol.xPlusY.reduced)
        assertEquals(Fraction(4, 1), sol.xMinusY.reduced)
        assertEquals(Fraction(3, 1), sol.xSolution.reduced)
        assertEquals(Fraction(-1, 1), sol.ySolution.reduced)

        assertEquals(5L, eq1.a * sol.xSolution.numerator + eq1.b * sol.ySolution.numerator)
        assertEquals(17L, eq2.a * sol.xSolution.numerator + eq2.b * sol.ySolution.numerator)
    }

    @Test
    fun test20HandVerifiedCases() {
        val cases = listOf(
            Triple(LinearEquation(5, 3, 29), LinearEquation(3, 5, 27), Pair(Fraction(4, 1), Fraction(3, 1))),
            Triple(LinearEquation(7, 2, 31), LinearEquation(2, 7, 41), Pair(Fraction(3, 1), Fraction(5, 1))),
            Triple(LinearEquation(8, 5, 31), LinearEquation(5, 8, 34), Pair(Fraction(2, 1), Fraction(3, 1))),
            Triple(LinearEquation(9, 4, 31), LinearEquation(4, 9, 21), Pair(Fraction(3, 1), Fraction(1, 1))),
            Triple(LinearEquation(6, 1, 23), LinearEquation(1, 6, -2), Pair(Fraction(4, 1), Fraction(-1, 1))),
            Triple(LinearEquation(10, 3, 29), LinearEquation(3, 10, 36), Pair(Fraction(2, 1), Fraction(3, 1))),
            Triple(LinearEquation(4, 1, 14), LinearEquation(1, 4, 11), Pair(Fraction(3, 1), Fraction(2, 1))),
            Triple(LinearEquation(5, 2, 24), LinearEquation(2, 5, 18), Pair(Fraction(4, 1), Fraction(2, 1))),
            Triple(LinearEquation(7, 3, 41), LinearEquation(3, 7, 29), Pair(Fraction(5, 1), Fraction(2, 1))),
            Triple(LinearEquation(8, 1, 26), LinearEquation(1, 8, 19), Pair(Fraction(3, 1), Fraction(2, 1))),

            Triple(LinearEquation(12, 5, 29), LinearEquation(5, 12, 22), Pair(Fraction(2, 1), Fraction(1, 1))),
            Triple(LinearEquation(15, 4, 38), LinearEquation(4, 15, 38), Pair(Fraction(2, 1), Fraction(2, 1))),
            Triple(LinearEquation(11, 3, 31), LinearEquation(3, 11, 39), Pair(Fraction(2, 1), Fraction(3, 1))),
            Triple(LinearEquation(9, 2, 25), LinearEquation(2, 9, -3), Pair(Fraction(3, 1), Fraction(-1, 1))),
            Triple(LinearEquation(14, 3, 37), LinearEquation(3, 14, 48), Pair(Fraction(2, 1), Fraction(3, 1))),
            Triple(LinearEquation(6, 5, 27), LinearEquation(5, 6, 28), Pair(Fraction(2, 1), Fraction(3, 1))),
            Triple(LinearEquation(7, 4, 32), LinearEquation(4, 7, 23), Pair(Fraction(4, 1), Fraction(1, 1))),
            Triple(LinearEquation(10, 7, 51), LinearEquation(7, 10, 51), Pair(Fraction(3, 1), Fraction(3, 1))),
            Triple(LinearEquation(8, 3, 35), LinearEquation(3, 8, 20), Pair(Fraction(4, 1), Fraction(1, 1))),
            Triple(LinearEquation(13, 2, 43), LinearEquation(2, 13, 32), Pair(Fraction(3, 1), Fraction(2, 1)))
        )

        for ((index, item) in cases.withIndex()) {
            val (eq1, eq2, expected) = item
            val sol = generator.solveSystem(eq1, eq2)
            assertEquals("Index $index classification mismatch for ${eq1.toFormattedString()} | ${eq2.toFormattedString()}", SankalanaClassification.UNIQUE_SOLUTION, sol.classification)
            assertEquals("Index $index x mismatch for ${eq1.toFormattedString()} | ${eq2.toFormattedString()}", expected.first.reduced, sol.xSolution.reduced)
            assertEquals("Index $index y mismatch for ${eq1.toFormattedString()} | ${eq2.toFormattedString()}", expected.second.reduced, sol.ySolution.reduced)
        }
    }

    @Test
    fun testExceptionalCases() {
        val notApp = generator.solveSystem(LinearEquation(3, 4, 10), LinearEquation(5, 7, 19))
        assertEquals(SankalanaClassification.NOT_APPLICABLE, notApp.classification)

        val infSol = generator.solveSystem(LinearEquation(3, 3, 12), LinearEquation(3, 3, 12))
        assertEquals(SankalanaClassification.INFINITE_SOLUTIONS, infSol.classification)

        val inconst = generator.solveSystem(LinearEquation(3, 3, 12), LinearEquation(3, 3, 20))
        assertEquals(SankalanaClassification.INCONSISTENT, inconst.classification)
    }

    @Test
    fun testRandomizedPropertyTestTier1() {
        repeat(50) {
            val pProblem = generator.generateSankalanaProblem(DifficultyTier.TIER_1_EASY)

            val x = pProblem.xSolution.reduced
            val y = pProblem.ySolution.reduced

            val eq1Num = pProblem.firstEquation.a * x.numerator * y.denominator + pProblem.firstEquation.b * y.numerator * x.denominator
            val eq1Den = x.denominator * y.denominator
            assertEquals(pProblem.firstEquation.c, eq1Num / eq1Den)

            val eq2Num = pProblem.secondEquation.a * x.numerator * y.denominator + pProblem.secondEquation.b * y.numerator * x.denominator
            val eq2Den = x.denominator * y.denominator
            assertEquals(pProblem.secondEquation.c, eq2Num / eq2Den)

            val det = pProblem.firstEquation.a * pProblem.firstEquation.a - pProblem.firstEquation.b * pProblem.firstEquation.b
            assertTrue("Determinant must be non-zero", det != 0L)
        }
    }

    @Test
    fun testRandomizedPropertyTestTier2() {
        repeat(50) {
            val pProblem = generator.generateSankalanaProblem(DifficultyTier.TIER_2_HARD)

            val x = pProblem.xSolution.reduced
            val y = pProblem.ySolution.reduced

            val eq1Num = pProblem.firstEquation.a * x.numerator * y.denominator + pProblem.firstEquation.b * y.numerator * x.denominator
            val eq1Den = x.denominator * y.denominator
            assertEquals(pProblem.firstEquation.c, eq1Num / eq1Den)

            val det = pProblem.firstEquation.a * pProblem.firstEquation.a - pProblem.firstEquation.b * pProblem.firstEquation.b
            assertTrue("Determinant must be non-zero", det != 0L)
        }
    }
}
