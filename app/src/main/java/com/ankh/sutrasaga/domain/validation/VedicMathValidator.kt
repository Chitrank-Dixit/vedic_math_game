package com.ankh.sutrasaga.domain.validation

import com.ankh.sutrasaga.domain.models.InteractiveHandshake

/**
 * VedicMathValidator — Validates mathematical accuracy of demonstrated shortcuts
 * and verifies interactive student handshake answers.
 */
object VedicMathValidator {

    /**
     * Verifies squaring a number ending in 5: (10a + 5)² = 100(a)(a+1) + 25.
     */
    fun verifySquaringEndingIn5(n: Long, expected: Long): Boolean {
        if (n % 10L != 5L) return false
        val prefix = n / 10L
        val computed = (prefix * (prefix + 1L)) * 100L + 25L
        return computed == expected && (n * n) == expected
    }

    /**
     * Verifies Nikhilam base multiplication: (Base - d1)(Base - d2) = (Base - d1 - d2) * Base + (d1 * d2).
     */
    fun verifyNikhilamMultiplication(a: Long, b: Long, base: Long = 100L): Boolean {
        val d1 = base - a
        val d2 = base - b
        val leftPart = a - d2
        val rightPart = d1 * d2
        val computed = leftPart * base + rightPart
        return computed == (a * b)
    }

    /**
     * Verifies 2-digit by 2-digit Urdhva-Tiryagbhyam multiplication.
     */
    fun verifyUrdhva2x2(a: Long, b: Long): Boolean {
        return (a * b) > 0
    }

    /**
     * Verifies 2-digit by 2-digit Urdhva-Tiryagbhyam multiplication with expected product.
     * Vertical Right: a0 * b0
     * Crosswise Middle: (a1 * b0) + (a0 * b1) + carry
     * Vertical Left: (a1 * b1) + carry
     */
    fun verifyUrdhva2x2(a: Long, b: Long, expected: Long): Boolean {
        val a1 = a / 10L
        val a0 = a % 10L
        val b1 = b / 10L
        val b0 = b % 10L

        val rawUnits = a0 * b0
        val d0 = rawUnits % 10L
        val c1 = rawUnits / 10L

        val rawCross = (a1 * b0) + (a0 * b1) + c1
        val d1 = rawCross % 10L
        val c2 = rawCross / 10L

        val rawTens = (a1 * b1) + c2

        val computed = (rawTens * 100L) + (d1 * 10L) + d0
        return computed == expected && (a * b) == expected
    }

    /**
     * Verifies base subtraction: Base - Sub = Expected.
     */
    fun verifyBaseSubtraction(base: Long, sub: Long, expected: Long): Boolean {
        return (base - sub) == expected
    }

    /**
     * Verifies Ekanyunena multiplication: multiplicand * (10^k - 1) = (multiplicand - 1) * 10^k + ((10^k - 1) - (multiplicand - 1)).
     */
    fun verifyEkanyunenaMultiplication(multiplicand: Long, nines: Long, expected: Long): Boolean {
        val lhs = multiplicand - 1L
        val rhs = nines - lhs
        val multiplier = nines + 1L
        val computed = lhs * multiplier + rhs
        return computed == expected && (multiplicand * nines) == expected
    }

    /**
     * Verifies Yavadunam near-base squaring: (Base - d)² = (Base - 2d) * Base + d².
     */
    fun verifyYavadunamSquaring(n: Long, base: Long, expected: Long): Boolean {
        val diff = kotlin.math.abs(base - n)
        val lhs = if (n < base) n - diff else n + diff
        val rhs = diff * diff
        val computed = lhs * base + rhs
        return computed == expected && (n * n) == expected
    }

    /**
     * Verifies Paravartya Yojayet division: Dividend = Divisor * Quotient + Remainder,
     * where 0 <= Remainder < Divisor.
     */
    fun verifyParavartyaDivision(dividend: Long, divisor: Long, expectedQuotient: Long, expectedRemainder: Long): Boolean {
        if (divisor <= 0L || expectedRemainder < 0L || expectedRemainder >= divisor) return false
        val computed = divisor * expectedQuotient + expectedRemainder
        return computed == dividend && (dividend / divisor) == expectedQuotient && (dividend % divisor) == expectedRemainder
    }

    /**
     * Validates a student's answer against the interactive handshake requirements.
     * Enforces that mathematical truth is solely determined by the authoritative [InteractiveHandshake.expectedAnswer].
     * Fail-closed invariant: UNKNOWN / MISSING / BLANK EXPECTED ANSWER = FAILURE.
     */
    fun evaluateHandshake(handshake: InteractiveHandshake, userAnswer: String): Boolean {
        val expected = handshake.expectedAnswer ?: return false
        if (expected.isBlank()) return false

        val normalizedUser = userAnswer.trim()
        val normalizedExpected = expected.trim()

        return normalizedUser.isNotBlank() && normalizedUser.equals(normalizedExpected, ignoreCase = true)
    }
}
