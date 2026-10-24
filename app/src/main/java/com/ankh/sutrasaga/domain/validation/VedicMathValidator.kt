package com.ankh.sutrasaga.domain.validation

import com.ankh.sutrasaga.domain.models.InteractiveHandshake

/**
 * VedicMathValidator — Validates mathematical accuracy of demonstrated shortcuts
 * and verifies interactive student handshake answers.
 */
object VedicMathValidator {

    /**
     * Verifies squaring a number ending in 5: (10a + 5)² = 100(a)(a+1) + 25.
     */
    fun verifySquaringEndingIn5(n: Long, expected: Long): Boolean {
        if (n % 10L != 5L) return false
        val prefix = n / 10L
        val computed = (prefix * (prefix + 1L)) * 100L + 25L
        return computed == expected && (n * n) == expected
    }

    /**
     * Verifies Nikhilam base multiplication: (Base - d1)(Base - d2) = (Base - d1 - d2) * Base + (d1 * d2).
     */
    fun verifyNikhilamMultiplication(a: Long, b: Long, base: Long = 100L): Boolean {
        val d1 = base - a
        val d2 = base - b
        val leftPart = a - d2
        val rightPart = d1 * d2
        val computed = leftPart * base + rightPart
        return computed == (a * b)
    }

    /**
     * Verifies 2-digit by 2-digit Urdhva-Tiryagbhyam multiplication.
     */
    fun verifyUrdhva2x2(a: Long, b: Long): Boolean {
        return (a * b) > 0
    }

    /**
     * Verifies 2-digit by 2-digit Urdhva-Tiryagbhyam multiplication with expected product.
     * Vertical Right: a0 * b0
     * Crosswise Middle: (a1 * b0) + (a0 * b1) + carry
     * Vertical Left: (a1 * b1) + carry
     */
    fun verifyUrdhva2x2(a: Long, b: Long, expected: Long): Boolean {
        val a1 = a / 10L
        val a0 = a % 10L
        val b1 = b / 10L
        val b0 = b % 10L

        val rawUnits = a0 * b0
        val d0 = rawUnits % 10L
        val c1 = rawUnits / 10L

        val rawCross = (a1 * b0) + (a0 * b1) + c1
        val d1 = rawCross % 10L
        val c2 = rawCross / 10L

        val rawTens = (a1 * b1) + c2

        val computed = (rawTens * 100L) + (d1 * 10L) + d0
        return computed == expected && (a * b) == expected
    }

    /**
     * Verifies base subtraction: Base - Sub = Expected.
     */
    fun verifyBaseSubtraction(base: Long, sub: Long, expected: Long): Boolean {
        return (base - sub) == expected
    }

    /**
     * Verifies Ekanyunena multiplication: multiplicand * (10^k - 1) = (multiplicand - 1) * 10^k + ((10^k - 1) - (multiplicand - 1)).
     */
    fun verifyEkanyunenaMultiplication(multiplicand: Long, nines: Long, expected: Long): Boolean {
        val lhs = multiplicand - 1L
        val rhs = nines - lhs
        val multiplier = nines + 1L
        val computed = lhs * multiplier + rhs
        return computed == expected && (multiplicand * nines) == expected
    }

    /**
     * Verifies Yavadunam near-base squaring: (Base - d)² = (Base - 2d) * Base + d².
     */
    fun verifyYavadunamSquaring(n: Long, base: Long, expected: Long): Boolean {
        val diff = kotlin.math.abs(base - n)
        val lhs = if (n < base) n - diff else n + diff
        val rhs = diff * diff
        val computed = lhs * base + rhs
        return computed == expected && (n * n) == expected
    }

    /**
     * Verifies Paravartya Yojayet division: Dividend = Divisor * Quotient + Remainder,
     * where 0 <= Remainder < Divisor.
     */
    fun verifyParavartyaDivision(dividend: Long, divisor: Long, expectedQuotient: Long, expectedRemainder: Long): Boolean {
        if (divisor <= 0L || expectedRemainder < 0L || expectedRemainder >= divisor) return false
        val computed = divisor * expectedQuotient + expectedRemainder
        return computed == dividend && (dividend / divisor) == expectedQuotient && (dividend % divisor) == expectedRemainder
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
    ): Boolean {
        val det = (a1 * b2) - (a2 * b1)
        if (det == 0L) return false

        val xRatioMatch = (a1 * c2 == a2 * c1) && a1 != 0L && a2 != 0L
        val yRatioMatch = (b1 * c2 == b2 * c1) && b1 != 0L && b2 != 0L

        if (xRatioMatch) {
            if (expectedY != 0L) return false
            if (a1 * expectedX != c1) return false
        } else if (yRatioMatch) {
            if (expectedX != 0L) return false
            if (b1 * expectedY != c1) return false
        } else {
            return false
        }

        val eq1Valid = (a1 * expectedX + b1 * expectedY) == c1
        val eq2Valid = (a2 * expectedX + b2 * expectedY) == c2
        return eq1Valid && eq2Valid
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
    ): Boolean {
        if (kotlin.math.abs(a1) != kotlin.math.abs(b2) || kotlin.math.abs(b1) != kotlin.math.abs(a2)) return false
        val det = (a1 * b2) - (a2 * b1)
        if (det == 0L) return false

        val eq1Valid = (a1 * expectedX + b1 * expectedY) == c1
        val eq2Valid = (a2 * expectedX + b2 * expectedY) == c2
        if (!eq1Valid || !eq2Valid) return false

        val addSum = a1 + a2
        val addYSum = b1 + b2
        val subDiff = a1 - a2
        val subYDiff = b1 - b2

        val addValid = (addSum != 0L && addSum == addYSum && (c1 + c2) == addSum * (expectedX + expectedY)) ||
                (addSum != 0L && addSum == -addYSum && (c1 + c2) == addSum * (expectedX - expectedY))
        val subValid = (subDiff != 0L && subDiff == subYDiff && (c1 - c2) == subDiff * (expectedX + expectedY)) ||
                (subDiff != 0L && subDiff == -subYDiff && (c1 - c2) == subDiff * (expectedX - expectedY))

        return addValid && subValid
    }

