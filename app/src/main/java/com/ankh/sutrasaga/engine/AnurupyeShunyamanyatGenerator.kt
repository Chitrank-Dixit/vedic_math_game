package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.domain.models.AnswerFormat
import com.ankh.sutrasaga.domain.models.AnurupyeCase
import com.ankh.sutrasaga.domain.models.AnurupyeProblem
import com.ankh.sutrasaga.domain.models.AnurupyeSolution
import com.ankh.sutrasaga.domain.models.AnurupyeStep
import com.ankh.sutrasaga.domain.models.DecompositionStep
import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.Fraction
import com.ankh.sutrasaga.domain.models.LinearEquation
import com.ankh.sutrasaga.domain.models.SutraProblem
import java.util.UUID
import kotlin.random.Random

class AnurupyeShunyamanyatGenerator(
    private val random: Random = Random.Default
) : SutraProblemGenerator {

    override val sutraName: String = "Anurupye Shunyamanyat"

    override fun generateProblem(difficultyTier: DifficultyTier): SutraProblem {
        val pProblem = generateAnurupyeProblem(difficultyTier)
        return convertToSutraProblem(pProblem)
    }

    override fun generateSpecificProblem(operand: Long): SutraProblem {
        val eq1 = LinearEquation(3, 2, 12)
        val eq2 = LinearEquation(6, 5, 24)
        val pProblem = buildFromEquations(eq1, eq2, DifficultyTier.TIER_1_EASY)
        return convertToSutraProblem(pProblem)
    }

    override fun generateProblemSet(count: Int, difficultyTier: DifficultyTier): List<SutraProblem> {
        val list = mutableListOf<SutraProblem>()
        repeat(count) {
            list.add(generateProblem(difficultyTier))
        }
        return list
    }

    fun generateAnurupyeProblem(tier: DifficultyTier): AnurupyeProblem {
        val isYZero = random.nextBoolean()

        val nonZeroValue = when (tier) {
            DifficultyTier.TIER_1_EASY -> random.nextLong(1L, 10L)
            DifficultyTier.TIER_2_HARD -> random.nextLong(10L, 50L)
        }

        val k = random.nextLong(2L, 4L)
        val a1 = random.nextLong(1L, 10L)
        val a2 = a1 * k
        val c1 = a1 * nonZeroValue
        val c2 = c1 * k

        var b1 = random.nextLong(1L, 10L)
        var b2 = random.nextLong(1L, 10L)
        while (a1 * b2 == a2 * b1) {
            b2 += 1
        }

        val (eq1, eq2) = if (isYZero) {
            Pair(LinearEquation(a1, b1, c1), LinearEquation(a2, b2, c2))
        } else {
            Pair(LinearEquation(b1, a1, c1), LinearEquation(b2, a2, c2))
        }

        return buildFromEquations(eq1, eq2, tier)
    }

    fun buildFromEquations(eq1: LinearEquation, eq2: LinearEquation, tier: DifficultyTier): AnurupyeProblem {
        val solution = solveSystem(eq1, eq2)
        require(solution.caseType == AnurupyeCase.Y_ZERO || solution.caseType == AnurupyeCase.X_ZERO) {
            "Equations provided do not form a valid Anurupye Shunyamanyat system: ${solution.caseType}"
        }

        val correctAnswer = if (solution.caseType == AnurupyeCase.Y_ZERO) {
            solution.xSolution.reduced.numerator
        } else {
            solution.ySolution.reduced.numerator
        }

        val distractors = generateDistractors(correctAnswer)

        return AnurupyeProblem(
            id = UUID.randomUUID().toString(),
            firstEquation = eq1,
            secondEquation = eq2,
            caseType = solution.caseType,
            xSolution = solution.xSolution,
            ySolution = solution.ySolution,
            difficultyTier = tier,
            steps = solution.steps,
            distractors = distractors
        )
    }

    fun solveSystem(eq1: LinearEquation, eq2: LinearEquation): AnurupyeSolution {
        val det = (eq1.a * eq2.b) - (eq2.a * eq1.b)
        val xRatioMatch = (eq1.a * eq2.c == eq2.a * eq1.c) && eq1.a != 0L && eq2.a != 0L
        val yRatioMatch = (eq1.b * eq2.c == eq2.b * eq1.c) && eq1.b != 0L && eq2.b != 0L

        val steps = mutableListOf<AnurupyeStep>()

        if (xRatioMatch && yRatioMatch && det == 0L) {
            return AnurupyeSolution(
                caseType = AnurupyeCase.INFINITE_SOLUTIONS,
                xSolution = Fraction(0, 1),
                ySolution = Fraction(0, 1),
                steps = emptyList()
            )
        }

        if (det == 0L) {
            return AnurupyeSolution(
                caseType = AnurupyeCase.INCONSISTENT,
                xSolution = Fraction(0, 1),
                ySolution = Fraction(0, 1),
                steps = emptyList()
            )
        }

        if (xRatioMatch) {
            val xFrac = Fraction(eq1.c, eq1.a).reduced
            val yFrac = Fraction(0, 1)

            steps.add(
                AnurupyeStep(
                    stepIndex = 1,
                    ratioComparison = "${eq1.a} × ${eq2.c} = ${eq2.a} × ${eq1.c} (${eq1.a * eq2.c} = ${eq2.a * eq1.c})",
                    isRatioMatching = true,
                    inferredZeroVariable = "y = 0",
                    substitutionStep = "${eq1.a}x + ${eq1.b}(0) = ${eq1.c} → ${eq1.a}x = ${eq1.c}",
                    finalSolutionText = "x = ${xFrac.toFormattedString()}, y = 0",
                    explanationText = "Ratio of x-coefficients equals constant ratio (${eq1.a}/${eq2.a} = ${eq1.c}/${eq2.c}). Therefore, y = 0 and x = ${xFrac.toFormattedString()}."
                )
            )

            return AnurupyeSolution(
                caseType = AnurupyeCase.Y_ZERO,
                xSolution = xFrac,
                ySolution = yFrac,
                steps = steps
            )
        }

        if (yRatioMatch) {
            val xFrac = Fraction(0, 1)
            val yFrac = Fraction(eq1.c, eq1.b).reduced

            steps.add(
                AnurupyeStep(
                    stepIndex = 1,
                    ratioComparison = "${eq1.b} × ${eq2.c} = ${eq2.b} × ${eq1.c} (${eq1.b * eq2.c} = ${eq2.b * eq1.c})",
                    isRatioMatching = true,
                    inferredZeroVariable = "x = 0",
                    substitutionStep = "${eq1.a}(0) + ${eq1.b}y = ${eq1.c} → ${eq1.b}y = ${eq1.c}",
                    finalSolutionText = "x = 0, y = ${yFrac.toFormattedString()}",
                    explanationText = "Ratio of y-coefficients equals constant ratio (${eq1.b}/${eq2.b} = ${eq1.c}/${eq2.c}). Therefore, x = 0 and y = ${yFrac.toFormattedString()}."
                )
            )

            return AnurupyeSolution(
                caseType = AnurupyeCase.X_ZERO,
                xSolution = xFrac,
                ySolution = yFrac,
                steps = steps
            )
        }

        return AnurupyeSolution(
            caseType = AnurupyeCase.NOT_APPLICABLE,
            xSolution = Fraction(0, 1),
            ySolution = Fraction(0, 1),
            steps = emptyList()
        )
    }

    private fun convertToSutraProblem(p: AnurupyeProblem): SutraProblem {
        val decSteps = p.steps.map { s ->
            DecompositionStep(
                stepNumber = s.stepIndex,
                label = "Ratio Test",
                formulaDisplay = s.ratioComparison,
                stepResult = s.finalSolutionText,
                explanation = s.explanationText
            )
        }

        return SutraProblem(
            id = p.id,
            sutraName = sutraName,
            questionText = "Solve: ${p.firstEquation.toFormattedString()} | ${p.secondEquation.toFormattedString()}",
            operand = p.firstEquation.a,
            correctAnswer = p.xSolution.reduced.numerator,
            prefixPart = p.firstEquation.a,
            incrementedPrefix = p.secondEquation.a,
            prefixProduct = p.xSolution.reduced.numerator,
            appendedSuffix = p.caseType.name,
            decompositionSteps = decSteps,
            distractors = p.distractors,
            difficultyTier = p.difficultyTier,
            answerFormat = AnswerFormat.ORDERED_PAIR,
            expectedSecondaryAnswer = p.ySolution.reduced.numerator
        )
    }

    private fun generateDistractors(correctVal: Long): List<Long> {
        val list = mutableSetOf<Long>()
        list.add(0L)
        list.add(correctVal + 1)
        list.add((correctVal - 1).coerceAtLeast(1))
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
