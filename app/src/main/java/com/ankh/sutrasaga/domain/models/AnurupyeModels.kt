package com.ankh.sutrasaga.domain.models

import kotlin.math.abs

data class Fraction(val numerator: Long, val denominator: Long = 1L) {
    val reduced: Fraction
        get() {
            require(denominator != 0L) { "Denominator cannot be zero" }
            val gcdVal = gcd(abs(numerator), abs(denominator))
            val sign = if (denominator < 0) -1L else 1L
            return Fraction((numerator / gcdVal) * sign, (denominator / gcdVal) * sign)
        }

    fun toFormattedString(): String {
        val r = reduced
        return if (r.denominator == 1L) "${r.numerator}" else "${r.numerator}/${r.denominator}"
    }

    private fun gcd(a: Long, b: Long): Long {
        var x = a
        var y = b
        while (y != 0L) {
            val t = y
            y = x % y
            x = t
        }
        return if (x == 0L) 1L else x
    }
}

data class LinearEquation(val a: Long, val b: Long, val c: Long) {
    fun toFormattedString(): String {
        val aStr = if (a == 1L) "x" else if (a == -1L) "-x" else "${a}x"
        val bSign = if (b >= 0) " + " else " - "
        val bVal = abs(b)
        val bStr = if (bVal == 1L) "y" else "${bVal}y"
        return "$aStr$bSign$bStr = $c"
    }
}

enum class AnurupyeCase {
    Y_ZERO,
    X_ZERO,
    NOT_APPLICABLE,
    INFINITE_SOLUTIONS,
    INCONSISTENT
}

data class AnurupyeStep(
    val stepIndex: Int,
    val ratioComparison: String,
    val isRatioMatching: Boolean,
    val inferredZeroVariable: String,
    val substitutionStep: String,
    val finalSolutionText: String,
    val explanationText: String
)

data class AnurupyeSolution(
    val caseType: AnurupyeCase,
    val xSolution: Fraction,
    val ySolution: Fraction,
    val steps: List<AnurupyeStep>
)

data class AnurupyeProblem(
    val id: String,
    val firstEquation: LinearEquation,
    val secondEquation: LinearEquation,
    val caseType: AnurupyeCase,
    val xSolution: Fraction,
    val ySolution: Fraction,
    val difficultyTier: DifficultyTier,
    val steps: List<AnurupyeStep>,
    val distractors: List<Long>
)
