package com.ankh.sutrasaga.engine.upasutras

import com.ankh.sutrasaga.domain.models.AnswerFormat
import com.ankh.sutrasaga.domain.models.DecompositionStep
import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.SutraProblem
import com.ankh.sutrasaga.domain.models.UpaSutraId
import com.ankh.sutrasaga.engine.UpaSutraGenerator
import java.util.UUID
import kotlin.random.Random

/**
 * Mathematical problem generator for Upa-Sutra 10: Samuccayaguṇitaḥ
 * ("The Sum of the Products / Product of the Coefficient Sums").
 *
 * Principle:
 * Verifies polynomial identities, expansions, and factorizations by evaluating
 * the Sum of Coefficients (Sc) at x = 1 (or variables = 1).
 *
 * Formula:
 * For (ax + b)(cx + d) = (ac)x² + (ad + bc)x + (bd)
 * Sc(LHS) = (a + b) × (c + d)
 * Sc(RHS) = (ac) + (ad + bc) + (bd)
 *
 * Example:
 * (2x + 3)(x + 4) = 2x² + 11x + 12
 * Sc = (2 + 3) × (1 + 4) = 5 × 5 = 25
 * Sum of RHS coefficients = 2 + 11 + 12 = 25 (Verified!)
 */
class SamuccayagunitahGenerator : UpaSutraGenerator {

    override val upaSutraId: UpaSutraId = UpaSutraId.SAMUCCAYAGUNITAH
    override val sutraName: String = "Samuccayagunitah"

    override fun generateProblem(difficultyTier: DifficultyTier): SutraProblem {
        val random = Random.Default
        val (a, b, c, d) = when (difficultyTier) {
            DifficultyTier.TIER_1_EASY -> {
                val aVal = random.nextLong(1, 4)
                val bVal = random.nextLong(1, 6)
                val cVal = 1L
                val dVal = random.nextLong(1, 6)
                listOf(aVal, bVal, cVal, dVal)
            }
            DifficultyTier.TIER_2_HARD -> {
                val aVal = random.nextLong(2, 6)
                val bVal = random.nextLong(2, 8)
                val cVal = random.nextLong(2, 5)
                val dVal = random.nextLong(2, 8)
                listOf(aVal, bVal, cVal, dVal)
            }
        }

        return buildProblem(a, b, c, d, difficultyTier)
    }

    override fun generateSpecificProblem(operand: Long): SutraProblem {
        val a = if (operand > 0) operand else 2L
        val b = 3L
        val c = 1L
        val d = 4L
        val tier = if (a < 4) DifficultyTier.TIER_1_EASY else DifficultyTier.TIER_2_HARD
        return buildProblem(a, b, c, d, tier)
    }

    override fun generateProblemSet(count: Int, difficultyTier: DifficultyTier): List<SutraProblem> {
        val set = mutableListOf<SutraProblem>()
        val seen = mutableSetOf<Pair<Long, Long>>()
        var attempts = 0

        while (set.size < count && attempts < count * 20) {
            attempts++
            val p = generateProblem(difficultyTier)
            val key = Pair(p.prefixPart, p.incrementedPrefix)
            if (seen.add(key)) {
                set.add(p)
            }
        }

        while (set.size < count) {
            set.add(generateProblem(difficultyTier))
        }

        return set
    }

    private fun buildProblem(
        a: Long,
        b: Long,
        c: Long,
        d: Long,
        tier: DifficultyTier
    ): SutraProblem {
        val sum1 = a + b
        val sum2 = c + d
        val totalSc = sum1 * sum2

        val polyA = a * c
        val polyB = (a * d) + (b * c)
        val polyC = b * d

        val aStr = if (a == 1L) "" else "$a"
        val cStr = if (c == 1L) "" else "$c"
        val question = "Find the Sum of Coefficients (Sc) for (${aStr}x + $b)(${cStr}x + $d)"

        val steps = listOf(
            DecompositionStep(
                stepNumber = 1,
                label = "Evaluate Factor 1 Sum",
                formulaDisplay = "Sc(Factor 1) = $a + $b = $sum1",
                stepResult = "$sum1",
                explanation = "Substitute x = 1 into (${aStr}x + $b) to get the first sum $sum1."
            ),
            DecompositionStep(
                stepNumber = 2,
                label = "Evaluate Factor 2 Sum",
                formulaDisplay = "Sc(Factor 2) = $c + $d = $sum2",
                stepResult = "$sum2",
                explanation = "Substitute x = 1 into (${cStr}x + $d) to get the second sum $sum2."
            ),
            DecompositionStep(
                stepNumber = 3,
                label = "Multiply Factor Sums",
                formulaDisplay = "Sc = $sum1 × $sum2 = $totalSc",
                stepResult = "$totalSc",
                explanation = "Multiply the sums: $sum1 × $sum2 = $totalSc. Note that RHS: $polyA + $polyB + $polyC = $totalSc (Verified!)."
            )
        )

        return SutraProblem(
            id = UUID.randomUUID().toString(),
            sutraName = sutraName,
            questionText = question,
            operand = a,
            correctAnswer = totalSc,
            prefixPart = sum1,
            incrementedPrefix = sum2,
            prefixProduct = totalSc,
            appendedSuffix = "$totalSc",
            decompositionSteps = steps,
            distractors = listOf(totalSc - 2, totalSc + 3, totalSc + 5),
            difficultyTier = tier,
            answerFormat = AnswerFormat.INTEGER
        )
    }
}
