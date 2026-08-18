package com.ankh.sutrasaga.engine.upasutras

import com.ankh.sutrasaga.domain.models.AnswerFormat
import com.ankh.sutrasaga.domain.models.DecompositionStep
import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.SimplePolynomial
import com.ankh.sutrasaga.domain.models.SisyateClassification
import com.ankh.sutrasaga.domain.models.SisyateSolution
import com.ankh.sutrasaga.domain.models.SisyateStep
import com.ankh.sutrasaga.domain.models.SutraProblem
import com.ankh.sutrasaga.domain.models.UpaSutraId
import com.ankh.sutrasaga.engine.UpaSutraGenerator
import java.util.UUID
import kotlin.math.abs
import kotlin.random.Random

/**
 * Problem generator and solver for Upa-Sutra 2: Sisyate Sesasamjnah
 * ("The remainder remains constant" — Polynomial Remainder Theorem).
 */
class SisyateSesasamjnahGenerator(
    private val random: Random = Random.Default
) : UpaSutraGenerator {

    override val upaSutraId: UpaSutraId = UpaSutraId.SHISYATE_SHESAMAJNA
    override val sutraName: String = "Sisyate Sesasamjnah"

    fun solve(poly: SimplePolynomial, k: Long): SisyateSolution {
        val remainder = poly.evaluate(k)
        if (abs(remainder) > 10000000L) {
            return SisyateSolution(
                polynomial = poly,
                k = k,
                remainder = remainder,
                quotientCoefficients = emptyList(),
                steps = emptyList(),
                classification = SisyateClassification.OVERFLOW_RISK
            )
        }

        val (quotientCoeffs, divRemainder) = polynomialLongDivision(poly, k)
        require(remainder == divRemainder) {
            "Internal Error: Remainder theorem evaluation $remainder does not match synthetic division remainder $divRemainder"
        }

        val steps = mutableListOf<SisyateStep>()
        val divisorStr = if (k >= 0) "(x - $k)" else "(x + ${-k})"

        steps.add(
            SisyateStep(
                stepNumber = 1,
                label = "Identify Root of Divisor",
                formulaDisplay = "$divisorStr = 0 ⟹ x = $k",
                stepResult = "k = $k",
                explanation = "Set linear divisor to zero to find substitution root k = $k."
            )
        )

        val termEvaluations = mutableListOf<String>()
        val n = poly.degree
        var sum = 0L

        for (i in poly.coefficients.indices) {
            val coeff = poly.coefficients[i]
            val power = n - i
            val kPower = powerOf(k, power)
            val termVal = coeff * kPower
            sum += termVal

            val powerStr = when (power) {
                0 -> "$coeff"
                1 -> "$coeff($k)"
                else -> "$coeff($k^$power)"
            }
            termEvaluations.add("$powerStr = $termVal")
        }

        steps.add(
            SisyateStep(
                stepNumber = 2,
                label = "Substitute x = $k into Terms",
                formulaDisplay = "P($k)",
                stepResult = termEvaluations.joinToString(", "),
                explanation = "Evaluate each polynomial term with x = $k."
            )
        )

        val isFactor = (remainder == 0L)
        val classification = if (isFactor) {
            SisyateClassification.ZERO_REMAINDER_FACTOR_CONFIRMED
        } else {
            SisyateClassification.NON_ZERO_REMAINDER
        }

        steps.add(
            SisyateStep(
                stepNumber = 3,
                label = "Sum Terms & Evaluate Remainder",
                formulaDisplay = "P($k) = $sum",
                stepResult = if (isFactor) "0 (Factor Confirmed!)" else "$remainder",
                explanation = if (isFactor) {
                    "P($k) = 0. Remainder is 0, confirming $divisorStr is an exact factor of P(x)."
                } else {
                    "P($k) = $remainder. Remainder upon dividing by $divisorStr is $remainder."
                }
            )
        )

        return SisyateSolution(
            polynomial = poly,
            k = k,
            remainder = remainder,
            quotientCoefficients = quotientCoeffs,
            steps = steps,
            classification = classification
        )
    }

    /**
     * Independent polynomial division oracle: P(x) / (x - k).
     * Computes quotient Q(x) and scalar remainder R using synthetic division.
     */
    fun polynomialLongDivision(poly: SimplePolynomial, k: Long): Pair<List<Long>, Long> {
        val coeffs = poly.coefficients
        if (coeffs.isEmpty()) return Pair(emptyList(), 0L)

        val quotient = mutableListOf<Long>()
        var carry = coeffs[0]
        quotient.add(carry)

        for (i in 1 until coeffs.size - 1) {
            carry = coeffs[i] + carry * k
            quotient.add(carry)
        }

        val remainder = if (coeffs.size > 1) {
            coeffs.last() + carry * k
        } else {
            coeffs[0]
        }

        return Pair(quotient, remainder)
    }

    private fun powerOf(base: Long, exp: Int): Long {
        var res = 1L
        for (i in 0 until exp) {
            res *= base
        }
        return res
    }

    override fun generateProblem(difficultyTier: DifficultyTier): SutraProblem {
        val (poly, k) = when (difficultyTier) {
            DifficultyTier.TIER_1_EASY -> {
                // Degree 2 polynomial, positive k in 1..5
                val kVal = random.nextLong(1, 6)
                val a = random.nextLong(1, 4)
                val b = random.nextLong(-6, 7)
                val c = random.nextLong(-8, 9)
                Pair(SimplePolynomial(listOf(a, b, c)), kVal)
            }
            DifficultyTier.TIER_2_HARD -> {
                // Degree 3 polynomial, positive k in 1..4
                val kVal = random.nextLong(1, 5)
                val a = random.nextLong(1, 3)
                val b = random.nextLong(-5, 6)
                val c = random.nextLong(-6, 7)
                val d = random.nextLong(-8, 9)
                Pair(SimplePolynomial(listOf(a, b, c, d)), kVal)
            }
        }

        return buildProblem(poly, k, difficultyTier)
    }

    fun generateTier3Problem(): SutraProblem {
        // Tier 3: negative k in -4..-1 or zero-remainder factor confirmation
        val isFactorCase = random.nextBoolean()
        val kVal = if (random.nextBoolean()) -random.nextLong(1, 5) else random.nextLong(1, 5)

        val poly = if (isFactorCase) {
            // Construct polynomial with known factor (x - kVal): P(x) = (x - kVal)(ax + b)
            val a = random.nextLong(1, 3)
            val b = random.nextLong(-4, 5)
            // (x - k)(ax + b) = ax^2 + (b - a*k)x - b*k
            val a2 = a
            val a1 = b - a * kVal
            val a0 = -b * kVal
            SimplePolynomial(listOf(a2, a1, a0))
        } else {
            val a = random.nextLong(1, 3)
            val b = random.nextLong(-4, 5)
            val c = random.nextLong(-5, 6)
            SimplePolynomial(listOf(a, b, c))
        }

        return buildProblem(poly, kVal, DifficultyTier.TIER_2_HARD)
    }

    override fun generateSpecificProblem(operand: Long): SutraProblem {
        // Canonical example: P(x) = x^3 - 3x^2 + 4x - 5, k = 2
        val poly = SimplePolynomial(listOf(1L, -3L, 4L, -5L))
        return buildProblem(poly, 2L, DifficultyTier.TIER_1_EASY)
    }

    override fun generateProblemSet(count: Int, difficultyTier: DifficultyTier): List<SutraProblem> {
        val set = mutableListOf<SutraProblem>()
        val seen = mutableSetOf<String>()
        var attempts = 0

        while (set.size < count && attempts < count * 20) {
            attempts++
            val prob = if (attempts % 3 == 0) generateTier3Problem() else generateProblem(difficultyTier)
            if (!seen.contains(prob.questionText)) {
                seen.add(prob.questionText)
                set.add(prob)
            }
        }

        while (set.size < count) {
            set.add(generateProblem(difficultyTier))
        }

        return set
    }

    private fun buildProblem(
        poly: SimplePolynomial,
        k: Long,
        tier: DifficultyTier
    ): SutraProblem {
        val solution = solve(poly, k)
        val remainder = solution.remainder
        val divisorStr = if (k >= 0) "(x - $k)" else "(x + ${-k})"

        val decompositionSteps = solution.steps.map { step ->
            DecompositionStep(
                stepNumber = step.stepNumber,
                label = step.label,
                formulaDisplay = step.formulaDisplay,
                stepResult = step.stepResult,
                explanation = step.explanation
            )
        }

        val distractors = listOf(
            -remainder,
            remainder + 2,
            remainder - 2,
            remainder + k,
            remainder - k
        ).filter { it != remainder }.distinct().take(3)

        return SutraProblem(
            id = UUID.randomUUID().toString(),
            sutraName = sutraName,
            questionText = "Remainder of (${poly.toFormattedString()}) ÷ $divisorStr?",
            operand = remainder,
            correctAnswer = remainder,
            prefixPart = k,
            incrementedPrefix = poly.degree.toLong(),
            prefixProduct = if (remainder == 0L) 1L else 0L,
            appendedSuffix = if (remainder == 0L) "FACTOR_CONFIRMED" else "REMAINDER",
            decompositionSteps = decompositionSteps,
            distractors = distractors,
            difficultyTier = tier,
            answerFormat = AnswerFormat.INTEGER,
            expectedRemainder = 0,
            expectedSecondaryAnswer = 0
        )
    }
}
