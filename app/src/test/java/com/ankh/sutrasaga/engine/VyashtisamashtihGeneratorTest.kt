package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.Fraction
import com.ankh.sutrasaga.domain.models.VyashtisamashtihClassification
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class VyashtisamashtihGeneratorTest {

    private lateinit var generator: VyashtisamashtihGenerator

    @Before
    fun setUp() {
        generator = VyashtisamashtihGenerator()
    }

    @Test
    fun testCanonicalExample1_58_times_62() {
        val p = Fraction(58, 1)
        val q = Fraction(62, 1)
        val sol = generator.solveSymmetricProduct(p, q)

        assertEquals(VyashtisamashtihClassification.SUPPORTED_INTEGER_PATH, sol.classification)
        assertEquals(Fraction(60, 1), sol.parts.wholeAverage.reduced)
        assertEquals(Fraction(2, 1), sol.parts.positiveDeviation.reduced)
        assertEquals(Fraction(3600, 1), sol.wholeSquare.reduced)
        assertEquals(Fraction(4, 1), sol.deviationSquare.reduced)
        assertEquals(Fraction(3596, 1), sol.product.reduced)

        // Verification of direct multiplication: 58 * 62 == 3596
        assertEquals(58L * 62L, sol.product.numerator)
    }

    @Test
    fun testPositiveNonRoundExample2_25_times_31() {
        val p = Fraction(25, 1)
        val q = Fraction(31, 1)
        val sol = generator.solveSymmetricProduct(p, q)

        assertEquals(VyashtisamashtihClassification.SUPPORTED_INTEGER_PATH, sol.classification)
        assertEquals(Fraction(28, 1), sol.parts.wholeAverage.reduced)
        assertEquals(Fraction(3, 1), sol.parts.positiveDeviation.reduced)
        assertEquals(Fraction(784, 1), sol.wholeSquare.reduced)
        assertEquals(Fraction(9, 1), sol.deviationSquare.reduced)
        assertEquals(Fraction(775, 1), sol.product.reduced)

        assertEquals(25L * 31L, sol.product.numerator)
    }

    @Test
    fun testOddSumPairExample3_12_times_15() {
        val p = Fraction(12, 1)
        val q = Fraction(15, 1)
        val sol = generator.solveSymmetricProduct(p, q)

        assertEquals(VyashtisamashtihClassification.SUPPORTED_FRACTION_PATH, sol.classification)
        assertEquals(Fraction(27, 2), sol.parts.wholeAverage.reduced)
        assertEquals(Fraction(3, 2), sol.parts.positiveDeviation.reduced)
        assertEquals(Fraction(729, 4), sol.wholeSquare.reduced)
        assertEquals(Fraction(9, 4), sol.deviationSquare.reduced)
        assertEquals(Fraction(180, 1), sol.product.reduced)

        assertEquals(12L * 15L, sol.product.numerator)
    }

    @Test
    fun test20HandVerifiedIntegerPathCases() {
        val cases = listOf(
            Pair(48L, 52L) to 2496L, // 50^2 - 2^2
            Pair(94L, 106L) to 9964L, // 100^2 - 6^2
            Pair(18L, 22L) to 396L,   // 20^2 - 2^2
            Pair(35L, 45L) to 1575L,  // 40^2 - 5^2
            Pair(67L, 73L) to 4891L,  // 70^2 - 3^2
            Pair(89L, 91L) to 8099L,  // 90^2 - 1^2
            Pair(14L, 16L) to 224L,   // 15^2 - 1^2
            Pair(26L, 34L) to 884L,   // 30^2 - 4^2
            Pair(43L, 57L) to 2451L,  // 50^2 - 7^2
            Pair(78L, 82L) to 6396L,  // 80^2 - 2^2

            Pair(11L, 19L) to 209L,   // 15^2 - 4^2
            Pair(24L, 36L) to 864L,   // 30^2 - 6^2
            Pair(37L, 43L) to 1591L,  // 40^2 - 3^2
            Pair(55L, 65L) to 3575L,  // 60^2 - 5^2
            Pair(72L, 88L) to 6336L,  // 80^2 - 8^2
            Pair(91L, 109L) to 9919L, // 100^2 - 9^2
            Pair(13L, 17L) to 221L,   // 15^2 - 2^2
            Pair(22L, 28L) to 616L,   // 25^2 - 3^2
            Pair(46L, 54L) to 2484L,  // 50^2 - 4^2
            Pair(64L, 76L) to 4864L   // 70^2 - 6^2
        )

        for ((pair, expectedProd) in cases) {
            val (pVal, qVal) = pair
            val sol = generator.solveSymmetricProduct(Fraction(pVal, 1), Fraction(qVal, 1))

            assertEquals("Product mismatch for $pVal x $qVal", expectedProd, sol.product.numerator)
            assertEquals("Direct multiplication identity must hold", pVal * qVal, sol.product.numerator)

            val avg = (pVal + qVal) / 2
            val dev = (qVal - pVal) / 2
            assertEquals("Average mismatch", avg, sol.parts.wholeAverage.numerator)
            assertEquals("Deviation mismatch", dev, sol.parts.positiveDeviation.numerator)
        }
    }

    @Test
    fun test10CasesWithNegativeZeroOrEqualFactors() {
        val cases = listOf(
            Pair(0L, 10L) to 0L,       // avg 5, dev 5 -> 25 - 25 = 0
            Pair(10L, 10L) to 100L,    // avg 10, dev 0 -> 100 - 0 = 100
            Pair(0L, 0L) to 0L,        // avg 0, dev 0 -> 0 - 0 = 0
            Pair(-4L, 4L) to -16L,     // avg 0, dev 4 -> 0 - 16 = -16
            Pair(-10L, -6L) to 60L,    // avg -8, dev 2 -> 64 - 4 = 60
            Pair(-8L, 2L) to -16L,     // avg -3, dev 5 -> 9 - 25 = -16
            Pair(-12L, -8L) to 96L,    // avg -10, dev 2 -> 100 - 4 = 96
            Pair(-5L, 5L) to -25L,     // avg 0, dev 5 -> 0 - 25 = -25
            Pair(15L, 15L) to 225L,    // avg 15, dev 0 -> 225 - 0 = 225
            Pair(-20L, 0L) to 0L       // avg -10, dev 10 -> 100 - 100 = 0
        )

        for ((pair, expectedProd) in cases) {
            val (pVal, qVal) = pair
            val sol = generator.solveSymmetricProduct(Fraction(pVal, 1), Fraction(qVal, 1))
            assertEquals("Product mismatch for $pVal x $qVal", expectedProd, sol.product.numerator)
            assertEquals("Direct product identity check", pVal * qVal, sol.product.numerator)
        }
    }

    @Test
    fun test10FractionalIntermediateCases() {
        val cases = listOf(
            Pair(1L, 2L) to 2L,    // avg 3/2, dev 1/2 -> 9/4 - 1/4 = 8/4 = 2
            Pair(3L, 4L) to 12L,   // avg 7/2, dev 1/2 -> 49/4 - 1/4 = 48/4 = 12
            Pair(5L, 8L) to 40L,   // avg 13/2, dev 3/2 -> 169/4 - 9/4 = 160/4 = 40
            Pair(7L, 10L) to 70L,  // avg 17/2, dev 3/2 -> 289/4 - 9/4 = 280/4 = 70
            Pair(9L, 12L) to 108L, // avg 21/2, dev 3/2 -> 441/4 - 9/4 = 432/4 = 108
            Pair(11L, 14L) to 154L,// avg 25/2, dev 3/2 -> 625/4 - 9/4 = 616/4 = 154
            Pair(13L, 18L) to 234L,// avg 31/2, dev 5/2 -> 961/4 - 25/4 = 936/4 = 234
            Pair(15L, 20L) to 300L,// avg 35/2, dev 5/2 -> 1225/4 - 25/4 = 1200/4 = 300
            Pair(17L, 22L) to 374L,// avg 39/2, dev 5/2 -> 1521/4 - 25/4 = 1496/4 = 374
            Pair(19L, 24L) to 456L // avg 43/2, dev 5/2 -> 1849/4 - 25/4 = 1824/4 = 456
        )

        for ((pair, expectedProd) in cases) {
            val (pVal, qVal) = pair
            val sol = generator.solveSymmetricProduct(Fraction(pVal, 1), Fraction(qVal, 1))
            assertEquals(VyashtisamashtihClassification.SUPPORTED_FRACTION_PATH, sol.classification)
            assertEquals("Product mismatch for $pVal x $qVal", expectedProd, sol.product.numerator)
            assertEquals("Direct product identity check", pVal * qVal, sol.product.numerator)
        }
    }

    @Test
    fun testRandomizedPropertyTestTier1() {
        repeat(50) {
            val vProblem = generator.generateSymmetricProblem(DifficultyTier.TIER_1_EASY)
            assertEquals(VyashtisamashtihClassification.SUPPORTED_INTEGER_PATH, vProblem.solution.classification)

            val p = vProblem.left.numerator
            val q = vProblem.right.numerator
            val expectedProduct = p * q

            assertEquals("Direct product must match A^2 - d^2", expectedProduct, vProblem.solution.product.numerator)

            val avg = (p + q) / 2
            val dev = (q - p) / 2
            assertEquals("Whole average identity", avg, vProblem.parts.wholeAverage.numerator)
            assertEquals("Deviation identity", dev, vProblem.parts.positiveDeviation.numerator)
            assertEquals("A^2 - d^2 identity", avg * avg - dev * dev, vProblem.solution.product.numerator)
        }
    }

    @Test
    fun testRandomizedPropertyTestTier2() {
        repeat(50) {
            val vProblem = generator.generateSymmetricProblem(DifficultyTier.TIER_2_HARD)
            val p = vProblem.left.numerator
            val q = vProblem.right.numerator
            val expectedProduct = p * q

            assertEquals("Direct product must match A^2 - d^2 for Tier 2", expectedProduct, vProblem.solution.product.numerator)
        }
    }
}
