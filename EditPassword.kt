package com.example.plaintext.ui.screens.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.plaintext.data.model.PasswordInfo

/**
 * ATIVIDADE ACADÊMICA: PlainText Password Manager
 * TELA: Edição e Inserção de Senhas
 * RESPONSÁVEIS: Desenvolvedores da Equipe PlainText (Bob e Alice)
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPasswordScreen(
    passwordInfo: PasswordInfo? = null, // Recebe o objeto para edição ou null para nova senha
    onBack: () -> Unit,                 // Callback para voltar à tela anterior
    onSave: (PasswordInfo) -> Unit      // Callback para salvar os dados
) {
    // Definição do estado local para cada campo, permitindo a edição reativa.
    // Caso passwordInfo não seja nulo, os campos são preenchidos com os dados existentes.
    var name by remember { mutableStateOf(passwordInfo?.name ?: "") }
    var login by remember { mutableStateOf(passwordInfo?.login ?: "") }
    var password by remember { mutableStateOf(passwordInfo?.password ?: "") }
    var notes by remember { mutableStateOf(passwordInfo?.notes ?: "") }

    // O Scaffold fornece a estrutura básica do Material Design, incluindo a TopBar.
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar senha") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        // A Column organiza os elementos verticalmente com um espaçamento padrão (16dp).
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()), // Habilita a rolagem para conteúdos longos.
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Campo para o Nome do serviço ou site.
            EditInput(
                label = "Nome",
                value = name,
                onValueChange = { name = it }
            )

            // Campo para o Usuário/Login.
            EditInput(
                label = "Usuário",
                value = login,
                onValueChange = { login = it }
            )

            // Campo para a Senha, configurado com máscara de visualização por segurança.
            EditInput(
                label = "Senha",
                value = password,
                onValueChange = { password = it },
                isPasswordField = true
            )

            // Campo para Notas adicionais, permitindo múltiplas linhas de texto.
            EditInput(
                label = "Notas",
                value = notes,
                onValueChange = { notes = it },
                singleLine = false
            )

            Spacer(modifier = Modifier.weight(1f))

            // Botão de ação para salvar ou atualizar a senha.
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onSave(
                        PasswordInfo(
                            id = passwordInfo?.id ?: 0,
                            name = name,
                            login = login,
                            password = password,
                            notes = notes
                        )
                    )
                },
                // Regra de validação: o botão só habilita se os campos essenciais estiverem preenchidos.
                enabled = name.isNotBlank() && login.isNotBlank() && password.isNotBlank()
            ) {
                Text("Salvar")
            }
        }
    }
}

/**
 * Componente reutilizável EditInput para padronização visual dos campos de texto.
 */
@Composable
fun EditInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPasswordField: Boolean = false,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = singleLine,
        // Define a transformação visual baseada no tipo de campo (senha ou texto comum).
        visualTransformation = if (isPasswordField) PasswordVisualTransformation() else VisualTransformation.None
    )
}