package com.technopradyumn.flyingmarioo.data

import kotlinx.coroutines.flow.Flow

class ScoreRepository(private val scoreDao: ScoreDao) {
    fun getAllScores(): Flow<List<Score>> = scoreDao.getAllScores()

    suspend fun insertScore(score: Score) =scoreDao.insertScore(score)
}