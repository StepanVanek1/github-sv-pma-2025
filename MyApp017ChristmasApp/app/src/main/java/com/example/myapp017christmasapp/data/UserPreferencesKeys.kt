package com.example.myapp017christmasapp.data

import androidx.datastore.preferences.core.booleanPreferencesKey

object UserPreferencesKeys {
    val GIFT_MOM = booleanPreferencesKey("gift_mom")
    val GIFT_DAD = booleanPreferencesKey("gift_dad")
    val GIFT_CAT = booleanPreferencesKey("gift_cat")
    val GIFT_DOG = booleanPreferencesKey("gift_dog")
    val GIFT_SIBLING = booleanPreferencesKey("gift_sibling")
}