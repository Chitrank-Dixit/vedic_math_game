package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.domain.models.AnswerFormat
import com.ankh.sutrasaga.domain.models.DecompositionStep
import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.Fraction
import com.ankh.sutrasaga.domain.models.LinearEquation
import com.ankh.sutrasaga.domain.models.SankalanaClassification
import com.ankh.sutrasaga.domain.models.SankalanaProblem
import com.ankh.sutrasaga.domain.models.SankalanaSolution
import com.ankh.sutrasaga.domain.models.SankalanaStep
import com.ankh.sutrasaga.domain.models.SutraProblem
import java.util.UUID
import kotlin.math.abs
import kotlin.random.Random

class SankalanaVyavakalanabhyamGenerator(
    private val random: Random = Random.Default
) : SutraProblemGenerator {

    override val sutraName: String = "Sankalana-Vyavakalanabhyam"

    override fun generateProblem(difficultyTier: DifficultyTier): SutraProblem {
        val sProblem = generateSankalanaProblem(difficultyTier)
        return convertToSutraProblem(sProblem)
    }

    override fun generateSpecificProblem(operand: Long): SutraProblem {
        val eq1 = LinearEquation(45, -23, 113)
        val eq2 = LinearEquation(23, -45, 91)
        val sProblem = buildFromEquations(eq1, eq2, DifficultyTier.TIER_1_EASY)
        return convertToSutraProblem(sProblem)
    }

    override fun generateProblemSet(count: Int, difficultyTier: DifficultyTier): List<SutraProblem> {
        val list = mutableListOf<SutraProblem>()
        repeat(count) {
            list.add(generateProblem(difficultyTier))
        }
        return list
    }

    fun generateSankalanaProblem(tier: DifficultyTier): SankalanaProblem {
        val (a, b) = when (tier) {
            DifficultyTier.TIER_1_EASY -> Pair(random.nextLong(2L, 10L), random.nextLong(1L, 9L))
            DifficultyTier.TIER_2_HARD -> Pair(random.nextLong(10L, 50L), -random.nextLong(10L, 50L))
        }

        var validB = b
        while (abs(a) == abs(validB)) {
            validB += 1L
        }

        val targetX = when (tier) {
            DifficultyTier.TIER_1_EASY -> random.nextLong(1L, 10L)
            DifficultyTier.TIER_2_HARD -> random.nextLong(-10L, 20L)
        }
        val targetY = when (tier) {
            DifficultyTier.TIER_1_EASY -> random.nextLong(1L, 10L)
            DifficultyTier.TIER_2_HARD -> random.nextLong(-10L, 20L)
        }

        val c = a * targetX + validB * targetY
        val d = validB * targetX + a * targetY

        val eq1 = LinearEquation(a, validB, c)
        val eq2 = LinearEquation(validB, a, d)

        return buildFromEquations(eq1, eq2, tier)
    }

    fun buildFromEquations(eq1: LinearEquation, eq2: LinearEquation, tier: DifficultyTier): SankalanaProblem {
        val solution = solveSystem(eq1, eq2)
        require(solution.classification == SankalanaClassification.UNIQUE_SOLUTION) {
            "Equations provided do not form a valid Sankalana-Vyavakalanabhyam system: ${solution.classification}"
        }

        val correctAnswer = solution.xSolution.reduced.numerator
        val distractors = generateDistractors(correctAnswer)

        return SankalanaProblem(
            id = UUID.randomUUID().toString(),
            firstEquation = eq1,
            secondEquation = eq2,
            classification = solution.classification,
            xSolution = solution.xSolution,
            ySolution = solution.ySolution,
            xPlusY = solution.xPlusY,
            xMinusY = solution.xMinusY,
            difficultyTier = tier,
            steps = solution.steps,
            distractors = distractors
        )
    }

    fun solveSystem(eq1: LinearEquation, eq2: LinearEquation): SankalanaSolution {
        val a1 = eq1.a
        val b1 = eq1.b
        val c1 = eq1.c
        val a2 = eq2.a
        val b2 = eq2.b
        val c2 = eq2.c

        // Mirror coefficient check: |a1| == |b2| and |b1| == |a2|
        val isMirror = (abs(a1) == abs(b2) && abs(b1) == abs(a2))
        if (!isMirror) {
            return SankalanaSolution(
                classification = SankalanaClassification.NOT_APPLICABLE,
                xSolution = Fraction(0, 1),
                ySolution = Fraction(0, 1),
                xPlusY = Fraction(0, 1),
                xMinusY = Fraction(0, 1),
                determinant = 0L,
                steps = emptyList()
            )
        }

        val det = a1 * b2 - a2 * b1
        val addSum = a1 + a2
        val addYSum = b1 + b2
        val subDiff = a1 - a2
        val subYDiff = b1 - b2

        if (det == 0L) {
            val isInf = (c1 == c2)
            val classif = if (isInf) SankalanaClassification.INFINITE_SOLUTIONS else SankalanaClassification.INCONSISTENT
            return SankalanaSolution(
                classification = classif,
                xSolution = Fraction(0, 1),
                ySolution = Fraction(0, 1),
                xPlusY = Fraction(0, 1),
                xMinusY = Fraction(0, 1),
                determinant = det,
                steps = emptyList()
            )
        }

        var xPlusYFrac = Fraction(0, 1)
        var xMinusYFrac = Fraction(0, 1)
        val steps = mutableListOf<SankalanaStep>()

        // Addition Step (Eq 1 + Eq 2): (a1+a2)x + (b1+b2)y = c1+c2
        if (addSum != 0L && addSum == addYSum) {
            xPlusYFrac = Fraction(c1 + c2, addSum).reduced
            steps.add(
                SankalanaStep(
                    stepIndex = 1,
                    operationName = "Addition Equation",
                    formulaDisplay = "${addSum}x + ${addYSum}y = ${c1 + c2}",
                    stepResult = "x + y = ${xPlusYFrac.toFormattedString()}",
                    explanationText = "Adding both equations yields ${addSum}(x + y) = ${c1 + c2}, so x + y = ${xPlusYFrac.toFormattedString()}."
                )
            )
        } else if (addSum != 0L && addSum == -addYSum) {
            xMinusYFrac = Fraction(c1 + c2, addSum).reduced
            steps.add(
                SankalanaStep(
                    stepIndex = 1,
                    operationName = "Addition Equation",
                    formulaDisplay = "${addSum}x ${if (addYSum >= 0) "+" else ""}${addYSum}y = ${c1 + c2}",
                    stepResult = "x - y = ${xMinusYFrac.toFormattedString()}",
                    explanationText = "Adding both equations yields ${addSum}(x - y) = ${c1 + c2}, so x - y = ${xMinusYFrac.toFormattedString()}."
                )
            )
        }

        // Subtraction Step (Eq 1 - Eq 2): (a1-a2)x + (b1-b2)y = c1-c2
        if (subDiff != 0L && subDiff == subYDiff) {
            xPlusYFrac = Fraction(c1 - c2, subDiff).reduced
            steps.add(
                SankalanaStep(
                    stepIndex = 2,
                    operationName = "Subtraction Equation",
                    formulaDisplay = "${subDiff}x ${if (subYDiff >= 0) "+" else ""}${subYDiff}y = ${c1 - c2}",
                    stepResult = "x + y = ${xPlusYFrac.toFormattedString()}",
                    explanationText = "Subtracting Eq 2 from Eq 1 yields ${subDiff}(x + y) = ${c1 - c2}, so x + y = ${xPlusYFrac.toFormattedString()}."
                )
            )
        } else if (subDiff != 0L && subDiff == -subYDiff) {
            xMinusYFrac = Fraction(c1 - c2, subDiff).reduced
            steps.add(
                SankalanaStep(
                    stepIndex = 2,
                    operationName = "Subtraction Equation",
                    formulaDisplay = "${subDiff}x ${if (subYDiff >= 0) "+" else ""}${subYDiff}y = ${c1 - c2}",
                    stepResult = "x - y = ${xMinusYFrac.toFormattedString()}",
                    explanationText = "Subtracting Eq 2 from Eq 1 yields ${subDiff}(x - y) = ${c1 - c2}, so x - y = ${xMinusYFrac.toFormattedString()}."
                )
            )
        }

        // Handle case where one operation resolves 2x or 2y directly (e.g. x + y = 10, x - y = 2)
        if (addYSum == 0L && addSum != 0L) { // 2x = c1 + c2
            val xOnly = Fraction(c1 + c2, addSum).reduced
            val yOnly = Fraction(c1 - c2, subYDiff).reduced
            return SankalanaSolution(
                classification = SankalanaClassification.UNIQUE_SOLUTION,
                xSolution = xOnly,
                ySolution = yOnly,
                xPlusY = Fraction(xOnly.numerator + yOnly.numerator, 1),
                xMinusY = Fraction(xOnly.numerator - yOnly.numerator, 1),
                determinant = det,
                steps = listOf(
                    SankalanaStep(1, "Addition Equation", "2x = ${c1 + c2}", "x = ${xOnly.toFormattedString()}", "Adding equations eliminates y."),
                    SankalanaStep(2, "Subtraction Equation", "2y = ${c1 - c2}", "y = ${yOnly.toFormattedString()}", "Subtracting equations eliminates x.")
                )
            )
        }

        // Recombination (x = (S + D)/2, y = (S - D)/2)
        val sFrac = xPlusYFrac
        val dfFrac = xMinusYFrac

        val xNum = sFrac.numerator * dfFrac.denominator + dfFrac.numerator * sFrac.denominator
        val xDen = 2L * sFrac.denominator * dfFrac.denominator
        val xFrac = Fraction(xNum, xDen).reduced

        val yNum = sFrac.numerator * dfFrac.denominator - dfFrac.numerator * sFrac.denominator
        val yDen = 2L * sFrac.denominator * dfFrac.denominator
        val yFrac = Fraction(yNum, yDen).reduced

        steps.add(
            SankalanaStep(
                stepIndex = 3,
                operationName = "Recombination (Find x & y)",
                formulaDisplay = "x = (S + D)/2, y = (S - D)/2",
                stepResult = "x = ${xFrac.toFormattedString()}, y = ${yFrac.toFormattedString()}",
                explanationText = "Combining x + y = ${sFrac.toFormattedString()} and x - y = ${dfFrac.toFormattedString()} gives x = ${xFrac.toFormattedString()}, y = ${yFrac.toFormattedString()}."
            )
        )

        return SankalanaSolution(
            classification = SankalanaClassification.UNIQUE_SOLUTION,
            xSolution = xFrac,
            ySolution = yFrac,
            xPlusY = sFrac,
            xMinusY = dfFrac,
            determinant = det,
            steps = steps
        )
    }

    private fun convertToSutraProblem(p: SankalanaProblem): SutraProblem {
        val decSteps = p.steps.map { s ->
            DecompositionStep(
                stepNumber = s.stepIndex,
                label = s.operationName,
                formulaDisplay = s.formulaDisplay,
                stepResult = s.stepResult,
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
            appendedSuffix = p.classification.name,
            decompositionSteps = decSteps,
            distractors = p.distractors,
            difficultyTier = p.difficultyTier,
            answerFormat = AnswerFormat.ORDERED_PAIR,
            expectedSecondaryAnswer = p.ySolution.reduced.numerator
        )
    }

    private fun generateDistractors(correctVal: Long): List<Long> {
        val list = mutableSetOf<Long>()
        list.add(correctVal + 1)
        list.add(correctVal - 1)
        list.add(correctVal * 2)
        list.add(-correctVal)

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
