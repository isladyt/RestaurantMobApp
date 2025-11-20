package com.example.restaurant.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onUserDeleted: () -> Unit,
    onLogout: () -> Unit,
    showMessage: (String) -> Unit // Новый callback
) {
    val user by viewModel.user.collectAsState()
    val updateState by viewModel.updateState.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    var name by remember(user) { mutableStateOf(user?.name ?: "") }
    var email by remember(user) { mutableStateOf(user?.email ?: "") }
    var phone by remember(user) { mutableStateOf(user?.phone ?: "") }

    // Обрабатываем события из ViewModel
    LaunchedEffect(updateState) {
        when (val state = updateState) {
            is UpdateState.Success -> {
                showMessage(state.message)
                viewModel.consumedUpdateState()
            }
            is UpdateState.Error -> {
                showMessage(state.message)
                viewModel.consumedUpdateState()
            }
            is UpdateState.UserDeleted -> {
                showMessage("Профиль успешно удален")
                onUserDeleted()
                viewModel.consumedUpdateState()
            }
            else -> {}
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Подтверждение выхода") },
            text = { Text("Вы уверены, что хотите выйти?") },
            confirmButton = {
                Button(onClick = onLogout) {
                    Text("Да")
                }
            },
            dismissButton = {
                Button(onClick = { showLogoutDialog = false }) {
                    Text("Нет")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Ваш профиль", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Имя") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Электронная почта") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Номер телефона") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { viewModel.updateUser(name, email, phone) }) {
            Text("Сохранить изменения")
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(onClick = { viewModel.deleteUser() }) {
            Text("Удалить профиль")
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = { showLogoutDialog = true }) {
            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Выйти", modifier = Modifier.padding(end = 8.dp))
            Text("Выйти из аккаунта")
        }
    }
}
