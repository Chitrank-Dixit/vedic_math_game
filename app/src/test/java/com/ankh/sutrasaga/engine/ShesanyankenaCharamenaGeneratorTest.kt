package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.domain.models.DecimalClassification
import com.ankh.sutrasaga.domain.models.DifficultyTier
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ShesanyankenaCharamenaGeneratorTest {

    private lateinit var generator: ShesanyankenaCharamenaGenerator

    @Before
    fun setUp() {
        generator = ShesanyankenaCharamenaGenerator()
    }

    @Test
    fun testCanonicalExample1_1_over_7() {
        val sol = generator.computeDecimalExpansion(1, 7)

        assertEquals(DecimalClassification.PURE_RECURRING, sol.classification)
        assertEquals(0L, sol.expansion.integerPart)
        assertTrue(sol.expansion.nonRepeatingDigits.isEmpty())
        assertEquals(listOf(1, 4, 2, 8, 5, 7), sol.expansion.repeatingDigits)
        assertFalse(sol.expansion.isTerminating)
        assertEquals("0.(142857)", sol.expansion.toFormattedString())

        // Remainder sequence verification: 1 -> 3 -> 2 -> 6 -> 4 -> 5 -> 1
        val remainders = sol.remainderSteps.map { it.incomingRemainder }
        assertEquals(listOf(1L, 3L, 2L, 6L, 4L, 5L), remainders)
    }

    @Test
    fun testTerminatingExample2_1_over_8() {
        val sol = generator.computeDecimalExpansion(1, 8)

        assertEquals(DecimalClassification.TERMINATING, sol.classification)
        assertEquals(0L, sol.expansion.integerPart)
        assertEquals(listOf(1, 2, 5), sol.expansion.nonRepeatingDigits)
        assertTrue(sol.expansion.repeatingDigits.isEmpty())
        assertTrue(sol.expansion.isTerminating)
        assertEquals("0.125", sol.expansion.toFormattedString())
    }

    @Test
    fun testNonUnitNumeratorExample3_3_over_7() {
        val sol = generator.computeDecimalExpansion(3, 7)

        assertEquals(DecimalClassification.PURE_RECURRING, sol.classification)
        assertEquals(0L, sol.expansion.integerPart)
        assertTrue(sol.expansion.nonRepeatingDigits.isEmpty())
        assertEquals(listOf(4, 2, 8, 5, 7, 1), sol.expansion.repeatingDigits)
        assertEquals("0.(428571)", sol.expansion.toFormattedString())
    }

    @Test
    fun testMixedRecurringExample4_1_over_6() {
        val sol = generator.computeDecimalExpansion(1, 6)

        assertEquals(DecimalClassification.MIXED_RECURRING, sol.classification)
        assertEquals(0L, sol.expansion.integerPart)
        assertEquals(listOf(1), sol.expansion.nonRepeatingDigits)
        assertEquals(listOf(6), sol.expansion.repeatingDigits)
        assertFalse(sol.expansion.isTerminating)
        assertEquals("0.1(6)", sol.expansion.toFormattedString())
    }

    @Test
    fun testLongerCycleExample5_1_over_13() {
        val sol = generator.computeDecimalExpansion(1, 13)

        assertEquals(DecimalClassification.PURE_RECURRING, sol.classification)
        assertEquals(0L, sol.expansion.integerPart)
        assertTrue(sol.expansion.nonRepeatingDigits.isEmpty())
        assertEquals(listOf(0, 7, 6, 9, 2, 3), sol.expansion.repeatingDigits)
        assertEquals("0.(076923)", sol.expansion.toFormattedString())
    }

    @Test
    fun testUnreducedEquivalentInputs_2_over_14_matches_1_over_7() {
        val solUnreduced = generator.computeDecimalExpansion(2, 14)
        val solReduced = generator.computeDecimalExpansion(1, 7)

        assertEquals(solReduced.expansion.toFormattedString(), solUnreduced.expansion.toFormattedString())
        assertEquals(solReduced.classification, solUnreduced.classification)
    }

    @Test
    fun testZeroNumerator_0_over_7() {
        val sol = generator.computeDecimalExpansion(0, 7)

        assertEquals(DecimalClassification.ZERO, sol.classification)
        assertEquals("0", sol.expansion.toFormattedString())
    }

    @Test
    fun testImproperFraction_8_over_7() {
        val sol = generator.computeDecimalExpansion(8, 7)

        assertEquals(DecimalClassification.PURE_RECURRING, sol.classification)
        assertEquals(1L, sol.expansion.integerPart)
        assertEquals(listOf(1, 4, 2, 8, 5, 7), sol.expansion.repeatingDigits)
        assertEquals("1.(142857)", sol.expansion.toFormattedString())
    }

    @Test
    fun testNegativeFraction_minus_1_over_7() {
        val sol = generator.computeDecimalExpansion(-1, 7)

        assertEquals(DecimalClassification.NEGATIVE, sol.classification)
        assertEquals(-0L, sol.expansion.integerPart)
    }

    @Test
    fun testDenominatorsWithFactors2And5() {
        val cases = listOf(
            Pair(1L, 2L) to "0.5",
            Pair(1L, 4L) to "0.25",
            Pair(1L, 5L) to "0.2",
            Pair(1L, 10L) to "0.1",
            Pair(1L, 20L) to "0.05",
            Pair(1L, 25L) to "0.04",
            Pair(3L, 8L) to "0.375",
            Pair(7L, 20L) to "0.35"
        )

        for ((frac, expectedStr) in cases) {
            val sol = generator.computeDecimalExpansion(frac.first, frac.second)
            assertEquals(DecimalClassification.TERMINATING, sol.classification)
            assertEquals("Expansion mismatch for ${frac.first}/${frac.second}", expectedStr, sol.expansion.toFormattedString())
        }
    }

    @Test
    fun test20HandVerifiedFractions() {
        val cases = listOf(
            Pair(1L, 3L) to "0.(3)",
            Pair(2L, 3L) to "0.(6)",
            Pair(1L, 9L) to "0.(1)",
            Pair(4L, 9L) to "0.(4)",
            Pair(1L, 11L) to "0.(09)",
            Pair(2L, 11L) to "0.(18)",
            Pair(1L, 12L) to "0.08(3)",
            Pair(5L, 12L) to "0.41(6)",
            Pair(1L, 15L) to "0.0(6)",
            Pair(2L, 15L) to "0.1(3)",

            Pair(1L, 16L) to "0.0625",
            Pair(1L, 18L) to "0.0(5)",
            Pair(5L, 18L) to "0.2(7)",
            Pair(1L, 22L) to "0.0(45)",
            Pair(1L, 24L) to "0.041(6)",
            Pair(1L, 30L) to "0.0(3)",
            Pair(7L, 30L) to "0.2(3)",
            Pair(1L, 33L) to "0.(03)",
            Pair(1L, 40L) to "0.025",
            Pair(1L, 50L) to "0.02"
        )

        for ((frac, expectedStr) in cases) {
            val sol = generator.computeDecimalExpansion(frac.first, frac.second)
            assertEquals("Formatted expansion mismatch for ${frac.first}/${frac.second}", expectedStr, sol.expansion.toFormattedString())
        }
    }

    @Test
    fun testRandomizedPropertyTestTier1() {
        repeat(50) {
            val sProblem = generator.generateDecimalProblem(DifficultyTier.TIER_1_EASY)
            val p = sProblem.numerator
            val q = sProblem.denominator

            val sol = generator.computeDecimalExpansion(p, q)
            assertTrue("Emitted digits must not be empty", sol.remainderSteps.isNotEmpty())
            assertTrue("Every remainder step digit must be in 0..9", sol.remainderSteps.all { it.emittedDigit in 0..9 })
        }
    }

    @Test
    fun testRandomizedPropertyTestTier2() {
        repeat(50) {
            val sProblem = generator.generateDecimalProblem(DifficultyTier.TIER_2_HARD)
            val p = sProblem.numerator
            val q = sProblem.denominator

            val sol = generator.computeDecimalExpansion(p, q)
            assertTrue("Emitted digits must not be empty", sol.remainderSteps.isNotEmpty())
            assertTrue("Every remainder step digit must be in 0..9", sol.remainderSteps.all { it.emittedDigit in 0..9 })
        }
    }
}
