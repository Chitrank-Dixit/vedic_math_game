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
 * Mathematical problem generator for Upa-Sutra 9: Antyayoreva ("Only the End Terms / Constant Terms").
 *
 * Principle:
 * In rational algebraic equations of the form (x + a)/(x + b) = (x + c)/(x + d),
 * if the ratio of constant terms is equal (a/b = c/d, i.e., a*d = b*c),
 * then x = 0 is immediately the unique root!
 *
 * Example:
 * (x + 2)/(x + 3) = (x + 4)/(x + 6)
 * Check: 2 / 3 == 4 / 6 ⟹ Ratio matches ⟹ Root x = 0.
 */
class AntyayerevaGenerator : UpaSutraGenerator {

    override val upaSutraId: UpaSutraId = UpaSutraId.ANTYAYEREVA
    override val sutraName: String = "Antyayoreva"

    override fun generateProblem(difficultyTier: DifficultyTier): SutraProblem {
        val random = Random.Default
        val (a, b, multiplier) = when (difficultyTier) {
            DifficultyTier.TIER_1_EASY -> {
                val aVal = random.nextLong(1, 6)
                val bVal = random.nextLong(1, 6)
                val mVal = random.nextLong(2, 4)
                Triple(aVal, bVal, mVal)
            }
            DifficultyTier.TIER_2_HARD -> {
                val aVal = random.nextLong(3, 12)
                val bVal = random.nextLong(3, 12)
                val mVal = random.nextLong(3, 6)
                Triple(aVal, bVal, mVal)
            }
        }

        val c = a * multiplier
        val d = b * multiplier
        return buildProblem(a, b, c, d, difficultyTier)
    }

    override fun generateSpecificProblem(operand: Long): SutraProblem {
        val a = if (operand > 0) operand else 2L
        val b = a + 1
        val multiplier = 2L
        val c = a * multiplier
        val d = b * multiplier
        val tier = if (a < 6) DifficultyTier.TIER_1_EASY else DifficultyTier.TIER_2_HARD
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
        val question = "Solve for x: (x + $a)/(x + $b) = (x + $c)/(x + $d)"
        val steps = listOf(
            DecompositionStep(
                stepNumber = 1,
                label = "Inspect Constant Terms",
                formulaDisplay = "LHS ratio = $a / $b, RHS ratio = $c / $d",
                stepResult = "$a/$b = $c/$d",
                explanation = "Observe the independent constant terms in the numerators and denominators."
            ),
            DecompositionStep(
                stepNumber = 2,
                label = "Cross-Product Equality",
                formulaDisplay = "$a × $d = ${a * d}, $b × $c = ${b * c}",
                stepResult = "${a * d} = ${b * c}",
                explanation = "Because $a × $d = $b × $c, the end terms are in exact proportion."
            ),
            DecompositionStep(
                stepNumber = 3,
                label = "Apply Antyayoreva",
                formulaDisplay = "Ratio equality ⟹ x = 0",
                stepResult = "x = 0",
                explanation = "By Antyayoreva ('Only the End Terms'), when constant ratios balance, x must equal 0."
            )
        )

        return SutraProblem(
            id = UUID.randomUUID().toString(),
            sutraName = sutraName,
            questionText = question,
            operand = a,
            correctAnswer = 0L,
            prefixPart = a,
            incrementedPrefix = b,
            prefixProduct = a * d,
            appendedSuffix = "0",
            decompositionSteps = steps,
            distractors = listOf(1L, -1L, 2L),
            difficultyTier = tier,
            answerFormat = AnswerFormat.INTEGER
        )
    }
}
