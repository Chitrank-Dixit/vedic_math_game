package com.ank.sutrasaga.domain.validation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Comprehensive tests for Urdhva-Tiryagbhyam (2x2) validation in [VedicMathValidator].
 *
 * Verifies:
 * 1. Valid 2-digit multiplication (with and without carries).
 * 2. Rejection of incorrect final products.
 * 3. Rejection of incorrect crosswise sums.
 * 4. Rejection of incorrect carry propagation (carry1 from units and carry2 from crosswise).
 * 5. Rejection of zero and negative inputs (including negative pairs where a * b > 0).
 * 6. Rejection of unsupported digit lengths (1-digit, 3+ digits).
 * 7. Correct behavior across all overloads (2-argument, 3-argument, step validator, crosswise validator).
 * 8. Independent mathematical calculation of intermediate and final values.
 */
class UrdhvaValidationTest {

    // ─────────────────────────────────────────────────────────────────────────────
    // 1. Valid Two-Digit Multiplication
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    fun validMultiplication_withoutCarries_succeeds() {
        // 31 × 21 = 651
        // Step 1: 1 × 1 = 1 (carry1 = 0, digit = 1)
        // Step 2: (3×1) + (1×2) = 5 (carry2 = 0, digit = 5)
        // Step 3: 3 × 2 = 6
        // Product = 651
        val a = 31L
        val b = 21L
        val expectedProduct = 651L
        val expectedCrosswiseSum = 5L
        val expectedCarry1 = 0L
        val expectedCarry2 = 0L

        // Independent arithmetic check
        assertEquals(expectedProduct, a * b)

        assertTrue("2-arg overload must succeed", VedicMathValidator.verifyUrdhva2x2(a, b))
        assertTrue("3-arg overload must succeed", VedicMathValidator.verifyUrdhva2x2(a, b, expectedProduct))
        assertTrue(
            "6-arg overload must succeed with correct steps",
            VedicMathValidator.verifyUrdhva2x2(a, b, expectedCrosswiseSum, expectedCarry1, expectedCarry2, expectedProduct)
        )
        assertTrue(
            "Crosswise sum validator must succeed",
            VedicMathValidator.verifyUrdhvaCrosswiseSum(a, b, expectedCrosswiseSum)
        )
    }

    @Test
    fun validMultiplication_withSingleCarry_canonicalWorld5Example() {
        // 23 × 41 = 943 (Canonical tutorial problem)
        // Step 1: 3 × 1 = 3 (carry1 = 0, digit = 3)
        // Step 2: (2×1) + (3×4) = 14 (carry2 = 1, digit = 4)
        // Step 3: 2 × 4 + 1 = 9
        // Product = 943
        val a = 23L
        val b = 41L
        val expectedProduct = 943L
        val expectedCrosswiseSum = 14L
        val expectedCarry1 = 0L
        val expectedCarry2 = 1L

        assertEquals(expectedProduct, a * b)

        assertTrue(VedicMathValidator.verifyUrdhva2x2(a, b))
        assertTrue(VedicMathValidator.verifyUrdhva2x2(a, b, expectedProduct))
        assertTrue(
            VedicMathValidator.verifyUrdhva2x2(a, b, expectedCrosswiseSum, expectedCarry1, expectedCarry2, expectedProduct)
        )
        assertTrue(VedicMathValidator.verifyUrdhvaCrosswiseSum(a, b, expectedCrosswiseSum))
    }

