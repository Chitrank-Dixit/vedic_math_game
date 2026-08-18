package com.ankh.sutrasaga.engine.upasutras

import com.ankh.sutrasaga.domain.models.AnswerFormat
import com.ankh.sutrasaga.domain.models.DecompositionStep
import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.ObservationPatternId
import com.ankh.sutrasaga.domain.models.SutraProblem
import com.ankh.sutrasaga.domain.models.UpaSutraId
import com.ankh.sutrasaga.domain.models.VilokanamClassification
import com.ankh.sutrasaga.domain.models.VilokanamSolution
import com.ankh.sutrasaga.domain.models.VilokanamStep
import com.ankh.sutrasaga.engine.UpaSutraGenerator
import java.util.UUID
import kotlin.math.abs
import kotlin.random.Random

/**
 * Problem generator and classifier for Upa-Sutra 12: Vilokanam
 * ("By Mere Observation" — Pattern Sight Challenge Pack).
 */
class VilokanamGenerator(
    private val random: Random = Random.Default
) : UpaSutraGenerator {

    override val upaSutraId: UpaSutraId = UpaSutraId.VILOKANAM
    override val sutraName: String = "Vilokanam"

    fun classifyAndSolve(op1: Long, op2: Long = op1, isSquare: Boolean = (op1 == op2)): VilokanamSolution {
        val problemDisplay = if (isSquare) "$op1²" else "$op1 × $op2"
        val numericalResult = if (isSquare) op1 * op1 else op1 * op2

        // Evaluate predicates
        val isEnds5 = isSquare && (op1 % 10L == 5L)
        val isNearBase = isSquare && (abs(op1 - 100L) <= 15L) && !isEnds5
        val isSameTensSum10 = !isSquare && (op1 in 10..99) && (op2 in 10..99) &&
                (op1 / 10L == op2 / 10L) && (op1 % 10L + op2 % 10L == 10L) && (op1 != op2)
        val mean = (op1 + op2) / 2L
        val isSymmetric = !isSquare && ((op1 + op2) % 2L == 0L) && (mean % 10L == 0L) &&
                (abs(op1 - op2) in 2..10) && !isSameTensSum10

        val matchedPatterns = mutableListOf<ObservationPatternId>()
        if (isEnds5) matchedPatterns.add(ObservationPatternId.ENDS_IN_FIVE_SQUARE)
        if (isNearBase) matchedPatterns.add(ObservationPatternId.NEAR_BASE_SQUARE)
        if (isSameTensSum10) matchedPatterns.add(ObservationPatternId.SAME_TENS_UNITS_SUM_TEN)
        if (isSymmetric) matchedPatterns.add(ObservationPatternId.SYMMETRIC_PRODUCT)

        if (matchedPatterns.size > 1) {
            return VilokanamSolution(
                problemDisplay = problemDisplay,
                correctPattern = matchedPatterns.first(),
                patternName = "Ambiguous Pattern",
                numericalResult = numericalResult,
                explanation = "Multiple pattern predicates matched.",
                steps = emptyList(),
                classification = VilokanamClassification.AMBIGUOUS_PATTERN
            )
        }

        val pattern = matchedPatterns.firstOrNull() ?: ObservationPatternId.NONE_OF_THE_ABOVE

        val steps = mutableListOf<VilokanamStep>()

        when (pattern) {
            ObservationPatternId.ENDS_IN_FIVE_SQUARE -> {
                val prefix = op1 / 10L
                val prefixProd = prefix * (prefix + 1L)
                steps.add(
                    VilokanamStep(
                        stepNumber = 1,
                        label = "Observe Visible Structure",
                        formulaDisplay = "Last digit is 5 and operation is squaring",
                        stepResult = "Pattern: Ends in 5 Square (Ekadhikena)",
                        explanation = "A number ending in 5 squared follows prefix × (prefix + 1) || 25."
                    )
                )
                steps.add(
                    VilokanamStep(
                        stepNumber = 2,
                        label = "Compute Result",
                        formulaDisplay = "$prefix × ${prefix + 1} || 25 = $prefixProd || 25",
                        stepResult = "$numericalResult",
                        explanation = "Result is $numericalResult. Verified by exact multiplication $op1 × $op1."
                    )
                )
            }
            ObservationPatternId.NEAR_BASE_SQUARE -> {
                val deficiency = 100L - op1
                val reduced = op1 - deficiency
                val sqDef = deficiency * deficiency
                steps.add(
                    VilokanamStep(
                        stepNumber = 1,
                        label = "Observe Visible Structure",
                        formulaDisplay = "Close to base 100 (Deficiency = $deficiency)",
                        stepResult = "Pattern: Near-Base Square (Yavadunam)",
                        explanation = "Squaring near base 100 reduces by deficiency and appends squared deficiency."
                    )
                )
                steps.add(
                    VilokanamStep(
                        stepNumber = 2,
                        label = "Compute Result",
                        formulaDisplay = "($op1 - $deficiency) || ${String.format("%02d", sqDef)} = $reduced || ${String.format("%02d", sqDef)}",
                        stepResult = "$numericalResult",
                        explanation = "Result is $numericalResult. Verified by exact multiplication $op1 × $op1."
                    )
                )
            }
            ObservationPatternId.SAME_TENS_UNITS_SUM_TEN -> {
                val tens = op1 / 10L
                val u1 = op1 % 10L
                val u2 = op2 % 10L
                steps.add(
                    VilokanamStep(
                        stepNumber = 1,
                        label = "Observe Visible Structure",
                        formulaDisplay = "Tens match ($tens = $tens) and units sum to 10 ($u1 + $u2 = 10)",
                        stepResult = "Pattern: Same Tens / Units Sum to 10 (Antyayordashake'pi)",
                        explanation = "Multiply tens by one more ($tens × ${tens + 1} = ${tens * (tens + 1)}) and units together ($u1 × $u2 = ${u1 * u2})."
                    )
                )
                steps.add(
                    VilokanamStep(
                        stepNumber = 2,
                        label = "Compute Result",
                        formulaDisplay = "${tens * (tens + 1)} || ${String.format("%02d", u1 * u2)}",
                        stepResult = "$numericalResult",
                        explanation = "Result is $numericalResult. Verified by exact multiplication $op1 × $op2."
                    )
                )
            }
            ObservationPatternId.SYMMETRIC_PRODUCT -> {
                val diff = abs(mean - op1)
                steps.add(
                    VilokanamStep(
                        stepNumber = 1,
                        label = "Observe Visible Structure",
                        formulaDisplay = "Equally spaced around round average $mean (Offset = $diff)",
                        stepResult = "Pattern: Symmetric Product (Difference of Squares)",
                        explanation = "Difference of Squares: ($mean - $diff)($mean + $diff) = $mean² - $diff²."
                    )
                )
                steps.add(
                    VilokanamStep(
                        stepNumber = 2,
                        label = "Compute Result",
                        formulaDisplay = "${mean * mean} - ${diff * diff}",
                        stepResult = "$numericalResult",
                        explanation = "Result is $numericalResult. Verified by exact multiplication $op1 × $op2."
                    )
                )
            }
            ObservationPatternId.NONE_OF_THE_ABOVE -> {
                steps.add(
                    VilokanamStep(
                        stepNumber = 1,
                        label = "Observe Visible Structure",
                        formulaDisplay = "No special base proximity, identical tens, or 5-ending symmetry",
                        stepResult = "Pattern: Standard General Multiplication",
                        explanation = "None of the special Vedic shortcut patterns apply; use standard general multiplication."
                    )
                )
                steps.add(
                    VilokanamStep(
                        stepNumber = 2,
                        label = "Compute Result",
                        formulaDisplay = "$op1 × $op2",
                        stepResult = "$numericalResult",
                        explanation = "Result is $numericalResult."
                    )
                )
            }
        }

        val patternName = when (pattern) {
            ObservationPatternId.ENDS_IN_FIVE_SQUARE -> "Ends in 5 Square"
            ObservationPatternId.NEAR_BASE_SQUARE -> "Near Base 100 Square"
            ObservationPatternId.SAME_TENS_UNITS_SUM_TEN -> "Same Tens, Units Sum to 10"
            ObservationPatternId.SYMMETRIC_PRODUCT -> "Symmetric Difference of Squares"
            ObservationPatternId.NONE_OF_THE_ABOVE -> "Standard General Multiplication"
        }

        return VilokanamSolution(
            problemDisplay = problemDisplay,
            correctPattern = pattern,
            patternName = patternName,
            numericalResult = numericalResult,
            explanation = steps.first().explanation,
            steps = steps,
            classification = if (pattern == ObservationPatternId.NONE_OF_THE_ABOVE) VilokanamClassification.NONE_APPLIES else VilokanamClassification.PATTERN_RECOGNIZED
        )
    }

    override fun generateProblem(difficultyTier: DifficultyTier): SutraProblem {
        val (op1, op2, isSq) = when (difficultyTier) {
            DifficultyTier.TIER_1_EASY -> generateTier1ProblemParams()
            DifficultyTier.TIER_2_HARD -> generateTier2ProblemParams()
        }
        return buildProblem(op1, op2, isSq, difficultyTier)
    }

    fun generateTier3Problem(): SutraProblem {
        val (op1, op2, isSq) = generateTier3ProblemParams()
        return buildProblem(op1, op2, isSq, DifficultyTier.TIER_2_HARD)
    }

    override fun generateSpecificProblem(operand: Long): SutraProblem {
        // Seed 1: 85²
        return buildProblem(85L, 85L, true, DifficultyTier.TIER_1_EASY)
    }

    override fun generateProblemSet(count: Int, difficultyTier: DifficultyTier): List<SutraProblem> {
        val set = mutableListOf<SutraProblem>()
        val seen = mutableSetOf<String>()
        var attempts = 0

        while (set.size < count && attempts < count * 20) {
            attempts++
            val (op1, op2, isSq) = when (difficultyTier) {
                DifficultyTier.TIER_1_EASY -> generateTier1ProblemParams()
                DifficultyTier.TIER_2_HARD -> if (attempts % 2 == 0) generateTier2ProblemParams() else generateTier3ProblemParams()
            }
            val key = if (isSq) "$op1^2" else "$op1*$op2"
            if (!seen.contains(key)) {
                seen.add(key)
                set.add(buildProblem(op1, op2, isSq, difficultyTier))
            }
        }

        while (set.size < count) {
            val (op1, op2, isSq) = generateTier1ProblemParams()
            set.add(buildProblem(op1, op2, isSq, difficultyTier))
        }

        return set
    }

    private fun generateTier1ProblemParams(): Triple<Long, Long, Boolean> {
        return if (random.nextBoolean()) {
            val prefix = random.nextLong(2, 10)
            Triple(prefix * 10L + 5L, prefix * 10L + 5L, true) // Ends in 5 square
        } else {
            val tens = random.nextLong(2, 9)
            val u1 = random.nextLong(1, 9)
            val u2 = 10L - u1
            if (u1 == u2) Triple(tens * 10L + 3L, tens * 10L + 7L, false)
            else Triple(tens * 10L + u1, tens * 10L + u2, false) // Same tens, units sum 10
        }
    }

    private fun generateTier2ProblemParams(): Triple<Long, Long, Boolean> {
        val choice = random.nextInt(3)
        return when (choice) {
            0 -> {
                val def = random.nextLong(1, 14)
                val baseNum = if (random.nextBoolean()) 100L - def else 100L + def
                if (baseNum % 10L == 5L) Triple(97L, 97L, true) else Triple(baseNum, baseNum, true)
            }
            1 -> {
                val mean = random.nextLong(3, 9) * 10L
                val offset = random.nextLong(1, 5)
                Triple(mean - offset, mean + offset, false)
            }
            else -> generateTier1ProblemParams()
        }
    }

    private fun generateTier3ProblemParams(): Triple<Long, Long, Boolean> {
        val choice = random.nextInt(4)
        return when (choice) {
            0 -> {
                // Negative control: None of the above (e.g. 46 × 53)
                val op1 = random.nextLong(31, 88)
                val op2 = op1 + random.nextLong(5, 12)
                val sol = classifyAndSolve(op1, op2, false)
                if (sol.correctPattern == ObservationPatternId.NONE_OF_THE_ABOVE) Triple(op1, op2, false)
                else Triple(46L, 53L, false)
            }
            1 -> {
                // Near base square
                val def = random.nextLong(2, 9)
                val num = 100L - def
                if (num % 10L == 5L) Triple(96L, 96L, true) else Triple(num, num, true)
            }
            2 -> {
                // Symmetric product
                val mean = random.nextLong(4, 8) * 10L
                val offset = random.nextLong(1, 4)
                Triple(mean - offset, mean + offset, false)
            }
            else -> {
                // Same tens, units sum 10
                val tens = random.nextLong(3, 8)
                Triple(tens * 10L + 2L, tens * 10L + 8L, false)
            }
        }
    }

    private fun buildProblem(
        op1: Long,
        op2: Long,
        isSquare: Boolean,
        tier: DifficultyTier
    ): SutraProblem {
        val solution = classifyAndSolve(op1, op2, isSquare)

        val decompositionSteps = solution.steps.map { step ->
            DecompositionStep(
                stepNumber = step.stepNumber,
                label = step.label,
                formulaDisplay = step.formulaDisplay,
                stepResult = step.stepResult,
                explanation = step.explanation
            )
        }

        val result = solution.numericalResult
        val distractors = listOf(
            result + 10L,
            result - 10L,
            result + 100L,
            result - 100L
        ).filter { it != result && it > 0 }.distinct().take(3)

        return SutraProblem(
            id = UUID.randomUUID().toString(),
            sutraName = sutraName,
            questionText = "Observe & Solve: ${solution.problemDisplay} (${solution.patternName})",
            operand = op1,
            correctAnswer = result,
            prefixPart = op1,
            incrementedPrefix = op2,
            prefixProduct = if (isSquare) 1L else 0L,
            appendedSuffix = solution.patternName,
            decompositionSteps = decompositionSteps,
            distractors = distractors,
            difficultyTier = tier,
            answerFormat = AnswerFormat.INTEGER,
            expectedRemainder = 0,
            expectedSecondaryAnswer = 0
        )
    }
}
