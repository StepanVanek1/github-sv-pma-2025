package com.example.myapp013aeducationgame.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapp013aeducationgame.database.user.User
import com.example.myapp013aeducationgame.database.user.UserDao
import com.example.myapp013aeducationgame.database.user.Word
import com.example.myapp013aeducationgame.database.word.WordDao

@Database(entities = [Word::class, User::class], version = 1)
abstract class GameDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
    abstract fun userDao(): UserDao
}