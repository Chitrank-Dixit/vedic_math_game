package com.ankh.sutrasaga.domain.models

data class MonicQuadratic(
    val b: Long,
    val c: Long
) {
    fun eval(x: Long): Long = x * x + b * x + c

    fun toFormattedString(): String {
        val bStr = when {
            b > 0L -> " + ${if (b == 1L) "" else b.toString()}x"
            b < 0L -> " - ${if (b == -1L) "" else (-b).toString()}x"
            else -> ""
        }
        val cStr = when {
            c > 0L -> " + $c"
            c < 0L -> " - ${-c}"
            else -> ""
        }
        return "x²$bStr$cStr = 0"
    }
}

data class FactorPair(
    val p: Long,
    val q: Long
) {
    fun product(): Long = p * q
    fun sum(): Long = p + q
}

data class FactorizationCandidate(
    val pair: FactorPair,
    val productMatches: Boolean,
    val sumMatches: Boolean
)

enum class GunakasamuccayahOperationType {
    LIST_PAIRS,
    CHECK_PRODUCT,
    CHECK_SUM,
    FORM_FACTORS,
    SOLVE_ROOTS,
    VERIFY
}

data class GunakasamuccayahStep(
    val type: GunakasamuccayahOperationType,
    val description: String
)

enum class GunakasamuccayahClassification {
    FACTORED,
    REPEATED_FACTOR,
    NO_INTEGER_FACTOR_PAIR,
    INVALID_INPUT,
    OVERFLOW_RISK,
    UNSUPPORTED_NON_MONIC
}

data class GunakasamuccayahSolution(
    val quadratic: MonicQuadratic,
    val matchingPair: FactorPair?,
    val candidates: List<FactorizationCandidate>,
    val roots: List<Long>,
    val classification: GunakasamuccayahClassification,
    val steps: List<GunakasamuccayahStep>
)

data class GunakasamuccayahProblem(
    val id: String,
    val quadratic: MonicQuadratic,
    val solution: GunakasamuccayahSolution,
    val difficultyTier: DifficultyTier,
    val distractors: List<Long>
)
