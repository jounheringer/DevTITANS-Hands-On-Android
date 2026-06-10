package com.example.plaintext.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

data class LoginState(
    val login: String,
    val password: String,
    val saveInfo: Boolean
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
    data class Error(val message: String): LoginEffects()
    data object Success: LoginEffects()
}

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    private val _effects = Channel<LoginEffects>()

    val effect = _effects.receiveAsFlow()

    private val _state = MutableStateFlow(LoginState.initialState)
    val state = _state.asStateFlow()

    fun onLoginChange(login: String) {
        _state.value = _state.value.copy(login = login)
    }

    fun onPasswordChange(password: String) {
        _state.value = _state.value.copy(password = password)
    }

    fun onSaveInfoChange(saveInfo: Boolean) {
        _state.value = _state.value.copy(saveInfo = saveInfo)
    }

    fun onLoginClick() {
        if (_state.value.loginValid()) {
            _effects.trySend(LoginEffects.Success)
        } else {
            _effects.trySend(LoginEffects.Error("Senha "))
        }
    }
}