    @Test
    fun validMultiplication_withMultipleCarries_handlesCarryPropagation() {
        // 27 × 38 = 1026
        // Step 1: 7 × 8 = 56 -> unitDigit = 6, carry1 = 5
        // Step 2: (2×8) + (7×3) = 16 + 21 = 37 -> rawCross = 37 + 5 = 42 -> tensDigit = 2, carry2 = 4
        // Step 3: 2 × 3 + 4 = 10
        // Product = 1026
        val a = 27L
        val b = 38L
        val expectedProduct = 1026L
        val expectedCrosswiseSum = 37L
        val expectedCarry1 = 5L
        val expectedCarry2 = 4L

        assertEquals(expectedProduct, a * b)

        val steps = VedicMathValidator.calculateUrdhva2x2Steps(a, b)
        assertNotNull(steps)
        assertEquals(56L, steps!!.rawUnits)
        assertEquals(6L, steps.unitDigit)
        assertEquals(5L, steps.carry1)
        assertEquals(37L, steps.crosswiseSum)
        assertEquals(42L, steps.rawCrosswise)
        assertEquals(2L, steps.tensDigit)
        assertEquals(4L, steps.carry2)
        assertEquals(6L, steps.tensProduct)
        assertEquals(10L, steps.hundredsPart)
        assertEquals(1026L, steps.finalProduct)

        assertTrue(VedicMathValidator.verifyUrdhva2x2(a, b))
        assertTrue(VedicMathValidator.verifyUrdhva2x2(a, b, expectedProduct))
        assertTrue(
            VedicMathValidator.verifyUrdhva2x2(a, b, expectedCrosswiseSum, expectedCarry1, expectedCarry2, expectedProduct)
        )
    }

