package com.example.plaintext.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.plaintext.data.model.PasswordInfo
import com.example.plaintext.ui.screens.Screen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Classe que encapsula o NavHostController e fornece funções de navegação.
 * Isso ajuda a centralizar a lógica de navegação e a torná-la mais testável.
 *
 * @param navController O NavHostController responsável por gerenciar a pilha de navegação.
 */
class AppState(
    val navController: NavHostController
) {
    // Função para navegar para a tela de Login.
    fun navigateToLogin() {
        navController.navigate(Screen.Login.route) {
            // Limpa a pilha de navegação até o destino inicial, garantindo que o login seja o ponto de entrada.
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true
            }
        }
    }

    // Função para navegar para a tela de Listagem de Senhas.
    fun navigateToList() {
        navController.navigate(Screen.List.route) {
            // Limpa a pilha de navegação até a tela de Login, para que o usuário não possa voltar ao login após entrar.
            popUpTo(Screen.Login.route) {
                inclusive = true
            }
        }
    }

    // Função para navegar para a tela de Preferências.
    fun navigateToPreferences() {
        navController.navigate(Screen.Preferences.route)
    }

    // Função para navegar para a tela de Edição/Criação de Senhas.
    // Recebe um PasswordInfo opcional: se nulo, é uma nova senha; caso contrário, é uma edição.
    fun navigateToEditPassword(passwordInfo: PasswordInfo? = null) {
        if (passwordInfo == null) {
            // Navega para adicionar uma nova senha (sem argumentos).
            navController.navigate(Screen.EditPassword.ROUTE)
        } else {
            // Navega para editar uma senha existente, passando o objeto serializado como argumento.
            val passwordJson = Json.encodeToString(passwordInfo)
            navController.navigate("${Screen.EditPassword.ROUTE}?${Screen.EditPassword.ARG_PASSWORD_INFO}=${passwordJson}")
        }
    }

    // Função para voltar à tela anterior na pilha de navegação.
    fun navigateBack() {
        navController.popBackStack()
    }
}

/**
 * Composable para criar e lembrar uma instância de AppState.
 * Garante que a instância de AppState seja mantida durante recomposições.
 */
@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController()
) = remember(navController) {
    AppState(navController)
}