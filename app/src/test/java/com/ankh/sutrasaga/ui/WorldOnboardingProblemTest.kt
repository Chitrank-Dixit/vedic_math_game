package com.ankh.sutrasaga.ui

import com.ankh.sutrasaga.ui.screens.canonicalOnboardingProblem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class WorldOnboardingProblemTest {

    @Test
    fun testAllSixteenWorldsHaveDedicatedCanonicalOnboardingProblems() {
        val expectedSutraNames = listOf(
            1 to "Ekadhikena Purvena",
            2 to "Nikhilam Navatashcaramam Dashatah",
            3 to "Ekanyunena Purvena",
            4 to "Yavadunam",
            5 to "Urdhva-Tiryagbhyam",
            6 to "Paravartya Yojayet",
            7 to "Anurupye Shunyamanyat",
            8 to "Sankalana-Vyavakalanabhyam",
            9 to "Shunyam Samyasamuccaye",
            10 to "Puranapuranabhyam",
            11 to "Vyashtisamashtih",
            12 to "Shesanyankena Charamena",
            13 to "Sopantyadvayamantyam",
            14 to "Gunitasamuccayah",
            15 to "Gunakasamuccayah",
            16 to "Chalana-Kalanabhyam"
        )

        for ((worldId, expectedName) in expectedSutraNames) {
            val problem = canonicalOnboardingProblem(worldId)
            assertNotNull("Problem for World $worldId must not be null", problem)
            assertEquals("World $worldId sutraName mismatch", expectedName, problem.sutraName)
            assertTrue("World $worldId questionText must not be blank", problem.questionText.isNotBlank())
            assertTrue("World $worldId decompositionSteps must not be empty", problem.decompositionSteps.isNotEmpty())
        }
    }

    @Test
    fun worldsFourThroughThirteenUseTheirOwnCanonicalProblems() {
        val world4 = canonicalOnboardingProblem(4)
        val world5 = canonicalOnboardingProblem(5)
        val world6 = canonicalOnboardingProblem(6)
        val world7 = canonicalOnboardingProblem(7)
        val world8 = canonicalOnboardingProblem(8)
        val world9 = canonicalOnboardingProblem(9)
        val world10 = canonicalOnboardingProblem(10)
        val world11 = canonicalOnboardingProblem(11)
        val world12 = canonicalOnboardingProblem(12)
        val world13 = canonicalOnboardingProblem(13)

        assertEquals("Yavadunam", world4.sutraName)
        assertEquals(8836L, world4.correctAnswer)
        assertEquals("Urdhva-Tiryagbhyam", world5.sutraName)
        assertEquals(943L, world5.correctAnswer)
        assertEquals("Paravartya Yojayet", world6.sutraName)
        assertEquals(102L, world6.correctAnswer)
        assertEquals("Anurupye Shunyamanyat", world7.sutraName)
        assertEquals(4L, world7.correctAnswer)
        assertEquals("Sankalana-Vyavakalanabhyam", world8.sutraName)
        assertEquals(2L, world8.correctAnswer)
        assertEquals(-1L, world8.expectedSecondaryAnswer)
        assertEquals("Shunyam Samyasamuccaye", world9.sutraName)
        assertEquals(-1L, world9.correctAnswer)
        assertEquals("Puranapuranabhyam", world10.sutraName)
        assertEquals(-2L, world10.correctAnswer)
        assertEquals(-4L, world10.expectedSecondaryAnswer)
        assertEquals("Vyashtisamashtih", world11.sutraName)
        assertEquals(3596L, world11.correctAnswer)
        assertEquals("Shesanyankena Charamena", world12.sutraName)
        assertEquals(1L, world12.correctAnswer)
        assertEquals(6L, world12.prefixProduct)
        assertEquals("0.(142857)", world12.appendedSuffix)
        assertEquals("Sopantyadvayamantyam", world13.sutraName)
        assertEquals(1716L, world13.correctAnswer)
        assertEquals(143L, world13.prefixPart)
        assertEquals(12L, world13.incrementedPrefix)
        assertEquals(2L, world13.prefixProduct)
        assertEquals("1716", world13.appendedSuffix)
    }
}
