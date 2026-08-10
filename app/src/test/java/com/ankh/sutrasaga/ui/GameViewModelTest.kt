package com.ankh.sutrasaga.ui

import com.ankh.sutrasaga.ui.viewmodel.GameScreen
import com.ankh.sutrasaga.ui.viewmodel.GameViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GameViewModelTest {

    private lateinit var viewModel: GameViewModel

    @Before
    fun setUp() {
        viewModel = GameViewModel()
    }

    @Test
    fun testInitialUiState() {
        val state = viewModel.uiState.value
        assertEquals(GameScreen.WORLD_SELECT, state.currentScreen)
        assertEquals(1, state.selectedWorldId)
        assertFalse(state.isWorld1Completed)
        assertFalse(state.isWorld2Completed)
        assertFalse(state.isWorld3Completed)
    }

    @Test
    fun testSelectWorldNavigation() {
        viewModel.selectWorld(2)
        assertEquals(2, viewModel.uiState.value.selectedWorldId)
        assertEquals(GameScreen.STORY_BEAT, viewModel.uiState.value.currentScreen)

        viewModel.selectWorld(3)
        assertEquals(3, viewModel.uiState.value.selectedWorldId)
        assertEquals(GameScreen.STORY_BEAT, viewModel.uiState.value.currentScreen)
    }

    @Test
    fun testNavigateToTutorial() {
        viewModel.selectWorld(1)
        viewModel.navigateToTutorial()
        assertEquals(GameScreen.TUTORIAL, viewModel.uiState.value.currentScreen)
    }

    @Test
    fun testStartPracticeArena() {
        viewModel.selectWorld(1)
        viewModel.startPractice()

        val state = viewModel.uiState.value
        assertEquals(GameScreen.PRACTICE_ARENA, state.currentScreen)
        assertEquals(5, state.problemList.size)
        assertEquals(0, state.currentProblemIndex)
        assertNotNull(state.currentProblem)
        assertEquals(0, state.score)
    }

    @Test
    fun testInputBufferOperations() {
        viewModel.selectWorld(1)
        viewModel.startPractice()

        viewModel.appendDigit('4')
        viewModel.appendDigit('2')
        viewModel.appendDigit('2')
        viewModel.appendDigit('5')
        assertEquals("4225", viewModel.uiState.value.userInput)

        viewModel.backspaceDigit()
        assertEquals("422", viewModel.uiState.value.userInput)

        viewModel.clearDigit()
        assertEquals("", viewModel.uiState.value.userInput)
    }

    @Test
    fun testInputBufferMaxLimit() {
        viewModel.selectWorld(1)
        viewModel.startPractice()

        repeat(12) { viewModel.appendDigit('9') }
        assertEquals("99999999", viewModel.uiState.value.userInput)
    }

    @Test
    fun testRevealStepCount() {
        viewModel.selectWorld(1)
        viewModel.startPractice()

        assertEquals(0, viewModel.uiState.value.revealedStepsCount)
        viewModel.revealNextStep()
        assertEquals(1, viewModel.uiState.value.revealedStepsCount)
        viewModel.revealNextStep()
        assertEquals(2, viewModel.uiState.value.revealedStepsCount)
    }

    @Test
    fun testSubmitCorrectAnswerAndScoreCalculation() {
        viewModel.selectWorld(1)
        viewModel.startPractice()

        val currentProblem = viewModel.uiState.value.currentProblem!!
        val correctAnswer = currentProblem.correctAnswer.toString()

        for (char in correctAnswer) {
            viewModel.appendDigit(char)
        }

        viewModel.submitAnswer()

        val state = viewModel.uiState.value
        assertTrue(state.isAnswerSubmitted)
        assertEquals(true, state.isAnswerCorrect)
        assertTrue(state.score > 0)
        assertTrue(state.feedbackMessage.contains("CORRECT!"))
    }

    @Test
    fun testSubmitIncorrectAnswer() {
        viewModel.selectWorld(1)
        viewModel.startPractice()

        viewModel.appendDigit('1')
        viewModel.submitAnswer()

        val state = viewModel.uiState.value
        assertTrue(state.isAnswerSubmitted)
        assertEquals(false, state.isAnswerCorrect)
        assertEquals(0, state.score)
        assertTrue(state.feedbackMessage.contains("INCORRECT!"))
    }

    @Test
    fun testNextProblemStateReset() {
        viewModel.selectWorld(1)
        viewModel.startPractice()

        viewModel.appendDigit('1')
        viewModel.submitAnswer()
        viewModel.nextProblem()

        val state = viewModel.uiState.value
        assertEquals(1, state.currentProblemIndex)
        assertEquals("", state.userInput)
        assertFalse(state.isAnswerSubmitted)
        assertNull(state.isAnswerCorrect)
    }

    @Test
    fun testReturnToWorldSelect() {
        viewModel.selectWorld(1)
        viewModel.startPractice()
        viewModel.returnToWorldSelect()

        assertEquals(GameScreen.WORLD_SELECT, viewModel.uiState.value.currentScreen)
    }
}
