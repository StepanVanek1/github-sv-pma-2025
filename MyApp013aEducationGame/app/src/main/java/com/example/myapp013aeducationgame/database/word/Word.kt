package com.example.myapp013aeducationgame.database.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word")
data class Word(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val czech: String,
    val foreign: String
)