package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.domain.models.DecompositionStep
import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.SutraProblem
import java.util.UUID
import kotlin.math.pow
import kotlin.random.Random

/**
 * NikhilamGenerator — "All from 9 and the Last from 10" (निखिलं नवतश्चरमं दशतः)
 *
 * Mathematical Shortcut Rule:
 * For subtracting any number from a base power of 10 (e.g. 1000 - 364):
 * - Subtract all leading digits from 9: (9 - 3 = 6), (9 - 6 = 3)
 * - Subtract the final non-zero digit from 10: (10 - 4 = 6)
 * - Result: 636
 */
class NikhilamGenerator(
    private val random: Random = Random.Default
) : SutraProblemGenerator {

    override val sutraName: String = "Nikhilam Navatashcaramam Dashatah"

    override fun generateProblem(difficultyTier: DifficultyTier): SutraProblem {
        val (base, subtrahend) = generateBaseAndSubtrahend(difficultyTier)
        return buildProblem(base, subtrahend, difficultyTier)
    }

    override fun generateSpecificProblem(operand: Long): SutraProblem {
        // Here operand represents subtrahend, and base is the next power of 10
        val base = calculateNextPowerOf10(operand)
        val tier = if (base <= 1000L) DifficultyTier.TIER_1_EASY else DifficultyTier.TIER_2_HARD
        return buildProblem(base, operand, tier)
    }

    override fun generateProblemSet(count: Int, difficultyTier: DifficultyTier): List<SutraProblem> {
        val problemPairs = mutableSetOf<Pair<Long, Long>>()
        val maxAttempts = count * 10
        var attempts = 0
        while (problemPairs.size < count && attempts < maxAttempts) {
            problemPairs.add(generateBaseAndSubtrahend(difficultyTier))
            attempts++
        }
        return problemPairs.map { (base, subtrahend) -> buildProblem(base, subtrahend, difficultyTier) }
    }

    private fun generateBaseAndSubtrahend(difficultyTier: DifficultyTier): Pair<Long, Long> {
        return when (difficultyTier) {
            DifficultyTier.TIER_1_EASY -> {
                val base = if (random.nextBoolean()) 100L else 1000L
                val digits = if (base == 100L) 2 else 3
                val minVal = 10.0.pow(digits - 1).toLong()
                val maxVal = base - 1
                val subtrahend = random.nextLong(minVal, maxVal + 1)
                Pair(base, subtrahend)
            }
            DifficultyTier.TIER_2_HARD -> {
                val base = 10000L
                val minVal = 1000L
                val maxVal = 9999L
                val subtrahend = random.nextLong(minVal, maxVal + 1)
                Pair(base, subtrahend)
            }
        }
    }

    private fun calculateNextPowerOf10(number: Long): Long {
        var base = 10L
        while (base <= number) {
            base *= 10
        }
        return base
    }

    private fun buildProblem(base: Long, subtrahend: Long, tier: DifficultyTier): SutraProblem {
        val correctAnswer = base - subtrahend
        val subtrahendStr = subtrahend.toString()
        val numDigits = subtrahendStr.length

        // Decomposition steps according to Nikhilam rule:
        // Subtract all digits except last from 9, last from 10
        val initialDigits = subtrahendStr.substring(0, numDigits - 1)
        val lastDigit = subtrahendStr.last().digitToInt()

        val initialComplements = initialDigits.map { 9 - it.digitToInt() }.joinToString("")
        val lastComplement = 10 - lastDigit
        val requiresCarryNormalization = lastDigit == 0
        val combinedResultDisplay = correctAnswer.toString().padStart(numDigits, '0')
        val combineFormula = if (requiresCarryNormalization) {
            "$initialComplements || $lastComplement (normalize carry)"
        } else {
            "$initialComplements || $lastComplement"
        }
        val combineExplanation = if (requiresCarryNormalization) {
            "The final complement is 10, so carry 1 into the initial complement and keep 0 in the units place: $combinedResultDisplay."
        } else {
            "Combine initial complement digits ($initialComplements) and last digit ($lastComplement) $\\rightarrow$ $correctAnswer."
        }

        val steps = listOf(
            DecompositionStep(
                stepNumber = 1,
                label = "Identify Base & Subtrahend",
                formulaDisplay = "$base - $subtrahend",
                stepResult = "Base = $base",
                explanation = "Identify the nearest power of 10 base ($base) for $subtrahend."
            ),
            DecompositionStep(
                stepNumber = 2,
                label = "All From 9 (Initial Digits)",
                formulaDisplay = "9 - [${initialDigits.toCharArray().joinToString()}]",
                stepResult = initialComplements,
                explanation = "Subtract all initial digits ($initialDigits) from 9: yields $initialComplements."
            ),
            DecompositionStep(
                stepNumber = 3,
                label = "Last From 10 (Final Digit)",
                formulaDisplay = "10 - $lastDigit",
                stepResult = "$lastComplement",
                explanation = "Subtract the final unit digit ($lastDigit) from 10: yields $lastComplement."
            ),
            DecompositionStep(
                stepNumber = 4,
                label = "Combine Digits",
                formulaDisplay = combineFormula,
                stepResult = combinedResultDisplay,
                explanation = combineExplanation
            )
        )

        val distractors = generateDistractors(base, subtrahend, correctAnswer)

        return SutraProblem(
            id = UUID.randomUUID().toString(),
            sutraName = sutraName,
            questionText = "Calculate $base - $subtrahend using $sutraName",
            operand = subtrahend,
            correctAnswer = correctAnswer,
            prefixPart = base,
            incrementedPrefix = 0L,
            prefixProduct = 0L,
            appendedSuffix = "",
            decompositionSteps = steps,
            distractors = distractors,
            difficultyTier = tier
        )
    }

    private fun generateDistractors(base: Long, subtrahend: Long, correctAnswer: Long): List<Long> {
        val candidates = mutableListOf<Long>()

        // Distractor 1: Forgot "last from 10" (subtracted all from 9)
        val subtrahendStr = subtrahend.toString()
        val allFrom9Str = subtrahendStr.map { 9 - it.digitToInt() }.joinToString("")
        val d1 = allFrom9Str.toLongOrNull() ?: (correctAnswer - 1)
        candidates.add(d1)

        // Distractor 2: Subtracted all from 10
        val allFrom10Str = subtrahendStr.map { 10 - it.digitToInt() }.joinToString("")
        val d2 = allFrom10Str.toLongOrNull() ?: (correctAnswer + 10)
        candidates.add(d2)

        // Distractor 3: Off by one error (+1)
        candidates.add(correctAnswer + 1)

        // Distractor 4: Off by 10 error (-10)
        candidates.add((correctAnswer - 10).coerceAtLeast(1))

        return candidates
            .filter { it != correctAnswer && it > 0 }
            .distinct()
            .take(4)
    }
}
