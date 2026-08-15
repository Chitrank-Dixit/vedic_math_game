package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.domain.models.DecompositionStep
import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.SutraProblem
import java.util.UUID
import kotlin.math.abs
import kotlin.random.Random

class YavadunamGenerator(
    private val random: Random = Random.Default
) : SutraProblemGenerator {

    override val sutraName: String = "Yavadunam"

    override fun generateProblem(difficultyTier: DifficultyTier): SutraProblem {
        val operand = when (difficultyTier) {
            DifficultyTier.TIER_1_EASY -> {
                // Tier 1: 2-digit numbers near base 100 (85..99)
                random.nextLong(85L, 100L)
            }
            DifficultyTier.TIER_2_HARD -> {
                // Tier 2: 3-digit numbers near base 1000 (980..999) or base 100 (101..115)
                if (random.nextBoolean()) {
                    random.nextLong(980L, 1000L)
                } else {
                    random.nextLong(101L, 116L)
                }
            }
        }
        return buildProblem(operand, difficultyTier)
    }

    override fun generateSpecificProblem(operand: Long): SutraProblem {
        val tier = if (operand in 85L..99L) DifficultyTier.TIER_1_EASY else DifficultyTier.TIER_2_HARD
        return buildProblem(operand, tier)
    }

    override fun generateProblemSet(count: Int, difficultyTier: DifficultyTier): List<SutraProblem> {
        val operands = mutableSetOf<Long>()
        val maxAttempts = count * 10
        var attempts = 0
        while (operands.size < count && attempts < maxAttempts) {
            val candidate = when (difficultyTier) {
                DifficultyTier.TIER_1_EASY -> random.nextLong(85L, 100L)
                DifficultyTier.TIER_2_HARD -> if (random.nextBoolean()) random.nextLong(980L, 1000L) else random.nextLong(101L, 116L)
            }
            operands.add(candidate)
            attempts++
        }
        return operands.map { buildProblem(it, difficultyTier) }
    }

    private fun buildProblem(operand: Long, tier: DifficultyTier): SutraProblem {
        val base = determineBase(operand)
        val isBelowBase = operand < base
        val diff = abs(base - operand)
        val numZeros = getZerosInBase(base)

        val rawLhs = if (isBelowBase) operand - diff else operand + diff
        val rawRhs = diff * diff
        val rhsDigitsMax = numZeros

        val rhsString = rawRhs.toString().padStart(rhsDigitsMax, '0')
        val carry = if (rhsString.length > rhsDigitsMax) {
            rhsString.substring(0, rhsString.length - rhsDigitsMax).toLong()
        } else {
            0L
        }
        val finalRhsString = if (rhsString.length > rhsDigitsMax) {
            rhsString.substring(rhsString.length - rhsDigitsMax)
        } else {
            rhsString
        }

        val finalLhs = rawLhs + carry
        val correctAnswerStr = "$finalLhs$finalRhsString"
        val correctAnswer = correctAnswerStr.toLong()

        val steps = listOf(
            DecompositionStep(
                stepNumber = 1,
                label = "Base & Deficiency",
                formulaDisplay = "Base = $base, ${if (isBelowBase) "Deficiency" else "Surplus"} D = $diff",
                stepResult = diff.toString(),
                explanation = "Identify base $base and compute deficiency $diff ($base - $operand)."
            ),
            DecompositionStep(
                stepNumber = 2,
                label = "LHS Calculation",
                formulaDisplay = "$operand ${if (isBelowBase) "-" else "+"} $diff",
                stepResult = rawLhs.toString(),
                explanation = "Reduce operand $operand by deficiency $diff $\\rightarrow$ $rawLhs."
            ),
            DecompositionStep(
                stepNumber = 3,
                label = "RHS Calculation",
                formulaDisplay = "$diff²",
                stepResult = finalRhsString,
                explanation = "Square the deficiency $diff² $\\rightarrow$ $finalRhsString."
            ),
            DecompositionStep(
                stepNumber = 4,
                label = "Concatenation",
                formulaDisplay = "$finalLhs || $finalRhsString",
                stepResult = correctAnswerStr,
                explanation = "Combine LHS ($finalLhs) and RHS ($finalRhsString) $\\rightarrow$ $correctAnswer."
            )
        )

        val distractors = generateDistractors(correctAnswer, operand, diff)

        return SutraProblem(
            id = UUID.randomUUID().toString(),
            sutraName = sutraName,
            questionText = "Calculate $operand² using $sutraName",
            operand = operand,
            correctAnswer = correctAnswer,
            prefixPart = diff,
            incrementedPrefix = rawLhs,
            prefixProduct = finalLhs,
            appendedSuffix = finalRhsString,
            decompositionSteps = steps,
            distractors = distractors,
            difficultyTier = tier
        )
    }

    private fun determineBase(n: Long): Long {
        return when {
            n in 8L..15L -> 10L
            n in 80L..120L -> 100L
            n in 800L..1200L -> 1000L
            else -> 100L
        }
    }

    private fun getZerosInBase(base: Long): Int {
        return when (base) {
            10L -> 1
            100L -> 2
            1000L -> 3
            else -> 2
        }
    }

    private fun generateDistractors(correctAnswer: Long, operand: Long, diff: Long): List<Long> {
        val list = mutableSetOf<Long>()
        val d1 = operand * 100 + diff
        if (d1 != correctAnswer && d1 > 0) list.add(d1)

        val wrongLhs = operand + diff
        val d2 = ("$wrongLhs${(diff * diff)}").toLongOrNull()
        if (d2 != null && d2 != correctAnswer && d2 > 0) list.add(d2)

        val d3 = (operand * operand) - 10
        if (d3 != correctAnswer && d3 > 0) list.add(d3)

        val d4 = correctAnswer + 100
        if (d4 != correctAnswer && d4 > 0) list.add(d4)

        var delta = 10L
        while (list.size < 4) {
            val candidate = correctAnswer + delta
            if (candidate != correctAnswer && candidate > 0) {
                list.add(candidate)
            }
            delta = if (delta > 0) -delta else (-delta + 10L)
        }

        return list.take(4)
    }
}
