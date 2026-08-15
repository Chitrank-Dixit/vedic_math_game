package com.ankh.sutrasaga.domain.models

import kotlin.math.abs

enum class ShunyamFamily {
    FAMILY_A_COMMON_FACTOR,
    FAMILY_B_EQUAL_NUMERATOR_RECIPROCAL
}

data class LinearExpression(val a: Fraction, val b: Fraction) {
    fun toFormattedString(): String {
        val aRed = a.reduced
        val bRed = b.reduced

        val aStr = when {
            aRed.numerator == 1L && aRed.denominator == 1L -> "x"
            aRed.numerator == -1L && aRed.denominator == 1L -> "-x"
            aRed.denominator == 1L -> "${aRed.numerator}x"
            else -> "(${aRed.toFormattedString()})x"
        }

        if (bRed.numerator == 0L) return aStr

        val bSign = if (bRed.numerator > 0) " + " else " - "
        val bAbs = Fraction(abs(bRed.numerator), bRed.denominator).toFormattedString()
        return "$aStr$bSign$bAbs"
    }

    /**
     * Evaluates a*x + b for given x.
     */
    fun evaluate(x: Fraction): Fraction {
        val axNum = a.numerator * x.numerator
        val axDen = a.denominator * x.denominator
        val num = axNum * b.denominator + b.numerator * axDen
        val den = axDen * b.denominator
        return Fraction(num, den).reduced
    }

    /**
     * Solves a*x + b = 0 => x = -b/a.
     */
    fun solveZero(): Fraction {
        require(a.numerator != 0L) { "Coefficient 'a' cannot be zero when solving linear expression" }
        val num = -b.numerator * a.denominator
        val den = b.denominator * a.numerator
        return Fraction(num, den).reduced
    }
}

data class CommonFactorEquation(
    val k1: Long,
    val factor: LinearExpression,
    val k2: Long
) {
    fun toFormattedString(): String {
        return "$k1(${factor.toFormattedString()}) = $k2(${factor.toFormattedString()})"
    }
}

data class EqualNumeratorFractionEquation(
    val numerator: Long,
    val d1: LinearExpression,
    val d2: LinearExpression,
    val d3: LinearExpression,
    val d4: LinearExpression
) {
    fun toFormattedString(): String {
        val pStr = if (numerator == 1L) "1" else "$numerator"
        return "$pStr/(${d1.toFormattedString()}) + $pStr/(${d2.toFormattedString()}) = $pStr/(${d3.toFormattedString()}) + $pStr/(${d4.toFormattedString()})"
    }
}

enum class ShunyamClassification {
    UNIQUE_SOLUTION,
    NO_SOLUTION,
    IDENTITY,
    INVALID_DOMAIN,
    NOT_APPLICABLE,
    UNSUPPORTED
}

data class ShunyamStep(
    val stepIndex: Int,
    val recognizedPattern: String,
    val transformation: String,
    val candidateSolution: String,
    val excludedDomainValues: List<Fraction>,
    val verificationResultText: String,
    val explanationText: String
)

data class ShunyamSolution(
    val classification: ShunyamClassification,
    val candidateSolution: Fraction,
    val excludedValues: List<Fraction>,
    val isCandidateExcluded: Boolean,
    val steps: List<ShunyamStep>
)

data class ShunyamProblem(
    val id: String,
    val family: ShunyamFamily,
    val commonFactorEq: CommonFactorEquation?,
    val fractionEq: EqualNumeratorFractionEquation?,
    val classification: ShunyamClassification,
    val solution: Fraction,
    val excludedValues: List<Fraction>,
    val difficultyTier: DifficultyTier,
    val steps: List<ShunyamStep>,
    val distractors: List<Long>
)
