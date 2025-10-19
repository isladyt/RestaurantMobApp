package com.example.restaurant.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurant.data.entity.User
import com.example.restaurant.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(login: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val result = authRepository.login(login, password)
            result.onSuccess {
                _loginState.value = LoginState.Success(it)
            }.onFailure {
                val message = when (it.message) {
                    "Invalid login or password" -> "Неверный логин или пароль"
                    else -> it.message ?: "Произошла неизвестная ошибка"
                }
                _loginState.value = LoginState.Error(message)
            }
        }
    }

    // Обновляем метод register
    fun register(login: String, name: String, email: String, phone: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.registerUser(login, name, email, phone, password)
            result.onSuccess {
                _loginState.value = LoginState.RegistrationSuccess
            }.onFailure {
                val message = when (it.message) {
                    "User with this login already exists" -> "Пользователь с таким логином уже существует"
                    else -> it.message ?: "Ошибка регистрации"
                }
                _loginState.value = LoginState.Error(message)
            }
        }
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object RegistrationSuccess : LoginState()
    data class Success(val user: User) : LoginState()
    data class Error(val message: String) : LoginState()
}
