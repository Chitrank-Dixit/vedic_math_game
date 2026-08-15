package com.ankh.sutrasaga.domain.models

import kotlin.math.abs

data class QuadraticEquation(val a: Long, val b: Long, val c: Long) {
    val discriminant: Long
        get() = b * b - 4 * a * c

    fun toFormattedString(): String {
        val aStr = when {
            a == 1L -> "x²"
            a == -1L -> "-x²"
            else -> "${a}x²"
        }

        val bStr = when {
            b == 0L -> ""
            b == 1L -> " + x"
            b == -1L -> " - x"
            b > 0L -> " + ${b}x"
            else -> " - ${abs(b)}x"
        }

        val cStr = when {
            c == 0L -> ""
            c > 0L -> " + $c"
            else -> " - ${abs(c)}"
        }

        return "$aStr$bStr$cStr = 0"
    }
}

enum class CompletionOperationType {
    NORMALIZE,
    MOVE_CONSTANT,
    ADD_COMPLETION,
    FORM_SQUARE,
    SOLVE_SQUARE,
    VERIFY
}

data class CompletionStep(
    val stepIndex: Int,
    val operationType: CompletionOperationType,
    val formulaDisplay: String,
    val stepResult: String,
    val explanationText: String
)

enum class QuadraticClassification {
    TWO_REAL_ROOTS,
    REPEATED_REAL_ROOT,
    NO_REAL_ROOTS,
    IRRATIONAL_REAL_ROOTS_DEFERRED,
    NOT_QUADRATIC,
    UNSUPPORTED
}

data class PuranapuranabhyamSolution(
    val classification: QuadraticClassification,
    val discriminant: Long,
    val completionTerm: Fraction,
    val root1: Fraction,
    val root2: Fraction,
    val isRepeated: Boolean,
    val steps: List<CompletionStep>
)

data class PuranapuranabhyamProblem(
    val id: String,
    val equation: QuadraticEquation,
    val classification: QuadraticClassification,
    val discriminant: Long,
    val completionTerm: Fraction,
    val root1: Fraction,
    val root2: Fraction,
    val difficultyTier: DifficultyTier,
    val steps: List<CompletionStep>,
    val distractors: List<Long>
)
