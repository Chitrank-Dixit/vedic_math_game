package com.ankh.sutrasaga.domain.validation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Contract verification test suite for Nikhilam Navatashcaramam Dashatah near-base multiplication validation.
 *
 * Verifies:
 * 1. Independent mathematical correctness of valid near-base multiplications (below, above, mixed).
 * 2. Strict rejection of incorrect claimed answers (ensuring identity check tautology is never mistaken for answer validation).
 * 3. Step decomposition, complement/deviation accuracy, carry/borrow handling, and intermediate arithmetic error detection.
 * 4. Rejection of invalid inputs (negative/zero operands, non-power-of-10 bases, arithmetic overflow).
 * 5. Regression coverage for all canonical curriculum lesson examples.
 */
class NikhilamValidatorContractTest {

    // ─────────────────────────────────────────────────────────────────────────────
    // 1. Valid Near-Base Multiplication Below Base
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    fun testValidNearBaseMultiplicationBelowBase() {
        // Base 100: 98 × 97 = 9506 (devs: -2, -3)
        val steps98x97 = VedicMathValidator.calculateNikhilamSteps(98L, 97L, 100L)
        assertNotNull(steps98x97)
        assertEquals(-2L, steps98x97!!.devA)
        assertEquals(-3L, steps98x97.devB)
        assertEquals(2L, steps98x97.deficiencyA)
        assertEquals(3L, steps98x97.deficiencyB)
        assertEquals(95L, steps98x97.crossLeft)
        assertEquals(6L, steps98x97.rawRight)
        assertEquals(0L, steps98x97.carry)
        assertEquals(0L, steps98x97.borrow)
        assertEquals(95L, steps98x97.adjustedLeft)
        assertEquals(6L, steps98x97.adjustedRight)
        assertEquals(9506L, steps98x97.finalProduct)
        assertTrue(VedicMathValidator.verifyNikhilamMultiplication(98L, 97L, 9506L, 100L))

        // Base 100: 96 × 92 = 8832 (devs: -4, -8)
        val steps96x92 = VedicMathValidator.calculateNikhilamSteps(96L, 92L, 100L)
        assertNotNull(steps96x92)
        assertEquals(-4L, steps96x92!!.devA)
        assertEquals(-8L, steps96x92.devB)
        assertEquals(88L, steps96x92.adjustedLeft)
        assertEquals(32L, steps96x92.adjustedRight)
        assertEquals(8832L, steps96x92.finalProduct)
        assertTrue(VedicMathValidator.verifyNikhilamMultiplication(96L, 92L, 8832L, 100L))

        // Base 100: 99 × 96 = 9504 (devs: -1, -4)
        val steps99x96 = VedicMathValidator.calculateNikhilamSteps(99L, 96L, 100L)
        assertNotNull(steps99x96)
        assertEquals(-1L, steps99x96!!.devA)
        assertEquals(-4L, steps99x96.devB)
        assertEquals(95L, steps99x96.adjustedLeft)
        assertEquals(4L, steps99x96.adjustedRight)
        assertEquals(9504L, steps99x96.finalProduct)
        assertTrue(VedicMathValidator.verifyNikhilamMultiplication(99L, 96L, 9504L, 100L))

        // Base 100: 93 × 94 = 8742 (devs: -7, -6)
        val steps93x94 = VedicMathValidator.calculateNikhilamSteps(93L, 94L, 100L)
        assertNotNull(steps93x94)
        assertEquals(8742L, steps93x94!!.finalProduct)
        assertTrue(VedicMathValidator.verifyNikhilamMultiplication(93L, 94L, 8742L, 100L))

        // Base 1000: 996 × 994 = 990024 (devs: -4, -6)
        val steps996x994 = VedicMathValidator.calculateNikhilamSteps(996L, 994L, 1000L)
        assertNotNull(steps996x994)
        assertEquals(-4L, steps996x994!!.devA)
        assertEquals(-6L, steps996x994.devB)
        assertEquals(990L, steps996x994.adjustedLeft)
        assertEquals(24L, steps996x994.adjustedRight)
        assertEquals(990024L, steps996x994.finalProduct)
        assertTrue(VedicMathValidator.verifyNikhilamMultiplication(996L, 994L, 990024L, 1000L))

        // Base 10: 8 × 9 = 72 (devs: -2, -1)
        val steps8x9 = VedicMathValidator.calculateNikhilamSteps(8L, 9L, 10L)
        assertNotNull(steps8x9)
        assertEquals(-2L, steps8x9!!.devA)
        assertEquals(-1L, steps8x9.devB)
        assertEquals(7L, steps8x9.adjustedLeft)
        assertEquals(2L, steps8x9.adjustedRight)
        assertEquals(72L, steps8x9.finalProduct)
        assertTrue(VedicMathValidator.verifyNikhilamMultiplication(8L, 9L, 72L, 10L))
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // 2. Valid Near-Base Multiplication Above Base
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    fun testValidNearBaseMultiplicationAboveBase() {
        // Base 100: 104 × 107 = 11128 (devs: +4, +7)
        val steps104x107 = VedicMathValidator.calculateNikhilamSteps(104L, 107L, 100L)
        assertNotNull(steps104x107)
        assertEquals(4L, steps104x107!!.devA)
        assertEquals(7L, steps104x107.devB)
        assertEquals(111L, steps104x107.crossLeft)
        assertEquals(28L, steps104x107.rawRight)
        assertEquals(0L, steps104x107.carry)
        assertEquals(0L, steps104x107.borrow)
        assertEquals(111L, steps104x107.adjustedLeft)
        assertEquals(28L, steps104x107.adjustedRight)
        assertEquals(11128L, steps104x107.finalProduct)
        assertTrue(VedicMathValidator.verifyNikhilamMultiplication(104L, 107L, 11128L, 100L))

        // Base 100: 105 × 106 = 11130 (devs: +5, +6)
        assertTrue(VedicMathValidator.verifyNikhilamMultiplication(105L, 106L, 11130L, 100L))

        // Base 10: 12 × 13 = 156 (devs: +2, +3)
        val steps12x13 = VedicMathValidator.calculateNikhilamSteps(12L, 13L, 10L)
        assertNotNull(steps12x13)
        assertEquals(2L, steps12x13!!.devA)
        assertEquals(3L, steps12x13.devB)
        assertEquals(15L, steps12x13.adjustedLeft)
        assertEquals(6L, steps12x13.adjustedRight)
        assertEquals(156L, steps12x13.finalProduct)
        assertTrue(VedicMathValidator.verifyNikhilamMultiplication(12L, 13L, 156L, 10L))

        // Base 1000: 1005 × 1008 = 1013040 (devs: +5, +8)
        assertTrue(VedicMathValidator.verifyNikhilamMultiplication(1005L, 1008L, 1013040L, 1000L))
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // 3. Numbers on Either Side of the Base (Mixed Signs & Borrow Behavior)
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    fun testNumbersOnEitherSideOfBaseWithBorrow() {
        // Base 100: 103 × 98 = 10094 (devA = +3, devB = -2)
        // crossLeft = 101, rawRight = -6.
        // Borrow 1 from 101 -> left = 100, right = 100 - 6 = 94.
        val steps103x98 = VedicMathValidator.calculateNikhilamSteps(103L, 98L, 100L)
        assertNotNull(steps103x98)
        assertEquals(3L, steps103x98!!.devA)
        assertEquals(-2L, steps103x98.devB)
        assertEquals(101L, steps103x98.crossLeft)
        assertEquals(-6L, steps103x98.rawRight)
        assertEquals(0L, steps103x98.carry)
        assertEquals(1L, steps103x98.borrow)
        assertEquals(100L, steps103x98.adjustedLeft)
        assertEquals(94L, steps103x98.adjustedRight)
        assertEquals(10094L, steps103x98.finalProduct)
        assertTrue(VedicMathValidator.verifyNikhilamMultiplication(103L, 98L, 10094L, 100L))

        // Base 100: 105 × 95 = 9975 (devA = +5, devB = -5)
        // crossLeft = 100, rawRight = -25 -> borrow 1 -> left = 99, right = 75.
        val steps105x95 = VedicMathValidator.calculateNikhilamSteps(105L, 95L, 100L)
        assertNotNull(steps105x95)
        assertEquals(100L, steps105x95!!.crossLeft)
        assertEquals(-25L, steps105x95.rawRight)
        assertEquals(1L, steps105x95.borrow)
        assertEquals(99L, steps105x95.adjustedLeft)
        assertEquals(75L, steps105x95.adjustedRight)
        assertEquals(9975L, steps105x95.finalProduct)
        assertTrue(VedicMathValidator.verifyNikhilamMultiplication(105L, 95L, 9975L, 100L))

        // Base 10: 12 × 8 = 96 (devA = +2, devB = -2)
        // crossLeft = 10, rawRight = -4 -> borrow 1 -> left = 9, right = 6.
        val steps12x8 = VedicMathValidator.calculateNikhilamSteps(12L, 8L, 10L)
        assertNotNull(steps12x8)
        assertEquals(10L, steps12x8!!.crossLeft)
        assertEquals(-4L, steps12x8.rawRight)
        assertEquals(1L, steps12x8.borrow)
        assertEquals(9L, steps12x8.adjustedLeft)
        assertEquals(6L, steps12x8.adjustedRight)
        assertEquals(96L, steps12x8.finalProduct)
        assertTrue(VedicMathValidator.verifyNikhilamMultiplication(12L, 8L, 96L, 10L))

        // Multi-base borrow: Base 100: 125 × 88 = 11000 (devA = +25, devB = -12)
        // crossLeft = 113, rawRight = -300 -> borrow 3 -> left = 110, right = 0.
        val steps125x88 = VedicMathValidator.calculateNikhilamSteps(125L, 88L, 100L)
        assertNotNull(steps125x88)
        assertEquals(113L, steps125x88!!.crossLeft)
        assertEquals(-300L, steps125x88.rawRight)
        assertEquals(3L, steps125x88.borrow)
        assertEquals(110L, steps125x88.adjustedLeft)
        assertEquals(0L, steps125x88.adjustedRight)
        assertEquals(11000L, steps125x88.finalProduct)
        assertTrue(VedicMathValidator.verifyNikhilamMultiplication(125L, 88L, 11000L, 100L))
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // 4. Carry Behavior (Right Part Exceeds Base Digits)
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    fun testCarryBehaviorWhenRightPartExceedsBase() {
        // Base 100: 88 × 88 = 7744 (devA = -12, devB = -12)
        // crossLeft = 76, rawRight = 144.
        // carry = 1, adjustedLeft = 77, adjustedRight = 44.
        val steps88x88 = VedicMathValidator.calculateNikhilamSteps(88L, 88L, 100L)
        assertNotNull(steps88x88)
        assertEquals(76L, steps88x88!!.crossLeft)
        assertEquals(144L, steps88x88.rawRight)
        assertEquals(1L, steps88x88.carry)
        assertEquals(0L, steps88x88.borrow)
        assertEquals(77L, steps88x88.adjustedLeft)
        assertEquals(44L, steps88x88.adjustedRight)
        assertEquals(7744L, steps88x88.finalProduct)
        assertTrue(VedicMathValidator.verifyNikhilamMultiplication(88L, 88L, 7744L, 100L))

        // Base 10: 6 × 7 = 42 (devA = -4, devB = -3)
        // crossLeft = 3, rawRight = 12 -> carry = 1 -> left = 4, right = 2.
        val steps6x7 = VedicMathValidator.calculateNikhilamSteps(6L, 7L, 10L)
        assertNotNull(steps6x7)
        assertEquals(3L, steps6x7!!.crossLeft)
        assertEquals(12L, steps6x7.rawRight)
        assertEquals(1L, steps6x7.carry)
        assertEquals(4L, steps6x7.adjustedLeft)
        assertEquals(2L, steps6x7.adjustedRight)
        assertEquals(42L, steps6x7.finalProduct)
        assertTrue(VedicMathValidator.verifyNikhilamMultiplication(6L, 7L, 42L, 10L))

        // Base 100: 115 × 112 = 12880 (devA = +15, devB = +12)
        // crossLeft = 127, rawRight = 180 -> carry = 1 -> left = 128, right = 80.
        val steps115x112 = VedicMathValidator.calculateNikhilamSteps(115L, 112L, 100L)
        assertNotNull(steps115x112)
        assertEquals(127L, steps115x112!!.crossLeft)
        assertEquals(180L, steps115x112.rawRight)
        assertEquals(1L, steps115x112.carry)
        assertEquals(128L, steps115x112.adjustedLeft)
        assertEquals(80L, steps115x112.adjustedRight)
        assertEquals(12880L, steps115x112.finalProduct)
        assertTrue(VedicMathValidator.verifyNikhilamMultiplication(115L, 112L, 12880L, 100L))
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // 5. Rejection of Incorrect Claimed Final Answers
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    fun testIncorrectClaimedFinalAnswerFails() {
        // Correct product of 98 × 97 is 9506.
        assertFalse("Off-by-one low must fail", VedicMathValidator.verifyNikhilamMultiplication(98L, 97L, 9505L, 100L))
        assertFalse("Off-by-one high must fail", VedicMathValidator.verifyNikhilamMultiplication(98L, 97L, 9507L, 100L))
        assertFalse("Rounded answer must fail", VedicMathValidator.verifyNikhilamMultiplication(98L, 97L, 9500L, 100L))
        assertFalse("Completely wrong answer must fail", VedicMathValidator.verifyNikhilamMultiplication(98L, 97L, 8832L, 100L))
        assertFalse("Zero expected product must fail", VedicMathValidator.verifyNikhilamMultiplication(98L, 97L, 0L, 100L))
        assertFalse("Negative expected product must fail", VedicMathValidator.verifyNikhilamMultiplication(98L, 97L, -9506L, 100L))

        // Incorrect answers on numbers above base
        assertFalse("Wrong answer for 104 × 107 must fail", VedicMathValidator.verifyNikhilamMultiplication(104L, 107L, 11120L, 100L))

        // Incorrect answers on numbers with borrow (unborrowed left 101, unborrowed right -6)
        assertFalse("Forgetting borrow must fail", VedicMathValidator.verifyNikhilamMultiplication(103L, 98L, 10106L, 100L))
        assertFalse("Wrong answer on borrow must fail", VedicMathValidator.verifyNikhilamMultiplication(103L, 98L, 10090L, 100L))

        // Incorrect answers on numbers with carry (forgetting carry: 7644 instead of 7744)
        assertFalse("Forgetting carry must fail", VedicMathValidator.verifyNikhilamMultiplication(88L, 88L, 7644L, 100L))
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // 6. Incorrect Complement or Deviation in Decomposition Fails
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    fun testIncorrectComplementOrDeviationFails() {
        // 98 × 97, base 100: correct devA = -2, devB = -3, left = 95, right = 6, product = 9506
        assertTrue(
            "Genuine steps must pass",
            VedicMathValidator.verifyNikhilamDecomposition(
                a = 98L, b = 97L,
                expectedDevA = -2L, expectedDevB = -3L,
                expectedLeft = 95L, expectedRight = 6L,
                expectedProduct = 9506L, base = 100L
            )
        )

        // Wrong deviations (positive instead of negative)
        assertFalse(
            "Positive deviations for numbers below base must fail",
            VedicMathValidator.verifyNikhilamDecomposition(
                a = 98L, b = 97L,
                expectedDevA = 2L, expectedDevB = 3L,
                expectedLeft = 95L, expectedRight = 6L,
                expectedProduct = 9506L, base = 100L
            )
        )

        // Incorrect devA
        assertFalse(
            "Wrong devA must fail",
            VedicMathValidator.verifyNikhilamDecomposition(
                a = 98L, b = 97L,
                expectedDevA = -4L, expectedDevB = -3L,
                expectedLeft = 95L, expectedRight = 6L,
                expectedProduct = 9506L, base = 100L
            )
        )

        // Incorrect devB
        assertFalse(
            "Wrong devB must fail",
            VedicMathValidator.verifyNikhilamDecomposition(
                a = 98L, b = 97L,
                expectedDevA = -2L, expectedDevB = -5L,
                expectedLeft = 95L, expectedRight = 6L,
                expectedProduct = 9506L, base = 100L
            )
        )
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // 7. Intermediate Arithmetic Errors Fail
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    fun testIntermediateArithmeticErrorsFail() {
        // Claimed left part arithmetic error (94 instead of 95)
        assertFalse(
            "Corrupted left part must fail",
            VedicMathValidator.verifyNikhilamDecomposition(
                a = 98L, b = 97L,
                expectedDevA = -2L, expectedDevB = -3L,
                expectedLeft = 94L, expectedRight = 6L,
                expectedProduct = 9506L, base = 100L
            )
        )

        // Claimed right part arithmetic error (7 instead of 6)
        assertFalse(
            "Corrupted right part must fail",
            VedicMathValidator.verifyNikhilamDecomposition(
                a = 98L, b = 97L,
                expectedDevA = -2L, expectedDevB = -3L,
                expectedLeft = 95L, expectedRight = 7L,
                expectedProduct = 9506L, base = 100L
            )
        )

        // Mutated step object verification
        val authenticSteps = VedicMathValidator.calculateNikhilamSteps(98L, 97L, 100L)!!
        assertTrue(VedicMathValidator.verifyNikhilamDecomposition(98L, 97L, authenticSteps, 9506L))

        // Corrupted step: wrong final product
        val corruptedProductSteps = authenticSteps.copy(finalProduct = 9500L)
        assertFalse(VedicMathValidator.verifyNikhilamDecomposition(98L, 97L, corruptedProductSteps, 9506L))

        // Corrupted step: wrong adjusted left
        val corruptedLeftSteps = authenticSteps.copy(adjustedLeft = 94L)
        assertFalse(VedicMathValidator.verifyNikhilamDecomposition(98L, 97L, corruptedLeftSteps, 9506L))

        // Corrupted step: wrong adjusted right
        val corruptedRightSteps = authenticSteps.copy(adjustedRight = 7L)
        assertFalse(VedicMathValidator.verifyNikhilamDecomposition(98L, 97L, corruptedRightSteps, 9506L))
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // 8. Invalid or Unsupported Inputs Fail Closed
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    fun testInvalidOrUnsupportedInputsFailClosed() {
        // Zero operands
        assertFalse(VedicMathValidator.verifyNikhilamMultiplication(0L, 97L, 0L, 100L))
        assertFalse(VedicMathValidator.verifyNikhilamMultiplication(98L, 0L, 0L, 100L))
        assertNull(VedicMathValidator.calculateNikhilamSteps(0L, 97L, 100L))

        // Negative operands
        assertFalse(VedicMathValidator.verifyNikhilamMultiplication(-98L, 97L, -9506L, 100L))
        assertFalse(VedicMathValidator.verifyNikhilamMultiplication(98L, -97L, -9506L, 100L))
        assertNull(VedicMathValidator.calculateNikhilamSteps(-98L, 97L, 100L))

        // Invalid non-power-of-10 bases
        assertFalse("Base 50 is an Anurupyena sub-base, not canonical Nikhilam base",
            VedicMathValidator.isValidNikhilamBase(50L))
        assertFalse("Base 200 is not a power of 10", VedicMathValidator.isValidNikhilamBase(200L))
        assertFalse("Base 25 is not a power of 10", VedicMathValidator.isValidNikhilamBase(25L))
        assertFalse("Base 0 is invalid", VedicMathValidator.isValidNikhilamBase(0L))
        assertFalse("Negative base is invalid", VedicMathValidator.isValidNikhilamBase(-100L))
        assertFalse("Base 1 is below minimum 10", VedicMathValidator.isValidNikhilamBase(1L))

        // Multiplications with invalid bases fail
        assertFalse(VedicMathValidator.verifyNikhilamMultiplication(48L, 46L, 2208L, 50L))
        assertNull(VedicMathValidator.calculateNikhilamSteps(48L, 46L, 50L))
        assertFalse(VedicMathValidator.verifyNikhilamMultiplication(98L, 97L, 9506L, 0L))
        assertFalse(VedicMathValidator.verifyNikhilamMultiplication(98L, 97L, 9506L, -100L))

        // Valid power-of-10 bases
        assertTrue(VedicMathValidator.isValidNikhilamBase(10L))
        assertTrue(VedicMathValidator.isValidNikhilamBase(100L))
        assertTrue(VedicMathValidator.isValidNikhilamBase(1000L))
        assertTrue(VedicMathValidator.isValidNikhilamBase(10000L))
        assertTrue(VedicMathValidator.isValidNikhilamBase(100000L))

        // Overflow safety
        assertFalse(
            "Near Long.MAX_VALUE multiplication must fail closed",
            VedicMathValidator.verifyNikhilamMultiplication(Long.MAX_VALUE - 1, Long.MAX_VALUE - 2, 100L, 100L)
        )
        assertNull(VedicMathValidator.calculateNikhilamSteps(Long.MAX_VALUE - 1, Long.MAX_VALUE - 2, 100L))
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // 9. Contract Distinction: Identity Check vs Answer Validation
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    fun testContractDistinction_IdentityCheckVsAnswerValidation() {
        // The algebraic identity ((a + b - base) * base) + (base - a)(base - b) == a * b
        // holds mathematically for ANY valid positive operands, even those not close to base:
        assertTrue(
            "Identity check passes on any mathematically consistent values",
            VedicMathValidator.verifyNikhilamIdentity(47L, 83L, 100L)
        )
        assertTrue(
            "Identity check passes on canonical example",
            VedicMathValidator.verifyNikhilamIdentity(98L, 97L, 100L)
        )

        // CRITICAL CONTRACT DISTINCTION:
        // verifyNikhilamMultiplication requires the CLAIMED answer and verifies it independently.
        // A bogus claimed answer CANNOT pass simply because the identity is internally consistent!
        assertFalse(
            "Bogus answer MUST be rejected by verifyNikhilamMultiplication",
            VedicMathValidator.verifyNikhilamMultiplication(98L, 97L, 9999L, 100L)
        )
        assertFalse(
            "Off-by-one answer MUST be rejected by verifyNikhilamMultiplication",
            VedicMathValidator.verifyNikhilamMultiplication(98L, 97L, 9507L, 100L)
        )
        assertTrue(
            "Correct answer passes verifyNikhilamMultiplication",
            VedicMathValidator.verifyNikhilamMultiplication(98L, 97L, 9506L, 100L)
        )
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // 10. Regression Coverage for Actual Lesson Examples
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    fun testRegressionCasesForActualLessonExamples() {
        // World 2 Curriculum Canonical Examples:
        // Example 1: 98 × 97 = 9506
        val expected1 = 98L * 97L
        assertEquals(9506L, expected1)
        assertTrue(VedicMathValidator.verifyNikhilamMultiplication(98L, 97L, expected1, 100L))

        // Example 2: 96 × 92 = 8832
        val expected2 = 96L * 92L
        assertEquals(8832L, expected2)
        assertTrue(VedicMathValidator.verifyNikhilamMultiplication(96L, 92L, expected2, 100L))

        // Example 3: 99 × 96 = 9504
        val expected3 = 99L * 96L
        assertEquals(9504L, expected3)
        assertTrue(VedicMathValidator.verifyNikhilamMultiplication(99L, 96L, expected3, 100L))

        // Example 4: 104 × 106 = 11024
        val expected4 = 104L * 106L
        assertEquals(11024L, expected4)
        assertTrue(VedicMathValidator.verifyNikhilamMultiplication(104L, 106L, expected4, 100L))

        // World 2 canonical subtraction demonstration: 1000 - 364 = 636
        val expectedSub = 1000L - 364L
        assertEquals(636L, expectedSub)
        assertTrue(VedicMathValidator.verifyBaseSubtraction(1000L, 364L, expectedSub))
    }
}
