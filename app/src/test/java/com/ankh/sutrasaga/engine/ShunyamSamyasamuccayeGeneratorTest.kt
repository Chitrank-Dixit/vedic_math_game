package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.domain.models.CommonFactorEquation
import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.EqualNumeratorFractionEquation
import com.ankh.sutrasaga.domain.models.Fraction
import com.ankh.sutrasaga.domain.models.LinearExpression
import com.ankh.sutrasaga.domain.models.ShunyamClassification
import com.ankh.sutrasaga.domain.models.ShunyamFamily
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ShunyamSamyasamuccayeGeneratorTest {

    private lateinit var generator: ShunyamSamyasamuccayeGenerator

    @Before
    fun setUp() {
        generator = ShunyamSamyasamuccayeGenerator()
    }

    // --- Family A Tests ---

    @Test
    fun testCanonicalExampleFamilyA_7_x_plus_1_eq_8_x_plus_1() {
        val eq = CommonFactorEquation(
            k1 = 7,
            factor = LinearExpression(Fraction(1, 1), Fraction(1, 1)),
            k2 = 8
        )
        val sol = generator.solveFamilyA(eq)

        assertEquals(ShunyamClassification.UNIQUE_SOLUTION, sol.classification)
        assertEquals(Fraction(-1, 1), sol.candidateSolution.reduced)

        // Exact substitution in original equation: 7(-1+1) == 8(-1+1)
        val lhs = eq.k1 * eq.factor.evaluate(sol.candidateSolution).numerator
        val rhs = eq.k2 * eq.factor.evaluate(sol.candidateSolution).numerator
        assertEquals(lhs, rhs)
    }

    @Test
    fun test20HandVerifiedFamilyACases() {
        val cases = listOf(
            Triple(CommonFactorEquation(2, LinearExpression(Fraction(1, 1), Fraction(3, 1)), 5), Fraction(-3, 1), ShunyamClassification.UNIQUE_SOLUTION),
            Triple(CommonFactorEquation(4, LinearExpression(Fraction(1, 1), Fraction(-2, 1)), 9), Fraction(2, 1), ShunyamClassification.UNIQUE_SOLUTION),
            Triple(CommonFactorEquation(3, LinearExpression(Fraction(2, 1), Fraction(6, 1)), 7), Fraction(-3, 1), ShunyamClassification.UNIQUE_SOLUTION),
            Triple(CommonFactorEquation(5, LinearExpression(Fraction(3, 1), Fraction(-9, 1)), 11), Fraction(3, 1), ShunyamClassification.UNIQUE_SOLUTION),
            Triple(CommonFactorEquation(6, LinearExpression(Fraction(1, 1), Fraction(5, 1)), 12), Fraction(-5, 1), ShunyamClassification.UNIQUE_SOLUTION),

            Triple(CommonFactorEquation(8, LinearExpression(Fraction(4, 1), Fraction(12, 1)), 15), Fraction(-3, 1), ShunyamClassification.UNIQUE_SOLUTION),
            Triple(CommonFactorEquation(10, LinearExpression(Fraction(1, 1), Fraction(-7, 1)), 3), Fraction(7, 1), ShunyamClassification.UNIQUE_SOLUTION),
            Triple(CommonFactorEquation(1, LinearExpression(Fraction(5, 1), Fraction(10, 1)), 6), Fraction(-2, 1), ShunyamClassification.UNIQUE_SOLUTION),
            Triple(CommonFactorEquation(9, LinearExpression(Fraction(2, 1), Fraction(-4, 1)), 2), Fraction(2, 1), ShunyamClassification.UNIQUE_SOLUTION),
            Triple(CommonFactorEquation(14, LinearExpression(Fraction(3, 1), Fraction(15, 1)), 20), Fraction(-5, 1), ShunyamClassification.UNIQUE_SOLUTION),

            Triple(CommonFactorEquation(7, LinearExpression(Fraction(1, 1), Fraction(8, 1)), 13), Fraction(-8, 1), ShunyamClassification.UNIQUE_SOLUTION),
            Triple(CommonFactorEquation(11, LinearExpression(Fraction(2, 1), Fraction(8, 1)), 4), Fraction(-4, 1), ShunyamClassification.UNIQUE_SOLUTION),
            Triple(CommonFactorEquation(3, LinearExpression(Fraction(4, 1), Fraction(-16, 1)), 8), Fraction(4, 1), ShunyamClassification.UNIQUE_SOLUTION),
            Triple(CommonFactorEquation(12, LinearExpression(Fraction(1, 1), Fraction(-10, 1)), 5), Fraction(10, 1), ShunyamClassification.UNIQUE_SOLUTION),
            Triple(CommonFactorEquation(15, LinearExpression(Fraction(6, 1), Fraction(18, 1)), 2), Fraction(-3, 1), ShunyamClassification.UNIQUE_SOLUTION),

            Triple(CommonFactorEquation(18, LinearExpression(Fraction(1, 1), Fraction(4, 1)), 7), Fraction(-4, 1), ShunyamClassification.UNIQUE_SOLUTION),
            Triple(CommonFactorEquation(21, LinearExpression(Fraction(3, 1), Fraction(-12, 1)), 10), Fraction(4, 1), ShunyamClassification.UNIQUE_SOLUTION),
            Triple(CommonFactorEquation(13, LinearExpression(Fraction(2, 1), Fraction(14, 1)), 25), Fraction(-7, 1), ShunyamClassification.UNIQUE_SOLUTION),
            Triple(CommonFactorEquation(16, LinearExpression(Fraction(5, 1), Fraction(-25, 1)), 3), Fraction(5, 1), ShunyamClassification.UNIQUE_SOLUTION),
            Triple(CommonFactorEquation(19, LinearExpression(Fraction(1, 1), Fraction(-6, 1)), 4), Fraction(6, 1), ShunyamClassification.UNIQUE_SOLUTION)
        )

        for ((eq, expectedSol, expectedClassif) in cases) {
            val sol = generator.solveFamilyA(eq)
            assertEquals("Classification mismatch for ${eq.toFormattedString()}", expectedClassif, sol.classification)
            assertEquals("Solution mismatch for ${eq.toFormattedString()}", expectedSol.reduced, sol.candidateSolution.reduced)
        }
    }

    @Test
    fun testFamilyAIdentityK1EqualsK2() {
        val eq = CommonFactorEquation(
            k1 = 7,
            factor = LinearExpression(Fraction(1, 1), Fraction(1, 1)),
            k2 = 7
        )
        val sol = generator.solveFamilyA(eq)
        assertEquals(ShunyamClassification.IDENTITY, sol.classification)
    }

    // --- Family B Tests ---

    @Test
    fun testCanonicalExampleFamilyB_1_over_x_plus_2_plus_1_over_x_plus_3() {
        val eq = EqualNumeratorFractionEquation(
            numerator = 1,
            d1 = LinearExpression(Fraction(1, 1), Fraction(2, 1)),
            d2 = LinearExpression(Fraction(1, 1), Fraction(3, 1)),
            d3 = LinearExpression(Fraction(1, 1), Fraction(1, 1)),
            d4 = LinearExpression(Fraction(1, 1), Fraction(4, 1))
        )
        val sol = generator.solveFamilyB(eq)

        assertEquals(ShunyamClassification.UNIQUE_SOLUTION, sol.classification)
        assertEquals(Fraction(-5, 2), sol.candidateSolution.reduced)

        // Exclusions test: x != -2, -3, -1, -4
        val expectedExclusions = listOf(Fraction(-2, 1), Fraction(-3, 1), Fraction(-1, 1), Fraction(-4, 1))
        for (ex in expectedExclusions) {
            assertTrue("Must contain exclusion $ex", sol.excludedValues.contains(ex.reduced))
        }

        // Exact substitution in original equation: 1/(-0.5) + 1/(0.5) == 1/(-1.5) + 1/(1.5)
        val x = sol.candidateSolution
        val lhs1 = eq.d1.evaluate(x)
        val lhs2 = eq.d2.evaluate(x)
        val rhs1 = eq.d3.evaluate(x)
        val rhs2 = eq.d4.evaluate(x)

        assertEquals("LHS evaluation at x=-5/2 must be equal", 0L, lhs1.numerator * lhs2.denominator + lhs2.numerator * lhs1.denominator)
        assertEquals("RHS evaluation at x=-5/2 must be equal", 0L, rhs1.numerator * rhs2.denominator + rhs2.numerator * rhs1.denominator)
    }

    @Test
    fun test20HandVerifiedFamilyBCases() {
        val cases = listOf(
            Pair(EqualNumeratorFractionEquation(1, LinearExpression(Fraction(1, 1), Fraction(1, 1)), LinearExpression(Fraction(1, 1), Fraction(4, 1)), LinearExpression(Fraction(1, 1), Fraction(2, 1)), LinearExpression(Fraction(1, 1), Fraction(3, 1))), Fraction(-5, 2)),
            Pair(EqualNumeratorFractionEquation(1, LinearExpression(Fraction(1, 1), Fraction(2, 1)), LinearExpression(Fraction(1, 1), Fraction(6, 1)), LinearExpression(Fraction(1, 1), Fraction(3, 1)), LinearExpression(Fraction(1, 1), Fraction(5, 1))), Fraction(-4, 1)),
            Pair(EqualNumeratorFractionEquation(1, LinearExpression(Fraction(1, 1), Fraction(3, 1)), LinearExpression(Fraction(1, 1), Fraction(7, 1)), LinearExpression(Fraction(1, 1), Fraction(4, 1)), LinearExpression(Fraction(1, 1), Fraction(6, 1))), Fraction(-5, 1)),
            Pair(EqualNumeratorFractionEquation(1, LinearExpression(Fraction(1, 1), Fraction(4, 1)), LinearExpression(Fraction(1, 1), Fraction(8, 1)), LinearExpression(Fraction(1, 1), Fraction(5, 1)), LinearExpression(Fraction(1, 1), Fraction(7, 1))), Fraction(-6, 1)),
            Pair(EqualNumeratorFractionEquation(1, LinearExpression(Fraction(1, 1), Fraction(5, 1)), LinearExpression(Fraction(1, 1), Fraction(9, 1)), LinearExpression(Fraction(1, 1), Fraction(6, 1)), LinearExpression(Fraction(1, 1), Fraction(8, 1))), Fraction(-7, 1)),

            Pair(EqualNumeratorFractionEquation(2, LinearExpression(Fraction(1, 1), Fraction(1, 1)), LinearExpression(Fraction(1, 1), Fraction(5, 1)), LinearExpression(Fraction(1, 1), Fraction(2, 1)), LinearExpression(Fraction(1, 1), Fraction(4, 1))), Fraction(-3, 1)),
            Pair(EqualNumeratorFractionEquation(3, LinearExpression(Fraction(1, 1), Fraction(2, 1)), LinearExpression(Fraction(1, 1), Fraction(8, 1)), LinearExpression(Fraction(1, 1), Fraction(4, 1)), LinearExpression(Fraction(1, 1), Fraction(6, 1))), Fraction(-5, 1)),
            Pair(EqualNumeratorFractionEquation(4, LinearExpression(Fraction(1, 1), Fraction(3, 1)), LinearExpression(Fraction(1, 1), Fraction(9, 1)), LinearExpression(Fraction(1, 1), Fraction(5, 1)), LinearExpression(Fraction(1, 1), Fraction(7, 1))), Fraction(-6, 1)),
            Pair(EqualNumeratorFractionEquation(5, LinearExpression(Fraction(1, 1), Fraction(4, 1)), LinearExpression(Fraction(1, 1), Fraction(10, 1)), LinearExpression(Fraction(1, 1), Fraction(6, 1)), LinearExpression(Fraction(1, 1), Fraction(8, 1))), Fraction(-7, 1)),
            Pair(EqualNumeratorFractionEquation(2, LinearExpression(Fraction(1, 1), Fraction(6, 1)), LinearExpression(Fraction(1, 1), Fraction(12, 1)), LinearExpression(Fraction(1, 1), Fraction(8, 1)), LinearExpression(Fraction(1, 1), Fraction(10, 1))), Fraction(-9, 1)),

            Pair(EqualNumeratorFractionEquation(1, LinearExpression(Fraction(1, 1), Fraction(1, 1)), LinearExpression(Fraction(1, 1), Fraction(7, 1)), LinearExpression(Fraction(1, 1), Fraction(3, 1)), LinearExpression(Fraction(1, 1), Fraction(5, 1))), Fraction(-4, 1)),
            Pair(EqualNumeratorFractionEquation(1, LinearExpression(Fraction(1, 1), Fraction(2, 1)), LinearExpression(Fraction(1, 1), Fraction(10, 1)), LinearExpression(Fraction(1, 1), Fraction(4, 1)), LinearExpression(Fraction(1, 1), Fraction(8, 1))), Fraction(-6, 1)),
            Pair(EqualNumeratorFractionEquation(1, LinearExpression(Fraction(1, 1), Fraction(3, 1)), LinearExpression(Fraction(1, 1), Fraction(11, 1)), LinearExpression(Fraction(1, 1), Fraction(5, 1)), LinearExpression(Fraction(1, 1), Fraction(9, 1))), Fraction(-7, 1)),
            Pair(EqualNumeratorFractionEquation(1, LinearExpression(Fraction(1, 1), Fraction(4, 1)), LinearExpression(Fraction(1, 1), Fraction(12, 1)), LinearExpression(Fraction(1, 1), Fraction(6, 1)), LinearExpression(Fraction(1, 1), Fraction(10, 1))), Fraction(-8, 1)),
            Pair(EqualNumeratorFractionEquation(1, LinearExpression(Fraction(1, 1), Fraction(5, 1)), LinearExpression(Fraction(1, 1), Fraction(13, 1)), LinearExpression(Fraction(1, 1), Fraction(7, 1)), LinearExpression(Fraction(1, 1), Fraction(11, 1))), Fraction(-9, 1)),

            Pair(EqualNumeratorFractionEquation(3, LinearExpression(Fraction(1, 1), Fraction(1, 1)), LinearExpression(Fraction(1, 1), Fraction(9, 1)), LinearExpression(Fraction(1, 1), Fraction(3, 1)), LinearExpression(Fraction(1, 1), Fraction(7, 1))), Fraction(-5, 1)),
            Pair(EqualNumeratorFractionEquation(4, LinearExpression(Fraction(1, 1), Fraction(2, 1)), LinearExpression(Fraction(1, 1), Fraction(12, 1)), LinearExpression(Fraction(1, 1), Fraction(6, 1)), LinearExpression(Fraction(1, 1), Fraction(8, 1))), Fraction(-7, 1)),
            Pair(EqualNumeratorFractionEquation(2, LinearExpression(Fraction(1, 1), Fraction(3, 1)), LinearExpression(Fraction(1, 1), Fraction(13, 1)), LinearExpression(Fraction(1, 1), Fraction(7, 1)), LinearExpression(Fraction(1, 1), Fraction(9, 1))), Fraction(-8, 1)),
            Pair(EqualNumeratorFractionEquation(5, LinearExpression(Fraction(1, 1), Fraction(4, 1)), LinearExpression(Fraction(1, 1), Fraction(14, 1)), LinearExpression(Fraction(1, 1), Fraction(8, 1)), LinearExpression(Fraction(1, 1), Fraction(10, 1))), Fraction(-9, 1)),
            Pair(EqualNumeratorFractionEquation(1, LinearExpression(Fraction(1, 1), Fraction(5, 1)), LinearExpression(Fraction(1, 1), Fraction(15, 1)), LinearExpression(Fraction(1, 1), Fraction(9, 1)), LinearExpression(Fraction(1, 1), Fraction(11, 1))), Fraction(-10, 1))
        )

        for ((eq, expectedSol) in cases) {
            val sol = generator.solveFamilyB(eq)
            assertEquals("Classification mismatch for ${eq.toFormattedString()}", ShunyamClassification.UNIQUE_SOLUTION, sol.classification)
            assertEquals("Solution mismatch for ${eq.toFormattedString()}", expectedSol.reduced, sol.candidateSolution.reduced)
        }
    }

    // --- Property Tests ---

    @Test
    fun testRandomizedPropertyTestTier1() {
        repeat(50) {
            val pProblem = generator.generateShunyamProblem(DifficultyTier.TIER_1_EASY)
            assertEquals(ShunyamClassification.UNIQUE_SOLUTION, pProblem.classification)
            assertEquals(ShunyamFamily.FAMILY_A_COMMON_FACTOR, pProblem.family)

            val x = pProblem.solution.reduced
            val eq = pProblem.commonFactorEq!!

            val evalFactor = eq.factor.evaluate(x)
            assertEquals("Factor evaluated at x must be 0", 0L, evalFactor.numerator)
        }
    }

    @Test
    fun testRandomizedPropertyTestTier2() {
        repeat(50) {
            val pProblem = generator.generateShunyamProblem(DifficultyTier.TIER_2_HARD)
            assertEquals(ShunyamClassification.UNIQUE_SOLUTION, pProblem.classification)

            val x = pProblem.solution.reduced
            if (pProblem.family == ShunyamFamily.FAMILY_A_COMMON_FACTOR) {
                val eq = pProblem.commonFactorEq!!
                val evalFactor = eq.factor.evaluate(x)
                assertEquals("Factor evaluated at x must be 0", 0L, evalFactor.numerator)
            } else {
                val eq = pProblem.fractionEq!!
                // Verify candidate x is not in excluded values
                assertFalse("Candidate x cannot match excluded domain roots", pProblem.excludedValues.contains(x))
            }
        }
    }
}
