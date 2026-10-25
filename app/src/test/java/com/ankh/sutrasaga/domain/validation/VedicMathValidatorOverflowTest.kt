package com.ankh.sutrasaga.domain.validation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Boundary and overflow-safety tests for [VedicMathValidator].
 *
 * Verifies that:
 * 1. Arithmetic overflow in multiplication, addition, subtraction, squaring, or intermediate
 *    calculations fails closed and never returns false-positive validation.
 * 2. Handled negative, zero, and out-of-range inputs reject cleanly without silent wraparound.
 * 3. Normal canonical Vedic Mathematics calculations pass accurately.
 */
class VedicMathValidatorOverflowTest {

    // ─────────────────────────────────────────────────────────────────────────────
    // 1. Ekadhikena Squaring Ending in 5
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    fun squaringEndingIn5_canonicalAndBoundary_succeeds() {
        assertTrue(VedicMathValidator.verifySquaringEndingIn5(35L, 1225L))
        assertTrue(VedicMathValidator.verifySquaringEndingIn5(65L, 4225L))
        assertTrue(VedicMathValidator.verifySquaringEndingIn5(105L, 11025L))
        assertTrue(VedicMathValidator.verifySquaringEndingIn5(1005L, 1010025L))
    }

    @Test
    fun squaringEndingIn5_overflowGuards_failClosed() {
        // n^2 overflows Long.MAX_VALUE (approx 3.037e9)
        val overflowN = 3_037_000_505L
        assertFalse(
            "Squaring overflow must fail closed",
            VedicMathValidator.verifySquaringEndingIn5(overflowN, Long.MAX_VALUE)
        )
        assertFalse(
            "Long.MAX_VALUE input must fail closed",
            VedicMathValidator.verifySquaringEndingIn5(Long.MAX_VALUE, Long.MAX_VALUE)
        )
        assertFalse(
            "Negative input must fail",
            VedicMathValidator.verifySquaringEndingIn5(-35L, 1225L)
        )
        assertFalse(
            "Zero input must fail",
            VedicMathValidator.verifySquaringEndingIn5(0L, 0L)
        )
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // 2. Nikhilam Base Multiplication
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    fun nikhilamMultiplication_canonicalAndBoundary_succeeds() {
        // Answer validation against independently computed products
        assertTrue(VedicMathValidator.verifyNikhilamMultiplication(98L, 97L, 9506L, 100L))
        assertTrue(VedicMathValidator.verifyNikhilamMultiplication(996L, 994L, 990024L, 1000L))

        // Identity verification
        assertTrue(VedicMathValidator.verifyNikhilamIdentity(98L, 97L, 100L))
        assertTrue(VedicMathValidator.verifyNikhilamIdentity(996L, 994L, 1000L))
    }

    @Test
    fun nikhilamMultiplication_overflowGuards_failClosed() {
        // Multiplication overflow
        assertFalse(
            "Operands near Long.MAX_VALUE must fail closed in multiplication answer check",
            VedicMathValidator.verifyNikhilamMultiplication(Long.MAX_VALUE - 1, Long.MAX_VALUE - 2, 100L, 100L)
        )
        assertFalse(
            "Operands near Long.MAX_VALUE must fail closed in identity check",
            VedicMathValidator.verifyNikhilamIdentity(Long.MAX_VALUE - 1, Long.MAX_VALUE - 2, 100L)
        )
        // Subtraction overflow
        assertFalse(
            "Long.MIN_VALUE operand must fail closed in multiplication answer check",
            VedicMathValidator.verifyNikhilamMultiplication(Long.MIN_VALUE, 97L, 100L, 100L)
        )
        assertFalse(
            "Long.MIN_VALUE operand must fail closed in identity check",
            VedicMathValidator.verifyNikhilamIdentity(Long.MIN_VALUE, 97L, 100L)
        )
        assertFalse(
            "Zero base or negative base must fail in answer check",
            VedicMathValidator.verifyNikhilamMultiplication(98L, 97L, 9506L, 0L)
        )
        assertFalse(
            "Zero base or negative base must fail in identity check",
            VedicMathValidator.verifyNikhilamIdentity(98L, 97L, 0L)
        )
        assertFalse(
            "Zero operands must fail in answer check",
            VedicMathValidator.verifyNikhilamMultiplication(0L, 97L, 0L, 100L)
        )
        assertFalse(
            "Zero operands must fail in identity check",
            VedicMathValidator.verifyNikhilamIdentity(0L, 97L, 100L)
        )
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // 3. Base Subtraction
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    fun baseSubtraction_canonicalAndBoundary_succeeds() {
        assertTrue(VedicMathValidator.verifyBaseSubtraction(1000L, 364L, 636L))
        assertTrue(VedicMathValidator.verifyBaseSubtraction(100L, 42L, 58L))
    }

    @Test
    fun baseSubtraction_overflowGuards_failClosed() {
        // base - sub with negative sub causing subtraction overflow
        assertFalse(
            "Subtraction overflow with negative sub must fail closed",
            VedicMathValidator.verifyBaseSubtraction(Long.MAX_VALUE, -10L, Long.MIN_VALUE)
        )
        assertFalse(
            "Long.MIN_VALUE subtrahend must fail closed",
            VedicMathValidator.verifyBaseSubtraction(1000L, Long.MIN_VALUE, 1000L)
        )
        assertFalse(
            "Zero base must fail",
            VedicMathValidator.verifyBaseSubtraction(0L, 50L, -50L)
        )
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // 4. Ekanyunena Multiplication
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    fun ekanyunenaMultiplication_canonicalAndBoundary_succeeds() {
        assertTrue(VedicMathValidator.verifyEkanyunenaMultiplication(47L, 99L, 4653L))
        assertTrue(VedicMathValidator.verifyEkanyunenaMultiplication(234L, 999L, 233766L))
    }

    @Test
    fun ekanyunenaMultiplication_overflowGuards_failClosed() {
        // Multiplier addition overflow: nines = Long.MAX_VALUE -> nines + 1 overflows
        assertFalse(
            "nines = Long.MAX_VALUE must fail closed",
            VedicMathValidator.verifyEkanyunenaMultiplication(47L, Long.MAX_VALUE, 47L)
        )
        // Multiplication overflow
        assertFalse(
            "Huge multiplicand multiplication overflow must fail closed",
            VedicMathValidator.verifyEkanyunenaMultiplication(Long.MAX_VALUE / 2, 99L, 1000L)
        )
        assertFalse(
            "Zero multiplicand must fail",
            VedicMathValidator.verifyEkanyunenaMultiplication(0L, 99L, 0L)
        )
        assertFalse(
            "Negative multiplicand must fail",
            VedicMathValidator.verifyEkanyunenaMultiplication(-47L, 99L, -4653L)
        )
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // 5. Yavadunam Squaring
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    fun yavadunamSquaring_canonicalAndBoundary_succeeds() {
        assertTrue(VedicMathValidator.verifyYavadunamSquaring(94L, 100L, 8836L))
        assertTrue(VedicMathValidator.verifyYavadunamSquaring(96L, 100L, 9216L))
        assertTrue(VedicMathValidator.verifyYavadunamSquaring(104L, 100L, 10816L))
    }

    @Test
    fun yavadunamSquaring_overflowGuards_failClosed() {
        // Squaring overflow
        val hugeN = 3_037_000_500L
        assertFalse(
            "Squaring overflow must fail closed",
            VedicMathValidator.verifyYavadunamSquaring(hugeN, 1000000L, Long.MAX_VALUE)
        )
        // Difference squaring overflow
        assertFalse(
            "Huge base causing diff overflow must fail closed",
            VedicMathValidator.verifyYavadunamSquaring(10L, Long.MAX_VALUE, Long.MAX_VALUE)
        )
        assertFalse(
            "Long.MIN_VALUE n must fail closed",
            VedicMathValidator.verifyYavadunamSquaring(Long.MIN_VALUE, 100L, 100L)
        )
        assertFalse(
            "Negative n must fail",
            VedicMathValidator.verifyYavadunamSquaring(-94L, 100L, 8836L)
        )
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // 6. Paravartya Division
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    fun paravartyaDivision_canonicalAndBoundary_succeeds() {
        assertTrue(VedicMathValidator.verifyParavartyaDivision(1225L, 12L, 102L, 1L))
        assertTrue(VedicMathValidator.verifyParavartyaDivision(144L, 12L, 12L, 0L))
    }

    @Test
    fun paravartyaDivision_overflowGuards_failClosed() {
        // divisor * quotient overflow
        assertFalse(
            "Divisor * quotient overflow must fail closed",
            VedicMathValidator.verifyParavartyaDivision(100L, Long.MAX_VALUE, 2L, 1L)
        )
        assertFalse(
            "Zero divisor must fail",
            VedicMathValidator.verifyParavartyaDivision(100L, 0L, 100L, 0L)
        )
        assertFalse(
            "Negative divisor must fail",
            VedicMathValidator.verifyParavartyaDivision(100L, -10L, -10L, 0L)
        )
        assertFalse(
            "Remainder equal to divisor must fail",
            VedicMathValidator.verifyParavartyaDivision(100L, 10L, 9L, 10L)
        )
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // 7. Anurupye Shunyamanyat Linear Systems
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    fun anurupyeSystem_canonicalAndBoundary_succeeds() {
        // 3x + 2y = 12, 6x + 5y = 24 -> x = 4, y = 0
        assertTrue(
            VedicMathValidator.verifyAnurupyeSystem(
                3L, 2L, 12L,
                6L, 5L, 24L,
                4L, 0L
            )
        )
    }

    @Test
    fun anurupyeSystem_overflowGuards_failClosed() {
        // Determinant overflow
        assertFalse(
            "Determinant overflow must fail closed",
            VedicMathValidator.verifyAnurupyeSystem(
                Long.MAX_VALUE / 2, 3L, 12L,
                1L, 3L, 24L,
                4L, 0L
            )
        )
        // Ratio cross-multiplication overflow
        assertFalse(
            "Ratio cross-multiplication overflow must fail closed",
            VedicMathValidator.verifyAnurupyeSystem(
                Long.MAX_VALUE, 2L, Long.MAX_VALUE,
                2L, 5L, 4L,
                1L, 0L
            )
        )
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // 8. Sankalana-Vyavakalanabhyam Linear Systems
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    fun sankalanaSystem_canonicalAndBoundary_succeeds() {
        // 3x + 2y = 12, 2x + 3y = 13 -> x = 2, y = 3
        assertTrue(
            VedicMathValidator.verifySankalanaSystem(
                3L, 2L, 12L,
                2L, 3L, 13L,
                2L, 3L
            )
        )
    }

    @Test
    fun sankalanaSystem_overflowGuards_failClosed() {
        // Long.MIN_VALUE input
        assertFalse(
            "Long.MIN_VALUE coefficient must fail closed",
            VedicMathValidator.verifySankalanaSystem(
                Long.MIN_VALUE, 2L, 12L,
                2L, Long.MIN_VALUE, 13L,
                2L, 3L
            )
        )
        // Cross multiplication determinant overflow
        assertFalse(
            "Determinant overflow must fail closed",
            VedicMathValidator.verifySankalanaSystem(
                Long.MAX_VALUE / 2, 2L, 12L,
                2L, Long.MAX_VALUE / 2, 13L,
                2L, 3L
            )
        )
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // 9. Shunyam Samyasamuccaye
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    fun shunyamCommonFactor_canonicalAndBoundary_succeeds() {
        // 2(3x + 6) = 5(3x + 6) -> 3x + 6 = 0 -> x = -2
        assertTrue(
            VedicMathValidator.verifyShunyamCommonFactor(2L, 3L, 6L, 5L, -2L)
        )
    }

    @Test
    fun shunyamCommonFactor_overflowGuards_failClosed() {
        // a * x overflow
        assertFalse(
            "a * x overflow must fail closed",
            VedicMathValidator.verifyShunyamCommonFactor(2L, Long.MAX_VALUE, 6L, 5L, 2L)
        )
        assertFalse(
            "k1 == k2 must fail (identity, not solvable via this sutra)",
            VedicMathValidator.verifyShunyamCommonFactor(2L, 3L, 6L, 2L, -2L)
        )
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // 10. Puranapuranabhyam Quadratic Completing the Square
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    fun puranapuranabhyam_canonicalAndBoundary_succeeds() {
        // x^2 + 6x - 16 = 0 -> roots 2, -8; completion term (6/2)^2 = 9/1
        assertTrue(
            VedicMathValidator.verifyPuranapuranabhyam(
                1L, 6L, -16L,
                9L, 1L,
                2L, -8L
            )
        )
    }

    @Test
    fun puranapuranabhyam_overflowGuards_failClosed() {
        // b^2 overflow
        assertFalse(
            "b^2 overflow must fail closed",
            VedicMathValidator.verifyPuranapuranabhyam(
                1L, 3_000_000_000L, -16L,
                9L, 1L,
                2L, -8L
            )
        )
        // 4ac overflow
        assertFalse(
            "4ac overflow must fail closed",
            VedicMathValidator.verifyPuranapuranabhyam(
                Long.MAX_VALUE / 2, 6L, 4L,
                9L, 1L,
                2L, -8L
            )
        )
        assertFalse(
            "a = 0 must fail (not a quadratic)",
            VedicMathValidator.verifyPuranapuranabhyam(0L, 6L, -16L, 9L, 1L, 2L, -8L)
        )
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // 11. Vyashtisamashtih Symmetric Average Multiplication
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    fun vyashtisamashtih_canonicalAndBoundary_succeeds() {
        // 58 × 62 = 60^2 - 2^2 = 3600 - 4 = 3596
        assertTrue(
            VedicMathValidator.verifyVyashtisamashtih(58L, 62L, 60L, 2L, 3596L)
        )
    }

    @Test
    fun vyashtisamashtih_overflowGuards_failClosed() {
        // p + q overflow
        assertFalse(
            "p + q overflow must fail closed",
            VedicMathValidator.verifyVyashtisamashtih(
                Long.MAX_VALUE - 1, Long.MAX_VALUE - 3,
                Long.MAX_VALUE - 2, 1L,
                1000L
            )
        )
        // avg^2 overflow
        assertFalse(
            "avg^2 overflow must fail closed",
            VedicMathValidator.verifyVyashtisamashtih(
                3_037_000_500L, 3_037_000_504L,
                3_037_000_502L, 2L,
                Long.MAX_VALUE
            )
        )
        assertFalse(
            "Long.MIN_VALUE must fail closed",
            VedicMathValidator.verifyVyashtisamashtih(Long.MIN_VALUE, 62L, 60L, 2L, 3596L)
        )
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // 12. Sopantyadvayamantyam Digit-Pairing Multiplication
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    fun sopantyadvayamantyam_canonicalAndBoundary_succeeds() {
        // 143 × 12 = 1716, rawValues = [1, 6, 11, 6]
        assertTrue(
            VedicMathValidator.verifySopantyadvayamantyam(
                143L,
                12,
                listOf(1, 6, 11, 6),
                1716L
            )
        )
    }

    @Test
    fun sopantyadvayamantyam_overflowGuards_failClosed() {
        // Multiplicand * multiplier overflow
        assertFalse(
            "Multiplicand * multiplier overflow must fail closed",
            VedicMathValidator.verifySopantyadvayamantyam(
                Long.MAX_VALUE / 2,
                12,
                listOf(1, 2, 3),
                1000L
            )
        )
        assertFalse(
            "Invalid multiplier outside 12..19 must fail",
            VedicMathValidator.verifySopantyadvayamantyam(143L, 25, listOf(1, 6, 11, 6), 1716L)
        )
        assertFalse(
            "Negative multiplicand must fail",
            VedicMathValidator.verifySopantyadvayamantyam(-143L, 12, listOf(1, 6, 11, 6), -1716L)
        )
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // 13. Gunitasamuccayah Factorization Auditing
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    fun gunitasamuccayah_canonicalAndBoundary_succeeds() {
        // (x + 2)(x + 3) = x^2 + 5x + 6
        assertTrue(
            VedicMathValidator.verifyGunitasamuccayah(
                1L, 2L,
                1L, 3L,
                1L, 5L, 6L
            )
        )
    }

    @Test
    fun gunitasamuccayah_overflowGuards_failClosed() {
        // Multiplication overflow in coefficients
        assertFalse(
            "Coefficients multiplication overflow must fail closed",
            VedicMathValidator.verifyGunitasamuccayah(
                Long.MAX_VALUE / 2, 2L,
                3L, 3L,
                1L, 5L, 6L
            )
        )
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // 14. Gunakasamuccayah Monic Factorization
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    fun gunakasamuccayah_canonicalAndBoundary_succeeds() {
        // x^2 + 7x + 10 = (x + 2)(x + 5) -> roots -2, -5
        assertTrue(
            VedicMathValidator.verifyGunakasamuccayah(
                7L, 10L,
                2L, 5L,
                -2L, -5L
            )
        )
    }

    @Test
    fun gunakasamuccayah_overflowGuards_failClosed() {
        // p * q overflow
        assertFalse(
            "p * q overflow must fail closed",
            VedicMathValidator.verifyGunakasamuccayah(
                7L, 10L,
                Long.MAX_VALUE / 2, 3L,
                -2L, -5L
            )
        )
        // Root negation overflow with Long.MIN_VALUE
        assertFalse(
            "Long.MIN_VALUE factor must fail closed",
            VedicMathValidator.verifyGunakasamuccayah(
                7L, 10L,
                Long.MIN_VALUE, 5L,
                -2L, -5L
            )
        )
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // 15. Chalana-Kalanabhyam Derivative-Discriminant Solver
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    fun chalanaKalanabhyam_canonicalAndBoundary_succeeds() {
        // x^2 - 8x + 7 = 0 -> D = 64 - 28 = 36, sqrt(D) = 6, roots 7, 1
        assertTrue(
            VedicMathValidator.verifyChalanaKalanabhyam(
                1L, -8L, 7L,
                36L, 6L,
                7L, 1L
            )
        )
    }

    @Test
    fun chalanaKalanabhyam_overflowGuards_failClosed() {
        // b^2 overflow
        assertFalse(
            "b^2 overflow must fail closed",
            VedicMathValidator.verifyChalanaKalanabhyam(
                1L, 3_000_000_000L, 7L,
                36L, 6L,
                7L, 1L
            )
        )
        // 4ac overflow
        assertFalse(
            "4ac overflow must fail closed",
            VedicMathValidator.verifyChalanaKalanabhyam(
                Long.MAX_VALUE / 2, -8L, 4L,
                36L, 6L,
                7L, 1L
            )
        )
        assertFalse(
            "a = 0 must fail",
            VedicMathValidator.verifyChalanaKalanabhyam(0L, -8L, 7L, 36L, 6L, 7L, 1L)
        )
        assertFalse(
            "Negative sqrtD must fail",
            VedicMathValidator.verifyChalanaKalanabhyam(1L, -8L, 7L, 36L, -6L, 7L, 1L)
        )
    }
}
