package com.example.storyapp.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserPreferences(private val dataStore: DataStore<Preferences>) {

    private val ID = stringPreferencesKey("id")
    private val NAME = stringPreferencesKey("name")
    private val TOKEN_KEY = stringPreferencesKey("token")

    fun getId(): Flow<String> {
        return dataStore.data.map {
            it[ID] ?: ""
        }
    }

    fun getName(): Flow<String> {
        return dataStore.data.map {
            it[NAME] ?: ""
        }
    }

    fun getToken(): Flow<String> {
        return dataStore.data.map {
            it[TOKEN_KEY] ?: ""
        }
    }

    suspend fun saveId(id: String) {
        dataStore.edit {
            it[ID] = id
        }
    }

    suspend fun saveName(name: String) {
        dataStore.edit {
            it[NAME] = name
        }
    }

    suspend fun saveToken(token: String) {
        dataStore.edit {
            it[TOKEN_KEY] = token
        }
    }

    suspend fun removeAll() {
        dataStore.edit {
            it.clear()
        }
    }

}
