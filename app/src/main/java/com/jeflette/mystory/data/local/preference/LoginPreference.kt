package com.jeflette.mystory.data.local.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LoginPreference @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val isLogin= booleanPreferencesKey("is_login")
    private val token = stringPreferencesKey("token")
    private val username = stringPreferencesKey("username")

    fun readToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[token] ?: ""
        }
    }

    fun readUsername(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[username] ?: ""
        }
    }

    suspend fun saveLogin(userToken: String,userUsername:String) {
        dataStore.edit { preferences ->
            preferences[token] = userToken
            preferences[isLogin] = true
            preferences[username] = userUsername
        }
    }

    suspend fun clearLogin() {
        dataStore.edit { preferences ->
            preferences[isLogin] = false
            preferences[token] = ""
            preferences[username] = ""
        }
    }
}