package com.example.plaintext.ui.screens

import android.os.Parcelable
import com.example.plaintext.data.model.PasswordInfo
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * Define as rotas de navegação para as diferentes telas do aplicativo.
 * Cada objeto ou data class representa uma tela e pode conter argumentos para navegação.
 */
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object List : Screen("list")
    object Preferences : Screen("preferences")

    // Rota para a tela de edição/criação de senhas.
    // Pode receber um objeto PasswordInfo opcional para edição.
    @Serializable
    @Parcelize
    data class EditPassword(val passwordInfo: PasswordInfo? = null) : Screen("edit_password") {
        // Constantes para facilitar a construção da rota e extração de argumentos.
        companion object {
            const val ROUTE = "edit_password"
            const val ARG_PASSWORD_INFO = "passwordInfo"
            val routeWithArgs = "$ROUTE?$ARG_PASSWORD_INFO={$ARG_PASSWORD_INFO}"
        }
    }
}