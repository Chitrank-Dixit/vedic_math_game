package com.ankh.sutrasaga.domain.models

data class QuadraticForChalana(
    val a: Long,
    val b: Long,
    val c: Long
) {
    fun eval(x: Long): Long = a * x * x + b * x + c

    fun toFormattedString(): String {
        val aStr = when {
            a == 1L -> "x²"
            a == -1L -> "-x²"
            else -> "${a}x²"
        }
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
        return "$aStr$bStr$cStr = 0"
    }
}

data class DerivativeExpression(
    val coef: Long,
    val const: Long
) {
    fun eval(x: Long): Long = coef * x + const

    fun toFormattedString(): String {
        val cStr = when {
            coef == 1L -> "x"
            coef == -1L -> "-x"
            else -> "${coef}x"
        }
        val kStr = when {
            const > 0L -> " + $const"
            const < 0L -> " - ${-const}"
            else -> ""
        }
        return "$cStr$kStr"
    }
}

data class DiscriminantValue(
    val value: Long,
    val isPerfectSquare: Boolean,
    val squareRoot: Long?
)

enum class ChalanaOperationType {
    IDENTIFY_COEFFICIENTS,
    DERIVE,
    COMPUTE_DISCRIMINANT,
    TAKE_SQUARE_ROOT,
    SET_PLUS_BRANCH,
    SET_MINUS_BRANCH,
    SOLVE,
    VERIFY
}

data class ChalanaStep(
    val type: ChalanaOperationType,
    val description: String
)

enum class ChalanaClassification {
    TWO_REAL_ROOTS,
    REPEATED_REAL_ROOT,
    NO_REAL_ROOTS,
    IRRATIONAL_ROOTS_DEFERRED,
    NOT_QUADRATIC,
    OVERFLOW_RISK
}

data class ChalanaSolution(
    val quadratic: QuadraticForChalana,
    val derivative: DerivativeExpression,
    val discriminant: DiscriminantValue,
    val roots: List<Long>,
    val classification: ChalanaClassification,
    val steps: List<ChalanaStep>
)

data class ChalanaProblem(
    val id: String,
    val quadratic: QuadraticForChalana,
    val solution: ChalanaSolution,
    val difficultyTier: DifficultyTier,
    val distractors: List<Long>
)
