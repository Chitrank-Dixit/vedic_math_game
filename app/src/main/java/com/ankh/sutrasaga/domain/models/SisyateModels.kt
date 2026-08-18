package com.ankh.sutrasaga.domain.models

/**
 * Represents a polynomial P(x) with integer coefficients: a_n x^n + ... + a_1 x + a_0.
 * Coefficients are ordered from highest degree (a_n) down to constant term (a_0).
 */
data class SimplePolynomial(
    val coefficients: List<Long>
) {
    val degree: Int
        get() = (coefficients.size - 1).coerceAtLeast(0)

    fun evaluate(x: Long): Long {
        var result = 0L
        for (coeff in coefficients) {
            result = result * x + coeff
        }
        return result
    }

    fun toFormattedString(): String {
        if (coefficients.isEmpty()) return "0"
        val sb = StringBuilder()
        val n = degree

        for (i in coefficients.indices) {
            val coeff = coefficients[i]
            val power = n - i
            if (coeff == 0L) continue

            val isFirst = sb.isEmpty()
            val sign = when {
                isFirst -> if (coeff < 0) "-" else ""
                coeff > 0 -> " + "
                else -> " - "
            }

            val absCoeff = kotlin.math.abs(coeff)
            val coeffStr = when {
                power == 0 -> "$absCoeff"
                absCoeff == 1L -> ""
                else -> "$absCoeff"
            }

            val varStr = when (power) {
                0 -> ""
                1 -> "x"
                2 -> "x²"
                3 -> "x³"
                4 -> "x⁴"
                else -> "x^$power"
            }

            sb.append(sign).append(coeffStr).append(varStr)
        }

        return if (sb.isEmpty()) "0" else sb.toString()
    }
}

/**
 * Classification states for Sisyate Shesamajna polynomial remainder evaluation.
 */
enum class SisyateClassification {
    NON_ZERO_REMAINDER,
    ZERO_REMAINDER_FACTOR_CONFIRMED,
    OVERFLOW_RISK
}

/**
 * Step in polynomial remainder substitution and evaluation.
 */
data class SisyateStep(
    val stepNumber: Int,
    val label: String,
    val formulaDisplay: String,
    val stepResult: String,
    val explanation: String
)

/**
 * Complete mathematical solution for Sisyate Shesamajna.
 */
data class SisyateSolution(
    val polynomial: SimplePolynomial,
    val k: Long,
    val remainder: Long,
    val quotientCoefficients: List<Long>,
    val steps: List<SisyateStep>,
    val classification: SisyateClassification
)
