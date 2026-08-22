package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.domain.models.DecompositionStep
import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.SutraProblem
import java.util.UUID
import kotlin.random.Random

/**
 * EkadhikenaPurvenaGenerator — "By One More than the Previous One" (एकाधिकेन पूर्वेण)
 *
 * Mathematical Shortcut Rule:
 * For squaring any integer ending in 5: (10a + 5)² = 100 · a(a + 1) + 25
 * - Left Part: Multiply the prefix `a` by its consecutive integer `(a + 1)`
 * - Right Part: Append constant `25` (5² = 25)
 *
 * Examples:
 * - 65² → (6 × 7) | 25 = 4225
 * - 115² → (11 × 12) | 25 = 13225
 */
class EkadhikenaPurvenaGenerator(
    private val random: Random = Random.Default
) : SutraProblemGenerator {

    override val sutraName: String = "Ekadhikena Purvena"

    override fun generateProblem(difficultyTier: DifficultyTier): SutraProblem {
        val operand = generateOperand(difficultyTier)
        return buildProblem(operand, difficultyTier)
    }

    override fun generateSpecificProblem(operand: Long): SutraProblem {
        require(operand % 10 == 5L && operand > 0) { "Operand must be a positive number ending in 5" }
        val tier = if (operand < 100) DifficultyTier.TIER_1_EASY else DifficultyTier.TIER_2_HARD
        return buildProblem(operand, tier)
    }

    override fun generateProblemSet(count: Int, difficultyTier: DifficultyTier): List<SutraProblem> {
        val operands = mutableSetOf<Long>()
        val maxAttempts = count * 10
        var attempts = 0
        while (operands.size < count && attempts < maxAttempts) {
            operands.add(generateOperand(difficultyTier))
            attempts++
        }
        return operands.map { buildProblem(it, difficultyTier) }
    }

    private fun generateOperand(difficultyTier: DifficultyTier): Long {
        return when (difficultyTier) {
            DifficultyTier.TIER_1_EASY -> {
                // 2-digit numbers ending in 5: 15, 25, 35, 45, 55, 65, 75, 85, 95
                val prefix = random.nextInt(1, 10) // 1..9
                (prefix * 10 + 5).toLong()
            }
            DifficultyTier.TIER_2_HARD -> {
                // 3-digit numbers ending in 5: 105, 115, ..., 995
                val prefix = random.nextInt(10, 100) // 10..99
                (prefix * 10 + 5).toLong()
            }
        }
    }

    private fun buildProblem(operand: Long, tier: DifficultyTier): SutraProblem {
        val prefixPart = operand / 10
        val incrementedPrefix = prefixPart + 1
        val prefixProduct = prefixPart * incrementedPrefix
        val correctAnswer = prefixProduct * 100 + 25

        val steps = listOf(
            DecompositionStep(
                stepNumber = 1,
                label = "Extract Tens/Prefix Part",
                formulaDisplay = "Prefix n of $operand",
                stepResult = "$prefixPart",
                explanation = "Remove the units digit (5) to extract the remaining prefix part n = $prefixPart."
            ),
            DecompositionStep(
                stepNumber = 2,
                label = "Ekadhikena (One More Than)",
                formulaDisplay = "$prefixPart + 1",
                stepResult = "$incrementedPrefix",
                explanation = "Apply Ekadhikena Purvena rule: Add 1 to the prefix part ($prefixPart + 1 = $incrementedPrefix)."
            ),
            DecompositionStep(
                stepNumber = 3,
                label = "Multiply Prefixes",
                formulaDisplay = "$prefixPart × $incrementedPrefix",
                stepResult = "$prefixProduct",
                explanation = "Multiply the original prefix by one more than itself ($prefixPart × $incrementedPrefix = $prefixProduct)."
            ),
            DecompositionStep(
                stepNumber = 4,
                label = "Append 25 (Units Square)",
                formulaDisplay = "$prefixProduct || 25",
                stepResult = "$correctAnswer",
                explanation = "Append 25 (the square of 5) to the prefix product: $prefixProduct || 25 = $correctAnswer."
            )
        )

        val distractors = generateDistractors(prefixPart, incrementedPrefix, prefixProduct, correctAnswer)

        return SutraProblem(
            id = UUID.randomUUID().toString(),
            sutraName = sutraName,
            questionText = "Calculate $operand² using $sutraName",
            operand = operand,
            correctAnswer = correctAnswer,
            prefixPart = prefixPart,
            incrementedPrefix = incrementedPrefix,
            prefixProduct = prefixProduct,
            appendedSuffix = "25",
            decompositionSteps = steps,
            distractors = distractors,
            difficultyTier = tier
        )
    }

    private fun generateDistractors(
        prefix: Long,
        incrementedPrefix: Long,
        prefixProduct: Long,
        correctAnswer: Long
    ): List<Long> {
        val candidates = mutableListOf<Long>()

        // Distractor 1: Forgot "+1" increment (multiplied prefix by itself)
        val d1 = (prefix * prefix) * 100 + 25
        candidates.add(d1)

        // Distractor 2: Off-by-one increment (multiplied prefix by prefix + 2)
        val d2 = (prefix * (prefix + 2)) * 100 + 25
        candidates.add(d2)

        // Distractor 3: Suffix typo (appended 15 instead of 25)
        val d3 = prefixProduct * 100 + 15
        candidates.add(d3)

        // Distractor 4: Forgot to append 25 (just prefix product)
        val d4 = prefixProduct
        candidates.add(d4)

        // Distractor 5: Added prefix and incremented prefix instead of multiplying
        val d5 = (prefix + incrementedPrefix) * 100 + 25
        candidates.add(d5)

        return candidates
            .filter { it != correctAnswer && it > 0 }
            .distinct()
            .take(4)
    }
}
