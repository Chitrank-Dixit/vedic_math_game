package com.ank.sutrasaga.domain.validation

import com.ank.sutrasaga.domain.models.InteractiveHandshake

/**
 * Mathematical breakdown of 2-digit by 2-digit Urdhva-Tiryagbhyam multiplication.
 */
data class Urdhva2x2Steps(
    val a: Long,
    val b: Long,
    val a1: Long,
    val a0: Long,
    val b1: Long,
    val b0: Long,
    val rawUnits: Long,
    val unitDigit: Long,
    val carry1: Long,
    val crosswiseSum: Long,
    val rawCrosswise: Long,
    val tensDigit: Long,
    val carry2: Long,
    val tensProduct: Long,
    val hundredsPart: Long,
    val finalProduct: Long
)

/**
 * Mathematical breakdown of Nikhilam Navatashcaramam Dashatah near-base multiplication.
 *
 * Applicable for numbers close to a power-of-10 base (10, 100, 1000, ...).
 * Mathematical formulation:
 * For base B:
 *   a = B + devA   (where devA = a - B)
 *   b = B + devB   (where devB = b - B)
 *
 * Step 1: Deviations and Deficiencies:
 *   devA = a - B (negative when a < B, positive when a > B)
 *   devB = b - B
 *   deficiencyA = B - a = -devA
 *   deficiencyB = B - b = -devB
 *
 * Step 2: Cross-addition (Left Part):
 *   crossLeft = a + devB == b + devA == a + b - B
 *
 * Step 3: Vertical product of deviations (Right Part):
 *   rawRight = devA * devB
 *
 * Step 4: Normalization (Carry / Borrow):
 *   - If rawRight >= base: carry = rawRight / base, adjustedRight = rawRight % base, adjustedLeft = crossLeft + carry.
 *   - If rawRight < 0 (numbers on opposite sides of base):
 *       borrow = (-rawRight + base - 1) / base
 *       adjustedRight = rawRight + borrow * base
 *       adjustedLeft = crossLeft - borrow
 *   - Otherwise: carry = 0, borrow = 0, adjustedLeft = crossLeft, adjustedRight = rawRight.
 *
 * Step 5: Final Product:
 *   finalProduct = adjustedLeft * base + adjustedRight == a * b
 */
data class NikhilamSteps(
    val a: Long,
    val b: Long,
    val base: Long,
    val devA: Long,
    val devB: Long,
    val crossLeft: Long,
    val rawRight: Long,
    val carry: Long,
    val borrow: Long,
    val adjustedLeft: Long,
    val adjustedRight: Long,
    val finalProduct: Long
) {
    /** Deficiency of a from base: base - a = -devA (positive when a < base). */
    val deficiencyA: Long get() = -devA

    /** Deficiency of b from base: base - b = -devB (positive when b < base). */
    val deficiencyB: Long get() = -devB
}

/**
 * VedicMathValidator — Validates mathematical accuracy of demonstrated shortcuts
 * and verifies interactive student handshake answers.
 *
 * Overflow Safety Policy:
 * All arithmetic operations (multiplication, addition, subtraction, squaring, negation)
 * use checked arithmetic (e.g. [Math.multiplyExact], [Math.addExact], [Math.subtractExact])
 * wrapped in [safeCheck]. Any overflow or invalid numeric parse fails closed and immediately
 * returns false, guaranteeing no silent wraparound or false-positive validations.
 */
object VedicMathValidator {

    /**
     * Executes mathematical validation inside an overflow-safe boundary.
     * If any intermediate or final arithmetic operation overflows (producing [ArithmeticException])
     * or fails numeric conversion ([NumberFormatException]), it returns false (fail-closed)
     * rather than wrapping silently.
     */
    inline fun safeCheck(block: () -> Boolean): Boolean {
        return try {
            block()
        } catch (_: ArithmeticException) {
            false
        } catch (_: NumberFormatException) {
            false
        }
    }

    /**
     * Verifies squaring a number ending in 5: (10a + 5)² = 100(a)(a+1) + 25.
     */
    fun verifySquaringEndingIn5(n: Long, expected: Long): Boolean = safeCheck {
        if (n <= 0L || expected <= 0L || n % 10L != 5L) return false
        val prefix = n / 10L
        val prefixPlusOne = Math.addExact(prefix, 1L)
        val prefixProduct = Math.multiplyExact(prefix, prefixPlusOne)
        val hundred = Math.multiplyExact(prefixProduct, 100L)
        val computed = Math.addExact(hundred, 25L)
        val nSquared = Math.multiplyExact(n, n)
        computed == expected && nSquared == expected
    }

