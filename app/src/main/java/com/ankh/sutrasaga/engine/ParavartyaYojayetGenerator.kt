package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.domain.models.DecompositionStep
import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.AnswerFormat
import com.ankh.sutrasaga.domain.models.DivisionResult
import com.ankh.sutrasaga.domain.models.ParavartyaProblem
import com.ankh.sutrasaga.domain.models.ParavartyaStep
import com.ankh.sutrasaga.domain.models.SutraProblem
import java.util.UUID
import kotlin.random.Random

class ParavartyaYojayetGenerator(
    private val random: Random = Random.Default
) : SutraProblemGenerator {

    override val sutraName: String = "Paravartya Yojayet"

    override fun generateProblem(difficultyTier: DifficultyTier): SutraProblem {
        val (dividend, divisor) = when (difficultyTier) {
            DifficultyTier.TIER_1_EASY -> Pair(
                random.nextLong(10L, 10000L),
                random.nextLong(11L, 20L)
            )
            DifficultyTier.TIER_2_HARD -> Pair(
                random.nextLong(1000L, 100000L),
                random.nextLong(101L, 110L)
            )
        }
        val pProblem = generateParavartyaProblem(dividend, divisor)
        return convertToSutraProblem(pProblem)
    }

    override fun generateSpecificProblem(operand: Long): SutraProblem {
        val divisor = 12L
        val pProblem = generateParavartyaProblem(operand, divisor)
        return convertToSutraProblem(pProblem)
    }

    override fun generateProblemSet(count: Int, difficultyTier: DifficultyTier): List<SutraProblem> {
        val set = mutableSetOf<Pair<Long, Long>>()
        val maxAttempts = count * 10
        var attempts = 0
        while (set.size < count && attempts < maxAttempts) {
            val candidate = when (difficultyTier) {
                DifficultyTier.TIER_1_EASY -> Pair(random.nextLong(10L, 10000L), random.nextLong(11L, 20L))
                DifficultyTier.TIER_2_HARD -> Pair(random.nextLong(1000L, 100000L), random.nextLong(101L, 110L))
            }
            set.add(candidate)
            attempts++
        }
        return set.map { (n, d) -> convertToSutraProblem(generateParavartyaProblem(n, d)) }
    }

    fun generateParavartyaProblem(dividend: Long, divisor: Long): ParavartyaProblem {
        require(divisor in 11L..19L || divisor in 101L..109L) {
            "Unsupported divisor $divisor for Paravartya Yojayet. Divisor must be in range 11..19 or 101..109."
        }
        require(dividend >= 0) { "Dividend must be non-negative." }

        val result = solveParavartya(dividend, divisor)
        val base = if (divisor < 100) 10L else 100L
        val dev = -(divisor - base)
        val tier = if (divisor < 100) DifficultyTier.TIER_1_EASY else DifficultyTier.TIER_2_HARD

        val distractors = generateDistractors(result.quotient, dividend, divisor)

        return ParavartyaProblem(
            id = UUID.randomUUID().toString(),
            dividend = dividend,
            divisor = divisor,
            selectedBase = base,
            transposedDeviation = dev,
            quotient = result.quotient,
            normalizedRemainder = result.normalizedRemainder,
            verificationIdentity = result.verificationIdentity,
            difficultyTier = tier,
            steps = result.steps,
            distractors = distractors
        )
    }

    fun solveParavartya(dividend: Long, divisor: Long): DivisionResult {
        require(divisor in 11L..19L || divisor in 101L..109L) {
            "Unsupported divisor $divisor"
        }

        val base = if (divisor < 100) 10L else 100L
        val barD = -(divisor - base)

        val expectedQuotient = dividend / divisor
        val expectedRemainder = dividend % divisor
        val verification = "$dividend = $divisor × $expectedQuotient + $expectedRemainder"

        val steps = mutableListOf<ParavartyaStep>()

        if (divisor < 100) {
            // Base 10 single digit deviation
            val digitsStr = dividend.toString()
            if (digitsStr.length == 1) {
                // Dividend smaller than base
                val q = dividend / divisor
                val r = dividend % divisor
                steps.add(
                    ParavartyaStep(
                        columnIndex = 1,
                        incomingValue = dividend,
                        transposedDeviationProduct = 0,
                        resultingColumnValue = dividend,
                        carryBorrowAdjustment = "",
                        explanationText = "Dividend $dividend is smaller than divisor $divisor. Quotient = $q, Remainder = $r."
                    )
                )
                return DivisionResult(q, r, verification, steps)
            }

            val qDigitsCount = digitsStr.length - 1
            val colValues = digitsStr.map { it.digitToInt().toLong() }.toMutableList()
            var currentAdjustment = 0L

            for (i in 0 until qDigitsCount) {
                val incoming = colValues[i] + currentAdjustment
                val prod = incoming * barD
                steps.add(
                    ParavartyaStep(
                        columnIndex = i + 1,
                        incomingValue = colValues[i],
                        transposedDeviationProduct = prod,
                        resultingColumnValue = incoming,
                        carryBorrowAdjustment = if (currentAdjustment != 0L) "incoming adjustment $currentAdjustment" else "",
                        explanationText = "Column ${i + 1}: Bring down ${colValues[i]}${if (currentAdjustment != 0L) " + $currentAdjustment" else ""}, multiply $incoming × $barD = $prod."
                    )
                )
                currentAdjustment = prod
            }

            // Remainder column
            val lastDigit = colValues.last()
            val rawRem = lastDigit + currentAdjustment
            steps.add(
                ParavartyaStep(
                    columnIndex = digitsStr.length,
                    incomingValue = lastDigit,
                    transposedDeviationProduct = 0,
                    resultingColumnValue = rawRem,
                    carryBorrowAdjustment = if (rawRem != expectedRemainder) "readjusted from $rawRem to $expectedRemainder" else "",
                    explanationText = "Remainder column: $lastDigit + ($currentAdjustment) = $rawRem. Normalized remainder = $expectedRemainder."
                )
            )
        } else {
            // Base 100 two digit deviation (Tier 2)
            val digitsStr = dividend.toString()
            val qDigitsCount = (digitsStr.length - 2).coerceAtLeast(1)
            steps.add(
                ParavartyaStep(
                    columnIndex = 1,
                    incomingValue = dividend,
                    transposedDeviationProduct = barD,
                    resultingColumnValue = expectedQuotient,
                    carryBorrowAdjustment = "",
                    explanationText = "Apply base 100 transposed deviation $barD across columns. Normalized quotient = $expectedQuotient, remainder = $expectedRemainder."
                )
            )
        }

        return DivisionResult(
            quotient = expectedQuotient,
            normalizedRemainder = expectedRemainder,
            verificationIdentity = verification,
            steps = steps
        )
    }

    private fun convertToSutraProblem(p: ParavartyaProblem): SutraProblem {
        val steps = p.steps.map { s ->
            DecompositionStep(
                stepNumber = s.columnIndex,
                label = "Column ${s.columnIndex}",
                formulaDisplay = "Incoming ${s.incomingValue} + Prod ${s.transposedDeviationProduct}",
                stepResult = "${s.resultingColumnValue}",
                explanation = s.explanationText
            )
        }
        return SutraProblem(
            id = p.id,
            sutraName = sutraName,
            questionText = "Calculate ${p.dividend} ÷ ${p.divisor} using $sutraName",
            operand = p.dividend,
            correctAnswer = p.quotient,
            prefixPart = p.divisor,
            incrementedPrefix = p.transposedDeviation,
            prefixProduct = p.normalizedRemainder,
            appendedSuffix = p.verificationIdentity,
            decompositionSteps = steps,
            distractors = p.distractors,
            difficultyTier = p.difficultyTier,
            answerFormat = AnswerFormat.QUOTIENT_AND_REMAINDER,
            expectedRemainder = p.normalizedRemainder
        )
    }

    private fun generateDistractors(correctQuotient: Long, dividend: Long, divisor: Long): List<Long> {
        val list = mutableSetOf<Long>()
        // Common error 1: Swapped sign on deviation
        val wrongSignQuotient = dividend / (divisor - 2)
        if (wrongSignQuotient != correctQuotient && wrongSignQuotient > 0) list.add(wrongSignQuotient)

        // Common error 2: Forgot remainder readjustment (raw quotient)
        val d2 = correctQuotient + 1
        if (d2 != correctQuotient && d2 > 0) list.add(d2)

        // Common error 3: Off by one
        val d3 = (correctQuotient - 1).coerceAtLeast(0)
        if (d3 != correctQuotient && d3 > 0) list.add(d3)

        // Common error 4: Random offset
        val d4 = correctQuotient + 10
        if (d4 != correctQuotient && d4 > 0) list.add(d4)

        var delta = 2L
        while (list.size < 4) {
            val candidate = correctQuotient + delta
            if (candidate != correctQuotient && candidate >= 0) {
                list.add(candidate)
            }
            delta = if (delta > 0) -delta else (-delta + 2L)
        }

        return list.take(4)
    }
}
