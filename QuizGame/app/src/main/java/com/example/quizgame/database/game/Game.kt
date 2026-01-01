package com.example.quizgame.database.game

import androidx.room.Entity

@Entity(tableName = "game")
data class Game(
    val id: Long = 0L,
    val playerId: Long,
    val score: Int
)