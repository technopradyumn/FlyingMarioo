package com.technopradyumn.flyingmarioo.data

import com.technopradyumn.flyingmarioo.domain.GetScoresUseCase
import com.technopradyumn.flyingmarioo.domain.InsertScoreUseCase
import kotlinx.coroutines.flow.Flow

class ScoreRepository (
    scoreDao: ScoreDao
) {

    private val getScoresUseCase = GetScoresUseCase(scoreDao)
    private val insertScoreUseCase = InsertScoreUseCase(scoreDao)

    fun getAllScores(): Flow<List<Score>> = getScoresUseCase()

    suspend fun insertScore(score: Score) = insertScoreUseCase(score)
}