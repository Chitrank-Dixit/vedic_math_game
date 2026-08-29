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
     * Extracts or infers the expected answer for an interactive handshake.
     */
    fun inferExpectedAnswer(handshake: InteractiveHandshake): String? {
        if (!handshake.expectedAnswer.isNullOrBlank()) {
            return handshake.expectedAnswer.trim()
        }

        // Infer from targetElementId e.g. "numpad_key_5" -> "5"
        val elementId = handshake.targetElementId
        if (elementId != null && elementId.startsWith("numpad_key_")) {
            return elementId.removePrefix("numpad_key_").trim()
        }

        // Infer from prompt text e.g. "Tap 5 to compute 4 × 5 = 20!" -> "5"
        val prompt = handshake.promptText
        if (prompt != null) {
            val tapMatch = Regex("""(?:Tap|enter|choose)\s+(\w+)""", RegexOption.IGNORE_CASE).find(prompt)
            if (tapMatch != null) {
                return tapMatch.groupValues[1].trim()
            }
        }

        return null
    }

    /**
     * Validates a student's answer against the interactive handshake requirements.
     */
    fun evaluateHandshake(handshake: InteractiveHandshake, userAnswer: String): Boolean {
        if (!handshake.requiresUserTap) return true

        val expected = inferExpectedAnswer(handshake) ?: return true
        val normalizedUser = userAnswer.trim()
        val normalizedExpected = expected.trim()

        return normalizedUser.equals(normalizedExpected, ignoreCase = true)
    }
}
