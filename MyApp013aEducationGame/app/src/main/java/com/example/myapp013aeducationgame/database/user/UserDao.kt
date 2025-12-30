package com.example.myapp013aeducationgame.database.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: User): Long

    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getUserById(id: Long): User?

    @Query("SELECT * FROM user ORDER BY points DESC")
    suspend fun getAllUsers(): List<User?>

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)
}