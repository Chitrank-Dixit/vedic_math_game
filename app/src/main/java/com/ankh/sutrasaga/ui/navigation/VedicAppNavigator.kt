package com.ankh.sutrasaga.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import com.ankh.sutrasaga.domain.models.UpaSutraId
import com.ankh.sutrasaga.ui.components.GurukulSceneScreen
import com.ankh.sutrasaga.ui.screens.HomeScreen
import com.ankh.sutrasaga.ui.screens.SutraModule
import com.ankh.sutrasaga.ui.screens.SutraSolverScreen
import com.ankh.sutrasaga.ui.screens.UpaSutraCodexScreen
import com.ankh.sutrasaga.ui.screens.UpaSutraQuestScreen
import com.ankh.sutrasaga.ui.screens.UpaSutraTreasuryScreen
import com.ankh.sutrasaga.ui.screens.VedicPracticeArenaScreen
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
 * VedicAppNavigator — Authoritative UI Navigation Coordinator.
 * Pure rendering engine driven by GameUiState.
 * Contains zero duplicate state machines and zero synchronization effects.
 */
@Composable
fun VedicAppNavigator(
    initialRoute: VedicAppRoute = VedicAppRoute.HOME,
    uiState: GameUiState? = null,
    onNavigate: (VedicAppRoute) -> Unit = {},
    onSelectSutra: (worldId: Int) -> Unit = {},
    onCompleteLesson: (worldId: Int) -> Unit = {},
    onSolverStepAnswered: (isCorrect: Boolean) -> Unit = {},
    onStartQuest: (UpaSutraId) -> Unit = {},
    onAdvanceQuestStage: () -> Unit = {},
    onAppendDigit: (Char) -> Unit = {},
    onBackspaceDigit: () -> Unit = {},
    onClearDigit: () -> Unit = {},
    onSubmitAnswer: () -> Unit = {},
    onNextProblem: () -> Unit = {},
    onRevealStep: () -> Unit = {},
    onReturnToTreasury: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val state = uiState ?: GameUiState(currentRoute = initialRoute)
    val currentRoute = state.currentRoute

    // Authoritative Android System Back Navigation handling
    when (currentRoute) {
        VedicAppRoute.HOME -> {
            // Default system back: allows exiting the app cleanly
        }
        VedicAppRoute.GURUKUL_TUTORIAL,
        VedicAppRoute.SUTRA_SOLVER,
        VedicAppRoute.PRACTICE_ARENA,
        VedicAppRoute.UPA_SUTRA_TREASURY,
        VedicAppRoute.UPA_SUTRA_CODEX,
        VedicAppRoute.UPA_SUTRA_QUEST -> {
            BackHandler {
                onBack()
            }
        }
    }

    Crossfade(targetState = currentRoute, label = "VedicScreenNavigation") { route ->
        when (route) {
            VedicAppRoute.HOME -> {
                HomeScreen(
                    streakDays = state.currentStreak,
                    modules = state.sutraModules,
                    onSelectSutra = { module: SutraModule ->
                        onSelectSutra(module.worldNumber)
                    },
                    onStartPracticeArena = {
                        onNavigate(VedicAppRoute.PRACTICE_ARENA)
                    },
                    onOpenTreasury = {
                        onNavigate(VedicAppRoute.UPA_SUTRA_TREASURY)
                    }
                )
            }

            VedicAppRoute.GURUKUL_TUTORIAL -> {
                GurukulSceneScreen(
                    worldId = state.selectedScript.worldId,
                    script = state.selectedScript,
                    problem = state.selectedLessonProblem,
                    onComplete = {
                        onNavigate(VedicAppRoute.SUTRA_SOLVER)
                    },
                    onBack = {
                        onBack()
                    }
                )
            }

            VedicAppRoute.SUTRA_SOLVER -> {
                SutraSolverScreen(
                    lesson = state.selectedLesson,
                    score = state.score,
                    onBackClick = {
                        onBack()
                    },
                    onStepAnswered = onSolverStepAnswered,
                    onLessonComplete = {
                        onCompleteLesson(state.selectedWorldId)
                    },
                    onReplayTutorial = {
                        onNavigate(VedicAppRoute.GURUKUL_TUTORIAL)
                    }
                )
            }

            VedicAppRoute.PRACTICE_ARENA -> {
                VedicPracticeArenaScreen(
                    onBackClick = {
                        onBack()
                    },
                    onFinishArena = { _, _ ->
                        onNavigate(VedicAppRoute.HOME)
                    }
                )
            }

            VedicAppRoute.UPA_SUTRA_TREASURY -> {
                UpaSutraTreasuryScreen(
                    state = state,
                    onQuestClick = { questId: UpaSutraId ->
                        onStartQuest(questId)
                    },
                    onCodexClick = {
                        onNavigate(VedicAppRoute.UPA_SUTRA_CODEX)
                    },
                    onBackClick = {
                        onBack()
                    }
                )
            }

            VedicAppRoute.UPA_SUTRA_CODEX -> {
                UpaSutraCodexScreen(
                    state = state,
                    onBackClick = {
                        onBack()
                    }
                )
            }

            VedicAppRoute.UPA_SUTRA_QUEST -> {
                UpaSutraQuestScreen(
                    state = state,
                    onContinueClick = onAdvanceQuestStage,
                    onDigitClick = onAppendDigit,
                    onBackspaceClick = onBackspaceDigit,
                    onClearClick = onClearDigit,
                    onSubmitClick = onSubmitAnswer,
                    onNextProblemClick = onNextProblem,
                    onRevealStepClick = onRevealStep,
                    onReturnToTreasuryClick = {
                        onReturnToTreasury()
                    }
                )
            }
        }
    }
}
