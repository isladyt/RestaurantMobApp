package com.example.restaurant.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.restaurant.data.entity.User

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onLoginSuccess: (User) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val loginState by viewModel.loginState.collectAsState()

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    fun validate(): Boolean {
        return login.isNotBlank() && password.isNotBlank()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Авторизация", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = login,
            onValueChange = { login = it; showError = false },
            label = { Text("Логин") },
            isError = showError && login.isBlank(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it; showError = false },
            label = { Text("Пароль") },
            isError = showError && password.isBlank(),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            when (val state = loginState) {
                is LoginState.Success -> {
                    LaunchedEffect(state) {
                        onLoginSuccess(state.user)
                    }
                }
                is LoginState.Error -> {
                    Text(state.message, color = MaterialTheme.colorScheme.error)
                }
                is LoginState.Loading -> {
                    CircularProgressIndicator()
                }
                is LoginState.Idle, is LoginState.RegistrationSuccess -> { /* Do nothing */ }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { 
                if (validate()) {
                    viewModel.login(login, password)
                } else {
                    showError = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Войти")
        }

        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onNavigateToRegister) {
            Text("Нет аккаунта? Зарегистрироваться")
        }
    }
}
