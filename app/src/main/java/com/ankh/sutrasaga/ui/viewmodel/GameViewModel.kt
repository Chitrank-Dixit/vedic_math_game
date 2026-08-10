package com.ankh.sutrasaga.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.ankh.sutrasaga.data.db.AppDatabase
import com.ankh.sutrasaga.data.repository.GameRepository
import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.SutraProblem
import com.ankh.sutrasaga.engine.EkadhikenaPurvenaGenerator
import com.ankh.sutrasaga.engine.EkanyunenaGenerator
import com.ankh.sutrasaga.engine.NikhilamGenerator
import com.ankh.sutrasaga.engine.SutraProblemGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class GameScreen {
    WORLD_SELECT,
    STORY_BEAT,
    TUTORIAL,
    PRACTICE_ARENA,
    BOSS_BATTLE,
    REWARD
}

data class GameUiState(
    val currentScreen: GameScreen = GameScreen.WORLD_SELECT,
    val selectedWorldId: Int = 1,
    val isWorld1Completed: Boolean = false,
    val world1BestScore: Int = 0,
    val isWorld2Completed: Boolean = false,
    val world2BestScore: Int = 0,
    val isWorld3Completed: Boolean = false,
    val world3BestScore: Int = 0,
    val currentProblemIndex: Int = 0,
    val totalProblemsInMode: Int = 5,
    val problemList: List<SutraProblem> = emptyList(),
    val currentProblem: SutraProblem? = null,
    val revealedStepsCount: Int = 0,
    val userInput: String = "",
    val score: Int = 0,
    val isAnswerSubmitted: Boolean = false,
    val isAnswerCorrect: Boolean? = null,
    val feedbackMessage: String = ""
)

class GameViewModel : ViewModel() {

    private val ekadhikenaGenerator = EkadhikenaPurvenaGenerator()
    private val nikhilamGenerator = NikhilamGenerator()
    private val ekanyunenaGenerator = EkanyunenaGenerator()

