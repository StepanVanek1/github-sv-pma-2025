package com.example.quizgame.database.quiz

import Quiz
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface QuizDao {
    @Insert
    suspend fun insertQuiz(quiz: Quiz): Long

    @Delete
    suspend fun deleteQuiz(quiz: Quiz)

    @Query("SELECT * FROM quiz WHERE id = :id")
    suspend fun getQuizById(id: Long): Quiz

    @Query("SELECT * FROM quiz")
    suspend fun getAllQuizes(): List<Quiz>?

    @Query("SELECT * FROM quiz WHERE creatorId = :id ORDER BY createdAt DESC LIMIT 3")
    suspend fun getAllQuizesByCreatorId(id: Long): List<Quiz>?

    @Query("UPDATE quiz SET name = :newName WHERE id = :id")
    suspend fun updateName(id: Long, newName: String)
}