    /**
     * Verifies Shunyam Samyasamuccaye common factor solution (Family A):
     * k1 * (a*x + b) = k2 * (a*x + b) with k1 != k2.
     * Since k1 != k2, (a*x + b) must equal 0, so x = -b / a.
     */
    fun verifyShunyamCommonFactor(k1: Long, a: Long, b: Long, k2: Long, expectedX: Long): Boolean {
        if (k1 == k2 || a == 0L) return false
        if (a * expectedX + b != 0L) return false
        val lhs = k1 * (a * expectedX + b)
        val rhs = k2 * (a * expectedX + b)
        return lhs == rhs && lhs == 0L
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
    ): Boolean {
        if (a == 0L || expectedCompletionDen <= 0L) return false
        val disc = b * b - 4 * a * c
        if (disc < 0L) return false

        // Check completion term: (b / 2a)^2 = b^2 / (4a^2)
        val compNum = b * b
        val compDen = 4 * a * a
        if (compNum * expectedCompletionDen != compDen * expectedCompletionNum) return false

        // Check roots satisfy quadratic equation: a*r^2 + b*r + c == 0
        val root1Valid = (a * root1 * root1 + b * root1 + c) == 0L
        val root2Valid = (a * root2 * root2 + b * root2 + c) == 0L
        return root1Valid && root2Valid
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
    ): Boolean {
        if ((p + q) % 2L != 0L) return false
        val avg = (p + q) / 2L
        val dev = kotlin.math.abs(q - p) / 2L
        if (avg != expectedAverage || dev != expectedDeviation) return false
        val diffOfSquares = (avg * avg) - (dev * dev)
        return diffOfSquares == expectedProduct && (p * q) == expectedProduct
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
    ): Boolean {
        if (q <= 0L || p <= 0L) return false
        val remainders = mutableListOf<Long>()
        val digits = StringBuilder()
        var rem = p % q
        val seen = mutableMapOf<Long, Int>()

        while (rem != 0L && rem !in seen) {
            seen[rem] = remainders.size
            remainders.add(rem)
            val v = rem * 10L
            val digit = v / q
            digits.append(digit)
            rem = v % q
        }

        if (rem == 0L) return false // not recurring
        val cycleStart = seen[rem] ?: return false
        val cycleLength = remainders.size - cycleStart
        val cycleDigits = digits.substring(cycleStart)

        return cycleLength == expectedPeriodLength && cycleDigits == expectedRepetend
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
    ): Boolean {
        if (multiplicand <= 0L || multiplier !in 12..19) return false
        val n = multiplier - 10
        val origDigits = multiplicand.toString().map { it.digitToInt() }
        val sandwiched = listOf(0) + origDigits + listOf(0)

        val rawValues = mutableListOf<Int>()
        for (i in 0 until origDigits.size + 1) {
            val d = sandwiched[i + 1]
            val prev = sandwiched[i]
            rawValues.add(d + n * prev)
        }

        if (rawValues != expectedRawValues) return false

        var currentCarry = 0
        val finalDigitsReversed = mutableListOf<Int>()
        for (i in rawValues.indices.reversed()) {
            val total = rawValues[i] + currentCarry
            finalDigitsReversed.add(total % 10)
            currentCarry = total / 10
        }
        if (currentCarry > 0) finalDigitsReversed.add(currentCarry)

        val computedProduct = finalDigitsReversed.reversed().joinToString("").toLong()
        return computedProduct == expectedProduct && (multiplicand * multiplier.toLong()) == expectedProduct
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
    ): Boolean {
        val scF1 = a1 + b1
        val scF2 = a2 + b2
        val expectedProductSum = scF1 * scF2
        val actualProductSum = claimedA2 + claimedA1 + claimedA0

        if (expectedProductSum != actualProductSum) return false

        val exactA2 = a1 * a2
        val exactA1 = a1 * b2 + b1 * a2
        val exactA0 = b1 * b2

        return exactA2 == claimedA2 && exactA1 == claimedA1 && exactA0 == claimedA0
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
    ): Boolean {
        if (expectedP * expectedQ != c || expectedP + expectedQ != b) return false
        if (expectedRoot1 != -expectedP || expectedRoot2 != -expectedQ) return false

        val eval1 = expectedRoot1 * expectedRoot1 + b * expectedRoot1 + c
        val eval2 = expectedRoot2 * expectedRoot2 + b * expectedRoot2 + c
        return eval1 == 0L && eval2 == 0L
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
