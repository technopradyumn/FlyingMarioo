package com.technopradyumn.flyingmarioo.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "scores")
data class Score(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "score") val scoreValue: Int,
    @ColumnInfo(name = "game_over_time") val gameOverTime: String = SimpleDateFormat(
        "HH:mm:ss dd-MM-yyyy",
        Locale.getDefault()
    ).format(Date())
)