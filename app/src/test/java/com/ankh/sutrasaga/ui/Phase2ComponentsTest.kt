package com.ankh.sutrasaga.ui

import com.ankh.sutrasaga.domain.models.DecompositionStep
import com.ankh.sutrasaga.ui.components.YantraArrowPattern
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class Phase2ComponentsTest {

    @Test
    fun testYantraArrowPatternEnum() {
        assertEquals(6, YantraArrowPattern.entries.size)
        assertNotNull(YantraArrowPattern.CROSSWISE)
        assertNotNull(YantraArrowPattern.VERTICAL_RIGHT)
        assertNotNull(YantraArrowPattern.VERTICAL_LEFT)
        assertNotNull(YantraArrowPattern.DEFICIENCY_SQUARE)
        assertNotNull(YantraArrowPattern.COMPLEMENT_SUBTRACT)
        assertNotNull(YantraArrowPattern.NONE)
    }

    @Test
    fun testDecompositionStepModelingForVedicSutraCard() {
        val steps = listOf(
            DecompositionStep(
                stepNumber = 1,
                label = "Units x Units",
                formulaDisplay = "3 x 4",
                stepResult = "12 (write 2, carry 1)",
                explanation = "Multiply vertical units column"
            ),
            DecompositionStep(
                stepNumber = 2,
                label = "Crosswise Multiplication",
                formulaDisplay = "2(4) + 3(1) + 1",
                stepResult = "12 (write 2, carry 1)",
                explanation = "Multiply diagonals and add carry"
            ),
            DecompositionStep(
                stepNumber = 3,
                label = "Tens x Tens",
                formulaDisplay = "2 x 1 + 1",
                stepResult = "3",
                explanation = "Multiply tens and add carry"
            )
        )

        assertEquals(3, steps.size)
        assertEquals("3 x 4", steps[0].formulaDisplay)
        assertEquals("Units x Units", steps[0].label)
        assertEquals("Multiply vertical units column", steps[0].explanation)
    }
}
