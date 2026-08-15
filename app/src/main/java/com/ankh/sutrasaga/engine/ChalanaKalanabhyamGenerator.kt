package com.ankh.sutrasaga.engine

import com.ankh.sutrasaga.domain.models.AnswerFormat
import com.ankh.sutrasaga.domain.models.ChalanaClassification
import com.ankh.sutrasaga.domain.models.ChalanaOperationType
import com.ankh.sutrasaga.domain.models.ChalanaProblem
import com.ankh.sutrasaga.domain.models.ChalanaSolution
import com.ankh.sutrasaga.domain.models.ChalanaStep
import com.ankh.sutrasaga.domain.models.DecompositionStep
import com.ankh.sutrasaga.domain.models.DerivativeExpression
import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.DiscriminantValue
import com.ankh.sutrasaga.domain.models.QuadraticForChalana
import com.ankh.sutrasaga.domain.models.SutraProblem
import java.util.UUID
import kotlin.math.sqrt
import kotlin.random.Random

class ChalanaKalanabhyamGenerator(
    private val random: Random = Random.Default
) : SutraProblemGenerator {

    override val sutraName: String = "Chalana-Kalanabhyam"

    override fun generateProblem(difficultyTier: DifficultyTier): SutraProblem {
        val problem = generateChalanaProblem(difficultyTier)
        return convertToSutraProblem(problem)
    }

    override fun generateSpecificProblem(operand: Long): SutraProblem {
        val problem = buildValidProblem(1, -5, 6, DifficultyTier.TIER_1_EASY)
        return convertToSutraProblem(problem)
    }

    override fun generateProblemSet(count: Int, difficultyTier: DifficultyTier): List<SutraProblem> {
        val list = mutableListOf<SutraProblem>()
        repeat(count) {
            list.add(generateProblem(difficultyTier))
        }
        return list
    }

    fun generateChalanaProblem(tier: DifficultyTier): ChalanaProblem {
        return when (tier) {
            DifficultyTier.TIER_1_EASY -> {
                val r1 = random.nextLong(-6, 7)
                val r2 = random.nextLong(-6, 7)
                val a = 1L
                val b = -(r1 + r2)
                val c = r1 * r2
                buildValidProblem(a, b, c, tier)
            }
            DifficultyTier.TIER_2_HARD -> {
                val isSpecial = random.nextInt(10)
                when {
                    isSpecial == 0 -> buildValidProblem(1, 2, 5, tier) // D < 0
                    isSpecial == 1 -> buildValidProblem(2, -1, -12, tier) // D not square
                    else -> {
                        val r1 = random.nextLong(-5, 6)
                        val r2 = random.nextLong(-5, 6)
                        val a = random.nextLong(2, 4)
                        val b = -a * (r1 + r2)
                        val c = a * r1 * r2
                        buildValidProblem(a, b, c, tier)
                    }
                }
            }
        }
    }

    fun buildValidProblem(a: Long, b: Long, c: Long, tier: DifficultyTier): ChalanaProblem {
        val sol = solveQuadratic(a, b, c)
        val primaryRoot = sol.roots.firstOrNull() ?: 0L
        val secondaryRoot = sol.roots.lastOrNull() ?: primaryRoot
        val distractors = generateDistractors(primaryRoot, secondaryRoot, a, b)

        return ChalanaProblem(
            id = UUID.randomUUID().toString(),
            quadratic = QuadraticForChalana(a, b, c),
            solution = sol,
            difficultyTier = tier,
            distractors = distractors
        )
    }

    fun solveQuadratic(a: Long, b: Long, c: Long): ChalanaSolution {
        if (a == 0L) {
            return ChalanaSolution(
                quadratic = QuadraticForChalana(0, b, c),
                derivative = DerivativeExpression(0, b),
                discriminant = DiscriminantValue(0, false, null),
                roots = emptyList(),
                classification = ChalanaClassification.NOT_QUADRATIC,
                steps = listOf(ChalanaStep(ChalanaOperationType.IDENTIFY_COEFFICIENTS, "A = 0; linear equation, not quadratic."))
            )
        }

        val quad = QuadraticForChalana(a, b, c)
        val deriv = DerivativeExpression(2 * a, b)
        val d = b * b - 4 * a * c

        if (d < 0) {
            val steps = listOf(
                ChalanaStep(ChalanaOperationType.IDENTIFY_COEFFICIENTS, "A = $a, B = $b, C = $c."),
                ChalanaStep(ChalanaOperationType.DERIVE, "f'(x) = ${deriv.toFormattedString()}."),
                ChalanaStep(ChalanaOperationType.COMPUTE_DISCRIMINANT, "D = ($b)² - 4($a)($c) = $d < 0.")
            )
            return ChalanaSolution(
                quadratic = quad,
                derivative = deriv,
                discriminant = DiscriminantValue(d, false, null),
                roots = emptyList(),
                classification = ChalanaClassification.NO_REAL_ROOTS,
                steps = steps
            )
        }

        val s = sqrt(d.toDouble()).toLong()
        val isSquare = (s * s == d)

        if (!isSquare) {
            val steps = listOf(
                ChalanaStep(ChalanaOperationType.IDENTIFY_COEFFICIENTS, "A = $a, B = $b, C = $c."),
                ChalanaStep(ChalanaOperationType.DERIVE, "f'(x) = ${deriv.toFormattedString()}."),
                ChalanaStep(ChalanaOperationType.COMPUTE_DISCRIMINANT, "D = $d (not a perfect square).")
            )
            return ChalanaSolution(
                quadratic = quad,
                derivative = deriv,
                discriminant = DiscriminantValue(d, false, null),
                roots = emptyList(),
                classification = ChalanaClassification.IRRATIONAL_ROOTS_DEFERRED,
                steps = steps
            )
        }

        if (d == 0L) {
            val root = -b / (2 * a)
            val steps = listOf(
                ChalanaStep(ChalanaOperationType.IDENTIFY_COEFFICIENTS, "A = $a, B = $b, C = $c."),
                ChalanaStep(ChalanaOperationType.DERIVE, "f'(x) = ${deriv.toFormattedString()}."),
                ChalanaStep(ChalanaOperationType.COMPUTE_DISCRIMINANT, "D = 0."),
                ChalanaStep(ChalanaOperationType.SOLVE, "${deriv.toFormattedString()} = 0 ⟹ x = $root."),
                ChalanaStep(ChalanaOperationType.VERIFY, "f($root) = 0 and f'($root)² = 0 = D.")
            )
            return ChalanaSolution(
                quadratic = quad,
                derivative = deriv,
                discriminant = DiscriminantValue(0, true, 0),
                roots = listOf(root, root),
                classification = ChalanaClassification.REPEATED_REAL_ROOT,
                steps = steps
            )
        }

        // D > 0 and perfect square
        val num1 = -b + s
        val num2 = -b - s
        val denom = 2 * a

        if (num1 % denom != 0L || num2 % denom != 0L) {
            return ChalanaSolution(
                quadratic = quad,
                derivative = deriv,
                discriminant = DiscriminantValue(d, true, s),
                roots = emptyList(),
                classification = ChalanaClassification.IRRATIONAL_ROOTS_DEFERRED,
                steps = listOf(ChalanaStep(ChalanaOperationType.COMPUTE_DISCRIMINANT, "Roots are non-integer fractions."))
            )
        }

        val r1 = num1 / denom
        val r2 = num2 / denom

        val steps = listOf(
            ChalanaStep(ChalanaOperationType.IDENTIFY_COEFFICIENTS, "A = $a, B = $b, C = $c."),
            ChalanaStep(ChalanaOperationType.DERIVE, "f'(x) = ${deriv.toFormattedString()}."),
            ChalanaStep(ChalanaOperationType.COMPUTE_DISCRIMINANT, "D = ($b)² - 4($a)($c) = $d."),
            ChalanaStep(ChalanaOperationType.TAKE_SQUARE_ROOT, "√D = $s."),
            ChalanaStep(ChalanaOperationType.SET_PLUS_BRANCH, "${deriv.toFormattedString()} = +$s ⟹ x = $r1."),
            ChalanaStep(ChalanaOperationType.SET_MINUS_BRANCH, "${deriv.toFormattedString()} = -$s ⟹ x = $r2."),
            ChalanaStep(ChalanaOperationType.VERIFY, "f($r1) = 0, f($r2) = 0, and f'(r)² = D.")
        )

        return ChalanaSolution(
            quadratic = quad,
            derivative = deriv,
            discriminant = DiscriminantValue(d, true, s),
            roots = listOf(r1, r2),
            classification = ChalanaClassification.TWO_REAL_ROOTS,
            steps = steps
        )
    }

    private fun convertToSutraProblem(ch: ChalanaProblem): SutraProblem {
        val quadStr = ch.quadratic.toFormattedString()
        val primaryRoot = ch.solution.roots.firstOrNull() ?: 0L
        val secondaryRoot = ch.solution.roots.lastOrNull() ?: primaryRoot

        val decSteps = ch.solution.steps.mapIndexed { idx, s ->
            DecompositionStep(
                stepNumber = idx + 1,
                label = s.type.name,
                formulaDisplay = s.description,
                stepResult = s.type.name,
                explanation = s.description
            )
        }

        return SutraProblem(
            id = ch.id,
            sutraName = sutraName,
            questionText = "Solve $quadStr via derivative relation f'(x) = ±√D (Chalana-Kalanabhyam)",
            operand = primaryRoot,
            correctAnswer = primaryRoot,
            expectedSecondaryAnswer = secondaryRoot,
            prefixPart = ch.quadratic.a,
            incrementedPrefix = ch.quadratic.b,
            prefixProduct = ch.quadratic.c,
            appendedSuffix = ch.solution.discriminant.value.toString(),
            decompositionSteps = decSteps,
            distractors = ch.distractors,
            difficultyTier = ch.difficultyTier,
            answerFormat = AnswerFormat.ORDERED_PAIR
        )
    }

    private fun generateDistractors(r1: Long, r2: Long, a: Long, b: Long): List<Long> {
        val set = mutableSetOf<Long>()
        // Common mistake 1: sign flip
        set.add(-r1)
        set.add(-r2)
        // Common mistake 2: dividing by A instead of 2A
        set.add(r1 * 2)
        set.add(r2 * 2)

        var delta = 1L
        while (set.size < 4) {
            val candidate = r1 + delta
            if (candidate != r1 && candidate != r2) {
                set.add(candidate)
            }
            delta = if (delta > 0) -delta else (-delta + 1L)
        }

        return set.take(4)
    }
}
