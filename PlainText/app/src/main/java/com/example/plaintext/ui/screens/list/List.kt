package com.example.plaintext.ui.screens.list

import androidx.compose.material3.HorizontalDivider
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.plaintext.R
import com.example.plaintext.ui.screens.login.TopBarComponent
import com.example.plaintext.ui.viewmodel.ListViewModel
import com.example.plaintext.ui.viewmodel.ListViewState
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.plaintext.data.model.PasswordInfo
import com.example.plaintext.ui.theme.PlainTextTheme
import com.example.plaintext.ui.theme.Pink80

@Composable
fun ListView(
) {}


@Composable
fun ListHeader(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text,
        fontSize=16.sp,
        modifier=modifier
            .fillMaxWidth(),
        color= Color.Black
    )
}

@Composable
fun AddButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = { onClick() },
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.secondary
    ) {
        Icon(Icons.Filled.Add, "Small floating action button.")
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListItemContent(
    modifier: Modifier,
    listState: ListViewState,
    navigateToEdit: (password: PasswordInfo) -> Unit
) {
        when {
            !listState.isCollected -> {
                LoadingScreen()
            }

            else -> {
                LazyColumn(
                    modifier = modifier
                        .fillMaxWidth()
                ) {
                    items(listState.passwordList.size) {
                        ListItem(
                            listState.passwordList[it],
                            navigateToEdit
                        )
                    }
                }
            }
        }
}

@Composable
fun LoadingScreen() {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text("Carregando")
    }
}
@Composable
fun ListItem(
    password: PasswordInfo,
    navigateToEdit: (password: PasswordInfo) -> Unit
) {
    val title = password.name
    val subTitle = password.login

    var showPassword by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickable { showPassword = !showPassword }
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Logo",
            modifier = Modifier.fillMaxHeight()
        )
        Column(
            modifier = Modifier
                .weight(.7f)
                .padding(horizontal = 5.dp),
        ) {
            Text(title, fontSize = 20.sp)
            Text(subTitle, fontSize = 14.sp)

            AnimatedVisibility(visible = showPassword) {
                Text(
                    text = password.password,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        Icon(
            imageVector = if (showPassword)
                Icons.Filled.KeyboardArrowDown
            else
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Menu",
            tint = Color.Black,
            modifier = Modifier.clickable { showPassword = !showPassword }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun ListScreen(

    listState: ListViewState = ListViewState(
        isCollected = true,
        passwordList = listOf(
            PasswordInfo(
                id = 1,
                name = "Twitter",
                login = "dev",
                password = "senha123",
                notes = "Conta Teste Twitter"
            ),
            PasswordInfo(
                id = 2,
                name = "Facebook",
                login = "devtitans",
                password = "senha456",
                notes = "Conta Teste Facebook"
            ),
            PasswordInfo(
                id = 3,
                name = "Moodle",
                login = "dev.com",
                password = "senha789",
                notes = "Conta Teste universitária"
            )
        )
    ),
    navigateToEdit: (PasswordInfo) -> Unit = {},
    onAddClick: () -> Unit = {}
) {
    PlainTextTheme() {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        ListHeader(text = "PlainText")
                    },
                )
            },
            floatingActionButton = {
                AddButton(onClick = onAddClick)
            },
        ) { padding ->
            ListItemContent(
                modifier = Modifier.padding(padding),
                listState = listState,
                navigateToEdit = navigateToEdit
            )
        }
    }
}