    private var repository: GameRepository? = null

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    fun initRepository(context: Context) {
        if (repository == null) {
            val db = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "ankh_game_db"
            ).build()
            repository = GameRepository(db.userProgressDao())
            observeProgress()
        }
    }

    private fun observeProgress() {
        viewModelScope.launch {
            repository?.getAllProgress()?.collect { list ->
                val w1 = list.find { it.worldId == 1 }
                val w2 = list.find { it.worldId == 2 }
                val w3 = list.find { it.worldId == 3 }

                _uiState.value = _uiState.value.copy(
                    isWorld1Completed = w1?.isCompleted ?: false,
                    world1BestScore = w1?.bestScore ?: 0,
                    isWorld2Completed = w2?.isCompleted ?: false,
                    world2BestScore = w2?.bestScore ?: 0,
                    isWorld3Completed = w3?.isCompleted ?: false,
                    world3BestScore = w3?.bestScore ?: 0
                )
            }
        }
    }

    private fun getGeneratorForWorld(worldId: Int): SutraProblemGenerator {
        return when (worldId) {
            1 -> ekadhikenaGenerator
            2 -> nikhilamGenerator
            3 -> ekanyunenaGenerator
            else -> ekadhikenaGenerator
        }
    }

    fun selectWorld(worldId: Int) {
        if (worldId in 1..3) {
            _uiState.value = _uiState.value.copy(
                selectedWorldId = worldId,
                currentScreen = GameScreen.STORY_BEAT
            )
        }
    }

    fun navigateToTutorial() {
        _uiState.value = _uiState.value.copy(
            currentScreen = GameScreen.TUTORIAL
        )
    }

    fun startPractice() {
        val generator = getGeneratorForWorld(_uiState.value.selectedWorldId)
        val problems = generator.generateProblemSet(5, DifficultyTier.TIER_1_EASY)
        _uiState.value = _uiState.value.copy(
            currentScreen = GameScreen.PRACTICE_ARENA,
            problemList = problems,
            currentProblemIndex = 0,
            totalProblemsInMode = 5,
            currentProblem = problems.firstOrNull(),
            revealedStepsCount = 0,
            userInput = "",
            score = 0,
            isAnswerSubmitted = false,
            isAnswerCorrect = null,
            feedbackMessage = ""
        )
    }

    fun startBossBattle() {
        val generator = getGeneratorForWorld(_uiState.value.selectedWorldId)
        val problems = generator.generateProblemSet(5, DifficultyTier.TIER_2_HARD)
        _uiState.value = _uiState.value.copy(
            currentScreen = GameScreen.BOSS_BATTLE,
            problemList = problems,
            currentProblemIndex = 0,
            totalProblemsInMode = 5,
            currentProblem = problems.firstOrNull(),
            revealedStepsCount = 0,
            userInput = "",
            score = 0,
            isAnswerSubmitted = false,
            isAnswerCorrect = null,
            feedbackMessage = ""
        )
    }

    fun revealNextStep() {
        val currentProblem = _uiState.value.currentProblem ?: return
        val maxSteps = currentProblem.decompositionSteps.size
        if (_uiState.value.revealedStepsCount < maxSteps) {
            _uiState.value = _uiState.value.copy(
                revealedStepsCount = _uiState.value.revealedStepsCount + 1
            )
        }
    }

    fun appendDigit(char: Char) {
        if (_uiState.value.isAnswerSubmitted) return
        if (_uiState.value.userInput.length < 8) {
            _uiState.value = _uiState.value.copy(
                userInput = _uiState.value.userInput + char
            )
        }
    }

    fun backspaceDigit() {
        if (_uiState.value.isAnswerSubmitted) return
        if (_uiState.value.userInput.isNotEmpty()) {
            _uiState.value = _uiState.value.copy(
                userInput = _uiState.value.userInput.dropLast(1)
            )
        }
    }

    fun clearDigit() {
        if (_uiState.value.isAnswerSubmitted) return
        _uiState.value = _uiState.value.copy(userInput = "")
    }

    fun submitAnswer() {
        val state = _uiState.value
        val problem = state.currentProblem ?: return
        if (state.userInput.isBlank() || state.isAnswerSubmitted) return

        val userVal = state.userInput.toLongOrNull()
        val isCorrect = (userVal == problem.correctAnswer)
        val addedPoints = if (isCorrect) (100 - state.revealedStepsCount * 15).coerceAtLeast(40) else 0

        _uiState.value = state.copy(
            isAnswerSubmitted = true,
            isAnswerCorrect = isCorrect,
            score = state.score + addedPoints,
            feedbackMessage = if (isCorrect) "CORRECT! +$addedPoints points" else "INCORRECT! Correct answer was ${problem.correctAnswer}"
        )
    }

    fun nextProblem() {
        val state = _uiState.value
        val nextIndex = state.currentProblemIndex + 1

        if (nextIndex < state.problemList.size) {
            val nextProblem = state.problemList[nextIndex]
            _uiState.value = state.copy(
                currentProblemIndex = nextIndex,
                currentProblem = nextProblem,
                revealedStepsCount = 0,
                userInput = "",
                isAnswerSubmitted = false,
                isAnswerCorrect = null,
                feedbackMessage = ""
            )
        } else {
            // Mode finished
            if (state.currentScreen == GameScreen.PRACTICE_ARENA) {
                startBossBattle()
            } else if (state.currentScreen == GameScreen.BOSS_BATTLE) {
                completeCurrentWorld()
            }
        }
    }

    private fun completeCurrentWorld() {
        val worldId = _uiState.value.selectedWorldId
        val finalScore = _uiState.value.score
        _uiState.value = _uiState.value.copy(
            currentScreen = GameScreen.REWARD
        )
        viewModelScope.launch {
            repository?.saveWorldCompletion(worldId, finalScore)
        }
    }

    fun returnToWorldSelect() {
        _uiState.value = _uiState.value.copy(
            currentScreen = GameScreen.WORLD_SELECT
        )
    }
}
