package com.ankh.sutrasaga.ui

import com.ankh.sutrasaga.ui.screens.canonicalOnboardingProblem
import org.junit.Assert.assertEquals
import org.junit.Test

class WorldOnboardingProblemTest {

    @Test
    fun worldsFourThroughSevenUseTheirOwnCanonicalProblems() {
        val world4 = canonicalOnboardingProblem(4)
        val world5 = canonicalOnboardingProblem(5)
        val world6 = canonicalOnboardingProblem(6)
        val world7 = canonicalOnboardingProblem(7)

        assertEquals("Yavadunam", world4.sutraName)
        assertEquals(8836L, world4.correctAnswer)
        assertEquals("Urdhva-Tiryagbhyam", world5.sutraName)
        assertEquals(943L, world5.correctAnswer)
        assertEquals("Paravartya Yojayet", world6.sutraName)
        assertEquals(102L, world6.correctAnswer)
        assertEquals("Anurupye Shunyamanyat", world7.sutraName)
        assertEquals(4L, world7.correctAnswer)
    }
}
