package com.ankh.sutrasaga.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.ankh.sutrasaga.data.db.AppDatabase
import com.ankh.sutrasaga.data.repository.GameRepository
import com.ankh.sutrasaga.domain.models.AnswerFormat
import com.ankh.sutrasaga.domain.models.DifficultyTier
import com.ankh.sutrasaga.domain.models.SutraProblem
import com.ankh.sutrasaga.domain.models.UpaSutraCompletionState
import com.ankh.sutrasaga.domain.models.UpaSutraId
import com.ankh.sutrasaga.domain.models.UpaSutraProgress
import com.ankh.sutrasaga.engine.AnurupyeShunyamanyatGenerator
import com.ankh.sutrasaga.engine.ChalanaKalanabhyamGenerator
import com.ankh.sutrasaga.engine.EkadhikenaPurvenaGenerator
import com.ankh.sutrasaga.engine.EkanyunenaGenerator
import com.ankh.sutrasaga.engine.GunakasamuccayahGenerator
import com.ankh.sutrasaga.engine.GunitasamuccayahGenerator
import com.ankh.sutrasaga.engine.NikhilamGenerator
import com.ankh.sutrasaga.engine.ParavartyaYojayetGenerator
import com.ankh.sutrasaga.engine.PuranapuranabhyamGenerator
import com.ankh.sutrasaga.engine.SankalanaVyavakalanabhyamGenerator
import com.ankh.sutrasaga.engine.ShesanyankenaCharamenaGenerator
import com.ankh.sutrasaga.engine.ShunyamSamyasamuccayeGenerator
import com.ankh.sutrasaga.engine.SopantyadvayamantyamGenerator
import com.ankh.sutrasaga.engine.SutraProblemGenerator
import com.ankh.sutrasaga.engine.UpaSutraGenerator
import com.ankh.sutrasaga.engine.UrdhvaTiryagbhyamGenerator
import com.ankh.sutrasaga.engine.VyashtisamashtihGenerator
import com.ankh.sutrasaga.engine.YavadunamGenerator
import com.ankh.sutrasaga.engine.upasutras.AdyamadyenantyamantyenaGenerator
import com.ankh.sutrasaga.engine.upasutras.AntyayordashakepiGenerator
import com.ankh.sutrasaga.engine.upasutras.AntyayerevaGenerator
import com.ankh.sutrasaga.engine.upasutras.AnurupyenaGenerator
import com.ankh.sutrasaga.engine.upasutras.KevalaihSaptakamGunyatGenerator
import com.ankh.sutrasaga.engine.upasutras.LopanasthapanabhyamGenerator
import com.ankh.sutrasaga.engine.upasutras.SamuccayagunitahGenerator
import com.ankh.sutrasaga.engine.upasutras.SisyateSesasamjnahGenerator
import com.ankh.sutrasaga.engine.upasutras.VestanamGenerator
import com.ankh.sutrasaga.engine.upasutras.VilokanamGenerator
import com.ankh.sutrasaga.engine.upasutras.YavadunamRemixGenerator
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
    REWARD,
    UPA_SUTRA_TREASURY,
    UPA_SUTRA_CODEX,
    UPA_SUTRA_QUEST
}

enum class UpaSutraQuestStage {
    STORY_BEAT,
    GUIDED_EXAMPLE,
    PRACTICE,
    CHALLENGE,
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
    val isWorld4Completed: Boolean = false,
    val world4BestScore: Int = 0,
    val isWorld5Completed: Boolean = false,
    val world5BestScore: Int = 0,
    val isWorld6Completed: Boolean = false,
    val world6BestScore: Int = 0,
    val isWorld7Completed: Boolean = false,
    val world7BestScore: Int = 0,
    val isWorld8Completed: Boolean = false,
    val world8BestScore: Int = 0,
    val isWorld9Completed: Boolean = false,
    val world9BestScore: Int = 0,
    val isWorld10Completed: Boolean = false,
    val world10BestScore: Int = 0,
    val isWorld11Completed: Boolean = false,
    val world11BestScore: Int = 0,
    val isWorld12Completed: Boolean = false,
    val world12BestScore: Int = 0,
    val isWorld13Completed: Boolean = false,
    val world13BestScore: Int = 0,
    val isWorld14Completed: Boolean = false,
    val world14BestScore: Int = 0,
    val isWorld15Completed: Boolean = false,
    val world15BestScore: Int = 0,
    val isWorld16Completed: Boolean = false,
    val world16BestScore: Int = 0,
    val currentProblemIndex: Int = 0,
    val totalProblemsInMode: Int = 5,
    val problemList: List<SutraProblem> = emptyList(),
    val currentProblem: SutraProblem? = null,
    val revealedStepsCount: Int = 0,
    val userInput: String = "",
    val score: Int = 0,
    val isAnswerSubmitted: Boolean = false,
    val isAnswerCorrect: Boolean? = null,
    val feedbackMessage: String = "",
    // Upa-Sutra Treasury state
    val selectedUpaSutraId: UpaSutraId? = null,
    val questStage: UpaSutraQuestStage = UpaSutraQuestStage.STORY_BEAT,
    val upaSutraProgressMap: Map<UpaSutraId, UpaSutraProgress> = emptyMap(),
    val questPracticeCorrectCount: Int = 0,
    val questChallengeCorrectCount: Int = 0
)

