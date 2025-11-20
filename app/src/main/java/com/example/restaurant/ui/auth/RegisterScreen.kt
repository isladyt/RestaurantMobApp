package com.example.restaurant.ui.auth

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onRegisterSuccess: () -> Unit
) {
    val registerState by viewModel.loginState.collectAsState()

    var login by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var loginError by remember { mutableStateOf<String?>(null) }
    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    fun validate(): Boolean {
        loginError = if (login.isNotBlank() && login.matches(Regex("^[a-zA-Z0-9]+$"))) null else "Логин должен содержать только латинские буквы и цифры"
        nameError = if (name.isNotBlank()) null else "Имя не может быть пустым"
        emailError = if (email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) null else "Неверный формат email"
        passwordError = if (password.length >= 8) null else "Пароль должен быть не менее 8 символов"
        phoneError = if (phone.length == 10 && phone.startsWith("9")) null else "Неверный формат телефона"

        return loginError == null && nameError == null && emailError == null && passwordError == null && phoneError == null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Регистрация", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = login,
            onValueChange = { login = it; loginError = null },
            label = { Text("Логин") },
            isError = loginError != null,
            supportingText = { loginError?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it; nameError = null },
            label = { Text("Имя") },
            isError = nameError != null,
            supportingText = { nameError?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it; emailError = null },
            label = { Text("Электронная почта") },
            isError = emailError != null,
            supportingText = { emailError?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = phone,
            onValueChange = { if (it.length <= 10) phone = it; phoneError = null },
            label = { Text("Номер телефона") },
            prefix = { Text("+7 ") },
            isError = phoneError != null,
            supportingText = { phoneError?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it; passwordError = null },
            label = { Text("Пароль") },
            isError = passwordError != null,
            supportingText = { passwordError?.let { Text(it) } },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (registerState is LoginState.Error) {
            Text((registerState as LoginState.Error).message, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (registerState is LoginState.Loading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    if (validate()) {
                        viewModel.register(login, name, email, "+7$phone", password)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Зарегистрироваться")
            }
        }

        if (registerState is LoginState.RegistrationSuccess) {
            LaunchedEffect(Unit) {
                onRegisterSuccess()
            }
        }
    }
}
