package com.example.myapp017christmasapp.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map

class GiftsPreferencesRepository(context: Context) {

    private val dataStore = context.settingsDataStore

    val momGiftFlow = dataStore.data.map {
        it[UserPreferencesKeys.GIFT_MOM] ?: false
    }

    val dadGiftFlow = dataStore.data.map {
        it[UserPreferencesKeys.GIFT_DAD] ?: false
    }

    val dogGiftFlow = dataStore.data.map {
        it[UserPreferencesKeys.GIFT_DOG] ?: false
    }

    val catGiftFlow = dataStore.data.map {
        it[UserPreferencesKeys.GIFT_CAT] ?: false
    }

    val siblingGiftFlow = dataStore.data.map {
        it[UserPreferencesKeys.GIFT_SIBLING] ?: false
    }

    suspend fun setMomGift(value: Boolean) {
        dataStore.edit { it[UserPreferencesKeys.GIFT_MOM] = value }
    }

    suspend fun setDadGift(value: Boolean) {
        dataStore.edit { it[UserPreferencesKeys.GIFT_DAD] = value }
    }

    suspend fun setDogGift(value: Boolean) {
        dataStore.edit { it[UserPreferencesKeys.GIFT_DOG] = value }
    }

    suspend fun setCatGift(value: Boolean) {
        dataStore.edit { it[UserPreferencesKeys.GIFT_CAT] = value }
    }

    suspend fun setSiblingGift(value: Boolean) {
        dataStore.edit { it[UserPreferencesKeys.GIFT_SIBLING] = value }
    }
}
