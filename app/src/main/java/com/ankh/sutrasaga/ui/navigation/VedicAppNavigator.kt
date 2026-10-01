package com.ankh.sutrasaga.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.ankh.sutrasaga.domain.models.GurukulScript
import com.ankh.sutrasaga.domain.models.SutraProblem
import com.ankh.sutrasaga.domain.models.UpaSutraId
import com.ankh.sutrasaga.ui.components.GurukulSceneScreen
import com.ankh.sutrasaga.ui.screens.GurukulScriptsRepository
import com.ankh.sutrasaga.ui.screens.HomeScreen
import com.ankh.sutrasaga.ui.screens.SampleSutraModules
import com.ankh.sutrasaga.ui.screens.SampleUrdhvaLesson
import com.ankh.sutrasaga.ui.screens.SutraLesson
import com.ankh.sutrasaga.ui.screens.SutraLessonsRepository
import com.ankh.sutrasaga.ui.screens.SutraModule
import com.ankh.sutrasaga.ui.screens.SutraSolverScreen
import com.ankh.sutrasaga.ui.screens.UpaSutraCodexScreen
import com.ankh.sutrasaga.ui.screens.UpaSutraQuestScreen
import com.ankh.sutrasaga.ui.screens.UpaSutraTreasuryScreen
import com.ankh.sutrasaga.ui.screens.VedicPracticeArenaScreen
import com.ankh.sutrasaga.ui.viewmodel.GameScreen
import com.ankh.sutrasaga.ui.viewmodel.GameUiState

enum class VedicAppRoute {
    HOME,
    GURUKUL_TUTORIAL,
    SUTRA_SOLVER,
    PRACTICE_ARENA,
    UPA_SUTRA_TREASURY,
    UPA_SUTRA_CODEX,
    UPA_SUTRA_QUEST
}

/**
 * VedicAppNavigator — Top-Level Navigation Engine & State Coordinator
 * Handles:
 * 1. HomeScreen -> Select Sutra -> GurukulSceneScreen (Guru-Shishya Tutorial) -> SutraSolverScreen
 * 2. HomeScreen -> Quick Start -> PracticeArenaScreen
 * 3. HomeScreen -> Treasury -> UpaSutraTreasuryScreen -> Codex / UpaSutraQuestScreen
 * 4. Back navigation across all sub-screens
 */
