package com.ankh.sutrasaga.engine.upasutras

import com.ankh.sutrasaga.domain.models.AnswerFormat
import com.ankh.sutrasaga.domain.models.DecompositionStep
import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.KevalaihClassification
import com.ankh.sutrasaga.domain.models.KevalaihSolution
import com.ankh.sutrasaga.domain.models.KevalaihStep
import com.ankh.sutrasaga.domain.models.SutraProblem
import com.ankh.sutrasaga.domain.models.UpaSutraId
import com.ankh.sutrasaga.engine.ShesanyankenaCharamenaGenerator
import com.ankh.sutrasaga.engine.UpaSutraGenerator
import java.util.UUID
import kotlin.random.Random

/**
 * Problem generator and solver for Upa-Sutra 4: Kevalaih Saptakam Gunyat
 * ("For 7, the multiplicand is 143" — 7-Cyclic Repetend Engine).
 */
class KevalaihSaptakamGunyatGenerator(
    private val random: Random = Random.Default
) : UpaSutraGenerator {

    companion object {
        const val CANONICAL_BLOCK = 142857L
        const val MULTIPLIER_143 = 143L
        const val BASE_1001 = 1001L
        const val REPETEND_MOD = 999999L
    }

    override val upaSutraId: UpaSutraId = UpaSutraId.KEVALAIHSAPTAKAM_GUNYAT
    override val sutraName: String = "Kevalaih Saptakam Gunyat"

    private val referenceEngine = ShesanyankenaCharamenaGenerator()

    fun solve(numerator: Long, denominator: Long = 7L): KevalaihSolution {
        if (denominator != 7L) {
            return KevalaihSolution(
                numerator = numerator,
                denominator = denominator,
                cyclicBlock = 0L,
                cyclicBlockString = "",
                decimalString = "",
                steps = emptyList(),
                classification = KevalaihClassification.UNSUPPORTED_DENOMINATOR
            )
        }

        if (numerator !in 1L..6L) {
            return KevalaihSolution(
                numerator = numerator,
                denominator = denominator,
                cyclicBlock = 0L,
                cyclicBlockString = "",
                decimalString = "",
                steps = emptyList(),
                classification = KevalaihClassification.UNSUPPORTED_NUMERATOR
            )
        }

        val cyclicBlock = numerator * CANONICAL_BLOCK
        val blockStr = String.format("%06d", cyclicBlock)
        val decimalStr = "0.($blockStr)"

        // Reference check against World 12 remainder-cycle engine
        val referenceSolution = referenceEngine.computeDecimalExpansion(numerator, 7L)
        val refCycle = referenceSolution.expansion.repeatingDigits.joinToString("")
        if (refCycle != blockStr) {
            return KevalaihSolution(
                numerator = numerator,
                denominator = denominator,
                cyclicBlock = cyclicBlock,
                cyclicBlockString = blockStr,
                decimalString = decimalStr,
                steps = emptyList(),
                classification = KevalaihClassification.REFERENCE_MISMATCH
            )
        }

        val steps = mutableListOf<KevalaihStep>()
        steps.add(
            KevalaihStep(
                stepNumber = 1,
                label = "Foundational Identity",
                formulaDisplay = "7 × 143 = 1001",
                stepResult = "1001",
                explanation = "7 times 143 equals 1001. Multiplying by 999 yields 7 × 142857 = 999999."
            )
        )
        steps.add(
            KevalaihStep(
                stepNumber = 2,
                label = "Base Reciprocal (1/7)",
                formulaDisplay = "1/7 = 142857 / 999999",
                stepResult = "0.(142857)",
                explanation = "The canonical 6-digit recurring block for 1/7 is 142857."
            )
        )
        steps.add(
            KevalaihStep(
                stepNumber = 3,
                label = "Cyclic Multiplication for $numerator/7",
                formulaDisplay = "$numerator × 142857",
                stepResult = blockStr,
                explanation = "Multiplying 142857 by $numerator yields the 6-digit repetend $blockStr."
            )
        )
        steps.add(
            KevalaihStep(
                stepNumber = 4,
                label = "Full Repeating Decimal",
                formulaDisplay = "$numerator/7 = $blockStr / 999999",
                stepResult = decimalStr,
                explanation = "Hence, $numerator/7 = $decimalStr. Verified via remainder cycle."
            )
        )

        return KevalaihSolution(
            numerator = numerator,
            denominator = denominator,
            cyclicBlock = cyclicBlock,
            cyclicBlockString = blockStr,
            decimalString = decimalStr,
            steps = steps,
            classification = KevalaihClassification.VALID_SEVENTH_RECIPROCAL
        )
    }

    override fun generateProblem(difficultyTier: DifficultyTier): SutraProblem {
        val numerator = when (difficultyTier) {
            DifficultyTier.TIER_1_EASY -> 1L
            DifficultyTier.TIER_2_HARD -> random.nextLong(2, 4) // 2 or 3
        }
        return buildProblem(numerator, difficultyTier)
    }

    fun generateTier3Problem(): SutraProblem {
        val numerator = random.nextLong(4, 7) // 4, 5, or 6
        return buildProblem(numerator, DifficultyTier.TIER_2_HARD)
    }

    override fun generateSpecificProblem(operand: Long): SutraProblem {
        val num = if (operand in 1L..6L) operand else 1L
        return buildProblem(num, DifficultyTier.TIER_1_EASY)
    }

    override fun generateProblemSet(count: Int, difficultyTier: DifficultyTier): List<SutraProblem> {
        val set = mutableListOf<SutraProblem>()
        val seen = mutableSetOf<Long>()
        var attempts = 0

        while (set.size < count && attempts < count * 20) {
            attempts++
            val num = when (difficultyTier) {
                DifficultyTier.TIER_1_EASY -> if (attempts % 2 == 0) 1L else random.nextLong(1, 4)
                DifficultyTier.TIER_2_HARD -> random.nextLong(1, 7)
            }
            val prob = buildProblem(num, difficultyTier)
            if (!seen.contains(prob.operand)) {
                seen.add(prob.operand)
                set.add(prob)
            }
        }

        while (set.size < count) {
            set.add(buildProblem(random.nextLong(1, 7), difficultyTier))
        }

        return set
    }

    private fun buildProblem(numerator: Long, tier: DifficultyTier): SutraProblem {
        val solution = solve(numerator, 7L)
        val cyclicBlock = solution.cyclicBlock

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
            143L,
            14285L,
            cyclicBlock + 1000L,
            cyclicBlock - 1000L,
            142875L
        ).filter { it != cyclicBlock && it > 0 }.distinct().take(3)

        return SutraProblem(
            id = UUID.randomUUID().toString(),
            sutraName = sutraName,
            questionText = "Recurring 6-digit block of $numerator/7 ($numerator × 142857)?",
            operand = numerator,
            correctAnswer = cyclicBlock,
            prefixPart = numerator,
            incrementedPrefix = 7L,
            prefixProduct = CANONICAL_BLOCK,
            appendedSuffix = solution.decimalString,
            decompositionSteps = decompositionSteps,
            distractors = distractors,
            difficultyTier = tier,
            answerFormat = AnswerFormat.INTEGER,
            expectedRemainder = 0,
            expectedSecondaryAnswer = 0
        )
    }
}
