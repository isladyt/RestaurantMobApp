package com.example.restaurant.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurant.data.entity.User
import com.example.restaurant.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Возвращаем UpdateState для правильного общения с UI
sealed class UpdateState {
    object Idle : UpdateState()
    data class Success(val message: String) : UpdateState()
    data class Error(val message: String) : UpdateState()
    object UserDeleted : UpdateState()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _updateState = MutableStateFlow<UpdateState>(UpdateState.Idle)
    val updateState = _updateState.asStateFlow()

    init {
        viewModelScope.launch {
            _user.value = profileRepository.getCurrentUser()
        }
    }

    fun updateUser(name: String, email: String, phone: String) {
        _user.value?.let {
            viewModelScope.launch {
                val result = profileRepository.updateUser(it.copy(name = name, email = email, phone = phone))
                result.onSuccess { _updateState.value = UpdateState.Success("Профиль успешно обновлен") }
                result.onFailure { e -> _updateState.value = UpdateState.Error("Ошибка: ${e.message}") }
            }
        }
    }

    fun deleteUser() {
        _user.value?.let {
            viewModelScope.launch {
                profileRepository.deleteUser(it)
                _updateState.value = UpdateState.UserDeleted
            }
        }
    }
    
    fun consumedUpdateState() {
        _updateState.value = UpdateState.Idle
    }
}
