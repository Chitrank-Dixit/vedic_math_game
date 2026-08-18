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
 * Mathematical problem generator for Upa-Sutra 8: Antyayordashake'pi
 * ("Last digits sum to 10 and previous digits are identical").
 *
 * Example: 43 × 47
 * Condition: Tens equal (4 = 4), Units sum to 10 (3 + 7 = 10).
 * Rule: LHS = 4 × (4 + 1) = 20, RHS = 3 × 7 = 21 ⟹ 2021.
 */
class AntyayordashakepiGenerator : UpaSutraGenerator {

    override val upaSutraId: UpaSutraId = UpaSutraId.ANTYAYORDASHAKEPI
    override val sutraName: String = "Antyayordashake'pi"

    override fun generateProblem(difficultyTier: DifficultyTier): SutraProblem {
        val random = Random.Default
        val (tens, u1, u2) = when (difficultyTier) {
            DifficultyTier.TIER_1_EASY -> {
                // Single-digit tens (e.g. 21 to 99)
                val t = random.nextLong(2, 10)
                val u = random.nextLong(1, 10)
                Triple(t, u, 10 - u)
            }
            DifficultyTier.TIER_2_HARD -> {
                // Two-digit tens prefix (e.g. 101 to 199) or larger numbers
                val t = random.nextLong(10, 25)
                val u = random.nextLong(1, 10)
                Triple(t, u, 10 - u)
            }
        }

        val num1 = tens * 10 + u1
        val num2 = tens * 10 + u2
        return buildProblemForOperands(tens, u1, u2, num1, num2, difficultyTier)
    }

    override fun generateSpecificProblem(operand: Long): SutraProblem {
        val tens = operand / 10
        val u1 = operand % 10
        val u2 = if (u1 in 1..9) 10 - u1 else 5
        val num1 = tens * 10 + u1
        val num2 = tens * 10 + u2
        val tier = if (tens < 10) DifficultyTier.TIER_1_EASY else DifficultyTier.TIER_2_HARD
        return buildProblemForOperands(tens, u1, u2, num1, num2, tier)
    }

    override fun generateProblemSet(count: Int, difficultyTier: DifficultyTier): List<SutraProblem> {
        val set = mutableListOf<SutraProblem>()
        val seen = mutableSetOf<Pair<Long, Long>>()
        var attempts = 0

        while (set.size < count && attempts < count * 20) {
            attempts++
            val prob = generateProblem(difficultyTier)
            val pair = Pair(prob.operand, prob.prefixProduct)
            if (!seen.contains(pair)) {
                seen.add(pair)
                set.add(prob)
            }
        }

        while (set.size < count) {
            set.add(generateProblem(difficultyTier))
        }

        return set
    }

    private fun buildProblemForOperands(
        tens: Long,
        u1: Long,
        u2: Long,
        num1: Long,
        num2: Long,
        tier: DifficultyTier
    ): SutraProblem {
        val lhs = tens * (tens + 1)
        val rhs = u1 * u2
        val rhsFormatted = if (rhs < 10) "0$rhs" else "$rhs"
        val answer = num1 * num2

        val steps = listOf(
            DecompositionStep(
                stepNumber = 1,
                label = "Check Antyayo Condition",
                formulaDisplay = "Tens: $tens == $tens, Units: $u1 + $u2 = 10",
                stepResult = "Condition Verified",
                explanation = "Tens prefixes are identical ($tens) and unit digits add up to 10 ($u1 + $u2 = 10)."
            ),
            DecompositionStep(
                stepNumber = 2,
                label = "Calculate Left Part (Ekadhika)",
                formulaDisplay = "$tens × ($tens + 1)",
                stepResult = "$lhs",
                explanation = "Multiply tens digit by one more than itself: $tens × ($tens + 1) = $lhs."
            ),
            DecompositionStep(
                stepNumber = 3,
                label = "Calculate Right Part",
                formulaDisplay = "$u1 × $u2",
                stepResult = rhsFormatted,
                explanation = "Multiply unit digits: $u1 × $u2 = $rhsFormatted (formatted to 2 digits)."
            ),
            DecompositionStep(
                stepNumber = 4,
                label = "Combine Parts",
                formulaDisplay = "$lhs || $rhsFormatted",
                stepResult = "$answer",
                explanation = "Combine LHS and RHS: $lhs || $rhsFormatted = $answer."
            )
        )

        val distractors = listOf(
            answer + 10,
            answer - 10,
            (tens * tens) * 100 + rhs
        ).filter { it != answer && it > 0 }.distinct()

        return SutraProblem(
            id = UUID.randomUUID().toString(),
            sutraName = sutraName,
            questionText = "Calculate $num1 × $num2",
            operand = num1,
            correctAnswer = answer,
            prefixPart = tens,
            incrementedPrefix = tens + 1,
            prefixProduct = lhs,
            appendedSuffix = rhsFormatted,
            decompositionSteps = steps,
            distractors = distractors,
            difficultyTier = tier,
            answerFormat = AnswerFormat.INTEGER,
            expectedRemainder = 0,
            expectedSecondaryAnswer = 0
        )
    }
}
