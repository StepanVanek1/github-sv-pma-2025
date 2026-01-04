package com.example.quizgame.database.question

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface QuestionDao {
    @Insert
    suspend fun insertQuestion(question: Question)

    @Update
    suspend fun updateQuestion(question: Question)

    @Delete
    suspend fun deleteQuestion(question: Question)

    @Query("SELECT * FROM question WHERE id = :id")
    suspend fun getQuestionById(id: Long): Question

    @Query("SELECT * FROM question WHERE quizId = :id")
    suspend fun getAllQuizQuestions(id: Long): List<Question>?

    @Query("SELECT correctAnswer FROM question WHERE id != :excludeQuestionId ORDER BY RANDOM() LIMIT 2")
    suspend fun getRandomWrongAnswersGlobal(excludeQuestionId: Long): List<String>
}