    /**
     * Validates whether [base] is a canonical power-of-10 base (10, 100, 1000, 10000, ...).
     * Non-powers of 10 (e.g. 50, 0, negative numbers) return false.
     */
    fun isValidNikhilamBase(base: Long): Boolean {
        if (base < 10L) return false
        var temp = base
        while (temp % 10L == 0L) {
            temp /= 10L
        }
        return temp == 1L
    }

    /**
     * Computes the complete Nikhilam near-base multiplication decomposition for operands [a] and [b].
     *
     * Validates that:
     * - Operands [a] and [b] are strictly positive (> 0).
     * - [base] is a valid power-of-10 base (10, 100, 1000, ...).
     * - Deviations, cross-addition, carry/borrow normalization, and product evaluate without overflow.
     *
     * Returns a valid [NikhilamSteps] instance, or null if inputs are invalid or arithmetic overflows.
     */
    fun calculateNikhilamSteps(a: Long, b: Long, base: Long = 100L): NikhilamSteps? {
        if (a <= 0L || b <= 0L || !isValidNikhilamBase(base)) return null
        return try {
            val devA = Math.subtractExact(a, base)
            val devB = Math.subtractExact(b, base)

            val crossLeftA = Math.addExact(a, devB)
            val crossLeftB = Math.addExact(b, devA)
            if (crossLeftA != crossLeftB) return null

            val crossLeft = crossLeftA
            val rawRight = Math.multiplyExact(devA, devB)

            var carry = 0L
            var borrow = 0L
            var adjLeft = crossLeft
            var adjRight = rawRight

            if (rawRight >= base) {
                carry = rawRight / base
                adjRight = rawRight % base
                adjLeft = Math.addExact(crossLeft, carry)
            } else if (rawRight < 0L) {
                // When devA and devB have opposite signs (numbers on opposite sides of base),
                // rawRight is negative. We borrow from the left side.
                val negRight = Math.negateExact(rawRight)
                val baseMinusOne = Math.subtractExact(base, 1L)
                val numerator = Math.addExact(negRight, baseMinusOne)
                borrow = numerator / base
                val borrowScaled = Math.multiplyExact(borrow, base)
                adjRight = Math.addExact(rawRight, borrowScaled)
                adjLeft = Math.subtractExact(crossLeft, borrow)
            }

            val scaledLeft = Math.multiplyExact(adjLeft, base)
            val finalProduct = Math.addExact(scaledLeft, adjRight)
            val expectedProduct = Math.multiplyExact(a, b)

            if (finalProduct != expectedProduct) return null

            NikhilamSteps(
                a = a,
                b = b,
                base = base,
                devA = devA,
                devB = devB,
                crossLeft = crossLeft,
                rawRight = rawRight,
                carry = carry,
                borrow = borrow,
                adjustedLeft = adjLeft,
                adjustedRight = adjRight,
                finalProduct = finalProduct
            )
        } catch (_: ArithmeticException) {
            null
        }
    }

    /**
     * Verifies Nikhilam base multiplication of [a] and [b] against an [expected] final product.
     *
     * Validates that:
     * 1. Operands [a] and [b] are positive, and [expected] is positive.
     * 2. [base] is a valid power-of-10 base (10, 100, 1000, ...).
     * 3. The procedural Nikhilam near-base decomposition succeeds without overflow.
     * 4. The procedurally computed product equals [expected].
     * 5. An independent exact multiplication of [a] and [b] equals [expected].
     *
     * Fails closed on any mismatch, invalid base, negative/zero operand, or arithmetic overflow.
     */
    fun verifyNikhilamMultiplication(a: Long, b: Long, expected: Long, base: Long = 100L): Boolean = safeCheck {
        if (expected <= 0L) return false
        val steps = calculateNikhilamSteps(a, b, base) ?: return false
        val independentProduct = Math.multiplyExact(a, b)
        steps.finalProduct == expected && independentProduct == expected
    }

    /**
     * Verifies a complete [NikhilamSteps] breakdown against operands [a] and [b] and [expected] product.
     */
    fun verifyNikhilamDecomposition(a: Long, b: Long, steps: NikhilamSteps, expected: Long): Boolean = safeCheck {
        if (expected <= 0L) return false
        val computedSteps = calculateNikhilamSteps(a, b, steps.base) ?: return false
        val independentProduct = Math.multiplyExact(a, b)
        steps == computedSteps && steps.finalProduct == expected && independentProduct == expected
    }

