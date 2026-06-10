package com.example.plaintext.ui.screens.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.plaintext.R
import com.example.plaintext.ui.viewmodel.LoginEffects
import com.example.plaintext.ui.viewmodel.LoginState
import com.example.plaintext.ui.viewmodel.LoginViewModel
import com.example.plaintext.utils.rememberFlowWithLifecycle

//data class LoginState(
//    val preencher: Boolean,
//    val login: String,
//    val navigateToSettings: () -> Unit,
//    val navigateToList: (name: String) -> Unit,
//    val checkCredentials: (login: String, password: String) -> Boolean,
//)

@Composable
fun Login_screen(
    navigateToSettings: () -> Unit,
    navigateToList: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effects = rememberFlowWithLifecycle(viewModel.effect)

    LaunchedEffect(effects) {
        effects.collect { effect ->
            when (effect) {
                is LoginEffects.Error -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }

                LoginEffects.Success -> {
                    Toast.makeText(context, "Login realizado.", Toast.LENGTH_SHORT).show()
                    navigateToList()
                }
            }
        }
    }

    LoginScreenContent(
        state = state,
        onLoginChanged = viewModel::onLoginChange,
        onPasswordChanged = viewModel::onPasswordChange,
        onSaveInfoChanged = viewModel::onSaveInfoChange,
        navigateToSettings = navigateToSettings,
        navigateToList = viewModel::onLoginClick
    )
}

@Composable
fun LoginScreenContent(
    state: LoginState,
    onLoginChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onSaveInfoChanged: (Boolean) -> Unit,
    navigateToSettings: () -> Unit,
    navigateToList: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopBarComponent(
                navigateToSettings = navigateToSettings,
                navigateToSensores = navigateToList
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LoginHeader()

            LoginInputs(
                state = state,
                onLoginChanged = onLoginChanged,
                onPasswordChanged = onPasswordChanged
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = state.saveInfo,
                    onCheckedChange = { onSaveInfoChanged(it) }
                )

                Text(text = "Salvar as informações de login")
            }

            Button(
                onClick = navigateToList,
                enabled = state.loginValid()
            ) {
                Text(text = "Enviar")
            }
        }
    }
}

@Composable
fun LoginInputs(
    state: LoginState,
    onLoginChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Digite suas credenciais para continuar.",
            textAlign = TextAlign.Center
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(modifier = Modifier.weight(0.4f), text = "Login:", textAlign = TextAlign.Center)
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = state.login,
                onValueChange = { onLoginChanged(it) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(autoCorrectEnabled = false)
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(modifier = Modifier.weight(0.4f), text = "Senha:", textAlign = TextAlign.Center)
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = state.password,
                onValueChange = { onPasswordChanged(it) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(autoCorrectEnabled = false),
                visualTransformation = PasswordVisualTransformation()
            )
        }
    }
}

@Composable
fun LoginHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Green)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "Android Icon"
            )
            Spacer(modifier = Modifier.width(20.dp))
            Column(modifier = Modifier.width(130.dp)) {
                Text(text = "\"The most secure password manager\"")
                Text(text = "Bob and Alice")
            }
        }
    }
}

@Composable
fun MyAlertDialog(shouldShowDialog: MutableState<Boolean>) {
    if (shouldShowDialog.value) {
        AlertDialog(
            onDismissRequest = {
                shouldShowDialog.value = false
            },

            title = { Text(text = "Sobre") },
            text = { Text(text = "PlainText Password Manager v1.0") },
            confirmButton = {
                Button(
                    onClick = { shouldShowDialog.value = false }
                ) {
                    Text(text = "Ok")
                }
            }
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopBarComponent(
    navigateToSettings: (() -> Unit) = {},
    navigateToSensores: (() -> Unit) = {},
) {
    var expanded by remember { mutableStateOf(false) }
    val shouldShowDialog = remember { mutableStateOf(false) }

    if (shouldShowDialog.value) {
        MyAlertDialog(shouldShowDialog = shouldShowDialog)
    }

    TopAppBar(
        title = { Text("PlainText") },
        actions = {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Menu")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Configurações") },
                    onClick = {
                        navigateToSettings()
                        expanded = false
                    },
                    modifier = Modifier.padding(8.dp)
                )
                DropdownMenuItem(
                    text = {
                        Text("Sobre")
                    },
                    onClick = {
                        shouldShowDialog.value = true
                        expanded = false
                    },
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLogin() {
    MaterialTheme {
        LoginScreenContent(
            navigateToSettings = { },
            navigateToList = { },
            state = LoginState.initialState,
            onLoginChanged = {  },
            onPasswordChanged = {  },
            onSaveInfoChanged = {  },
        )
    }
}