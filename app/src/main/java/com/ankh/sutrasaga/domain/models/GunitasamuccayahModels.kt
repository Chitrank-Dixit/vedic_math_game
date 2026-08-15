package com.ankh.sutrasaga.domain.models

data class LinearFactor(
    val a: Long,
    val b: Long
) {
    fun eval(x: Long): Long = a * x + b

    fun sumOfCoefficients(): Long = a + b

    fun toFormattedString(): String {
        val aStr = when (a) {
            1L -> "x"
            -1L -> "-x"
            else -> "${a}x"
        }
        val bStr = when {
            b > 0 -> " + $b"
            b < 0 -> " - ${-b}"
            else -> ""
        }
        return "($aStr$bStr)"
    }
}

data class QuadraticPolynomial(
    val a2: Long,
    val a1: Long,
    val a0: Long
) {
    fun eval(x: Long): Long = a2 * x * x + a1 * x + a0

    fun sumOfCoefficients(): Long = a2 + a1 + a0

    fun toFormattedString(): String {
        val a2Str = when (a2) {
            1L -> "x²"
            -1L -> "-x²"
            else -> "${a2}x²"
        }
        val a1Str = when {
            a1 > 0L -> " + ${if (a1 == 1L) "" else a1.toString()}x"
            a1 < 0L -> " - ${if (a1 == -1L) "" else (-a1).toString()}x"
            else -> ""
        }
        val a0Str = when {
            a0 > 0L -> " + $a0"
            a0 < 0L -> " - ${-a0}"
            else -> ""
        }
        return "$a2Str$a1Str$a0Str"
    }
}

data class FactorizationProposal(
    val factors: List<LinearFactor>,
    val claimedProduct: QuadraticPolynomial
)

data class CoefficientSumCheck(
    val expectedSum: Long,
    val actualSum: Long,
    val passed: Boolean
)

data class ExpansionCheck(
    val expectedPolynomial: QuadraticPolynomial,
    val actualPolynomial: QuadraticPolynomial,
    val passed: Boolean
)

enum class GunitasamuccayahClassification {
    VALID_FACTORISATION,
    INVALID_EXPANSION,
    COEFFICIENT_SUM_MISMATCH,
    UNSUPPORTED_FORM,
    OVERFLOW_RISK
}

data class GunitasamuccayahSolution(
    val proposal: FactorizationProposal,
    val exactProduct: QuadraticPolynomial,
    val sumCheck: CoefficientSumCheck,
    val expansionCheck: ExpansionCheck,
    val classification: GunitasamuccayahClassification
)

data class GunitasamuccayahProblem(
    val id: String,
    val proposal: FactorizationProposal,
    val solution: GunitasamuccayahSolution,
    val difficultyTier: DifficultyTier,
    val distractors: List<Long>
)