    /**
     * Verifies individual learner-submitted intermediate steps of Nikhilam multiplication:
     * - [expectedDevA]: Claimed deviation of a from base (a - base)
     * - [expectedDevB]: Claimed deviation of b from base (b - base)
     * - [expectedLeft]: Claimed normalized left part after carry/borrow
     * - [expectedRight]: Claimed normalized right part after carry/borrow
     * - [expectedProduct]: Claimed final product
     */
    fun verifyNikhilamDecomposition(
        a: Long,
        b: Long,
        expectedDevA: Long,
        expectedDevB: Long,
        expectedLeft: Long,
        expectedRight: Long,
        expectedProduct: Long,
        base: Long = 100L
    ): Boolean = safeCheck {
        if (expectedProduct <= 0L) return false
        val steps = calculateNikhilamSteps(a, b, base) ?: return false
        val independentProduct = Math.multiplyExact(a, b)
        steps.devA == expectedDevA &&
                steps.devB == expectedDevB &&
                steps.adjustedLeft == expectedLeft &&
                steps.adjustedRight == expectedRight &&
                steps.finalProduct == expectedProduct &&
                independentProduct == expectedProduct
    }

    /**
     * Verifies the algebraic identity behind Nikhilam base multiplication:
     *   (Base - d1)(Base - d2) ≡ (Base - d1 - d2) * Base + (d1 * d2)
     * where d1 = Base - a and d2 = Base - b.
     *
     * WARNING: This method checks algebraic identity consistency only.
     * It does NOT receive or check an expected product or learner-submitted answer!
     * Because the identity is an algebraic tautology (always equal to a * b for positive numbers),
     * passing this check does NOT prove that a learner's answer or tutorial explanation is correct.
     * Callers validating final answers or learner inputs MUST use [verifyNikhilamMultiplication]
     * or [verifyNikhilamDecomposition].
     */
    fun verifyNikhilamIdentity(a: Long, b: Long, base: Long = 100L): Boolean = safeCheck {
        if (a <= 0L || b <= 0L || !isValidNikhilamBase(base)) return false
        val d1 = Math.subtractExact(base, a)
        val d2 = Math.subtractExact(base, b)
        val leftPart = Math.subtractExact(a, d2)
        val rightPart = Math.multiplyExact(d1, d2)
        val scaledLeft = Math.multiplyExact(leftPart, base)
        val computed = Math.addExact(scaledLeft, rightPart)
        val actualProduct = Math.multiplyExact(a, b)
        computed == actualProduct
    }

    /**
     * Validates whether an integer is within the supported 2-digit range (10..99)
     * for 2x2 Urdhva-Tiryagbhyam multiplication.
     */
    fun isValidTwoDigitOperand(n: Long): Boolean = n in 10L..99L

    /**
     * Computes the complete vertical-and-crosswise decomposition for two 2-digit operands.
     * Returns null if either operand is not a valid 2-digit number (10..99).
     *
     * Procedure:
     * - Step 1: Units vertical product: a0 * b0 -> unitDigit = rawUnits % 10, carry1 = rawUnits / 10
     * - Step 2: Crosswise sum: (a1 * b0) + (a0 * b1) -> rawCrosswise = crosswiseSum + carry1
     *           tensDigit = rawCrosswise % 10, carry2 = rawCrosswise / 10
     * - Step 3: Tens vertical product plus carry: (a1 * b1) + carry2 -> rawTens
     * - Step 4: Product = rawTens * 100 + tensDigit * 10 + unitDigit == a * b
     */
    fun calculateUrdhva2x2Steps(a: Long, b: Long): Urdhva2x2Steps? {
        if (!isValidTwoDigitOperand(a) || !isValidTwoDigitOperand(b)) return null

        val a1 = a / 10L
        val a0 = a % 10L
        val b1 = b / 10L
        val b0 = b % 10L

        val rawUnits = a0 * b0
        val d0 = rawUnits % 10L
        val c1 = rawUnits / 10L

        val crossSum = (a1 * b0) + (a0 * b1)
        val rawCross = crossSum + c1
        val d1 = rawCross % 10L
        val c2 = rawCross / 10L

        val rawTens = (a1 * b1) + c2
        val computed = (rawTens * 100L) + (d1 * 10L) + d0

        return Urdhva2x2Steps(
            a = a,
            b = b,
            a1 = a1,
            a0 = a0,
            b1 = b1,
            b0 = b0,
            rawUnits = rawUnits,
            unitDigit = d0,
            carry1 = c1,
            crosswiseSum = crossSum,
            rawCrosswise = rawCross,
            tensDigit = d1,
            carry2 = c2,
            tensProduct = a1 * b1,
            hundredsPart = rawTens,
            finalProduct = computed
        )
    }