class GameViewModel : ViewModel() {

    private val ekadhikenaGenerator = EkadhikenaPurvenaGenerator()
    private val nikhilamGenerator = NikhilamGenerator()
    private val ekanyunenaGenerator = EkanyunenaGenerator()
    private val yavadunamGenerator = YavadunamGenerator()
    private val urdhvaGenerator = UrdhvaTiryagbhyamGenerator()
    private val paravartyaGenerator = ParavartyaYojayetGenerator()
    private val anurupyeGenerator = AnurupyeShunyamanyatGenerator()
    private val sankalanaGenerator = SankalanaVyavakalanabhyamGenerator()
    private val shunyamGenerator = ShunyamSamyasamuccayeGenerator()
    private val puranapuranabhyamGenerator = PuranapuranabhyamGenerator()
    private val vyashtisamashtihGenerator = VyashtisamashtihGenerator()
    private val shesanyankenaGenerator = ShesanyankenaCharamenaGenerator()
    private val sopantyadvayamantyamGenerator = SopantyadvayamantyamGenerator()
    private val gunitasamuccayahGenerator = GunitasamuccayahGenerator()
    private val gunakasamuccayahGenerator = GunakasamuccayahGenerator()
    private val chalanaKalanabhyamGenerator = ChalanaKalanabhyamGenerator()

    // Upa-Sutra Generators
    private val antyayordashakepiGenerator = AntyayordashakepiGenerator()
    private val antyayerevaGenerator = AntyayerevaGenerator()
    private val samuccayagunitahGenerator = SamuccayagunitahGenerator()
    private val anurupyenaGenerator = AnurupyenaGenerator()
    private val yavadunamRemixGenerator = YavadunamRemixGenerator()
    private val adyamadyaGenerator = AdyamadyenantyamantyenaGenerator()
    private val vestanamGenerator = VestanamGenerator()
    private val sisyateGenerator = SisyateSesasamjnahGenerator()
    private val kevalaihGenerator = KevalaihSaptakamGunyatGenerator()
    private val lopanaGenerator = LopanasthapanabhyamGenerator()
    private val vilokanamGenerator = VilokanamGenerator()

