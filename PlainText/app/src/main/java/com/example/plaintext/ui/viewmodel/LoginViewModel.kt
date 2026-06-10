package com.example.plaintext.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plaintext.data.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginState(
    val login: String,
    val password: String,
    val saveInfo: Boolean,
) {
    fun loginValid() = login.isNotBlank() && password.isNotBlank()

    companion object {
        val initialState = LoginState(
            login = "",
            password = "",
            saveInfo = false
        )
    }
}

sealed class LoginEffects {
    data class Error(val message: String) : LoginEffects()
    data object Success : LoginEffects()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {
    private val _effects = Channel<LoginEffects>()

    val effect = _effects.receiveAsFlow()

    private val _state = MutableStateFlow(LoginState.initialState)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferencesRepository.getSaveInfo().collect { saveInfo ->
                _state.value = _state.value.copy(saveInfo = saveInfo)
            }
        }

        viewModelScope.launch {
            if (userPreferencesRepository.hasCredentials()) {
                val lastLogin = userPreferencesRepository.getLastLogin().first()
                _state.value = _state.value.copy(login = lastLogin)
            }
        }
    }

    fun onLoginChange(login: String) {
        _state.value = _state.value.copy(login = login)
    }

    fun onPasswordChange(password: String) {
        _state.value = _state.value.copy(password = password)
    }

    fun onSaveInfoChange(saveInfo: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setSaveInfo(saveInfo)
        }
    }

    fun onLoginClick() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentState = _state.value
            if (!currentState.loginValid()) {
                _effects.trySend(LoginEffects.Error("Login ou senha inválidos"))
                return@launch
            }

            val authLogin = userPreferencesRepository.getAuthLogin().first()
            val authPassword = userPreferencesRepository.getAuthPassword().first()

            if (currentState.login == authLogin && currentState.password == authPassword) {
                if (currentState.saveInfo) {
                    userPreferencesRepository.setLastLogin(currentState.login)
                }
                _effects.trySend(LoginEffects.Success)
            } else {
                _effects.trySend(LoginEffects.Error("Credenciais incorretas"))
            }
        }
    }
}
