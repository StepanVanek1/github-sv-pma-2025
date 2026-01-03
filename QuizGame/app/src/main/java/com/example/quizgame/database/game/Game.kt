package com.example.quizgame.database.game

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game")
data class Game(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val playerId: Long,
    val score: Int,
    val createdAt: Long,
    val quizId: Long
)