    /**
     * Verifies that operands a and b are valid 2-digit numbers and that the
     * vertical-and-crosswise procedure mathematically computes their exact product.
     *
     * Replaces the former weak check ((a * b) > 0) with genuine procedural verification.
     * Fails closed for zero, negative numbers, single-digit numbers, and numbers >= 100.
     */
    fun verifyUrdhva2x2(a: Long, b: Long): Boolean {
        val steps = calculateUrdhva2x2Steps(a, b) ?: return false
        return steps.finalProduct == (a * b)
    }

    /**
     * Verifies 2-digit by 2-digit Urdhva-Tiryagbhyam multiplication against an expected product.
     * Fails if operands are outside 10..99, or if expected != a * b, or if the procedure is inconsistent.
     */
    fun verifyUrdhva2x2(a: Long, b: Long, expected: Long): Boolean {
        val steps = calculateUrdhva2x2Steps(a, b) ?: return false
        return steps.finalProduct == expected && (a * b) == expected
    }

    /**
     * Verifies 2-digit by 2-digit Urdhva-Tiryagbhyam multiplication with intermediate step assertions:
     * - expectedCrosswiseSum: (a1 * b0) + (a0 * b1)
     * - expectedCarry1: (a0 * b0) / 10
     * - expectedCarry2: ((a1 * b0) + (a0 * b1) + expectedCarry1) / 10
     * - expectedProduct: a * b
     */
    fun verifyUrdhva2x2(
        a: Long,
        b: Long,
        expectedCrosswiseSum: Long,
        expectedCarry1: Long,
        expectedCarry2: Long,
        expectedProduct: Long
    ): Boolean {
        val steps = calculateUrdhva2x2Steps(a, b) ?: return false
        if (steps.carry1 != expectedCarry1) return false
        if (steps.crosswiseSum != expectedCrosswiseSum) return false
        if (steps.carry2 != expectedCarry2) return false
        return steps.finalProduct == expectedProduct && (a * b) == expectedProduct
    }

    /**
     * Verifies the crosswise sum step specifically (e.g. for learner handshake inputs):
     * Crosswise sum = (a1 * b0) + (a0 * b1).
     */
    fun verifyUrdhvaCrosswiseSum(a: Long, b: Long, claimedCrosswiseSum: Long): Boolean {
        val steps = calculateUrdhva2x2Steps(a, b) ?: return false
        return steps.crosswiseSum == claimedCrosswiseSum
    }

    /**
     * Verifies base subtraction: Base - Sub = Expected.
     */
    fun verifyBaseSubtraction(base: Long, sub: Long, expected: Long): Boolean = safeCheck {
        if (base <= 0L || sub < 0L || expected < 0L) return false
        val diff = Math.subtractExact(base, sub)
        diff == expected
    }

    /**
     * Verifies Ekanyunena multiplication: multiplicand * (10^k - 1) = (multiplicand - 1) * 10^k + ((10^k - 1) - (multiplicand - 1)).
     */
    fun verifyEkanyunenaMultiplication(multiplicand: Long, nines: Long, expected: Long): Boolean = safeCheck {
        if (multiplicand <= 0L || nines <= 0L || expected <= 0L) return false
        val lhs = Math.subtractExact(multiplicand, 1L)
        val rhs = Math.subtractExact(nines, lhs)
        val multiplier = Math.addExact(nines, 1L)
        val scaledLhs = Math.multiplyExact(lhs, multiplier)
        val computed = Math.addExact(scaledLhs, rhs)
        val actualProduct = Math.multiplyExact(multiplicand, nines)
        computed == expected && actualProduct == expected
    }

    /**
     * Verifies Yavadunam near-base squaring: (Base - d)² = (Base - 2d) * Base + d².
     */
    fun verifyYavadunamSquaring(n: Long, base: Long, expected: Long): Boolean = safeCheck {
        if (n <= 0L || base <= 0L || expected <= 0L) return false
        val rawDiff = Math.subtractExact(base, n)
        val diff = if (rawDiff < 0L) Math.negateExact(rawDiff) else rawDiff
        val lhs = if (n < base) Math.subtractExact(n, diff) else Math.addExact(n, diff)
        val rhs = Math.multiplyExact(diff, diff)
        val scaledLhs = Math.multiplyExact(lhs, base)
        val computed = Math.addExact(scaledLhs, rhs)
        val actualSquare = Math.multiplyExact(n, n)
        computed == expected && actualSquare == expected
    }

    /**
     * Verifies Paravartya Yojayet division: Dividend = Divisor * Quotient + Remainder,
     * where 0 <= Remainder < Divisor.
     */
    fun verifyParavartyaDivision(
        dividend: Long,
        divisor: Long,
        expectedQuotient: Long,
        expectedRemainder: Long
    ): Boolean = safeCheck {
        if (divisor <= 0L || expectedRemainder < 0L || expectedRemainder >= divisor) return false
        if (dividend < 0L || expectedQuotient < 0L) return false
        val product = Math.multiplyExact(divisor, expectedQuotient)
        val computed = Math.addExact(product, expectedRemainder)
        computed == dividend && (dividend / divisor) == expectedQuotient && (dividend % divisor) == expectedRemainder
    }

