package com.technopradyumn.flyingmarioo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao {

    @Query("SELECT * FROM scores ORDER BY id DESC")
    fun getAllScores(): Flow<List<Score>>

    @Insert
    suspend fun insertScore(score: Score)
}