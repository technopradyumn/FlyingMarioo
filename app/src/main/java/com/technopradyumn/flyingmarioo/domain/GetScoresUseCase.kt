package com.technopradyumn.flyingmarioo.domain

import com.technopradyumn.flyingmarioo.data.Score
import com.technopradyumn.flyingmarioo.data.ScoreDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetScoresUseCase (private val scoreDao: ScoreDao) {

    operator fun invoke(): Flow<List<Score>> = scoreDao.getAllScores()
}