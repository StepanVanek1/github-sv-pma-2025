package com.example.quizgame.database.game

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GameDao {
    @Insert
    suspend fun insertGame(game: Game)

    @Query("SELECT * FROM game WHERE playerId = :id ORDER BY createdAt DESC LIMIT 3")
    suspend fun getGamesByPlayerId(id: Long): List<Game>?

    @Query("SELECT * FROM game ORDER BY score DESC")
    suspend fun getGamesOrderedByScore(): List<Game>?
}