    /**
     * Verifies Anurupye Shunyamanyat linear system solution:
     * If a1/a2 == c1/c2, then y = 0 and x = c1/a1.
     * If b1/b2 == c1/c2, then x = 0 and y = c1/b1.
     */
    fun verifyAnurupyeSystem(
        a1: Long, b1: Long, c1: Long,
        a2: Long, b2: Long, c2: Long,
        expectedX: Long, expectedY: Long
    ): Boolean = safeCheck {
        val a1b2 = Math.multiplyExact(a1, b2)
        val a2b1 = Math.multiplyExact(a2, b1)
        val det = Math.subtractExact(a1b2, a2b1)
        if (det == 0L) return false

        val a1c2 = Math.multiplyExact(a1, c2)
        val a2c1 = Math.multiplyExact(a2, c1)
        val xRatioMatch = (a1c2 == a2c1) && a1 != 0L && a2 != 0L

        val b1c2 = Math.multiplyExact(b1, c2)
        val b2c1 = Math.multiplyExact(b2, c1)
        val yRatioMatch = (b1c2 == b2c1) && b1 != 0L && b2 != 0L

        if (xRatioMatch) {
            if (expectedY != 0L) return false
            val a1x = Math.multiplyExact(a1, expectedX)
            if (a1x != c1) return false
        } else if (yRatioMatch) {
            if (expectedX != 0L) return false
            val b1y = Math.multiplyExact(b1, expectedY)
            if (b1y != c1) return false
        } else {
            return false
        }

        val eq1Term1 = Math.multiplyExact(a1, expectedX)
        val eq1Term2 = Math.multiplyExact(b1, expectedY)
        val eq1Sum = Math.addExact(eq1Term1, eq1Term2)

        val eq2Term1 = Math.multiplyExact(a2, expectedX)
        val eq2Term2 = Math.multiplyExact(b2, expectedY)
        val eq2Sum = Math.addExact(eq2Term1, eq2Term2)

        eq1Sum == c1 && eq2Sum == c2
    }

    /**
     * Verifies Saṅkalana-Vyavakalanābhyām solution for symmetric 2x2 linear systems:
     * a1*x + b1*y = c1
     * a2*x + b2*y = c2
     * where |a1| == |b2| and |b1| == |a2|.
     * Adding equations yields (a1+a2)x + (b1+b2)y = c1+c2.
     * Subtracting equations yields (a1-a2)x + (b1-b2)y = c1-c2.
     */
    fun verifySankalanaSystem(
        a1: Long, b1: Long, c1: Long,
        a2: Long, b2: Long, c2: Long,
        expectedX: Long, expectedY: Long
    ): Boolean = safeCheck {
        if (a1 == Long.MIN_VALUE || b1 == Long.MIN_VALUE || a2 == Long.MIN_VALUE || b2 == Long.MIN_VALUE) return false
        if (c1 == Long.MIN_VALUE || c2 == Long.MIN_VALUE || expectedX == Long.MIN_VALUE || expectedY == Long.MIN_VALUE) return false

        if (Math.abs(a1) != Math.abs(b2) || Math.abs(b1) != Math.abs(a2)) return false
        val a1b2 = Math.multiplyExact(a1, b2)
        val a2b1 = Math.multiplyExact(a2, b1)
        val det = Math.subtractExact(a1b2, a2b1)
        if (det == 0L) return false

        val eq1Term1 = Math.multiplyExact(a1, expectedX)
        val eq1Term2 = Math.multiplyExact(b1, expectedY)
        val eq1Sum = Math.addExact(eq1Term1, eq1Term2)
        if (eq1Sum != c1) return false

        val eq2Term1 = Math.multiplyExact(a2, expectedX)
        val eq2Term2 = Math.multiplyExact(b2, expectedY)
        val eq2Sum = Math.addExact(eq2Term1, eq2Term2)
        if (eq2Sum != c2) return false

        val addSum = Math.addExact(a1, a2)
        val addYSum = Math.addExact(b1, b2)
        val subDiff = Math.subtractExact(a1, a2)
        val subYDiff = Math.subtractExact(b1, b2)

        val cSum = Math.addExact(c1, c2)
        val cDiff = Math.subtractExact(c1, c2)

        val xySum = Math.addExact(expectedX, expectedY)
        val xyDiff = Math.subtractExact(expectedX, expectedY)

        val addValid = (addSum != 0L && addSum == addYSum && cSum == Math.multiplyExact(addSum, xySum)) ||
                (addSum != 0L && addSum == -addYSum && cSum == Math.multiplyExact(addSum, xyDiff))
        val subValid = (subDiff != 0L && subDiff == subYDiff && cDiff == Math.multiplyExact(subDiff, xySum)) ||
                (subDiff != 0L && subDiff == -subYDiff && cDiff == Math.multiplyExact(subDiff, xyDiff))

        addValid && subValid
    }

