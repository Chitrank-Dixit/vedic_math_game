package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.domain.models.AnswerFormat
import com.ankh.sutrasaga.domain.models.CompletionOperationType
import com.ankh.sutrasaga.domain.models.CompletionStep
import com.ankh.sutrasaga.domain.models.DecompositionStep
import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.Fraction
import com.ankh.sutrasaga.domain.models.PuranapuranabhyamProblem
import com.ankh.sutrasaga.domain.models.PuranapuranabhyamSolution
import com.ankh.sutrasaga.domain.models.QuadraticClassification
import com.ankh.sutrasaga.domain.models.QuadraticEquation
import com.ankh.sutrasaga.domain.models.SutraProblem
import java.util.UUID
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.random.Random

class PuranapuranabhyamGenerator(
    private val random: Random = Random.Default
) : SutraProblemGenerator {

    override val sutraName: String = "Puranapuranabhyam"

    override fun generateProblem(difficultyTier: DifficultyTier): SutraProblem {
        val pProblem = generateQuadraticProblem(difficultyTier)
        return convertToSutraProblem(pProblem)
    }

    override fun generateSpecificProblem(operand: Long): SutraProblem {
        val pProblem = buildFromCoefficients(1, 6, 8, DifficultyTier.TIER_1_EASY)
        return convertToSutraProblem(pProblem)
    }

    override fun generateProblemSet(count: Int, difficultyTier: DifficultyTier): List<SutraProblem> {
        val list = mutableListOf<SutraProblem>()
        repeat(count) {
            list.add(generateProblem(difficultyTier))
        }
        return list
    }

    fun generateQuadraticProblem(tier: DifficultyTier): PuranapuranabhyamProblem {
        return when (tier) {
            DifficultyTier.TIER_1_EASY -> {
                // Monic quadratics (A = 1) with integer roots r1, r2
                val r1 = random.nextLong(-8L, 8L).let { if (it == 0L) -2L else it }
                val r2 = random.nextLong(-8L, 8L).let { if (it == 0L) -4L else it }
                val a = 1L
                val b = -(r1 + r2)
                val c = r1 * r2

                buildFromCoefficients(a, b, c, tier)
            }
            DifficultyTier.TIER_2_HARD -> {
                val isNonMonic = random.nextBoolean()
                if (isNonMonic) {
                    // Non-monic quadratics: A in 2..4, roots rational
                    val a = random.nextLong(2L, 4L)
                    val r1Num = random.nextLong(-5L, 5L).let { if (it == 0L) 1L else it }
                    val r1Den = random.nextLong(1L, 2L)
                    val r2Num = random.nextLong(-5L, 5L).let { if (it == 0L) -3L else it }
                    val r2Den = 1L

                    val num = a * (r1Num * r2Den + r2Num * r1Den)
                    val den = r1Den * r2Den
                    val b = -(num / den)
                    val c = (a * r1Num * r2Num) / (r1Den * r2Den)

                    val eq = QuadraticEquation(a, b, c)
                    val sol = solveQuadratic(eq)
                    if (sol.classification == QuadraticClassification.TWO_REAL_ROOTS) {
                        buildFromCoefficients(a, b, c, tier)
                    } else {
                        buildFromCoefficients(1, 6, 8, tier)
                    }
                } else {
                    // Monic with larger roots or repeated roots
                    val isRepeated = random.nextBoolean()
                    if (isRepeated) {
                        val r = random.nextLong(-6L, 6L).let { if (it == 0L) 3L else it }
                        val a = 1L
                        val b = -2 * r
                        val c = r * r
                        buildFromCoefficients(a, b, c, tier)
                    } else {
                        val r1 = random.nextLong(-12L, 12L).let { if (it == 0L) 3L else it }
                        val r2 = random.nextLong(-12L, 12L).let { if (it == 0L) -5L else it }
                        val a = 1L
                        val b = -(r1 + r2)
                        val c = r1 * r2
                        buildFromCoefficients(a, b, c, tier)
                    }
                }
            }
        }
    }

    fun buildFromCoefficients(a: Long, b: Long, c: Long, tier: DifficultyTier): PuranapuranabhyamProblem {
        val eq = QuadraticEquation(a, b, c)
        val sol = solveQuadratic(eq)
        val correctAnswer = sol.root1.reduced.numerator
        val distractors = generateDistractors(correctAnswer, sol.root1, sol.root2)

        return PuranapuranabhyamProblem(
            id = UUID.randomUUID().toString(),
            equation = eq,
            classification = sol.classification,
            discriminant = sol.discriminant,
            completionTerm = sol.completionTerm,
            root1 = sol.root1,
            root2 = sol.root2,
            difficultyTier = tier,
            steps = sol.steps,
            distractors = distractors
        )
    }

    fun solveQuadratic(eq: QuadraticEquation): PuranapuranabhyamSolution {
        val a = eq.a
        val b = eq.b
        val c = eq.c

        if (a == 0L) {
            return PuranapuranabhyamSolution(
                classification = QuadraticClassification.NOT_QUADRATIC,
                discriminant = 0L,
                completionTerm = Fraction(0, 1),
                root1 = Fraction(0, 1),
                root2 = Fraction(0, 1),
                isRepeated = false,
                steps = emptyList()
            )
        }

        val d = eq.discriminant

        if (d < 0L) {
            return PuranapuranabhyamSolution(
                classification = QuadraticClassification.NO_REAL_ROOTS,
                discriminant = d,
                completionTerm = Fraction(b * b, 4 * a * a).reduced,
                root1 = Fraction(0, 1),
                root2 = Fraction(0, 1),
                isRepeated = false,
                steps = emptyList()
            )
        }

        val sqrtDDouble = sqrt(d.toDouble())
        val sqrtD = sqrtDDouble.toLong()

        if (sqrtD * sqrtD != d) {
            return PuranapuranabhyamSolution(
                classification = QuadraticClassification.IRRATIONAL_REAL_ROOTS_DEFERRED,
                discriminant = d,
                completionTerm = Fraction(b * b, 4 * a * a).reduced,
                root1 = Fraction(0, 1),
                root2 = Fraction(0, 1),
                isRepeated = false,
                steps = emptyList()
            )
        }

        val root1 = Fraction(-b + sqrtD, 2 * a).reduced
        val root2 = Fraction(-b - sqrtD, 2 * a).reduced
        val isRepeated = (d == 0L)
        val classif = if (isRepeated) QuadraticClassification.REPEATED_REAL_ROOT else QuadraticClassification.TWO_REAL_ROOTS

        val completionTerm = Fraction(b * b, 4 * a * a).reduced

        val steps = mutableListOf<CompletionStep>()

        // Step 1: Move Constant
        steps.add(
            CompletionStep(
                stepIndex = 1,
                operationType = CompletionOperationType.MOVE_CONSTANT,
                formulaDisplay = "${if (a == 1L) "x²" else "${a}x²"} ${if (b >= 0) "+ ${b}x" else "- ${abs(b)}x"} = ${-c}",
                stepResult = "Constant moved: ${-c}",
                explanationText = "Move constant $c to the right side by subtracting it from both sides."
            )
        )

        // Step 2: Normalize by A (if A != 1)
        val bOverA = Fraction(b, a).reduced
        val negCOverA = Fraction(-c, a).reduced
        if (a != 1L) {
            steps.add(
                CompletionStep(
                    stepIndex = 2,
                    operationType = CompletionOperationType.NORMALIZE,
                    formulaDisplay = "x² + (${bOverA.toFormattedString()})x = ${negCOverA.toFormattedString()}",
                    stepResult = "Divided by coefficient A = $a",
                    explanationText = "Divide all terms by A = $a to make the leading coefficient 1."
                )
            )
        }

        // Step 3: Add Completion Term
        val halfBOverA = Fraction(b, 2 * a).reduced
        val rhsSum = Fraction(negCOverA.numerator * 4 * a * a + b * b * negCOverA.denominator, negCOverA.denominator * 4 * a * a).reduced
        steps.add(
            CompletionStep(
                stepIndex = steps.size + 1,
                operationType = CompletionOperationType.ADD_COMPLETION,
                formulaDisplay = "T_comp = (${halfBOverA.toFormattedString()})² = ${completionTerm.toFormattedString()}",
                stepResult = "Added ${completionTerm.toFormattedString()} to both sides",
                explanationText = "Take half of the linear coefficient (${halfBOverA.toFormattedString()}) and square it to get completion term ${completionTerm.toFormattedString()}. Add it to both sides."
            )
        )

        // Step 4: Form Perfect Square
        steps.add(
            CompletionStep(
                stepIndex = steps.size + 1,
                operationType = CompletionOperationType.FORM_SQUARE,
                formulaDisplay = "(x ${if (halfBOverA.numerator >= 0) "+ " else "- "}${abs(halfBOverA.numerator)}${if (halfBOverA.denominator > 1) "/${halfBOverA.denominator}" else ""})² = ${Fraction(d, 4 * a * a).reduced.toFormattedString()}",
                stepResult = "Perfect square formed",
                explanationText = "Rewrite the left side as a perfect square: (x + ${halfBOverA.toFormattedString()})²."
            )
        )

        // Step 5: Solve Square
        steps.add(
            CompletionStep(
                stepIndex = steps.size + 1,
                operationType = CompletionOperationType.SOLVE_SQUARE,
                formulaDisplay = "x = (${-b} ± ${sqrtD}) / ${2 * a}",
                stepResult = if (isRepeated) "x = ${root1.toFormattedString()}" else "x = ${root1.toFormattedString()} or x = ${root2.toFormattedString()}",
                explanationText = "Take the square root of both sides and solve for x. Derived roots: ${if (isRepeated) "x = ${root1.toFormattedString()} (repeated)" else "x₁ = ${root1.toFormattedString()}, x₂ = ${root2.toFormattedString()}"}."
            )
        )

        return PuranapuranabhyamSolution(
            classification = classif,
            discriminant = d,
            completionTerm = completionTerm,
            root1 = root1,
            root2 = root2,
            isRepeated = isRepeated,
            steps = steps
        )
    }

    private fun convertToSutraProblem(p: PuranapuranabhyamProblem): SutraProblem {
        val decSteps = p.steps.map { s ->
            DecompositionStep(
                stepNumber = s.stepIndex,
                label = s.operationType.name,
                formulaDisplay = s.formulaDisplay,
                stepResult = s.stepResult,
                explanation = s.explanationText
            )
        }

        return SutraProblem(
            id = p.id,
            sutraName = sutraName,
            questionText = "Solve by completing the square: ${p.equation.toFormattedString()}",
            operand = p.root1.reduced.numerator,
            correctAnswer = p.root1.reduced.numerator,
            prefixPart = p.root1.reduced.numerator,
            incrementedPrefix = p.root2.reduced.numerator,
            prefixProduct = p.root1.reduced.numerator,
            appendedSuffix = p.classification.name,
            decompositionSteps = decSteps,
            distractors = p.distractors,
            difficultyTier = p.difficultyTier,
            answerFormat = AnswerFormat.INTEGER,
            expectedSecondaryAnswer = p.root2.reduced.numerator
        )
    }

    private fun generateDistractors(correctVal: Long, root1: Fraction, root2: Fraction): List<Long> {
        val list = mutableSetOf<Long>()
        list.add(-correctVal)
        list.add(root2.reduced.numerator)
        list.add(correctVal + 1)
        list.add(correctVal - 1)

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
