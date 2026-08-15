package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.domain.models.AnurupyeCase
import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.Fraction
import com.ankh.sutrasaga.domain.models.LinearEquation
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AnurupyeShunyamanyatGeneratorTest {

    private lateinit var generator: AnurupyeShunyamanyatGenerator

    @Before
    fun setUp() {
        generator = AnurupyeShunyamanyatGenerator()
    }

    @Test
    fun testCanonicalExample1_3x_plus_2y_eq_12() {
        val eq1 = LinearEquation(3, 2, 12)
        val eq2 = LinearEquation(6, 5, 24)
        val sol = generator.solveSystem(eq1, eq2)

        assertEquals(AnurupyeCase.Y_ZERO, sol.caseType)
        assertEquals(Fraction(4, 1), sol.xSolution.reduced)
        assertEquals(Fraction(0, 1), sol.ySolution.reduced)

        // Verification in Eq 1 & Eq 2
        assertEquals(12L, eq1.a * sol.xSolution.numerator + eq1.b * sol.ySolution.numerator)
        assertEquals(24L, eq2.a * sol.xSolution.numerator + eq2.b * sol.ySolution.numerator)
    }

    @Test
    fun testSymmetricExample2_6x_plus_7y_eq_8() {
        val eq1 = LinearEquation(6, 7, 8)
        val eq2 = LinearEquation(19, 14, 16)
        val sol = generator.solveSystem(eq1, eq2)

        assertEquals(AnurupyeCase.X_ZERO, sol.caseType)
        assertEquals(Fraction(0, 1), sol.xSolution.reduced)
        assertEquals(Fraction(8, 7), sol.ySolution.reduced)

        // Exact rational substitution into Eq 1: 6(0) + 7(8/7) = 8
        val eq1LhsNum = eq1.a * sol.xSolution.numerator * sol.ySolution.denominator + eq1.b * sol.ySolution.numerator * sol.xSolution.denominator
        val eq1LhsDen = sol.xSolution.denominator * sol.ySolution.denominator
        assertEquals(eq1.c, eq1LhsNum / eq1LhsDen)

        // Exact rational substitution into Eq 2: 19(0) + 14(8/7) = 16
        val eq2LhsNum = eq2.a * sol.xSolution.numerator * sol.ySolution.denominator + eq2.b * sol.ySolution.numerator * sol.xSolution.denominator
        val eq2LhsDen = sol.xSolution.denominator * sol.ySolution.denominator
        assertEquals(eq2.c, eq2LhsNum / eq2LhsDen)
    }

    @Test
    fun test20HandVerifiedCases() {
        val cases = listOf(
            Triple(LinearEquation(2, 3, 10), LinearEquation(4, 5, 20), Pair(AnurupyeCase.Y_ZERO, Fraction(5, 1))),
            Triple(LinearEquation(5, 4, 15), LinearEquation(10, 9, 30), Pair(AnurupyeCase.Y_ZERO, Fraction(3, 1))),
            Triple(LinearEquation(7, 2, 21), LinearEquation(14, 8, 42), Pair(AnurupyeCase.Y_ZERO, Fraction(3, 1))),
            Triple(LinearEquation(4, 9, 12), LinearEquation(8, 11, 24), Pair(AnurupyeCase.Y_ZERO, Fraction(3, 1))),
            Triple(LinearEquation(1, 5, 7), LinearEquation(3, 8, 21), Pair(AnurupyeCase.Y_ZERO, Fraction(7, 1))),
            Triple(LinearEquation(3, 8, 9), LinearEquation(9, 10, 27), Pair(AnurupyeCase.Y_ZERO, Fraction(3, 1))),
            Triple(LinearEquation(4, 2, 8), LinearEquation(12, 5, 24), Pair(AnurupyeCase.Y_ZERO, Fraction(2, 1))),
            Triple(LinearEquation(5, 6, 25), LinearEquation(15, 7, 75), Pair(AnurupyeCase.Y_ZERO, Fraction(5, 1))),
            Triple(LinearEquation(8, 3, 16), LinearEquation(16, 9, 32), Pair(AnurupyeCase.Y_ZERO, Fraction(2, 1))),
            Triple(LinearEquation(9, 4, 18), LinearEquation(27, 10, 54), Pair(AnurupyeCase.Y_ZERO, Fraction(2, 1))),

            Triple(LinearEquation(2, 5, 15), LinearEquation(7, 10, 30), Pair(AnurupyeCase.X_ZERO, Fraction(3, 1))),
            Triple(LinearEquation(8, 3, 12), LinearEquation(9, 6, 24), Pair(AnurupyeCase.X_ZERO, Fraction(4, 1))),
            Triple(LinearEquation(4, 7, 28), LinearEquation(11, 14, 56), Pair(AnurupyeCase.X_ZERO, Fraction(4, 1))),
            Triple(LinearEquation(5, 2, 10), LinearEquation(13, 6, 30), Pair(AnurupyeCase.X_ZERO, Fraction(5, 1))),
            Triple(LinearEquation(6, 9, 18), LinearEquation(15, 18, 36), Pair(AnurupyeCase.X_ZERO, Fraction(2, 1))),
            Triple(LinearEquation(7, 4, 16), LinearEquation(12, 8, 32), Pair(AnurupyeCase.X_ZERO, Fraction(4, 1))),
            Triple(LinearEquation(3, 5, 20), LinearEquation(8, 10, 40), Pair(AnurupyeCase.X_ZERO, Fraction(4, 1))),
            Triple(LinearEquation(9, 2, 14), LinearEquation(10, 4, 28), Pair(AnurupyeCase.X_ZERO, Fraction(7, 1))),
            Triple(LinearEquation(4, 3, 15), LinearEquation(5, 9, 45), Pair(AnurupyeCase.X_ZERO, Fraction(5, 1))),
            Triple(LinearEquation(6, 5, 30), LinearEquation(7, 10, 60), Pair(AnurupyeCase.X_ZERO, Fraction(6, 1)))
        )

        for ((eq1, eq2, expected) in cases) {
            val sol = generator.solveSystem(eq1, eq2)
            assertEquals("Case mismatch for ${eq1.toFormattedString()} | ${eq2.toFormattedString()}", expected.first, sol.caseType)
            if (expected.first == AnurupyeCase.Y_ZERO) {
                assertEquals(expected.second.reduced, sol.xSolution.reduced)
                assertEquals(Fraction(0, 1), sol.ySolution.reduced)
            } else {
                assertEquals(Fraction(0, 1), sol.xSolution.reduced)
                assertEquals(expected.second.reduced, sol.ySolution.reduced)
            }
        }
    }

    @Test
    fun testExceptionalCases() {
        val notApp = generator.solveSystem(LinearEquation(3, 4, 10), LinearEquation(5, 7, 19))
        assertEquals(AnurupyeCase.NOT_APPLICABLE, notApp.caseType)

        val infSol = generator.solveSystem(LinearEquation(3, 2, 12), LinearEquation(6, 4, 24))
        assertEquals(AnurupyeCase.INFINITE_SOLUTIONS, infSol.caseType)

        val inconst = generator.solveSystem(LinearEquation(3, 2, 12), LinearEquation(3, 2, 20))
        assertEquals(AnurupyeCase.INCONSISTENT, inconst.caseType)
    }

    @Test
    fun testRandomizedPropertyTestTier1() {
        repeat(50) {
            val pProblem = generator.generateAnurupyeProblem(DifficultyTier.TIER_1_EASY)

            val x = pProblem.xSolution.reduced
            val y = pProblem.ySolution.reduced

            assertTrue("One variable must be zero", x == Fraction(0, 1) || y == Fraction(0, 1))

            val eq1Num = pProblem.firstEquation.a * x.numerator * y.denominator + pProblem.firstEquation.b * y.numerator * x.denominator
            val eq1Den = x.denominator * y.denominator
            assertEquals(pProblem.firstEquation.c, eq1Num / eq1Den)

            val eq2Num = pProblem.secondEquation.a * x.numerator * y.denominator + pProblem.secondEquation.b * y.numerator * x.denominator
            val eq2Den = x.denominator * y.denominator
            assertEquals(pProblem.secondEquation.c, eq2Num / eq2Den)
        }
    }

    @Test
    fun testRandomizedPropertyTestTier2() {
        repeat(50) {
            val pProblem = generator.generateAnurupyeProblem(DifficultyTier.TIER_2_HARD)

            val x = pProblem.xSolution.reduced
            val y = pProblem.ySolution.reduced

            assertTrue("One variable must be zero", x == Fraction(0, 1) || y == Fraction(0, 1))

            val eq1Num = pProblem.firstEquation.a * x.numerator * y.denominator + pProblem.firstEquation.b * y.numerator * x.denominator
            val eq1Den = x.denominator * y.denominator
            assertEquals(pProblem.firstEquation.c, eq1Num / eq1Den)
        }
    }
}