    /**
     * Verifies Shunyam Samyasamuccaye common factor solution (Family A):
     * k1 * (a*x + b) = k2 * (a*x + b) with k1 != k2.
     * Since k1 != k2, (a*x + b) must equal 0, so x = -b / a.
     */
    fun verifyShunyamCommonFactor(k1: Long, a: Long, b: Long, k2: Long, expectedX: Long): Boolean = safeCheck {
        if (k1 == k2 || a == 0L) return false
        val ax = Math.multiplyExact(a, expectedX)
        val inner = Math.addExact(ax, b)
        if (inner != 0L) return false
        val lhs = Math.multiplyExact(k1, inner)
        val rhs = Math.multiplyExact(k2, inner)
        lhs == rhs && lhs == 0L
    }

    /**
     * Verifies Puranapuranabhyam completing the square:
     * For A*x² + B*x + C = 0 with A != 0:
     * Completion term T_comp = (B / (2*A))² = B² / (4*A²)
     * And roots r satisfy A*r² + B*r + C = 0.
     */
    fun verifyPuranapuranabhyam(
        a: Long,
        b: Long,
        c: Long,
        expectedCompletionNum: Long,
        expectedCompletionDen: Long = 1L,
        root1: Long,
        root2: Long
    ): Boolean = safeCheck {
        if (a == 0L || expectedCompletionDen <= 0L) return false

        // b^2 - 4ac
        val bSquared = Math.multiplyExact(b, b)
        val fourA = Math.multiplyExact(4L, a)
        val fourAC = Math.multiplyExact(fourA, c)
        val disc = Math.subtractExact(bSquared, fourAC)
        if (disc < 0L) return false

        // (b / 2a)^2 = b^2 / (4a^2)
        val compNum = bSquared
        val twoA = Math.multiplyExact(2L, a)
        val compDen = Math.multiplyExact(twoA, twoA)

        val leftCross = Math.multiplyExact(compNum, expectedCompletionDen)
        val rightCross = Math.multiplyExact(compDen, expectedCompletionNum)
        if (leftCross != rightCross) return false

        // Check root 1: a * r1^2 + b * r1 + c == 0
        val r1Sq = Math.multiplyExact(root1, root1)
        val aR1Sq = Math.multiplyExact(a, r1Sq)
        val bR1 = Math.multiplyExact(b, root1)
        val root1Sum = Math.addExact(Math.addExact(aR1Sq, bR1), c)
        if (root1Sum != 0L) return false

        // Check root 2: a * r2^2 + b * r2 + c == 0
        val r2Sq = Math.multiplyExact(root2, root2)
        val aR2Sq = Math.multiplyExact(a, r2Sq)
        val bR2 = Math.multiplyExact(b, root2)
        val root2Sum = Math.addExact(Math.addExact(aR2Sq, bR2), c)
        root2Sum == 0L
    }

    /**
     * Verifies Vyashtisamashtih symmetric product around an average:
     * p * q = (A - d)(A + d) = A² - d²
     * where A = (p + q) / 2 and d = |q - p| / 2.
     */
    fun verifyVyashtisamashtih(
        p: Long,
        q: Long,
        expectedAverage: Long,
        expectedDeviation: Long,
        expectedProduct: Long
    ): Boolean = safeCheck {
        if (p == Long.MIN_VALUE || q == Long.MIN_VALUE) return false
        val sum = Math.addExact(p, q)
        if (sum % 2L != 0L) return false
        val avg = sum / 2L

        val diff = Math.subtractExact(q, p)
        val absDiff = if (diff < 0L) Math.negateExact(diff) else diff
        val dev = absDiff / 2L

        if (avg != expectedAverage || dev != expectedDeviation) return false

        val avgSq = Math.multiplyExact(avg, avg)
        val devSq = Math.multiplyExact(dev, dev)
        val diffOfSquares = Math.subtractExact(avgSq, devSq)
        val pq = Math.multiplyExact(p, q)

        diffOfSquares == expectedProduct && pq == expectedProduct
    }

