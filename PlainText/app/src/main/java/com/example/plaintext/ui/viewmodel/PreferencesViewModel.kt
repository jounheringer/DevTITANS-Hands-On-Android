package com.example.plaintext.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plaintext.data.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PreferencesState(
    var login: String,
    var password: String,
    var preencher: Boolean
)

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    handle: SavedStateHandle,
) : ViewModel() {
    var preferencesState by mutableStateOf(PreferencesState(login = "devtitans", password = "123", preencher = true))
        private set

    init {
        viewModelScope.launch {
            if (!userPreferencesRepository.hasCredentials()) {
                userPreferencesRepository.setAuthLogin(preferencesState.login)
                userPreferencesRepository.setAuthPassword(preferencesState.password)
                userPreferencesRepository.setPreencher(preferencesState.preencher)
            }

            combine(
                userPreferencesRepository.getAuthLogin(),
                userPreferencesRepository.getAuthPassword(),
                userPreferencesRepository.getPreencher()
            ) { authLogin, authPassword, preencher ->
                PreferencesState(
                    login = authLogin,
                    password = authPassword,
                    preencher = preencher
                )
            }.collect {
                preferencesState = it
            }
        }
    }

    fun updateLogin(login: String) {
        preferencesState = preferencesState.copy(
            login = login
        )
        viewModelScope.launch {
            userPreferencesRepository.setAuthLogin(login)
        }
    }

    fun updatePassword(password: String) {
        preferencesState = preferencesState.copy(
            password = password
        )
        viewModelScope.launch {
            userPreferencesRepository.setAuthPassword(password)
        }
    }

    fun updatePreencher(preencher: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setPreencher(preencher)
        }
    }

    fun checkCredentials(login: String, password: String): Boolean {
        return login == preferencesState.login && password == preferencesState.password
    }
}
