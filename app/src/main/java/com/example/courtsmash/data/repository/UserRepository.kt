package com.example.courtsmash.data.repository

import com.example.courtsmash.data.local.PreferencesManager
import kotlinx.coroutines.flow.Flow

class UserRepository(private val preferencesManager: PreferencesManager) {

    val displayName: Flow<String> = preferencesManager.displayNameFlow
    val preferredLocation: Flow<String> = preferencesManager.preferredLocationFlow

    suspend fun updateDisplayName(name: String) {
        preferencesManager.updateDisplayName(name)
    }

    suspend fun updatePreferredLocation(location: String) {
        preferencesManager.updatePreferredLocation(location)
    }
}