    /**
     * Verifies Shesanyankena Charamena remainder tracking and recurring decimal expansion:
     * For fraction p / q:
     * Verifies remainder sequence and repetend digits.
     */
    fun verifyShesanyankenaExpansion(
        p: Long,
        q: Long,
        expectedPeriodLength: Int,
        expectedRepetend: String
    ): Boolean = safeCheck {
        if (q <= 0L || p <= 0L || expectedPeriodLength <= 0) return false
        val remainders = mutableListOf<Long>()
        val digits = StringBuilder()
        var rem = p % q
        val seen = mutableMapOf<Long, Int>()

        while (rem != 0L && rem !in seen) {
            seen[rem] = remainders.size
            remainders.add(rem)
            val v = Math.multiplyExact(rem, 10L)
            val digit = v / q
            digits.append(digit)
            rem = v % q
        }

        if (rem == 0L) return false // not recurring
        val cycleStart = seen[rem] ?: return false
        val cycleLength = remainders.size - cycleStart
        val cycleDigits = digits.substring(cycleStart)

        cycleLength == expectedPeriodLength && cycleDigits == expectedRepetend
    }

    /**
     * Verifies Sopāntyadvayamantyam digit-pairing multiplication for multipliers 12..19:
     * Multiplicand M, Multiplier 10 + N.
     * Verifies raw values R[i] = d[i+1] + N * d[i] and carry-resolved product.
     */
    fun verifySopantyadvayamantyam(
        multiplicand: Long,
        multiplier: Int,
        expectedRawValues: List<Int>,
        expectedProduct: Long
    ): Boolean = safeCheck {
        if (multiplicand <= 0L || multiplier !in 12..19 || expectedProduct <= 0L) return false
        val n = multiplier - 10
        val origDigits = multiplicand.toString().map { it.digitToInt() }
        val sandwiched = listOf(0) + origDigits + listOf(0)

        val rawValues = mutableListOf<Int>()
        for (i in 0 until origDigits.size + 1) {
            val d = sandwiched[i + 1]
            val prev = sandwiched[i]
            val term = Math.multiplyExact(n, prev)
            rawValues.add(Math.addExact(d, term))
        }

        if (rawValues != expectedRawValues) return false

        var currentCarry = 0
        val finalDigitsReversed = mutableListOf<Int>()
        for (i in rawValues.indices.reversed()) {
            val total = Math.addExact(rawValues[i], currentCarry)
            finalDigitsReversed.add(total % 10)
            currentCarry = total / 10
        }
        if (currentCarry > 0) finalDigitsReversed.add(currentCarry)

        val computedString = finalDigitsReversed.reversed().joinToString("")
        val computedProduct = computedString.toLongOrNull() ?: return false
        val expectedActual = Math.multiplyExact(multiplicand, multiplier.toLong())

        computedProduct == expectedProduct && expectedActual == expectedProduct
    }

    /**
     * Verifies Guṇitasamuccayaḥ polynomial factorization consistency and exact expansion:
     * Factors (a1*x + b1) and (a2*x + b2), claimed quadratic (claimedA2*x^2 + claimedA1*x + claimedA0).
     * Verifies factor coefficient sum product, claimed polynomial sum, and exact term-by-term match.
     */
    fun verifyGunitasamuccayah(
        a1: Long, b1: Long,
        a2: Long, b2: Long,
        claimedA2: Long, claimedA1: Long, claimedA0: Long
    ): Boolean = safeCheck {
        val scF1 = Math.addExact(a1, b1)
        val scF2 = Math.addExact(a2, b2)
        val expectedProductSum = Math.multiplyExact(scF1, scF2)
        val actualProductSum = Math.addExact(Math.addExact(claimedA2, claimedA1), claimedA0)

        if (expectedProductSum != actualProductSum) return false

        val exactA2 = Math.multiplyExact(a1, a2)
        val a1b2 = Math.multiplyExact(a1, b2)
        val b1a2 = Math.multiplyExact(b1, a2)
        val exactA1 = Math.addExact(a1b2, b1a2)
        val exactA0 = Math.multiplyExact(b1, b2)

        exactA2 == claimedA2 && exactA1 == claimedA1 && exactA0 == claimedA0
    }

