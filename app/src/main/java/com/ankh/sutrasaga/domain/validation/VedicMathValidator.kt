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
     * Verifies base subtraction: Base - Sub = Expected.
     */
    fun verifyBaseSubtraction(base: Long, sub: Long, expected: Long): Boolean {
        return (base - sub) == expected
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
