package com.example.courtsmash.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class PreferencesManager(private val context: Context) {

    companion object {
        private val KEY_DISPLAY_NAME = stringPreferencesKey("display_name")
        private val KEY_PREFERRED_LOCATION = stringPreferencesKey("preferred_location")
    }

    val displayNameFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[KEY_DISPLAY_NAME] ?: "Alex Morgan"
    }

    val preferredLocationFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[KEY_PREFERRED_LOCATION] ?: "Bangkok, Thailand"
    }

    suspend fun updateDisplayName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_DISPLAY_NAME] = name
        }
    }

    suspend fun updatePreferredLocation(location: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_PREFERRED_LOCATION] = location
        }
    }
}