    /**
     * Verifies Guṇakasamuccayaḥ monic quadratic factorization:
     * For x^2 + Bx + C = 0:
     * Verifies that factor pair (p, q) satisfies p * q == C and p + q == B,
     * and that roots r1 = -p, r2 = -q satisfy (-p)^2 + B(-p) + C == 0 and (-q)^2 + B(-q) + C == 0.
     */
    fun verifyGunakasamuccayah(
        b: Long,
        c: Long,
        expectedP: Long,
        expectedQ: Long,
        expectedRoot1: Long,
        expectedRoot2: Long
    ): Boolean = safeCheck {
        if (expectedP == Long.MIN_VALUE || expectedQ == Long.MIN_VALUE) return false
        val pq = Math.multiplyExact(expectedP, expectedQ)
        val pPlusQ = Math.addExact(expectedP, expectedQ)
        if (pq != c || pPlusQ != b) return false

        val negP = Math.negateExact(expectedP)
        val negQ = Math.negateExact(expectedQ)
        if (expectedRoot1 != negP || expectedRoot2 != negQ) return false

        // root1^2 + b * root1 + c
        val r1Sq = Math.multiplyExact(expectedRoot1, expectedRoot1)
        val bR1 = Math.multiplyExact(b, expectedRoot1)
        val eval1 = Math.addExact(Math.addExact(r1Sq, bR1), c)

        // root2^2 + b * root2 + c
        val r2Sq = Math.multiplyExact(expectedRoot2, expectedRoot2)
        val bR2 = Math.multiplyExact(b, expectedRoot2)
        val eval2 = Math.addExact(Math.addExact(r2Sq, bR2), c)

        eval1 == 0L && eval2 == 0L
    }

    /**
     * Verifies Calana-Kalanābhyām quadratic solution via derivative-discriminant relation:
     * For Ax^2 + Bx + C = 0:
     * First derivative f'(x) = 2Ax + B.
     * Discriminant D = B^2 - 4AC.
     * Roots r1 and r2 satisfy 2A*r1 + B = +sqrt(D) and 2A*r2 + B = -sqrt(D).
     * Invariants: f'(r1)^2 == D, f'(r2)^2 == D, f(r1) == 0, f(r2) == 0.
     */
    fun verifyChalanaKalanabhyam(
        a: Long,
        b: Long,
        c: Long,
        claimedD: Long,
        claimedSqrtD: Long,
        expectedRoot1: Long,
        expectedRoot2: Long
    ): Boolean = safeCheck {
        if (a == 0L || claimedSqrtD < 0L) return false

        val bSq = Math.multiplyExact(b, b)
        val fourA = Math.multiplyExact(4L, a)
        val fourAC = Math.multiplyExact(fourA, c)
        val d = Math.subtractExact(bSq, fourAC)

        if (d != claimedD || d < 0L) return false
        val sqrtDSq = Math.multiplyExact(claimedSqrtD, claimedSqrtD)
        if (sqrtDSq != d) return false

        val twoA = Math.multiplyExact(2L, a)
        val twoAR1 = Math.multiplyExact(twoA, expectedRoot1)
        val derivR1 = Math.addExact(twoAR1, b)

        val twoAR2 = Math.multiplyExact(twoA, expectedRoot2)
        val derivR2 = Math.addExact(twoAR2, b)

        val negSqrtD = Math.negateExact(claimedSqrtD)
        val branchMatches = (derivR1 == claimedSqrtD && derivR2 == negSqrtD) ||
                (derivR1 == negSqrtD && derivR2 == claimedSqrtD)
        if (!branchMatches) return false

        // f(r1) = a * r1^2 + b * r1 + c == 0
        val r1Sq = Math.multiplyExact(expectedRoot1, expectedRoot1)
        val aR1Sq = Math.multiplyExact(a, r1Sq)
        val bR1 = Math.multiplyExact(b, expectedRoot1)
        val eval1 = Math.addExact(Math.addExact(aR1Sq, bR1), c)

        // f(r2) = a * r2^2 + b * r2 + c == 0
        val r2Sq = Math.multiplyExact(expectedRoot2, expectedRoot2)
        val aR2Sq = Math.multiplyExact(a, r2Sq)
        val bR2 = Math.multiplyExact(b, expectedRoot2)
        val eval2 = Math.addExact(Math.addExact(aR2Sq, bR2), c)

        val derivR1Sq = Math.multiplyExact(derivR1, derivR1)
        val derivR2Sq = Math.multiplyExact(derivR2, derivR2)

        eval1 == 0L && eval2 == 0L && derivR1Sq == d && derivR2Sq == d
    }

    /**
     * Validates a student's answer against the interactive handshake requirements.
     * Enforces that mathematical truth is solely determined by the authoritative [InteractiveHandshake.expectedAnswer].
     * Fail-closed invariant: UNKNOWN / MISSING / BLANK EXPECTED ANSWER = FAILURE.
     */
    fun evaluateHandshake(handshake: InteractiveHandshake, userAnswer: String): Boolean {
        val expected = handshake.expectedAnswer ?: return false
        if (expected.isBlank()) return false

        val normalizedUser = userAnswer.trim()
        val normalizedExpected = expected.trim()

        return normalizedUser.isNotBlank() && normalizedUser.equals(normalizedExpected, ignoreCase = true)
    }
}
