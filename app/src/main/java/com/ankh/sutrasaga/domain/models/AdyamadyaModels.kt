package com.ankh.sutrasaga.domain.models

/**
 * Non-monic quadratic polynomial: A*x^2 + B*x + C
 */
data class NonMonicQuadratic(
    val a: Long,
    val b: Long,
    val c: Long
) {
    fun toFormattedString(): String {
        val aStr = if (a == 1L) "x²" else "${a}x²"
        val bStr = when {
            b == 0L -> ""
            b == 1L -> " + x"
            b == -1L -> " - x"
            b > 0L -> " + ${b}x"
            else -> " - ${-b}x"
        }
        val cStr = when {
            c == 0L -> ""
            c > 0L -> " + $c"
            else -> " - ${-c}"
        }
        return "$aStr$bStr$cStr"
    }

    val discriminant: Long
        get() = b * b - 4 * a * c
}

/**
 * Candidate first-last factor pair: (a1*x + c1)(a2*x + c2)
 */
data class FirstLastCandidate(
    val a1: Long,
    val a2: Long,
    val c1: Long,
    val c2: Long
) {
    val crossTerm: Long
        get() = a1 * c2 + a2 * c1

    fun toFactorString(): String {
        val f1 = formatLinearFactor(a1, c1)
        val f2 = formatLinearFactor(a2, c2)
        return "($f1)($f2)"
    }

    private fun formatLinearFactor(a: Long, c: Long): String {
        val aPart = if (a == 1L) "x" else "${a}x"
        val cPart = when {
            c == 0L -> ""
            c > 0L -> " + $c"
            else -> " - ${-c}"
        }
        return "$aPart$cPart"
    }
}

/**
 * Classification states for Adyamadyenantyamantyena quadratic factoring.
 */
enum class AdyamadyaClassification {
    FACTORABLE_NON_MONIC,
    NO_INTEGER_FACTORIZATION,
    MONIC_DEFERRED_TO_WORLD_15,
    OVERFLOW_RISK
}

/**
 * Step in Adyamadya calculation breakdown.
 */
data class AdyamadyaStep(
    val stepNumber: Int,
    val label: String,
    val formulaDisplay: String,
    val stepResult: String,
    val explanation: String
)

/**
 * Complete mathematical solution for Adyamadyenantyamantyena.
 */
data class AdyamadyaSolution(
    val quadratic: NonMonicQuadratic,
    val confirmedCandidate: FirstLastCandidate?,
    val attemptedCandidates: List<FirstLastCandidate>,
    val steps: List<AdyamadyaStep>,
    val classification: AdyamadyaClassification
)
