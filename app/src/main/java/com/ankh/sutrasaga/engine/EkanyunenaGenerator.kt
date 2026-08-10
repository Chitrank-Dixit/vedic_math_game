package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.domain.models.DecompositionStep
import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.SutraProblem
import java.util.UUID
import kotlin.math.pow
import kotlin.random.Random

class EkanyunenaGenerator(
    private val random: Random = Random.Default
) : SutraProblemGenerator {

    override val sutraName: String = "Ekanyunena Purvena"

    override fun generateProblem(difficultyTier: DifficultyTier): SutraProblem {
        val (multiplicand, ninesMultiplier) = generateOperands(difficultyTier)
        return buildProblem(multiplicand, ninesMultiplier, difficultyTier)
    }

    override fun generateSpecificProblem(operand: Long): SutraProblem {
        val digits = operand.toString().length
        val ninesMultiplier = (10.0.pow(digits) - 1).toLong()
        val tier = if (digits <= 2) DifficultyTier.TIER_1_EASY else DifficultyTier.TIER_2_HARD
        return buildProblem(operand, ninesMultiplier, tier)
    }

    override fun generateProblemSet(count: Int, difficultyTier: DifficultyTier): List<SutraProblem> {
        val problemPairs = mutableSetOf<Pair<Long, Long>>()
        val maxAttempts = count * 10
        var attempts = 0
        while (problemPairs.size < count && attempts < maxAttempts) {
            problemPairs.add(generateOperands(difficultyTier))
            attempts++
        }
        return problemPairs.map { (multiplicand, nines) -> buildProblem(multiplicand, nines, difficultyTier) }
    }

    private fun generateOperands(difficultyTier: DifficultyTier): Pair<Long, Long> {
        return when (difficultyTier) {
            DifficultyTier.TIER_1_EASY -> {
                // 2-digit number * 99
                val multiplicand = random.nextLong(11, 100) // 11..99
                Pair(multiplicand, 99L)
            }
            DifficultyTier.TIER_2_HARD -> {
                // 3-digit number * 999
                val multiplicand = random.nextLong(101, 1000) // 101..999
                Pair(multiplicand, 999L)
            }
        }
    }

    private fun buildProblem(multiplicand: Long, ninesMultiplier: Long, tier: DifficultyTier): SutraProblem {
        val correctAnswer = multiplicand * ninesMultiplier
        val lhs = multiplicand - 1

        // RHS is the 9's complement of LHS (apply Nikhilam to LHS)
        val lhsStr = lhs.toString()
        val rhsStr = lhsStr.map { 9 - it.digitToInt() }.joinToString("")
        val rhs = rhsStr.toLong()

        val steps = listOf(
            DecompositionStep(
                stepNumber = 1,
                label = "Ekanyunena (One Less Than)",
                formulaDisplay = "$multiplicand - 1",
                stepResult = "$lhs",
                explanation = "Subtract 1 from the multiplicand ($multiplicand - 1 = $lhs). This forms the Left-Hand Side (LHS)."
            ),
            DecompositionStep(
                stepNumber = 2,
                label = "Nikhilam Complement of LHS",
                formulaDisplay = "9 - [${lhsStr.toCharArray().joinToString()}]",
                stepResult = rhsStr,
                explanation = "Subtract each digit of LHS ($lhs) from 9 to get the Right-Hand Side (RHS): yields $rhsStr."
            ),
            DecompositionStep(
                stepNumber = 3,
                label = "Concatenate LHS & RHS",
                formulaDisplay = "$lhs || $rhsStr",
                stepResult = "$correctAnswer",
                explanation = "Combine LHS ($lhs) and RHS ($rhsStr) $\\rightarrow$ $correctAnswer."
            )
        )

        val distractors = generateDistractors(multiplicand, ninesMultiplier, lhs, rhsStr, correctAnswer)

        return SutraProblem(
            id = UUID.randomUUID().toString(),
            sutraName = sutraName,
            questionText = "Calculate $multiplicand × $ninesMultiplier using $sutraName",
            operand = multiplicand,
            correctAnswer = correctAnswer,
            prefixPart = lhs,
            incrementedPrefix = 0L,
            prefixProduct = 0L,
            appendedSuffix = rhsStr,
            decompositionSteps = steps,
            distractors = distractors,
            difficultyTier = tier
        )
    }

    private fun generateDistractors(
        multiplicand: Long,
        ninesMultiplier: Long,
        lhs: Long,
        rhsStr: String,
        correctAnswer: Long
    ): List<Long> {
        val candidates = mutableListOf<Long>()

        // Distractor 1: Forgot "-1" (used multiplicand directly on LHS)
        val d1LhsStr = multiplicand.toString()
        val d1RhsStr = d1LhsStr.map { 9 - it.digitToInt() }.joinToString("")
        val d1 = "$d1LhsStr$d1RhsStr".toLongOrNull() ?: (correctAnswer + 100)
        candidates.add(d1)

        // Distractor 2: Subtracted 1 from RHS as well
        val rhsVal = rhsStr.toLongOrNull() ?: 0L
        val d2RhsStr = (rhsVal - 1).coerceAtLeast(0).toString().padStart(rhsStr.length, '0')
        val d2 = "$lhs$d2RhsStr".toLongOrNull() ?: (correctAnswer - 1)
        candidates.add(d2)

        // Distractor 3: Off by one (+1)
        candidates.add(correctAnswer + 1)

        // Distractor 4: Off by 10 (+10)
        candidates.add(correctAnswer + 10)

        return candidates
            .filter { it != correctAnswer && it > 0 }
            .distinct()
            .take(4)
    }
}