    @Test
    fun validMultiplication_largeCarries_89Times76Produces6764() {
        // 89 × 76 = 6764
        // Step 1: 9 × 6 = 54 -> digit = 4, carry1 = 5
        // Step 2: (8×6) + (9×7) = 48 + 63 = 111 -> rawCross = 111 + 5 = 116 -> digit = 6, carry2 = 11
        // Step 3: 8 × 7 + 11 = 56 + 11 = 67
        // Product = 6764
        val a = 89L
        val b = 76L
        val expectedProduct = 6764L
        val expectedCrosswiseSum = 111L
        val expectedCarry1 = 5L
        val expectedCarry2 = 11L

        assertEquals(expectedProduct, a * b)

        assertTrue(VedicMathValidator.verifyUrdhva2x2(a, b))
        assertTrue(VedicMathValidator.verifyUrdhva2x2(a, b, expectedProduct))
        assertTrue(
            VedicMathValidator.verifyUrdhva2x2(a, b, expectedCrosswiseSum, expectedCarry1, expectedCarry2, expectedProduct)
        )
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // 2. Rejection of Incorrect Final Products
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    fun incorrectFinalProduct_isRejected() {
        // 23 × 41 = 943
        assertFalse(
            "Off-by-one product must fail",
            VedicMathValidator.verifyUrdhva2x2(23L, 41L, 944L)
        )
        assertFalse(
            "Off-by-one product must fail",
            VedicMathValidator.verifyUrdhva2x2(23L, 41L, 942L)
        )
        assertFalse(
            "Transposed digits must fail",
            VedicMathValidator.verifyUrdhva2x2(23L, 41L, 934L)
        )
        assertFalse(
            "Negative product must fail",
            VedicMathValidator.verifyUrdhva2x2(23L, 41L, -943L)
        )
        assertFalse(
            "Zero product must fail",
            VedicMathValidator.verifyUrdhva2x2(23L, 41L, 0L)
        )
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // 3. Rejection of Incorrect Crosswise Sums
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    fun incorrectCrosswiseSum_isRejected() {
        // For 27 × 38 = 1026: crosswise sum = (2×8) + (7×3) = 37
        val a = 27L
        val b = 38L
        val correctCrosswiseSum = 37L
        val correctCarry1 = 5L
        val correctCarry2 = 4L
        val correctProduct = 1026L

        assertFalse(
            "Crosswise sum off by +1 must fail",
            VedicMathValidator.verifyUrdhva2x2(a, b, 38L, correctCarry1, correctCarry2, correctProduct)
        )
        assertFalse(
            "Crosswise sum off by -1 must fail",
            VedicMathValidator.verifyUrdhva2x2(a, b, 36L, correctCarry1, correctCarry2, correctProduct)
        )
        assertFalse(
            "Supplying rawCrosswise (37+5=42) instead of pure crosswise sum must fail",
            VedicMathValidator.verifyUrdhva2x2(a, b, 42L, correctCarry1, correctCarry2, correctProduct)
        )
        assertFalse(
            "Dedicated crosswise validator must reject wrong sum",
            VedicMathValidator.verifyUrdhvaCrosswiseSum(a, b, 999L)
        )
        assertTrue(
            "Dedicated crosswise validator must accept correct sum",
            VedicMathValidator.verifyUrdhvaCrosswiseSum(a, b, correctCrosswiseSum)
        )
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // 4. Rejection of Incorrect Carry Values
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    fun incorrectCarry1_isRejected() {
        // For 27 × 38: carry1 is 5 (from 7×8 = 56)
        val a = 27L
        val b = 38L
        val crosswiseSum = 37L
        val carry2 = 4L
        val product = 1026L

        assertFalse(
            "Carry1 off by -1 must fail",
            VedicMathValidator.verifyUrdhva2x2(a, b, crosswiseSum, 4L, carry2, product)
        )
        assertFalse(
            "Carry1 off by +1 must fail",
            VedicMathValidator.verifyUrdhva2x2(a, b, crosswiseSum, 6L, carry2, product)
        )
        assertFalse(
            "Zero carry1 when carry exists must fail",
            VedicMathValidator.verifyUrdhva2x2(a, b, crosswiseSum, 0L, carry2, product)
        )
    }

    @Test
    fun incorrectCarry2_isRejected() {
        // For 27 × 38: carry2 is 4 (from (37 + 5) / 10 = 4)
        val a = 27L
        val b = 38L
        val crosswiseSum = 37L
        val carry1 = 5L
        val product = 1026L

        assertFalse(
            "Carry2 off by -1 must fail",
            VedicMathValidator.verifyUrdhva2x2(a, b, crosswiseSum, carry1, 3L, product)
        )
        assertFalse(
            "Carry2 off by +1 must fail",
            VedicMathValidator.verifyUrdhva2x2(a, b, crosswiseSum, carry1, 5L, product)
        )
        assertFalse(
            "Zero carry2 when carry exists must fail",
            VedicMathValidator.verifyUrdhva2x2(a, b, crosswiseSum, carry1, 0L, product)
        )
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // 5. Zero and Negative Inputs
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    fun zeroInputs_areRejectedAcrossAllOverloads() {
        assertFalse("a = 0 must fail in 2-arg", VedicMathValidator.verifyUrdhva2x2(0L, 41L))
        assertFalse("b = 0 must fail in 2-arg", VedicMathValidator.verifyUrdhva2x2(23L, 0L))
        assertFalse("both 0 must fail in 2-arg", VedicMathValidator.verifyUrdhva2x2(0L, 0L))

        assertFalse("a = 0 must fail in 3-arg", VedicMathValidator.verifyUrdhva2x2(0L, 41L, 0L))
        assertFalse("b = 0 must fail in 3-arg", VedicMathValidator.verifyUrdhva2x2(23L, 0L, 0L))

        assertFalse(
            "a = 0 must fail in 6-arg",
            VedicMathValidator.verifyUrdhva2x2(0L, 41L, 0L, 0L, 0L, 0L)
        )
        assertFalse(
            "b = 0 must fail in crosswise validator",
            VedicMathValidator.verifyUrdhvaCrosswiseSum(23L, 0L, 0L)
        )
    }

    @Test
    fun negativeInputs_areRejectedEvenWhenProductIsPositive() {
        // Critical regression check:
        // In the legacy implementation, `return (a * b) > 0` returned TRUE for (-23) * (-41) = 943 > 0.
        // The validator must NOT return success merely because the product is positive!
        val negA = -23L
        val negB = -41L
        assertEquals("Product of two negatives is positive", 943L, negA * negB)

        assertFalse(
            "Two negative operands must FAIL in 2-arg overload despite positive product",
            VedicMathValidator.verifyUrdhva2x2(negA, negB)
        )
        assertFalse(
            "Two negative operands must FAIL in 3-arg overload despite expected == 943",
            VedicMathValidator.verifyUrdhva2x2(negA, negB, 943L)
        )
        assertFalse(
            "Two negative operands must FAIL in 6-arg overload",
            VedicMathValidator.verifyUrdhva2x2(negA, negB, 14L, 0L, 1L, 943L)
        )

        // Single negative operand
        assertFalse("Negative a must fail", VedicMathValidator.verifyUrdhva2x2(-23L, 41L))
        assertFalse("Negative b must fail", VedicMathValidator.verifyUrdhva2x2(23L, -41L))
        assertFalse("Negative a in 3-arg must fail", VedicMathValidator.verifyUrdhva2x2(-23L, 41L, -943L))
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // 6. Unsupported Digit Lengths (Contract: 10..99 only)
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    fun singleDigitInputs_areRejected() {
        // 5 × 6 = 30 > 0 (previously returned true in legacy 2-arg stub!)
        assertFalse("Single-digit operands must fail in 2-arg", VedicMathValidator.verifyUrdhva2x2(5L, 6L))
        assertFalse("Single-digit operands must fail in 3-arg", VedicMathValidator.verifyUrdhva2x2(5L, 6L, 30L))

        assertFalse("1-digit a with 2-digit b must fail", VedicMathValidator.verifyUrdhva2x2(9L, 25L))
        assertFalse("2-digit a with 1-digit b must fail", VedicMathValidator.verifyUrdhva2x2(25L, 9L))
        assertFalse("1-digit in crosswise validator must fail", VedicMathValidator.verifyUrdhvaCrosswiseSum(9L, 25L, 45L))
    }

    @Test
    fun threeOrMoreDigitInputs_areRejected() {
        // 100 × 25 = 2500 > 0
        assertFalse("3-digit operand must fail in 2-arg", VedicMathValidator.verifyUrdhva2x2(100L, 25L))
        assertFalse("3-digit operand must fail in 3-arg", VedicMathValidator.verifyUrdhva2x2(100L, 25L, 2500L))

        assertFalse("Both 3-digit operands must fail", VedicMathValidator.verifyUrdhva2x2(123L, 456L))
        assertFalse("4-digit operands must fail", VedicMathValidator.verifyUrdhva2x2(1234L, 5678L))
    }

    @Test
    fun boundaryInputs_10And99_areAccepted() {
        // Minimum valid 2-digit: 10 × 10 = 100
        assertTrue("10 × 10 must succeed in 2-arg", VedicMathValidator.verifyUrdhva2x2(10L, 10L))
        assertTrue("10 × 10 must succeed in 3-arg", VedicMathValidator.verifyUrdhva2x2(10L, 10L, 100L))

        // Maximum valid 2-digit: 99 × 99 = 9801
        assertTrue("99 × 99 must succeed in 2-arg", VedicMathValidator.verifyUrdhva2x2(99L, 99L))
        assertTrue("99 × 99 must succeed in 3-arg", VedicMathValidator.verifyUrdhva2x2(99L, 99L, 9801L))

        // Boundary rejects
        assertFalse("9 (just below min) must fail", VedicMathValidator.verifyUrdhva2x2(9L, 10L))
        assertFalse("100 (just above max) must fail", VedicMathValidator.verifyUrdhva2x2(100L, 99L))
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // 7. Step Calculation Utility and Helper Contract
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    fun calculateUrdhva2x2Steps_returnsNullForInvalidInputs() {
        assertNull("Negative input returns null", VedicMathValidator.calculateUrdhva2x2Steps(-10L, 20L))
        assertNull("Zero returns null", VedicMathValidator.calculateUrdhva2x2Steps(0L, 50L))
        assertNull("Single digit returns null", VedicMathValidator.calculateUrdhva2x2Steps(7L, 42L))
        assertNull("Three digits returns null", VedicMathValidator.calculateUrdhva2x2Steps(105L, 20L))
    }

    @Test
    fun isValidTwoDigitOperand_contract() {
        assertFalse(VedicMathValidator.isValidTwoDigitOperand(-1L))
        assertFalse(VedicMathValidator.isValidTwoDigitOperand(0L))
        assertFalse(VedicMathValidator.isValidTwoDigitOperand(9L))
        assertTrue(VedicMathValidator.isValidTwoDigitOperand(10L))
        assertTrue(VedicMathValidator.isValidTwoDigitOperand(55L))
        assertTrue(VedicMathValidator.isValidTwoDigitOperand(99L))
        assertFalse(VedicMathValidator.isValidTwoDigitOperand(100L))
        assertFalse(VedicMathValidator.isValidTwoDigitOperand(1000L))
    }
}
