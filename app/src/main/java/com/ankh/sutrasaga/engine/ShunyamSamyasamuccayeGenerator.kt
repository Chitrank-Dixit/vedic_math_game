package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.domain.models.AnswerFormat
import com.ankh.sutrasaga.domain.models.CommonFactorEquation
import com.ankh.sutrasaga.domain.models.DecompositionStep
import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.EqualNumeratorFractionEquation
import com.ankh.sutrasaga.domain.models.Fraction
import com.ankh.sutrasaga.domain.models.LinearExpression
import com.ankh.sutrasaga.domain.models.ShunyamClassification
import com.ankh.sutrasaga.domain.models.ShunyamFamily
import com.ankh.sutrasaga.domain.models.ShunyamProblem
import com.ankh.sutrasaga.domain.models.ShunyamSolution
import com.ankh.sutrasaga.domain.models.ShunyamStep
import com.ankh.sutrasaga.domain.models.SutraProblem
import java.util.UUID
import kotlin.math.abs
import kotlin.random.Random

class ShunyamSamyasamuccayeGenerator(
    private val random: Random = Random.Default
) : SutraProblemGenerator {

    override val sutraName: String = "Shunyam Samyasamuccaye"

    override fun generateProblem(difficultyTier: DifficultyTier): SutraProblem {
        val pProblem = generateShunyamProblem(difficultyTier)
        return convertToSutraProblem(pProblem)
    }

    override fun generateSpecificProblem(operand: Long): SutraProblem {
        val eq = CommonFactorEquation(
            k1 = 7,
            factor = LinearExpression(Fraction(1, 1), Fraction(1, 1)),
            k2 = 8
        )
        val pProblem = buildFromFamilyA(eq.k1, eq.factor.a.numerator, eq.factor.b.numerator, eq.k2, DifficultyTier.TIER_1_EASY)
        return convertToSutraProblem(pProblem)
    }

    override fun generateProblemSet(count: Int, difficultyTier: DifficultyTier): List<SutraProblem> {
        val list = mutableListOf<SutraProblem>()
        repeat(count) {
            list.add(generateProblem(difficultyTier))
        }
        return list
    }

    fun generateShunyamProblem(tier: DifficultyTier): ShunyamProblem {
        return when (tier) {
            DifficultyTier.TIER_1_EASY -> {
                // Family A easy: integer k1 != k2, integer a=1, integer b
                val k1 = random.nextLong(2L, 9L)
                var k2 = random.nextLong(2L, 9L)
                while (k2 == k1) { k2 += 1 }

                val a = 1L
                val b = random.nextLong(-9L, 9L).let { if (it == 0L) 1L else it }

                buildFromFamilyA(k1, a, b, k2, tier)
            }
            DifficultyTier.TIER_2_HARD -> {
                val isFamilyB = random.nextBoolean()
                if (!isFamilyB) {
                    // Family A hard: non-unit a, fractional / negative solutions
                    val k1 = random.nextLong(2L, 15L)
                    var k2 = random.nextLong(2L, 15L)
                    while (k2 == k1) { k2 += 1 }

                    val a = random.nextLong(2L, 6L)
                    val b = random.nextLong(-15L, 15L).let { if (it == 0L) 3L else it }

                    buildFromFamilyA(k1, a, b, k2, tier)
                } else {
                    // Family B: 1/(x+b1) + 1/(x+b2) = 1/(x+b3) + 1/(x+b4) where b1+b2 == b3+b4
                    val p = 1L
                    val b1 = random.nextLong(1L, 5L)
                    val b2 = random.nextLong(6L, 10L)
                    val sumB = b1 + b2

                    val b3 = random.nextLong(1L, 4L)
                    val b4 = sumB - b3

                    buildFromFamilyB(p, 1, b1, 1, b2, 1, b3, 1, b4, tier)
                }
            }
        }
    }

    fun buildFromFamilyA(k1: Long, a: Long, b: Long, k2: Long, tier: DifficultyTier): ShunyamProblem {
        val eq = CommonFactorEquation(
            k1 = k1,
            factor = LinearExpression(Fraction(a, 1), Fraction(b, 1)),
            k2 = k2
        )
        val sol = solveFamilyA(eq)
        val correctAnswer = sol.candidateSolution.reduced.numerator
        val distractors = generateDistractors(correctAnswer, sol.candidateSolution)

        return ShunyamProblem(
            id = UUID.randomUUID().toString(),
            family = ShunyamFamily.FAMILY_A_COMMON_FACTOR,
            commonFactorEq = eq,
            fractionEq = null,
            classification = sol.classification,
            solution = sol.candidateSolution,
            excludedValues = emptyList(),
            difficultyTier = tier,
            steps = sol.steps,
            distractors = distractors
        )
    }

    fun buildFromFamilyB(
        p: Long,
        a1: Long, b1: Long,
        a2: Long, b2: Long,
        a3: Long, b3: Long,
        a4: Long, b4: Long,
        tier: DifficultyTier
    ): ShunyamProblem {
        val eq = EqualNumeratorFractionEquation(
            numerator = p,
            d1 = LinearExpression(Fraction(a1, 1), Fraction(b1, 1)),
            d2 = LinearExpression(Fraction(a2, 1), Fraction(b2, 1)),
            d3 = LinearExpression(Fraction(a3, 1), Fraction(b3, 1)),
            d4 = LinearExpression(Fraction(a4, 1), Fraction(b4, 1))
        )
        val sol = solveFamilyB(eq)
        val correctAnswer = sol.candidateSolution.reduced.numerator
        val distractors = generateDistractors(correctAnswer, sol.candidateSolution)

        return ShunyamProblem(
            id = UUID.randomUUID().toString(),
            family = ShunyamFamily.FAMILY_B_EQUAL_NUMERATOR_RECIPROCAL,
            commonFactorEq = null,
            fractionEq = eq,
            classification = sol.classification,
            solution = sol.candidateSolution,
            excludedValues = sol.excludedValues,
            difficultyTier = tier,
            steps = sol.steps,
            distractors = distractors
        )
    }

    fun solveFamilyA(eq: CommonFactorEquation): ShunyamSolution {
        val k1 = eq.k1
        val k2 = eq.k2
        val factor = eq.factor
        val a = factor.a
        val b = factor.b

        if (k1 == k2) {
            return ShunyamSolution(
                classification = ShunyamClassification.IDENTITY,
                candidateSolution = Fraction(0, 1),
                excludedValues = emptyList(),
                isCandidateExcluded = false,
                steps = emptyList()
            )
        }

        if (a.numerator == 0L) {
            val isNoSol = (b.numerator != 0L)
            return ShunyamSolution(
                classification = if (isNoSol) ShunyamClassification.NO_SOLUTION else ShunyamClassification.IDENTITY,
                candidateSolution = Fraction(0, 1),
                excludedValues = emptyList(),
                isCandidateExcluded = false,
                steps = emptyList()
            )
        }

        val sol = factor.solveZero()
        val steps = listOf(
            ShunyamStep(
                stepIndex = 1,
                recognizedPattern = "Common Factor F(x) = ${factor.toFormattedString()}",
                transformation = "(${k1} - ${k2}) * (${factor.toFormattedString()}) = 0",
                candidateSolution = "${factor.toFormattedString()} = 0",
                excludedDomainValues = emptyList(),
                verificationResultText = "${k1}(0) = ${k2}(0) → 0 = 0",
                explanationText = "Unequal multipliers (${k1} ≠ ${k2}) force the common factor ${factor.toFormattedString()} to zero. Solved: x = ${sol.toFormattedString()}."
            )
        )

        return ShunyamSolution(
            classification = ShunyamClassification.UNIQUE_SOLUTION,
            candidateSolution = sol,
            excludedValues = emptyList(),
            isCandidateExcluded = false,
            steps = steps
        )
    }

    fun solveFamilyB(eq: EqualNumeratorFractionEquation): ShunyamSolution {
        val p = eq.numerator
        val d1 = eq.d1
        val d2 = eq.d2
        val d3 = eq.d3
        val d4 = eq.d4

        if (p == 0L) {
            return ShunyamSolution(
                classification = ShunyamClassification.IDENTITY,
                candidateSolution = Fraction(0, 1),
                excludedValues = emptyList(),
                isCandidateExcluded = false,
                steps = emptyList()
            )
        }

        // Calculate denominator roots (excluded values)
        val ex1 = d1.solveZero()
        val ex2 = d2.solveZero()
        val ex3 = d3.solveZero()
        val ex4 = d4.solveZero()
        val excludedList = listOf(ex1, ex2, ex3, ex4).map { it.reduced }.distinct()

        // Denominator sum LHS: (a1+a2)x + (b1+b2)
        val lhsA = Fraction(d1.a.numerator * d2.a.denominator + d2.a.numerator * d1.a.denominator, d1.a.denominator * d2.a.denominator).reduced
        val lhsB = Fraction(d1.b.numerator * d2.b.denominator + d2.b.numerator * d1.b.denominator, d1.b.denominator * d2.b.denominator).reduced

        // Denominator sum RHS: (a3+a4)x + (b3+b4)
        val rhsA = Fraction(d3.a.numerator * d4.a.denominator + d4.a.numerator * d3.a.denominator, d3.a.denominator * d4.a.denominator).reduced
        val rhsB = Fraction(d3.b.numerator * d4.b.denominator + d4.b.numerator * d3.b.denominator, d3.b.denominator * d4.b.denominator).reduced

        val isSumEqual = (lhsA == rhsA && lhsB == rhsB)
        if (!isSumEqual || lhsA.numerator == 0L) {
            return ShunyamSolution(
                classification = ShunyamClassification.NOT_APPLICABLE,
                candidateSolution = Fraction(0, 1),
                excludedValues = excludedList,
                isCandidateExcluded = false,
                steps = emptyList()
            )
        }

        val sumExpr = LinearExpression(lhsA, lhsB)
        val candidate = sumExpr.solveZero()

        // Check if candidate is in excluded values
        val isExcluded = excludedList.any { it.numerator == candidate.numerator && it.denominator == candidate.denominator }

        if (isExcluded) {
            return ShunyamSolution(
                classification = ShunyamClassification.INVALID_DOMAIN,
                candidateSolution = candidate,
                excludedValues = excludedList,
                isCandidateExcluded = true,
                steps = emptyList()
            )
        }

        val steps = listOf(
            ShunyamStep(
                stepIndex = 1,
                recognizedPattern = "Equal numerators (p=${p}) and equal denominator sum S(x) = ${sumExpr.toFormattedString()}",
                transformation = "S(x) = ${sumExpr.toFormattedString()} = 0",
                candidateSolution = "x = ${candidate.toFormattedString()}",
                excludedDomainValues = excludedList,
                verificationResultText = "Excluded values: ${excludedList.joinToString { it.toFormattedString() }}. Candidate x = ${candidate.toFormattedString()} is valid.",
                explanationText = "The sum of LHS denominators equals the sum of RHS denominators (${sumExpr.toFormattedString()}). Equating S(x) = 0 yields x = ${candidate.toFormattedString()}."
            )
        )

        return ShunyamSolution(
            classification = ShunyamClassification.UNIQUE_SOLUTION,
            candidateSolution = candidate,
            excludedValues = excludedList,
            isCandidateExcluded = false,
            steps = steps
        )
    }

    private fun convertToSutraProblem(p: ShunyamProblem): SutraProblem {
        val decSteps = p.steps.map { s ->
            DecompositionStep(
                stepNumber = s.stepIndex,
                label = "Zero-Equating Step",
                formulaDisplay = s.transformation,
                stepResult = s.candidateSolution,
                explanation = s.explanationText
            )
        }

        val qText = when (p.family) {
            ShunyamFamily.FAMILY_A_COMMON_FACTOR -> "Solve: ${p.commonFactorEq?.toFormattedString()}"
            ShunyamFamily.FAMILY_B_EQUAL_NUMERATOR_RECIPROCAL -> "Solve: ${p.fractionEq?.toFormattedString()}"
        }

        return SutraProblem(
            id = p.id,
            sutraName = sutraName,
            questionText = qText,
            operand = p.solution.reduced.numerator,
            correctAnswer = p.solution.reduced.numerator,
            prefixPart = p.solution.reduced.numerator,
            incrementedPrefix = p.solution.reduced.denominator,
            prefixProduct = p.solution.reduced.numerator,
            appendedSuffix = p.classification.name,
            decompositionSteps = decSteps,
            distractors = p.distractors,
            difficultyTier = p.difficultyTier,
            answerFormat = AnswerFormat.INTEGER,
            expectedSecondaryAnswer = p.solution.reduced.denominator
        )
    }

    private fun generateDistractors(correctVal: Long, solFrac: Fraction): List<Long> {
        val list = mutableSetOf<Long>()
        list.add(-correctVal)
        list.add(correctVal + 1)
        list.add(correctVal - 1)
        list.add(correctVal * 2)

        var delta = 2L
        while (list.size < 4) {
            val candidate = correctVal + delta
            if (candidate != correctVal) {
                list.add(candidate)
            }
            delta = if (delta > 0) -delta else (-delta + 2L)
        }

        return list.take(4)
    }
}
