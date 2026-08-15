package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.domain.models.AnswerFormat
import com.ankh.sutrasaga.domain.models.DecimalClassification
import com.ankh.sutrasaga.domain.models.DecimalExpansion
import com.ankh.sutrasaga.domain.models.DecompositionStep
import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.Fraction
import com.ankh.sutrasaga.domain.models.RemainderStep
import com.ankh.sutrasaga.domain.models.ShesanyankenaProblem
import com.ankh.sutrasaga.domain.models.ShesanyankenaSolution
import com.ankh.sutrasaga.domain.models.ShesanyankenaStep
import com.ankh.sutrasaga.domain.models.SutraProblem
import java.util.UUID
import kotlin.math.abs
import kotlin.random.Random

class ShesanyankenaCharamenaGenerator(
    private val random: Random = Random.Default
) : SutraProblemGenerator {

    override val sutraName: String = "Shesanyankena Charamena"

    override fun generateProblem(difficultyTier: DifficultyTier): SutraProblem {
        val sProblem = generateDecimalProblem(difficultyTier)
        return convertToSutraProblem(sProblem)
    }

    override fun generateSpecificProblem(operand: Long): SutraProblem {
        val sProblem = buildFromFraction(1, 7, DifficultyTier.TIER_1_EASY)
        return convertToSutraProblem(sProblem)
    }

    override fun generateProblemSet(count: Int, difficultyTier: DifficultyTier): List<SutraProblem> {
        val list = mutableListOf<SutraProblem>()
        repeat(count) {
            list.add(generateProblem(difficultyTier))
        }
        return list
    }

    fun generateDecimalProblem(tier: DifficultyTier): ShesanyankenaProblem {
        return when (tier) {
            DifficultyTier.TIER_1_EASY -> {
                // Short cycles / terminating fractions (e.g. 1/7, 1/8, 3/7, 1/4, 2/5, 1/6)
                val choices = listOf(
                    Pair(1L, 7L), Pair(1L, 8L), Pair(3L, 7L), Pair(1L, 4L), Pair(2L, 5L), Pair(1L, 6L), Pair(1L, 3L)
                )
                val (p, q) = choices[random.nextInt(choices.size)]
                buildFromFraction(p, q, tier)
            }
            DifficultyTier.TIER_2_HARD -> {
                val isImproper = random.nextBoolean()
                if (isImproper) {
                    val q = random.nextLong(6L, 14L)
                    val p = q + random.nextLong(1L, 5L)
                    buildFromFraction(p, q, tier)
                } else {
                    val choices = listOf(
                        Pair(1L, 13L), Pair(5L, 6L), Pair(7L, 12L), Pair(1L, 11L), Pair(4L, 9L)
                    )
                    val (p, q) = choices[random.nextInt(choices.size)]
                    buildFromFraction(p, q, tier)
                }
            }
        }
    }

    fun buildFromFraction(p: Long, q: Long, tier: DifficultyTier): ShesanyankenaProblem {
        val sol = computeDecimalExpansion(p, q)
        val frac = Fraction(p, q).reduced
        val correctVal = sol.expansion.repeatingDigits.firstOrNull()?.toLong()
            ?: sol.expansion.nonRepeatingDigits.firstOrNull()?.toLong()
            ?: 0L

        val distractors = generateDistractors(correctVal, sol)

        return ShesanyankenaProblem(
            id = UUID.randomUUID().toString(),
            numerator = p,
            denominator = q,
            fraction = frac,
            solution = sol,
            difficultyTier = tier,
            distractors = distractors
        )
    }

    fun computeDecimalExpansion(p: Long, q: Long): ShesanyankenaSolution {
        if (q <= 0L) {
            return ShesanyankenaSolution(
                classification = DecimalClassification.UNSUPPORTED,
                expansion = DecimalExpansion(0, emptyList(), emptyList(), true, Fraction(p, q)),
                remainderSteps = emptyList(),
                vedicSteps = emptyList()
            )
        }

        if (p == 0L) {
            return ShesanyankenaSolution(
                classification = DecimalClassification.ZERO,
                expansion = DecimalExpansion(0, emptyList(), emptyList(), true, Fraction(0, q)),
                remainderSteps = emptyList(),
                vedicSteps = emptyList()
            )
        }

        val isNegative = (p < 0L)
        val absP = abs(p)
        val frac = Fraction(absP, q).reduced
        val pNorm = frac.numerator
        val qNorm = frac.denominator

        val integerPart = pNorm / qNorm
        val r0 = pNorm % qNorm

        val remainderSteps = mutableListOf<RemainderStep>()
        val vedicSteps = mutableListOf<ShesanyankenaStep>()
        val digits = mutableListOf<Int>()

        val seenRemainderIndex = mutableMapOf<Long, Int>()
        var r = r0
        var isTerminating = false
        var cycleStartIndex = -1

        val maxDigits = 20

        while (true) {
            if (r == 0L) {
                isTerminating = true
                break
            }

            if (seenRemainderIndex.containsKey(r)) {
                cycleStartIndex = seenRemainderIndex[r]!!
                break
            }

            if (digits.size >= maxDigits) {
                break
            }

            seenRemainderIndex[r] = digits.size
            val multR = r * 10L
            val digit = (multR / qNorm).toInt()
            val nextR = multR % qNorm

            val isCycleStart = false
            digits.add(digit)

            remainderSteps.add(
                RemainderStep(
                    stepIndex = digits.size,
                    incomingRemainder = r,
                    multipliedRemainder = multR,
                    emittedDigit = digit,
                    nextRemainder = nextR,
                    isCycleStart = isCycleStart,
                    isCycleEnd = false
                )
            )

            val divisorLastDigit = (qNorm % 10).toInt()
            val product = r * divisorLastDigit
            val extractedLastDigit = (product % 10).toInt()
            val matches = (extractedLastDigit == digit)

            vedicSteps.add(
                ShesanyankenaStep(
                    remainder = r,
                    divisorLastDigit = divisorLastDigit,
                    product = product,
                    extractedLastDigit = extractedLastDigit,
                    matchesReference = matches
                )
            )

            r = nextR
        }

        val classif = when {
            isNegative -> DecimalClassification.NEGATIVE
            isTerminating -> DecimalClassification.TERMINATING
            cycleStartIndex == 0 -> DecimalClassification.PURE_RECURRING
            cycleStartIndex > 0 -> DecimalClassification.MIXED_RECURRING
            else -> DecimalClassification.OUTPUT_LIMIT_REACHED
        }

        val nonRepDigits = if (isTerminating) digits else if (cycleStartIndex >= 0) digits.subList(0, cycleStartIndex) else digits
        val repDigits = if (isTerminating || cycleStartIndex < 0) emptyList() else digits.subList(cycleStartIndex, digits.size)

        val expansion = DecimalExpansion(
            integerPart = if (isNegative) -integerPart else integerPart,
            nonRepeatingDigits = nonRepDigits,
            repeatingDigits = repDigits,
            isTerminating = isTerminating,
            sourceFraction = Fraction(p, q).reduced
        )

        return ShesanyankenaSolution(
            classification = classif,
            expansion = expansion,
            remainderSteps = remainderSteps,
            vedicSteps = vedicSteps
        )
    }

    private fun convertToSutraProblem(s: ShesanyankenaProblem): SutraProblem {
        val decSteps = s.solution.remainderSteps.map { step ->
            DecompositionStep(
                stepNumber = step.stepIndex,
                label = "REMAINDER_${step.incomingRemainder}",
                formulaDisplay = "10 × ${step.incomingRemainder} = ${step.multipliedRemainder} $\\rightarrow$ ${step.multipliedRemainder} ÷ ${s.fraction.denominator} = ${step.emittedDigit} r ${step.nextRemainder}",
                stepResult = "Digit: ${step.emittedDigit}, Remainder: ${step.nextRemainder}",
                explanation = "Multiply incoming remainder ${step.incomingRemainder} by 10 to emit digit ${step.emittedDigit} with next remainder ${step.nextRemainder}."
            )
        }

        val correctVal = s.solution.expansion.repeatingDigits.firstOrNull()?.toLong()
            ?: s.solution.expansion.nonRepeatingDigits.firstOrNull()?.toLong()
            ?: 0L

        return SutraProblem(
            id = s.id,
            sutraName = sutraName,
            questionText = "Find the decimal expansion of ${s.fraction.toFormattedString()} (Shesanyankena Charamena)",
            operand = correctVal,
            correctAnswer = correctVal,
            prefixPart = s.solution.expansion.integerPart,
            incrementedPrefix = s.solution.expansion.nonRepeatingDigits.size.toLong(),
            prefixProduct = s.solution.expansion.repeatingDigits.size.toLong(),
            appendedSuffix = s.solution.expansion.toFormattedString(),
            decompositionSteps = decSteps,
            distractors = s.distractors,
            difficultyTier = s.difficultyTier,
            answerFormat = AnswerFormat.INTEGER
        )
    }

    private fun generateDistractors(correctVal: Long, sol: ShesanyankenaSolution): List<Long> {
        val list = mutableSetOf<Long>()
        list.add((correctVal + 1) % 10)
        list.add((correctVal + 9) % 10)
        list.add((correctVal + 5) % 10)
        list.add((correctVal + 3) % 10)

        var delta = 1L
        while (list.size < 4) {
            val candidate = (correctVal + delta) % 10
            if (candidate != correctVal) {
                list.add(candidate)
            }
            delta = if (delta > 0) -delta else (-delta + 2L)
        }

        return list.take(4)
    }
}
