package com.ankh.sutrasaga

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.ankh.sutrasaga.ui.screens.BossBattleScreen
import com.ankh.sutrasaga.ui.screens.PracticeArenaScreen
import com.ankh.sutrasaga.ui.screens.RewardScreen
import com.ankh.sutrasaga.ui.screens.StoryBeatScreen
import com.ankh.sutrasaga.ui.screens.TutorialScreen
import com.ankh.sutrasaga.ui.screens.WorldSelectScreen
import com.ankh.sutrasaga.ui.theme.AnkhTheme
import com.ankh.sutrasaga.ui.viewmodel.GameScreen
import com.ankh.sutrasaga.ui.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnkhTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val context = LocalContext.current
                    LaunchedEffect(Unit) {
                        viewModel.initRepository(context)
                    }
                    MainGameContent(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun MainGameContent(viewModel: GameViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    when (uiState.currentScreen) {
        GameScreen.WORLD_SELECT -> {
            WorldSelectScreen(
                state = uiState,
                onWorldClick = { worldId -> viewModel.selectWorld(worldId) }
            )
        }
        GameScreen.STORY_BEAT -> {
            StoryBeatScreen(
                worldId = uiState.selectedWorldId,
                onContinueClick = { viewModel.navigateToTutorial() }
            )
        }
        GameScreen.TUTORIAL -> {
            TutorialScreen(
                worldId = uiState.selectedWorldId,
                onComplete = { viewModel.startPractice() }
            )
        }
        GameScreen.PRACTICE_ARENA -> {
            PracticeArenaScreen(
                state = uiState,
                onRevealStepClick = { viewModel.revealNextStep() },
                onDigitClick = { digit -> viewModel.appendDigit(digit) },
                onBackspaceClick = { viewModel.backspaceDigit() },
                onClearClick = { viewModel.clearDigit() },
                onRemainderClick = { viewModel.appendRemainderSeparator() },
                onOrderedPairClick = { viewModel.appendOrderedPairSeparator() },
                onSubmitClick = { viewModel.submitAnswer() },
                onNextProblemClick = { viewModel.nextProblem() }
            )
        }
        GameScreen.BOSS_BATTLE -> {
            BossBattleScreen(
                state = uiState,
                onRevealStepClick = { viewModel.revealNextStep() },
                onDigitClick = { digit -> viewModel.appendDigit(digit) },
                onBackspaceClick = { viewModel.backspaceDigit() },
                onClearClick = { viewModel.clearDigit() },
                onRemainderClick = { viewModel.appendRemainderSeparator() },
                onOrderedPairClick = { viewModel.appendOrderedPairSeparator() },
                onSubmitClick = { viewModel.submitAnswer() },
                onNextProblemClick = { viewModel.nextProblem() }
            )
        }
        GameScreen.REWARD -> {
            RewardScreen(
                state = uiState,
                onReturnHomeClick = { viewModel.returnToWorldSelect() }
            )
        }
    }
}
