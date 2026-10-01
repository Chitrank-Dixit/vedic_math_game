package com.ankh.sutrasaga.engine.upasutras

/**
 * UpaSutraMathUtils — Consolidated mathematical helper utilities for the 13 Upa-Sutras.
 *
 * Provides shared routines for:
 * - Greatest Common Divisor (GCD) & Least Common Multiple (LCM)
 * - Power of 10 base calculations and deficiencies
 * - Monic and non-monic polynomial quadratic discriminant checks
 * - Digit sum / digit root (Navasesha / Beejank) computations
 */
object UpaSutraMathUtils {

    /**
     * Computes the Greatest Common Divisor of two integers using the Euclidean algorithm.
     */
    fun gcd(a: Long, b: Long): Long {
        var num1 = kotlin.math.abs(a)
        var num2 = kotlin.math.abs(b)
        while (num2 != 0L) {
            val temp = num2
            num2 = num1 % num2
            num1 = temp
        }
        return if (num1 == 0L) 1L else num1
    }

    /**
     * Computes the Least Common Multiple of two integers.
     */
    fun lcm(a: Long, b: Long): Long {
        if (a == 0L || b == 0L) return 0L
        return (kotlin.math.abs(a) / gcd(a, b)) * kotlin.math.abs(b)
    }

    /**
     * Calculates the nearest base power of 10 for a given operand.
     */
    fun calculateNearestPowerOf10(number: Long): Long {
        var base = 10L
        while (base < number) {
            base *= 10L
        }
        return base
    }

    /**
     * Computes the single-digit Vedic root (Beejank / Digit Sum).
     */
    fun computeBeejank(number: Long): Int {
        var n = kotlin.math.abs(number)
        while (n >= 10) {
            var sum = 0L
            while (n > 0) {
                sum += n % 10
                n /= 10
            }
            n = sum
        }
        return n.toInt()
    }

    /**
     * Computes the discriminant (b² - 4ac) for a quadratic polynomial ax² + bx + c.
     */
    fun discriminant(a: Long, b: Long, c: Long): Long {
        return b * b - 4 * a * c
    }

    /**
     * Checks if a quadratic ax² + bx + c has rational factors (i.e. discriminant is a perfect square).
     */
    fun isPerfectSquareDiscriminant(a: Long, b: Long, c: Long): Boolean {
        val disc = discriminant(a, b, c)
        if (disc < 0) return false
        val root = kotlin.math.sqrt(disc.toDouble()).toLong()
        return root * root == disc
    }
}
