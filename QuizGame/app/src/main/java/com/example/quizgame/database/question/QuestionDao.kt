package com.example.quizgame.database.question

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface QuestionDao {
    @Insert
    suspend fun insertAll(words: List<Question>)

    @Query("SELECT * FROM question ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomQuestion(): Question?

    @Query("SELECT * FROM question")
    suspend fun getAllQuestions(): List<Question?>

    @Query("SELECT * FROM question WHERE id != :excludeId ORDER BY RANDOM() LIMIT 2")
    suspend fun getRandomQuestionExcluding(excludeId: Long): List<Question>
}