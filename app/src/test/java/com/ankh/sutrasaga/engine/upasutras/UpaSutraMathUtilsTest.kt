package com.ankh.sutrasaga.engine.upasutras

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UpaSutraMathUtilsTest {

    @Test
    fun testGcdAndLcm() {
        assertEquals(6L, UpaSutraMathUtils.gcd(54L, 24L))
        assertEquals(1L, UpaSutraMathUtils.gcd(17L, 19L))
        assertEquals(216L, UpaSutraMathUtils.lcm(54L, 24L))
    }

    @Test
    fun testNearestPowerOf10() {
        assertEquals(100L, UpaSutraMathUtils.calculateNearestPowerOf10(98L))
        assertEquals(1000L, UpaSutraMathUtils.calculateNearestPowerOf10(105L))
        assertEquals(10000L, UpaSutraMathUtils.calculateNearestPowerOf10(8972L))
    }

    @Test
    fun testBeejank() {
        assertEquals(9, UpaSutraMathUtils.computeBeejank(999L))
        assertEquals(7, UpaSutraMathUtils.computeBeejank(43L))
        assertEquals(1, UpaSutraMathUtils.computeBeejank(100L))
    }

    @Test
    fun testDiscriminantAndFactorability() {
        // 6x² + 5x - 6: disc = 25 - 4(6)(-6) = 25 + 144 = 169 (13²) -> factorable
        assertEquals(169L, UpaSutraMathUtils.discriminant(6L, 5L, -6L))
        assertTrue(UpaSutraMathUtils.isPerfectSquareDiscriminant(6L, 5L, -6L))

        // x² + 2x + 5: disc = 4 - 20 = -16 -> non-factorable
        assertFalse(UpaSutraMathUtils.isPerfectSquareDiscriminant(1L, 2L, 5L))
    }
}
