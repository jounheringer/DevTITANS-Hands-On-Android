package com.example.plaintext.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.plaintext.data.model.PasswordInfo
import com.example.plaintext.ui.screens.editList.EditList
import com.example.plaintext.ui.screens.hello.Hello_screen
import com.example.plaintext.ui.screens.login.Login_screen
import com.example.plaintext.ui.screens.preferences.SettingsScreen
import com.example.plaintext.utils.parcelableType
import kotlin.reflect.typeOf

@Composable
fun PlainTextApp(
    appState: JetcasterAppState = rememberJetcasterAppState()
) {
    NavHost(
        navController = appState.navController,
        startDestination = Screen.Login,
    ) {
        composable<Screen.Hello> {
            val args = it.toRoute<Screen.Hello>()
            Hello_screen(args)
        }

        composable<Screen.Login> {
            Login_screen(
                navigateToSettings = {
                    appState.navController.navigate(Screen.Preferences) {
                        launchSingleTop = true
                    }
                },
                navigateToList = {
                    appState.navigateToList()
                }
            )
        }

        composable<Screen.Preferences> {
            SettingsScreen(
                navController = appState.navController
            )
        }

        composable<Screen.List> {
            Text(text = "Hello! This is the upcoming Password List Screen.")
        }

        composable<Screen.EditList>(
            typeMap = mapOf(typeOf<PasswordInfo>() to parcelableType<PasswordInfo>())
        ) {
            val args = it.toRoute<Screen.EditList>()
            EditList(
                args,
                navigateBack = {},
                savePassword = { password -> Unit }
            )
        }
    }
}