@Composable
fun VedicAppNavigator(
    initialRoute: VedicAppRoute = VedicAppRoute.HOME,
    uiState: GameUiState? = null,
    onCompleteLesson: (worldId: Int) -> Unit = {},
    onStartQuest: (UpaSutraId) -> Unit = {},
    onAdvanceQuestStage: () -> Unit = {},
    onAppendDigit: (Char) -> Unit = {},
    onBackspaceDigit: () -> Unit = {},
    onClearDigit: () -> Unit = {},
    onSubmitAnswer: () -> Unit = {},
    onNextProblem: () -> Unit = {},
    onRevealStep: () -> Unit = {},
    onReturnToTreasury: () -> Unit = {}
) {

    var currentRoute by remember { mutableStateOf(initialRoute) }
    var currentStreak by remember { mutableIntStateOf(7) }
    var selectedLesson by remember { mutableStateOf<SutraLesson>(SampleUrdhvaLesson) }
    var selectedScript by remember { mutableStateOf<GurukulScript>(GurukulScriptsRepository.allScripts.values.first()) }
    var selectedProblem by remember { mutableStateOf<SutraProblem>(GurukulScriptsRepository.getProblemForModule(SampleSutraModules.first())) }

    // Sync currentRoute with external uiState changes (e.g. quest start or completion)
    LaunchedEffect(uiState?.currentScreen) {
        when (uiState?.currentScreen) {
            GameScreen.UPA_SUTRA_QUEST -> {
                if (currentRoute != VedicAppRoute.UPA_SUTRA_QUEST) {
                    currentRoute = VedicAppRoute.UPA_SUTRA_QUEST
                }
            }
            GameScreen.UPA_SUTRA_TREASURY -> {
                if (currentRoute == VedicAppRoute.UPA_SUTRA_QUEST) {
                    currentRoute = VedicAppRoute.UPA_SUTRA_TREASURY
                }
            }
            else -> {}
        }
    }

    // System Back Navigation handling
    when (currentRoute) {
        VedicAppRoute.HOME -> {
            // Default system back
        }
        VedicAppRoute.GURUKUL_TUTORIAL,
        VedicAppRoute.SUTRA_SOLVER,
        VedicAppRoute.PRACTICE_ARENA,
        VedicAppRoute.UPA_SUTRA_TREASURY -> {
            BackHandler {
                currentRoute = VedicAppRoute.HOME
            }
        }
        VedicAppRoute.UPA_SUTRA_CODEX,
        VedicAppRoute.UPA_SUTRA_QUEST -> {
            BackHandler {
                onReturnToTreasury()
                currentRoute = VedicAppRoute.UPA_SUTRA_TREASURY
            }
        }
    }

    Crossfade(targetState = currentRoute, label = "VedicScreenNavigation") { route ->
        when (route) {
            VedicAppRoute.HOME -> {
                HomeScreen(
                    streakDays = currentStreak,
                    onSelectSutra = { module: SutraModule ->
                        selectedLesson = SutraLessonsRepository.getLessonForModule(module)
                        selectedScript = GurukulScriptsRepository.getScriptForModule(module)
                        selectedProblem = GurukulScriptsRepository.getProblemForModule(module)
                        currentRoute = VedicAppRoute.GURUKUL_TUTORIAL
                    },
                    onStartPracticeArena = {
                        currentRoute = VedicAppRoute.PRACTICE_ARENA
                    },
                    onOpenTreasury = {
                        currentRoute = VedicAppRoute.UPA_SUTRA_TREASURY
                    }
                )
            }

            VedicAppRoute.GURUKUL_TUTORIAL -> {
                GurukulSceneScreen(
                    worldId = selectedScript.worldId,
                    script = selectedScript,
                    problem = selectedProblem,
                    onComplete = {
                        currentRoute = VedicAppRoute.SUTRA_SOLVER
                    },
                    onBack = {
                        currentRoute = VedicAppRoute.HOME
                    }
                )
            }

            VedicAppRoute.SUTRA_SOLVER -> {
                SutraSolverScreen(
                    lesson = selectedLesson,
                    onBackClick = {
                        currentRoute = VedicAppRoute.HOME
                    },
                    onLessonComplete = {
                        val worldId = selectedScript.worldId
                        onCompleteLesson(worldId)
                        currentStreak++
                        currentRoute = VedicAppRoute.HOME
                    },
                    onReplayTutorial = {
                        currentRoute = VedicAppRoute.GURUKUL_TUTORIAL
                    }
                )
            }


            VedicAppRoute.PRACTICE_ARENA -> {
                VedicPracticeArenaScreen(
                    onBackClick = {
                        currentRoute = VedicAppRoute.HOME
                    },
                    onFinishArena = { score, opsPerMin ->
                        currentRoute = VedicAppRoute.HOME
                    }
                )
            }

            VedicAppRoute.UPA_SUTRA_TREASURY -> {
                UpaSutraTreasuryScreen(
                    state = uiState ?: GameUiState(),
                    onQuestClick = { questId: UpaSutraId ->
                        onStartQuest(questId)
                        currentRoute = VedicAppRoute.UPA_SUTRA_QUEST
                    },
                    onCodexClick = {
                        currentRoute = VedicAppRoute.UPA_SUTRA_CODEX
                    },
                    onBackClick = {
                        currentRoute = VedicAppRoute.HOME
                    }
                )
            }

            VedicAppRoute.UPA_SUTRA_CODEX -> {
                UpaSutraCodexScreen(
                    state = uiState ?: GameUiState(),
                    onBackClick = {
                        currentRoute = VedicAppRoute.UPA_SUTRA_TREASURY
                    }
                )
            }

            VedicAppRoute.UPA_SUTRA_QUEST -> {
                UpaSutraQuestScreen(
                    state = uiState ?: GameUiState(),
                    onContinueClick = onAdvanceQuestStage,
                    onDigitClick = onAppendDigit,
                    onBackspaceClick = onBackspaceDigit,
                    onClearClick = onClearDigit,
                    onSubmitClick = onSubmitAnswer,
                    onNextProblemClick = onNextProblem,
                    onRevealStepClick = onRevealStep,
                    onReturnToTreasuryClick = {
                        onReturnToTreasury()
                        currentRoute = VedicAppRoute.UPA_SUTRA_TREASURY
                    }
                )
            }
        }
    }
}
