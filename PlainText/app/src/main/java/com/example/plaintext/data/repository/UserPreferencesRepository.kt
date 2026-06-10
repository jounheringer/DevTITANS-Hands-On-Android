package com.example.plaintext.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

interface UserPreferencesRepository {
    fun getAuthLogin(): Flow<String>
    fun getAuthPassword(): Flow<String>
    fun getLastLogin(): Flow<String>
    fun getSaveInfo(): Flow<Boolean>
    fun getPreencher(): Flow<Boolean>

    suspend fun setAuthLogin(login: String)
    suspend fun setAuthPassword(password: String)
    suspend fun setLastLogin(login: String)
    suspend fun setSaveInfo(saveInfo: Boolean)
    suspend fun setPreencher(preencher: Boolean)
    suspend fun hasCredentials(): Boolean
}

@Singleton
class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : UserPreferencesRepository {

    private object PreferencesKeys {
        val AUTH_LOGIN = stringPreferencesKey("auth_login")
        val AUTH_PASSWORD = stringPreferencesKey("auth_password")
        val LAST_LOGIN = stringPreferencesKey("last_login")
        val LAST_PASSWORD = stringPreferencesKey("last_password")
        val SAVE_INFO = booleanPreferencesKey("save_info")
        val PREENCHER = booleanPreferencesKey("preencher")
    }

    override fun getAuthLogin(): Flow<String> = dataStore.data
        .map { preferences -> preferences[PreferencesKeys.AUTH_LOGIN] ?: "devtitans" }

    override fun getAuthPassword(): Flow<String> = dataStore.data
        .map { preferences -> preferences[PreferencesKeys.AUTH_PASSWORD] ?: "123" }

    override fun getLastLogin(): Flow<String> = dataStore.data
        .map { preferences -> preferences[PreferencesKeys.LAST_LOGIN] ?: "" }

    override fun getSaveInfo(): Flow<Boolean> = dataStore.data
        .map { preferences ->
            val value =
                preferences.asMap().entries.find { it.key.name == PreferencesKeys.SAVE_INFO.name }?.value
            (value as? Boolean) ?: (value as? String)?.toBoolean() ?: false
        }

    override fun getPreencher(): Flow<Boolean> = dataStore.data
        .map { preferences ->
            val value =
                preferences.asMap().entries.find { it.key.name == PreferencesKeys.PREENCHER.name }?.value
            (value as? Boolean) ?: (value as? String)?.toBoolean() ?: false
        }

    override suspend fun setAuthLogin(login: String) {
        dataStore.edit { preferences -> preferences[PreferencesKeys.AUTH_LOGIN] = login }
    }

    override suspend fun setAuthPassword(password: String) {
        dataStore.edit { preferences -> preferences[PreferencesKeys.AUTH_PASSWORD] = password }
    }

    override suspend fun setLastLogin(login: String) {
        dataStore.edit { preferences -> preferences[PreferencesKeys.LAST_LOGIN] = login }
    }

    override suspend fun setSaveInfo(saveInfo: Boolean) {
        dataStore.edit { preferences -> preferences[PreferencesKeys.SAVE_INFO] = saveInfo }
    }

    override suspend fun setPreencher(preencher: Boolean) {
        dataStore.edit { preferences -> preferences[PreferencesKeys.PREENCHER] = preencher }
    }

    override suspend fun hasCredentials(): Boolean {
        val preferences = dataStore.data.first()
        val entries = preferences.asMap().entries
        val hasLogin = entries.any { it.key.name == PreferencesKeys.AUTH_LOGIN.name }
        val hasPassword = entries.any { it.key.name == PreferencesKeys.AUTH_PASSWORD.name }
        return hasLogin && hasPassword
    }
}
