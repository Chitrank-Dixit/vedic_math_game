package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.FactorizationProposal
import com.ankh.sutrasaga.domain.models.GunitasamuccayahClassification
import com.ankh.sutrasaga.domain.models.LinearFactor
import com.ankh.sutrasaga.domain.models.QuadraticPolynomial
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GunitasamuccayahGeneratorTest {

    private lateinit var generator: GunitasamuccayahGenerator

    @Before
    fun setUp() {
        generator = GunitasamuccayahGenerator()
    }

    @Test
    fun testCanonicalPositiveExample1_x_plus_3_times_x_plus_2() {
        val f1 = LinearFactor(1, 3)
        val f2 = LinearFactor(1, 2)
        val poly = QuadraticPolynomial(1, 5, 6)
        val proposal = FactorizationProposal(listOf(f1, f2), poly)

        val sol = generator.verifyFactorization(proposal)

        assertEquals(GunitasamuccayahClassification.VALID_FACTORISATION, sol.classification)
        assertEquals(12L, sol.sumCheck.expectedSum)
        assertEquals(12L, sol.sumCheck.actualSum)
        assertTrue(sol.sumCheck.passed)
        assertTrue(sol.expansionCheck.passed)
    }

    @Test
    fun testSignedCoefficientsExample2_x_minus_4_times_2x_plus_5() {
        val f1 = LinearFactor(1, -4)
        val f2 = LinearFactor(2, 5)
        val poly = QuadraticPolynomial(2, -3, -20)
        val proposal = FactorizationProposal(listOf(f1, f2), poly)

        val sol = generator.verifyFactorization(proposal)

        assertEquals(GunitasamuccayahClassification.VALID_FACTORISATION, sol.classification)
        assertEquals(-21L, sol.sumCheck.expectedSum)
        assertEquals(-21L, sol.sumCheck.actualSum)
        assertTrue(sol.sumCheck.passed)
        assertTrue(sol.expansionCheck.passed)
    }

    @Test
    fun testFalseProposal_sumCheckPasses_expansionFails() {
        val f1 = LinearFactor(1, 3)
        val f2 = LinearFactor(1, 2)
        // Correct product is x^2 + 5x + 6 (sum = 12).
        // False claimed product: x^2 + 7x + 4 (sum = 12).
        val claimedPoly = QuadraticPolynomial(1, 7, 4)
        val proposal = FactorizationProposal(listOf(f1, f2), claimedPoly)

        val sol = generator.verifyFactorization(proposal)

        assertEquals(GunitasamuccayahClassification.INVALID_EXPANSION, sol.classification)
        assertTrue("Sum check must pass by coincidence", sol.sumCheck.passed)
        assertFalse("Expansion check must fail", sol.expansionCheck.passed)
    }

    @Test
    fun testZeroCoefficientSumEdgeCase_x_minus_1_times_x_plus_5() {
        val f1 = LinearFactor(1, -1)
        val f2 = LinearFactor(1, 5)
        val poly = QuadraticPolynomial(1, 4, -5)
        val proposal = FactorizationProposal(listOf(f1, f2), poly)

        val sol = generator.verifyFactorization(proposal)

        assertEquals(GunitasamuccayahClassification.VALID_FACTORISATION, sol.classification)
        assertEquals(0L, sol.sumCheck.expectedSum)
        assertEquals(0L, sol.sumCheck.actualSum)
        assertTrue(sol.sumCheck.passed)
        assertTrue(sol.expansionCheck.passed)
    }

    @Test
    fun testMissingMiddleTerm_x_minus_4_times_x_plus_4() {
        val f1 = LinearFactor(1, -4)
        val f2 = LinearFactor(1, 4)
        val poly = QuadraticPolynomial(1, 0, -16)
        val proposal = FactorizationProposal(listOf(f1, f2), poly)

        val sol = generator.verifyFactorization(proposal)

        assertEquals(GunitasamuccayahClassification.VALID_FACTORISATION, sol.classification)
        assertEquals(-15L, sol.sumCheck.expectedSum)
        assertEquals(-15L, sol.sumCheck.actualSum)
        assertTrue(sol.sumCheck.passed)
        assertTrue(sol.expansionCheck.passed)
    }

    @Test
    fun test20HandVerifiedValidFactorizations() {
        val cases = listOf(
            Pair(LinearFactor(1, 1), LinearFactor(1, 1)) to QuadraticPolynomial(1, 2, 1),
            Pair(LinearFactor(1, 1), LinearFactor(1, 4)) to QuadraticPolynomial(1, 5, 4),
            Pair(LinearFactor(1, 2), LinearFactor(1, 3)) to QuadraticPolynomial(1, 5, 6),
            Pair(LinearFactor(1, -2), LinearFactor(1, -3)) to QuadraticPolynomial(1, -5, 6),
            Pair(LinearFactor(1, 5), LinearFactor(1, -5)) to QuadraticPolynomial(1, 0, -25),
            Pair(LinearFactor(2, 1), LinearFactor(3, 1)) to QuadraticPolynomial(6, 5, 1),
            Pair(LinearFactor(2, 3), LinearFactor(1, 4)) to QuadraticPolynomial(2, 11, 12),
            Pair(LinearFactor(3, -1), LinearFactor(2, 5)) to QuadraticPolynomial(6, 13, -5),
            Pair(LinearFactor(1, 0), LinearFactor(1, 5)) to QuadraticPolynomial(1, 5, 0),
            Pair(LinearFactor(2, 0), LinearFactor(3, 0)) to QuadraticPolynomial(6, 0, 0),

            Pair(LinearFactor(1, 6), LinearFactor(1, 6)) to QuadraticPolynomial(1, 12, 36),
            Pair(LinearFactor(1, -7), LinearFactor(1, 2)) to QuadraticPolynomial(1, -5, -14),
            Pair(LinearFactor(4, 1), LinearFactor(1, 1)) to QuadraticPolynomial(4, 5, 1),
            Pair(LinearFactor(5, -2), LinearFactor(1, 3)) to QuadraticPolynomial(5, 13, -6),
            Pair(LinearFactor(1, 10), LinearFactor(1, -10)) to QuadraticPolynomial(1, 0, -100),
            Pair(LinearFactor(2, 2), LinearFactor(2, 2)) to QuadraticPolynomial(4, 8, 4),
            Pair(LinearFactor(3, 3), LinearFactor(1, -1)) to QuadraticPolynomial(3, 0, -3),
            Pair(LinearFactor(1, 8), LinearFactor(1, 2)) to QuadraticPolynomial(1, 10, 16),
            Pair(LinearFactor(2, -1), LinearFactor(2, 1)) to QuadraticPolynomial(4, 0, -1),
            Pair(LinearFactor(1, 9), LinearFactor(1, 1)) to QuadraticPolynomial(1, 10, 9)
        )

        for ((factors, expectedPoly) in cases) {
            val proposal = FactorizationProposal(listOf(factors.first, factors.second), expectedPoly)
            val sol = generator.verifyFactorization(proposal)
            assertEquals("Factorization must be valid for ${factors.first.toFormattedString()}${factors.second.toFormattedString()}", GunitasamuccayahClassification.VALID_FACTORISATION, sol.classification)
            assertTrue(sol.sumCheck.passed)
            assertTrue(sol.expansionCheck.passed)
        }
    }

    @Test
    fun test5FalseProposalsWithMatchingSumCheck() {
        val falseCases = listOf(
            Pair(LinearFactor(1, 3), LinearFactor(1, 2)) to QuadraticPolynomial(1, 7, 4), // sum=12
            Pair(LinearFactor(1, 4), LinearFactor(1, 1)) to QuadraticPolynomial(1, 8, 1), // sum=10
            Pair(LinearFactor(2, 1), LinearFactor(3, 1)) to QuadraticPolynomial(6, 9, -3), // sum=12
            Pair(LinearFactor(1, 5), LinearFactor(1, 2)) to QuadraticPolynomial(1, 10, 7), // sum=18
            Pair(LinearFactor(1, -2), LinearFactor(1, -3)) to QuadraticPolynomial(1, -8, 9) // sum=2
        )

        for ((factors, claimedPoly) in falseCases) {
            val proposal = FactorizationProposal(listOf(factors.first, factors.second), claimedPoly)
            val sol = generator.verifyFactorization(proposal)
            assertEquals("Classification must be INVALID_EXPANSION when sum matches but terms differ", GunitasamuccayahClassification.INVALID_EXPANSION, sol.classification)
            assertTrue(sol.sumCheck.passed)
            assertFalse(sol.expansionCheck.passed)
        }
    }

    @Test
    fun testRandomizedPropertyTestTier1() {
        repeat(50) {
            val p = generator.generateGunitaProblem(DifficultyTier.TIER_1_EASY)
            val sol = p.solution
            val f1 = p.proposal.factors[0]
            val f2 = p.proposal.factors[1]

            val expectedSum = f1.sumOfCoefficients() * f2.sumOfCoefficients()
            assertEquals(expectedSum, sol.sumCheck.expectedSum)
        }
    }

    @Test
    fun testRandomizedPropertyTestTier2() {
        repeat(50) {
            val p = generator.generateGunitaProblem(DifficultyTier.TIER_2_HARD)
            val sol = p.solution
            val f1 = p.proposal.factors[0]
            val f2 = p.proposal.factors[1]

            val expectedSum = f1.sumOfCoefficients() * f2.sumOfCoefficients()
            assertEquals(expectedSum, sol.sumCheck.expectedSum)
        }
    }
}
