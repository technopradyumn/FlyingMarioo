package com.technopradyumn.flyingmarioo.domain

import com.technopradyumn.flyingmarioo.data.Score
import com.technopradyumn.flyingmarioo.data.ScoreDao
import javax.inject.Inject

class InsertScoreUseCase (private val scoreDao: ScoreDao) {

    suspend operator fun invoke(score: Score) = scoreDao.insertScore(score)
}