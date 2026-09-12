package com.ankh.sutrasaga.ui

import com.ankh.sutrasaga.domain.models.UpaSutraId
import com.ankh.sutrasaga.ui.viewmodel.GameScreen
import com.ankh.sutrasaga.ui.viewmodel.GameViewModel
import com.ankh.sutrasaga.ui.viewmodel.UpaSutraQuestStage
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
        assertFalse(state.isWorld4Completed)
    }

    @Test
    fun testSelectWorldNavigation() {
        for (w in 2..16) {
            viewModel.selectWorld(w)
            assertEquals(w, viewModel.uiState.value.selectedWorldId)
            assertEquals(GameScreen.STORY_BEAT, viewModel.uiState.value.currentScreen)
        }
    }

    @Test
    fun testNavigateToTutorial() {
        viewModel.selectWorld(1)
        viewModel.navigateToTutorial()
        assertEquals(GameScreen.TUTORIAL, viewModel.uiState.value.currentScreen)
    }

    @Test
    fun testStartPracticeArenaWorld1To16() {
        for (worldId in 1..16) {
            viewModel.selectWorld(worldId)
            viewModel.startPractice()

            val state = viewModel.uiState.value
            assertEquals(GameScreen.PRACTICE_ARENA, state.currentScreen)
            assertEquals(5, state.problemList.size)
            assertEquals(0, state.currentProblemIndex)
            assertNotNull("Problem for world $worldId must not be null", state.currentProblem)
            assertEquals(0, state.score)
        }
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

        repeat(20) { viewModel.appendDigit('9') }
        assertEquals("9999999999999999", viewModel.uiState.value.userInput)
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
        viewModel.selectWorld(4)
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
        viewModel.selectWorld(5)
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
    fun testDivisionRequiresQuotientAndRemainder() {
        viewModel.selectWorld(6)
        viewModel.startPractice()

        val problem = viewModel.uiState.value.currentProblem!!
        problem.correctAnswer.toString().forEach(viewModel::appendDigit)
        viewModel.appendRemainderSeparator()
        problem.expectedRemainder.toString().forEach(viewModel::appendDigit)
        viewModel.submitAnswer()

        val state = viewModel.uiState.value
        assertEquals("${problem.correctAnswer} R ${problem.expectedRemainder}", state.userInput)
        assertEquals(true, state.isAnswerCorrect)
    }

    @Test
    fun testDivisionRejectsQuotientWithoutRemainder() {
        viewModel.selectWorld(6)
        viewModel.startPractice()

        val problem = viewModel.uiState.value.currentProblem!!
        problem.correctAnswer.toString().forEach(viewModel::appendDigit)
        viewModel.submitAnswer()

        assertEquals(false, viewModel.uiState.value.isAnswerCorrect)
    }

    @Test
    fun testSimultaneousEquationsRequireAnOrderedPair() {
        viewModel.selectWorld(7)
        viewModel.startPractice()

        val problem = viewModel.uiState.value.currentProblem!!
        problem.correctAnswer.toString().forEach(viewModel::appendDigit)
        viewModel.appendOrderedPairSeparator()
        problem.expectedSecondaryAnswer.toString().forEach(viewModel::appendDigit)
        viewModel.submitAnswer()

        val state = viewModel.uiState.value
        assertEquals("${problem.correctAnswer}, ${problem.expectedSecondaryAnswer}", state.userInput)
        assertEquals(true, state.isAnswerCorrect)
    }

    @Test
    fun testSimultaneousEquationsRejectAValueWithoutItsVariablePair() {
        viewModel.selectWorld(7)
        viewModel.startPractice()

        val problem = viewModel.uiState.value.currentProblem!!
        problem.correctAnswer.toString().forEach(viewModel::appendDigit)
        viewModel.submitAnswer()

        assertEquals(false, viewModel.uiState.value.isAnswerCorrect)
    }

    @Test
    fun testNextProblemStateReset() {
        viewModel.selectWorld(6)
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

    @Test
    fun testTreasuryAndCodexNavigation() {
        viewModel.openTreasury()
        assertEquals(GameScreen.UPA_SUTRA_TREASURY, viewModel.uiState.value.currentScreen)

        viewModel.openCodex()
        assertEquals(GameScreen.UPA_SUTRA_CODEX, viewModel.uiState.value.currentScreen)

        viewModel.returnToWorldSelect()
        assertEquals(GameScreen.WORLD_SELECT, viewModel.uiState.value.currentScreen)
    }

    @Test
    fun testAntyayordashakepiQuestLifecycle() {
        viewModel.startUpaSutraQuest(UpaSutraId.ANTYAYORDASHAKEPI)
        var state = viewModel.uiState.value
        assertEquals(GameScreen.UPA_SUTRA_QUEST, state.currentScreen)
        assertEquals(UpaSutraId.ANTYAYORDASHAKEPI, state.selectedUpaSutraId)
        assertEquals(UpaSutraQuestStage.STORY_BEAT, state.questStage)

        // Advance to Guided Example
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.GUIDED_EXAMPLE, state.questStage)

        // Advance to Practice (loads 5 problems)
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.PRACTICE, state.questStage)
        assertEquals(5, state.problemList.size)
        assertNotNull(state.currentProblem)

        // Submit correct answer for first practice problem
        val prob = state.currentProblem!!
        prob.correctAnswer.toString().forEach(viewModel::appendDigit)
        viewModel.submitAnswer()
        state = viewModel.uiState.value
        assertTrue(state.isAnswerSubmitted)
        assertEquals(true, state.isAnswerCorrect)
        assertEquals(1, state.questPracticeCorrectCount)

        // Advance to Challenge
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.CHALLENGE, state.questStage)
        assertEquals(3, state.problemList.size)

        // Advance to Reward
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.REWARD, state.questStage)
    }

    @Test
    fun testAnurupyenaQuestLifecycle() {
        viewModel.startUpaSutraQuest(UpaSutraId.ANURUPYENA)
        var state = viewModel.uiState.value
        assertEquals(GameScreen.UPA_SUTRA_QUEST, state.currentScreen)
        assertEquals(UpaSutraId.ANURUPYENA, state.selectedUpaSutraId)
        assertEquals(UpaSutraQuestStage.STORY_BEAT, state.questStage)

        // Advance to Guided Example
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.GUIDED_EXAMPLE, state.questStage)

        // Advance to Practice (loads 5 problems)
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.PRACTICE, state.questStage)
        assertEquals(5, state.problemList.size)
        assertNotNull(state.currentProblem)

        // Submit correct answer for first practice problem
        val prob = state.currentProblem!!
        prob.correctAnswer.toString().forEach(viewModel::appendDigit)
        viewModel.submitAnswer()
        state = viewModel.uiState.value
        assertTrue(state.isAnswerSubmitted)
        assertEquals(true, state.isAnswerCorrect)
        assertEquals(1, state.questPracticeCorrectCount)

        // Advance to Challenge
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.CHALLENGE, state.questStage)
        assertEquals(3, state.problemList.size)

        // Advance to Reward
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.REWARD, state.questStage)
    }

    @Test
    fun testYavadunamRemixQuestLifecycle() {
        viewModel.startUpaSutraQuest(UpaSutraId.YAVADUNAM_TAVADUNIKRTYA_VARGANCHA_YOJAYET)
        var state = viewModel.uiState.value
        assertEquals(GameScreen.UPA_SUTRA_QUEST, state.currentScreen)
        assertEquals(UpaSutraId.YAVADUNAM_TAVADUNIKRTYA_VARGANCHA_YOJAYET, state.selectedUpaSutraId)
        assertEquals(UpaSutraQuestStage.STORY_BEAT, state.questStage)

        // Advance to Guided Example
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.GUIDED_EXAMPLE, state.questStage)

        // Advance to Practice (loads 5 problems)
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.PRACTICE, state.questStage)
        assertEquals(5, state.problemList.size)
        assertNotNull(state.currentProblem)

        // Submit correct answer for first practice problem
        val prob = state.currentProblem!!
        prob.correctAnswer.toString().forEach(viewModel::appendDigit)
        viewModel.submitAnswer()
        state = viewModel.uiState.value
        assertTrue(state.isAnswerSubmitted)
        assertEquals(true, state.isAnswerCorrect)
        assertEquals(1, state.questPracticeCorrectCount)

        // Advance to Challenge
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.CHALLENGE, state.questStage)
        assertEquals(3, state.problemList.size)

        // Advance to Reward
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.REWARD, state.questStage)
    }

    @Test
    fun testAdyamadyenantyamantyenaQuestLifecycle() {
        viewModel.startUpaSutraQuest(UpaSutraId.ADYAMADYENANTYAMANTYENA)
        var state = viewModel.uiState.value
        assertEquals(GameScreen.UPA_SUTRA_QUEST, state.currentScreen)
        assertEquals(UpaSutraId.ADYAMADYENANTYAMANTYENA, state.selectedUpaSutraId)
        assertEquals(UpaSutraQuestStage.STORY_BEAT, state.questStage)

        // Advance to Guided Example
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.GUIDED_EXAMPLE, state.questStage)

        // Advance to Practice (loads 5 problems)
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.PRACTICE, state.questStage)
        assertEquals(5, state.problemList.size)
        assertNotNull(state.currentProblem)

        // Submit correct answer for first practice problem
        val prob = state.currentProblem!!
        prob.correctAnswer.toString().forEach(viewModel::appendDigit)
        viewModel.submitAnswer()
        state = viewModel.uiState.value
        assertTrue(state.isAnswerSubmitted)
        assertEquals(true, state.isAnswerCorrect)
        assertEquals(1, state.questPracticeCorrectCount)

        // Advance to Challenge
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.CHALLENGE, state.questStage)
        assertEquals(3, state.problemList.size)

        // Advance to Reward
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.REWARD, state.questStage)
    }

    @Test
    fun testVestanamQuestLifecycle() {
        viewModel.startUpaSutraQuest(UpaSutraId.VESHTANAM)
        var state = viewModel.uiState.value
        assertEquals(GameScreen.UPA_SUTRA_QUEST, state.currentScreen)
        assertEquals(UpaSutraId.VESHTANAM, state.selectedUpaSutraId)
        assertEquals(UpaSutraQuestStage.STORY_BEAT, state.questStage)

        // Advance to Guided Example
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.GUIDED_EXAMPLE, state.questStage)

        // Advance to Practice (loads 5 problems)
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.PRACTICE, state.questStage)
        assertEquals(5, state.problemList.size)
        assertNotNull(state.currentProblem)

        // Submit correct answer for first practice problem
        val prob = state.currentProblem!!
        prob.correctAnswer.toString().forEach(viewModel::appendDigit)
        viewModel.submitAnswer()
        state = viewModel.uiState.value
        assertTrue(state.isAnswerSubmitted)
        assertEquals(true, state.isAnswerCorrect)
        assertEquals(1, state.questPracticeCorrectCount)

        // Advance to Challenge
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.CHALLENGE, state.questStage)
        assertEquals(3, state.problemList.size)

        // Advance to Reward
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.REWARD, state.questStage)
    }

    @Test
    fun testSisyateSesasamjnahQuestLifecycle() {
        viewModel.startUpaSutraQuest(UpaSutraId.SHISYATE_SHESAMAJNA)
        var state = viewModel.uiState.value
        assertEquals(GameScreen.UPA_SUTRA_QUEST, state.currentScreen)
        assertEquals(UpaSutraId.SHISYATE_SHESAMAJNA, state.selectedUpaSutraId)
        assertEquals(UpaSutraQuestStage.STORY_BEAT, state.questStage)

        // Advance to Guided Example
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.GUIDED_EXAMPLE, state.questStage)

        // Advance to Practice (loads 5 problems)
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.PRACTICE, state.questStage)
        assertEquals(5, state.problemList.size)
        assertNotNull(state.currentProblem)

        // Submit correct answer for first practice problem
        val prob = state.currentProblem!!
        prob.correctAnswer.toString().forEach(viewModel::appendDigit)
        viewModel.submitAnswer()
        state = viewModel.uiState.value
        assertTrue(state.isAnswerSubmitted)
        assertEquals(true, state.isAnswerCorrect)
        assertEquals(1, state.questPracticeCorrectCount)

        // Advance to Challenge
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.CHALLENGE, state.questStage)
        assertEquals(3, state.problemList.size)

        // Advance to Reward
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.REWARD, state.questStage)
    }

    @Test
    fun testKevalaihSaptakamGunyatQuestLifecycle() {
        viewModel.startUpaSutraQuest(UpaSutraId.KEVALAIHSAPTAKAM_GUNYAT)
        var state = viewModel.uiState.value
        assertEquals(GameScreen.UPA_SUTRA_QUEST, state.currentScreen)
        assertEquals(UpaSutraId.KEVALAIHSAPTAKAM_GUNYAT, state.selectedUpaSutraId)
        assertEquals(UpaSutraQuestStage.STORY_BEAT, state.questStage)

        // Advance to Guided Example
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.GUIDED_EXAMPLE, state.questStage)

        // Advance to Practice (loads 5 problems)
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.PRACTICE, state.questStage)
        assertEquals(5, state.problemList.size)
        assertNotNull(state.currentProblem)

        // Submit correct answer for first practice problem
        val prob = state.currentProblem!!
        prob.correctAnswer.toString().forEach(viewModel::appendDigit)
        viewModel.submitAnswer()
        state = viewModel.uiState.value
        assertTrue(state.isAnswerSubmitted)
        assertEquals(true, state.isAnswerCorrect)
        assertEquals(1, state.questPracticeCorrectCount)

        // Advance to Challenge
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.CHALLENGE, state.questStage)
        assertEquals(3, state.problemList.size)

        // Advance to Reward
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.REWARD, state.questStage)
    }

    @Test
    fun testLopanasthapanabhyamQuestLifecycle() {
        viewModel.startUpaSutraQuest(UpaSutraId.LOPANA_STHAPANABHYAM)
        var state = viewModel.uiState.value
        assertEquals(GameScreen.UPA_SUTRA_QUEST, state.currentScreen)
        assertEquals(UpaSutraId.LOPANA_STHAPANABHYAM, state.selectedUpaSutraId)
        assertEquals(UpaSutraQuestStage.STORY_BEAT, state.questStage)

        // Advance to Guided Example
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.GUIDED_EXAMPLE, state.questStage)

        // Advance to Practice (loads 5 problems)
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.PRACTICE, state.questStage)
        assertEquals(5, state.problemList.size)
        assertNotNull(state.currentProblem)

        // Submit correct answer for first practice problem
        val prob = state.currentProblem!!
        prob.correctAnswer.toString().forEach(viewModel::appendDigit)
        viewModel.submitAnswer()
        state = viewModel.uiState.value
        assertTrue(state.isAnswerSubmitted)
        assertEquals(true, state.isAnswerCorrect)
        assertEquals(1, state.questPracticeCorrectCount)

        // Advance to Challenge
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.CHALLENGE, state.questStage)
        assertEquals(3, state.problemList.size)

        // Advance to Reward
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.REWARD, state.questStage)
    }

    @Test
    fun testVilokanamQuestLifecycle() {
        viewModel.startUpaSutraQuest(UpaSutraId.VILOKANAM)
        var state = viewModel.uiState.value
        assertEquals(GameScreen.UPA_SUTRA_QUEST, state.currentScreen)
        assertEquals(UpaSutraId.VILOKANAM, state.selectedUpaSutraId)
        assertEquals(UpaSutraQuestStage.STORY_BEAT, state.questStage)

        // Advance to Guided Example
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.GUIDED_EXAMPLE, state.questStage)

        // Advance to Practice (loads 5 problems)
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.PRACTICE, state.questStage)
        assertEquals(5, state.problemList.size)
        assertNotNull(state.currentProblem)

        // Submit correct answer for first practice problem
        val prob = state.currentProblem!!
        prob.correctAnswer.toString().forEach(viewModel::appendDigit)
        viewModel.submitAnswer()
        state = viewModel.uiState.value
        assertTrue(state.isAnswerSubmitted)
        assertEquals(true, state.isAnswerCorrect)
        assertEquals(1, state.questPracticeCorrectCount)

        // Advance to Challenge
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.CHALLENGE, state.questStage)
        assertEquals(3, state.problemList.size)

        // Advance to Reward
        viewModel.advanceQuestStage()
        state = viewModel.uiState.value
        assertEquals(UpaSutraQuestStage.REWARD, state.questStage)
    }

    @Test
    fun testCompleteWorldUpdatesFlagsAndUnlocksUpaSutras() {
        // Initial state: all world completions false
        var state = viewModel.uiState.value
        assertFalse(state.isWorld1Completed)
        assertFalse(state.isWorld2Completed)
        assertFalse(state.isWorld3Completed)
        assertFalse(state.isWorld4Completed)
        assertFalse(state.isWorld5Completed)
        assertFalse(state.isWorld6Completed)
        assertFalse(state.isWorld7Completed)
        assertFalse(state.isWorld8Completed)
        assertFalse(state.isWorld9Completed)
        assertFalse(state.isWorld10Completed)
        assertFalse(state.isWorld11Completed)
        assertFalse(state.isWorld12Completed)
        assertFalse(state.isWorld13Completed)
        assertFalse(state.isWorld14Completed)
        assertFalse(state.isWorld15Completed)
        assertFalse(state.isWorld16Completed)

        // Complete World 1
        viewModel.completeWorld(1, score = 150)
        state = viewModel.uiState.value
        assertTrue(state.isWorld1Completed)
        assertEquals(150, state.world1BestScore)
        assertFalse(state.isWorld2Completed)

        // Complete all worlds 2..16
        for (w in 2..16) {
            viewModel.completeWorld(w, score = 100 + w)
        }

        state = viewModel.uiState.value
        assertTrue(state.isWorld1Completed)
        assertTrue(state.isWorld2Completed)
        assertTrue(state.isWorld3Completed)
        assertTrue(state.isWorld4Completed)
        assertTrue(state.isWorld5Completed)
        assertTrue(state.isWorld6Completed)
        assertTrue(state.isWorld7Completed)
        assertTrue(state.isWorld8Completed)
        assertTrue(state.isWorld9Completed)
        assertTrue(state.isWorld10Completed)
        assertTrue(state.isWorld11Completed)
        assertTrue(state.isWorld12Completed)
        assertTrue(state.isWorld13Completed)
        assertTrue(state.isWorld14Completed)
        assertTrue(state.isWorld15Completed)
        assertTrue(state.isWorld16Completed)
        assertEquals(116, state.world16BestScore)
    }
}

