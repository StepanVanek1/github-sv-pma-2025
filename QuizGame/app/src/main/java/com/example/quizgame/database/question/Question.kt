package com.example.quizgame.database.question

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "question")
data class Question(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val question: String,
    val correctAnswer: String,
    val creatorId: Long,
    val gameId: Long
)