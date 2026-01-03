package com.example.quizgame.database.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getUserById(id: Long): User?

    @Query("SELECT * FROM user")
    suspend fun getAllUsers(): List<User>?

    @Update
    suspend fun updateUser(user: User)
}