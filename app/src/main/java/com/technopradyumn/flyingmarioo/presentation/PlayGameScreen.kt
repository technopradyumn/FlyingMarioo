package com.technopradyumn.flyingmarioo.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.technopradyumn.flyingmarioo.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PlayGameScreen(
    navHostController: NavHostController,
) {
    val gameViewModel: GameViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()
    val mariooY by remember { gameViewModel::mariooY }
    val poles by remember { gameViewModel::poles }
    val gameState by remember { gameViewModel::gameState }
    val score by remember { gameViewModel::score }
    var showGameOverDialog by remember { mutableStateOf(false) }

    LaunchedEffect(gameState) {
        when (gameState) {
            GameViewModel.GameState.RUNNING -> {
                gameViewModel.playBackgroundMusic()
                coroutineScope.launch {
                    while (gameState == GameViewModel.GameState.RUNNING) {
                        gameViewModel.updateGame()
                        delay(16L)
                    }
                }
            }
            GameViewModel.GameState.PAUSED, GameViewModel.GameState.GAME_OVER -> {
                gameViewModel.pauseBackgroundMusic()
                if (gameState == GameViewModel.GameState.GAME_OVER) {
                    showGameOverDialog = true
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                if (gameState == GameViewModel.GameState.RUNNING) gameViewModel.onJump()
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = "Background",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Text(
            text = score.toString(),
            color = Color.Yellow,
            fontSize = 48.sp,
            modifier = Modifier
                .padding(32.dp)
                .align(Alignment.TopCenter)
        )

        poles.forEach { pole ->
            val poleHeight = max(0.dp, pole.height.dp)

            val poleLeft = pole.x.dp

            Image(
                painter = painterResource(id = R.drawable.pole_ic),
                contentDescription = "Top Pole",
                modifier = Modifier
                    .offset(x = poleLeft, y = 0.dp)
                    .size(width = 72.dp, height = poleHeight)
            )

            val bottomPoleTop = max(0.dp, (poleHeight + 300.dp))

            Image(
                painter = painterResource(id = R.drawable.pole_ic),
                contentDescription = "Bottom Pole",
                modifier = Modifier
                    .offset(x = poleLeft, y = bottomPoleTop)
                    .size(width = 72.dp, height = max(0.dp, (800 - (pole.height + 300)).dp))
            )
        }

        Image(
            painter = painterResource(id = R.drawable.supermarioo),
            contentDescription = "Marioo",
            modifier = Modifier
                .padding(start = 100.dp, top = mariooY.dp)
                .size(72.dp)
                .align(Alignment.TopStart)
        )

        if (showGameOverDialog) {
            AlertDialog(
                onDismissRequest = {
                    gameViewModel.resetGame()
                    gameViewModel.pauseGame()
                    showGameOverDialog = false
                },
                title = { Text(text = "Game Over") },
                text = { Text(text = "Your score: $score") },
                confirmButton = {
                    Button(onClick = {
                        gameViewModel.resetGame()
                        showGameOverDialog = false
                    }) {
                        Text("Try Again")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        navHostController.popBackStack()
                        gameViewModel.pauseBackgroundMusic()
                        showGameOverDialog = false
                    }) {
                        Text("Quit Game")
                    }
                }
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                gameViewModel.pauseGame()
            }) {
                Text(if (gameState == GameViewModel.GameState.RUNNING) "Pause" else "Start")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlayGameScreenPreview() {
    val navHostController = rememberNavController()
    PlayGameScreen(navHostController = navHostController)
}