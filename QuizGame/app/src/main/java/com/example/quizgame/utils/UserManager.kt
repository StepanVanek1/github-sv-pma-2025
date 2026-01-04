package com.example.quizgame.utils

import android.content.Context
import androidx.core.content.edit

object UserManager {
    private const val PREF_NAME = "QuizGamePrefs"
    private const val KEY_USER_ID = "current_user_id"

    fun getUserId(context: Context): Long {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getLong(KEY_USER_ID, -1L)
    }

    fun saveUserId(context: Context, userId: Long) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit { putLong(KEY_USER_ID, userId) }
    }
}