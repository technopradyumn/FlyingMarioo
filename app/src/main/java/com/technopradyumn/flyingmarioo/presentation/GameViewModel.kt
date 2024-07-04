package com.technopradyumn.flyingmarioo.presentation

import android.app.Application
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.technopradyumn.flyingmarioo.R
import com.technopradyumn.flyingmarioo.data.Score
import com.technopradyumn.flyingmarioo.data.ScoreRepository
import com.technopradyumn.flyingmarioo.data.database.ScoreDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class GameViewModel(application: Application) : AndroidViewModel(application) {

    var mariooY by mutableFloatStateOf(300f)
        private set
    var poles by mutableStateOf(listOf<Pole>())
        private set
    var gameState by mutableStateOf(GameState.RUNNING)
        private set
    var isGameOver by mutableStateOf(false)
        private set
    var score by mutableIntStateOf(0)
        private set

    private val scoreDao = ScoreDatabase.getDatabase(application).scoreDao()
    private val scoreRepository = ScoreRepository(scoreDao)

    private val _scores = MutableStateFlow<List<Score>>(emptyList())
    val scores: StateFlow<List<Score>> = _scores.asStateFlow()

    private val gravity = 4f
    private val jumpHeight = 80f

    private val poleGap = 400f
    private val mariooSize = 72f
    private val poleWidth = 100f
    private val screenWidth = 800f
    private val screenHeight = 800f
    private val poleSpacing = screenWidth / 2

    private var backgroundMusicPlayer: MediaPlayer? = null

    init {
        resetGame()
        viewModelScope.launch {
            loadScores()
        }
    }

    fun playBackgroundMusic() {
        if (gameState == GameState.RUNNING) {
            if (backgroundMusicPlayer == null) {
                backgroundMusicPlayer = MediaPlayer.create(getApplication(), R.raw.music2)
                backgroundMusicPlayer?.isLooping = true
            }
            backgroundMusicPlayer?.start()
        }
    }

    fun pauseBackgroundMusic() {
        backgroundMusicPlayer?.pause()
    }

    fun stopAllSounds() {
        backgroundMusicPlayer?.pause()
    }

    fun resetGame() {
        mariooY = screenHeight / 2
        poles = generatePoles()
        gameState = GameState.RUNNING
        isGameOver = false
        score = 0
        pauseBackgroundMusic()
    }

    fun onJump() {
        if (gameState == GameState.RUNNING) {
            mariooY = max(mariooY - jumpHeight, 0f)
        }
    }

    fun updateGame() {
        if (gameState == GameState.RUNNING) {
            mariooY = min(mariooY + gravity, screenHeight - mariooSize)
            val updatedPoles = poles.map { it.copy(x = it.x - 4f) }
            var newScore = score

            if (updatedPoles.isNotEmpty() && updatedPoles.last().x < screenWidth - poleSpacing) {
                poles = updatedPoles.filter { it.x >= -poleWidth } + generatePole(screenWidth)
                newScore++
            } else {
                poles = updatedPoles.filter { it.x >= -poleWidth }
                if (updatedPoles.any { it.x + 4f < 100f && it.x >= 100f }) {
                    newScore++
                }
            }

            if (checkCollision()) {
                gameState = GameState.GAME_OVER
                isGameOver = true
                stopAllSounds()
                if (score > 0) {
                    addScore(score)
                }
            } else {
                score = newScore
            }
        }
    }

    fun pauseGame() {
        gameState = when (gameState) {
            GameState.RUNNING -> {
                pauseBackgroundMusic()
                GameState.PAUSED
            }
            GameState.PAUSED -> {
                playBackgroundMusic()
                GameState.RUNNING
            }
            GameState.GAME_OVER -> GameState.GAME_OVER
        }
    }

    private fun checkCollision(): Boolean {
        if (mariooY < 0 || mariooY + mariooSize > screenHeight) {
            stopAllSounds()
            return true
        }

        for (pole in poles) {
            if (pole.x < 100 + mariooSize && pole.x + poleWidth > 100) {
                if (mariooY < pole.height || mariooY + mariooSize > pole.height + poleGap) {
                    stopAllSounds()
                    return true
                }
            }
        }
        return false
    }

    private fun generatePoles(): List<Pole> {
        return List(3) { generatePole(screenWidth + it * poleSpacing) }
    }

    private fun generatePole(startX: Float): Pole {
        val height = (100..500).random().toFloat()
        return Pole(x = abs(startX.toInt()).toFloat(), height = height)
    }

    override fun onCleared() {
        super.onCleared()
        backgroundMusicPlayer?.release()
    }

    data class Pole(val x: Float, val height: Float)

    enum class GameState {
        RUNNING, PAUSED, GAME_OVER
    }

    private fun loadScores() {
        viewModelScope.launch {
            scoreRepository.getAllScores().collect {
                _scores.value = it
            }
        }
    }

    fun addScore(score: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val newScore = Score(scoreValue = score)
            scoreRepository.insertScore(newScore)
        }
    }

}