    private var repository: GameRepository? = null

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    fun initRepository(context: Context) {
        if (repository == null) {
            val db = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "ankh_game_db"
            ).fallbackToDestructiveMigration().build()
            repository = GameRepository(db.userProgressDao(), db.upaSutraProgressDao())
            observeProgress()
        }
    }

    private fun observeProgress() {
        viewModelScope.launch {
            repository?.getAllProgress()?.collect { list ->
                val w1 = list.find { it.worldId == 1 }
                val w2 = list.find { it.worldId == 2 }
                val w3 = list.find { it.worldId == 3 }
                val w4 = list.find { it.worldId == 4 }
                val w5 = list.find { it.worldId == 5 }
                val w6 = list.find { it.worldId == 6 }
                val w7 = list.find { it.worldId == 7 }
                val w8 = list.find { it.worldId == 8 }
                val w9 = list.find { it.worldId == 9 }
                val w10 = list.find { it.worldId == 10 }
                val w11 = list.find { it.worldId == 11 }
                val w12 = list.find { it.worldId == 12 }
                val w13 = list.find { it.worldId == 13 }
                val w14 = list.find { it.worldId == 14 }
                val w15 = list.find { it.worldId == 15 }
                val w16 = list.find { it.worldId == 16 }

                _uiState.value = _uiState.value.copy(
                    isWorld1Completed = w1?.isCompleted ?: false,
                    world1BestScore = w1?.bestScore ?: 0,
                    isWorld2Completed = w2?.isCompleted ?: false,
                    world2BestScore = w2?.bestScore ?: 0,
                    isWorld3Completed = w3?.isCompleted ?: false,
                    world3BestScore = w3?.bestScore ?: 0,
                    isWorld4Completed = w4?.isCompleted ?: false,
                    world4BestScore = w4?.bestScore ?: 0,
                    isWorld5Completed = w5?.isCompleted ?: false,
                    world5BestScore = w5?.bestScore ?: 0,
                    isWorld6Completed = w6?.isCompleted ?: false,
                    world6BestScore = w6?.bestScore ?: 0,
                    isWorld7Completed = w7?.isCompleted ?: false,
                    world7BestScore = w7?.bestScore ?: 0,
                    isWorld8Completed = w8?.isCompleted ?: false,
                    world8BestScore = w8?.bestScore ?: 0,
                    isWorld9Completed = w9?.isCompleted ?: false,
                    world9BestScore = w9?.bestScore ?: 0,
                    isWorld10Completed = w10?.isCompleted ?: false,
                    world10BestScore = w10?.bestScore ?: 0,
                    isWorld11Completed = w11?.isCompleted ?: false,
                    world11BestScore = w11?.bestScore ?: 0,
                    isWorld12Completed = w12?.isCompleted ?: false,
                    world12BestScore = w12?.bestScore ?: 0,
                    isWorld13Completed = w13?.isCompleted ?: false,
                    world13BestScore = w13?.bestScore ?: 0,
                    isWorld14Completed = w14?.isCompleted ?: false,
                    world14BestScore = w14?.bestScore ?: 0,
                    isWorld15Completed = w15?.isCompleted ?: false,
                    world15BestScore = w15?.bestScore ?: 0,
                    isWorld16Completed = w16?.isCompleted ?: false,
                    world16BestScore = w16?.bestScore ?: 0
                )
            }
        }

        viewModelScope.launch {
            repository?.getAllUpaSutraProgress()?.collect { progressList ->
                val progressMap = progressList.associateBy { it.id }
                _uiState.value = _uiState.value.copy(
                    upaSutraProgressMap = progressMap
                )
            }
        }
    }

    private fun getGeneratorForWorld(worldId: Int): SutraProblemGenerator {
        return when (worldId) {
            1 -> ekadhikenaGenerator
            2 -> nikhilamGenerator
            3 -> ekanyunenaGenerator
            4 -> yavadunamGenerator
            5 -> urdhvaGenerator
            6 -> paravartyaGenerator
            7 -> anurupyeGenerator
            8 -> sankalanaGenerator
            9 -> shunyamGenerator
            10 -> puranapuranabhyamGenerator
            11 -> vyashtisamashtihGenerator
            12 -> shesanyankenaGenerator
            13 -> sopantyadvayamantyamGenerator
            14 -> gunitasamuccayahGenerator
            15 -> gunakasamuccayahGenerator
            16 -> chalanaKalanabhyamGenerator
            else -> ekadhikenaGenerator
        }
    }

    private fun getGeneratorForUpaSutra(id: UpaSutraId): UpaSutraGenerator {
        return when (id) {
            UpaSutraId.ANTYAYORDASHAKEPI -> antyayordashakepiGenerator
            UpaSutraId.ANTYAYEREVA -> antyayerevaGenerator
            UpaSutraId.SAMUCCAYAGUNITAH -> samuccayagunitahGenerator
            UpaSutraId.ANURUPYENA -> anurupyenaGenerator
            UpaSutraId.YAVADUNAM_TAVADUNIKRTYA_VARGANCHA_YOJAYET,
            UpaSutraId.YAVADUNAM_TAVADUNAM -> yavadunamRemixGenerator
            UpaSutraId.ADYAMADYENANTYAMANTYENA -> adyamadyaGenerator
            UpaSutraId.VESHTANAM -> vestanamGenerator
            UpaSutraId.SHISYATE_SHESAMAJNA -> sisyateGenerator
            UpaSutraId.KEVALAIHSAPTAKAM_GUNYAT -> kevalaihGenerator
            UpaSutraId.LOPANA_STHAPANABHYAM -> lopanaGenerator
            UpaSutraId.VILOKANAM -> vilokanamGenerator
            UpaSutraId.GUNITASAMUCCAYAH_SAMUCCAYAGUNITAH -> samuccayagunitahGenerator
        }
    }

    fun selectWorld(worldId: Int) {
        if (worldId in 1..16) {
            _uiState.value = _uiState.value.copy(
                selectedWorldId = worldId,
                currentScreen = GameScreen.STORY_BEAT
            )
        }
    }

    fun openTreasury() {
        _uiState.value = _uiState.value.copy(
            currentScreen = GameScreen.UPA_SUTRA_TREASURY
        )
    }

    fun openCodex() {
        _uiState.value = _uiState.value.copy(
            currentScreen = GameScreen.UPA_SUTRA_CODEX
        )
    }

    fun startUpaSutraQuest(id: UpaSutraId) {
        _uiState.value = _uiState.value.copy(
            selectedUpaSutraId = id,
            questStage = UpaSutraQuestStage.STORY_BEAT,
            currentScreen = GameScreen.UPA_SUTRA_QUEST,
            score = 0,
            questPracticeCorrectCount = 0,
            questChallengeCorrectCount = 0
        )
    }

    fun advanceQuestStage() {
        val currentStage = _uiState.value.questStage
        val upaSutraId = _uiState.value.selectedUpaSutraId ?: UpaSutraId.ANTYAYORDASHAKEPI
        val generator = getGeneratorForUpaSutra(upaSutraId)

        when (currentStage) {
            UpaSutraQuestStage.STORY_BEAT -> {
                _uiState.value = _uiState.value.copy(
                    questStage = UpaSutraQuestStage.GUIDED_EXAMPLE
                )
            }
            UpaSutraQuestStage.GUIDED_EXAMPLE -> {
                val practiceProblems = generator.generateQuestPracticeSet()
                _uiState.value = _uiState.value.copy(
                    questStage = UpaSutraQuestStage.PRACTICE,
                    problemList = practiceProblems,
                    currentProblemIndex = 0,
                    totalProblemsInMode = practiceProblems.size,
                    currentProblem = practiceProblems.firstOrNull(),
                    revealedStepsCount = 0,
                    userInput = "",
                    isAnswerSubmitted = false,
                    isAnswerCorrect = null,
                    feedbackMessage = "",
                    questPracticeCorrectCount = 0
                )
            }
            UpaSutraQuestStage.PRACTICE -> {
                val challengeProblems = generator.generateQuestChallengeSet()
                _uiState.value = _uiState.value.copy(
                    questStage = UpaSutraQuestStage.CHALLENGE,
                    problemList = challengeProblems,
                    currentProblemIndex = 0,
                    totalProblemsInMode = challengeProblems.size,
                    currentProblem = challengeProblems.firstOrNull(),
                    revealedStepsCount = 0,
                    userInput = "",
                    isAnswerSubmitted = false,
                    isAnswerCorrect = null,
                    feedbackMessage = "",
                    questChallengeCorrectCount = 0
                )
            }
            UpaSutraQuestStage.CHALLENGE -> {
                completeQuest(upaSutraId)
            }
            UpaSutraQuestStage.REWARD -> {
                openTreasury()
            }
        }
    }

    private fun completeQuest(upaSutraId: UpaSutraId) {
        val practiceCount = _uiState.value.questPracticeCorrectCount
        val challengeCount = _uiState.value.questChallengeCorrectCount
        val newState = if (challengeCount >= 2) {
            UpaSutraCompletionState.MASTERED
        } else {
            UpaSutraCompletionState.PRACTICED
        }

        _uiState.value = _uiState.value.copy(
            questStage = UpaSutraQuestStage.REWARD
        )

        repository?.let { repo ->
            viewModelScope.launch {
                repo.saveUpaSutraProgress(
                    UpaSutraProgress(
                        id = upaSutraId,
                        state = newState,
                        practiceCorrectCount = practiceCount,
                        challengeCorrectCount = challengeCount,
                        lastAttemptTimestamp = System.currentTimeMillis()
                    )
                )
            }
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
        // Only accept valid digits '0'..'9' or leading minus sign '-'
        if (!char.isDigit() && char != '-') return
        if (char == '-' && _uiState.value.userInput.isNotEmpty()) return
        if (_uiState.value.userInput.length < 16) {
            _uiState.value = _uiState.value.copy(
                userInput = _uiState.value.userInput + char
            )
        }
    }

    fun appendRemainderSeparator() {
        if (_uiState.value.isAnswerSubmitted) return
        if (_uiState.value.userInput.length < 16 && !_uiState.value.userInput.contains("R", ignoreCase = true)) {
            _uiState.value = _uiState.value.copy(
                userInput = _uiState.value.userInput + " R "
            )
        }
    }

    fun appendOrderedPairSeparator() {
        if (_uiState.value.isAnswerSubmitted) return
        if (_uiState.value.userInput.length < 16 && !_uiState.value.userInput.contains(',')) {
            _uiState.value = _uiState.value.copy(
                userInput = _uiState.value.userInput + ", "
            )
        }
    }

    fun backspaceDigit() {
        if (_uiState.value.isAnswerSubmitted) return
        val input = _uiState.value.userInput
        if (input.isNotEmpty()) {
            val newLength = when {
                input.endsWith(" R ") -> input.length - 3
                input.endsWith(", ") -> input.length - 2
                else -> input.length - 1
            }
            _uiState.value = _uiState.value.copy(
                userInput = input.substring(0, newLength)
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

        val isCorrect = when (problem.answerFormat) {
            AnswerFormat.QUOTIENT_AND_REMAINDER -> {
                val parts = state.userInput.split(Regex("(?i)\\s*R\\s*"))
                if (parts.size == 2) {
                    val q = parts[0].trim().toLongOrNull()
                    val r = parts[1].trim().toLongOrNull()
                    q == problem.correctAnswer && r == problem.expectedRemainder
                } else false
            }
            AnswerFormat.ORDERED_PAIR -> {
                val parts = state.userInput.split(Regex("\\s*,\\s*"))
                if (parts.size == 2) {
                    val x = parts[0].trim().toLongOrNull()
                    val y = parts[1].trim().toLongOrNull()
                    val correctPair = setOf(problem.correctAnswer, problem.expectedSecondaryAnswer)
                    setOf(x, y) == correctPair
                } else false
            }
            AnswerFormat.INTEGER -> {
                val userVal = state.userInput.trim().toLongOrNull()
                userVal == problem.correctAnswer || userVal == problem.expectedSecondaryAnswer
            }
        }

        val addedPoints = if (isCorrect) (100 - state.revealedStepsCount * 15).coerceAtLeast(40) else 0

        val expectedDisplay = when (problem.answerFormat) {
            AnswerFormat.QUOTIENT_AND_REMAINDER -> "${problem.correctAnswer} R ${problem.expectedRemainder}"
            AnswerFormat.ORDERED_PAIR -> "${problem.correctAnswer}, ${problem.expectedSecondaryAnswer}"
            AnswerFormat.INTEGER -> "${problem.correctAnswer}"
        }

        val newPracticeCount = if (state.currentScreen == GameScreen.UPA_SUTRA_QUEST && state.questStage == UpaSutraQuestStage.PRACTICE && isCorrect) {
            state.questPracticeCorrectCount + 1
        } else state.questPracticeCorrectCount

        val newChallengeCount = if (state.currentScreen == GameScreen.UPA_SUTRA_QUEST && state.questStage == UpaSutraQuestStage.CHALLENGE && isCorrect) {
            state.questChallengeCorrectCount + 1
        } else state.questChallengeCorrectCount

        _uiState.value = state.copy(
            isAnswerSubmitted = true,
            isAnswerCorrect = isCorrect,
            score = state.score + addedPoints,
            feedbackMessage = if (isCorrect) "CORRECT! +$addedPoints points" else "INCORRECT! Correct answer was $expectedDisplay",
            questPracticeCorrectCount = newPracticeCount,
            questChallengeCorrectCount = newChallengeCount
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
            } else if (state.currentScreen == GameScreen.UPA_SUTRA_QUEST) {
                advanceQuestStage()
            }
        }
    }

    private fun completeCurrentWorld() {
        val worldId = _uiState.value.selectedWorldId
        val finalScore = _uiState.value.score
        _uiState.value = _uiState.value.copy(
            currentScreen = GameScreen.REWARD
        )
        repository?.let { repo ->
            viewModelScope.launch {
                repo.saveWorldCompletion(worldId, finalScore)
            }
        }
    }

    fun returnToWorldSelect() {
        _uiState.value = _uiState.value.copy(
            currentScreen = GameScreen.WORLD_SELECT
        )
    }
}
