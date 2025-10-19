package com.example.restaurant.ui.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurant.data.entity.User
import com.example.restaurant.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _updateState = MutableStateFlow<UpdateState>(UpdateState.Idle)
    val updateState: StateFlow<UpdateState> = _updateState

    init {
        val userLogin = savedStateHandle.get<String>("userLogin")
        if (userLogin != null) {
            viewModelScope.launch {
                _user.value = profileRepository.getUser(userLogin)
            }
        }
    }

    fun updateUser(name: String, email: String, phone: String) {
        val currentUser = _user.value ?: return
        val updatedUser = currentUser.copy(name = name, email = email, phone = phone)

        viewModelScope.launch {
            _updateState.value = UpdateState.Loading
            val result = profileRepository.updateUser(updatedUser)
            result.onSuccess {
                _user.value = updatedUser
                _updateState.value = UpdateState.Success("Профиль успешно обновлен")
            }.onFailure {
                _updateState.value = UpdateState.Error("Ошибка при обновлении профиля")
            }
        }
    }

    fun deleteUser() {
        val userId = _user.value?.id ?: return
        viewModelScope.launch {
            _updateState.value = UpdateState.Loading
            val result = profileRepository.deleteUser(userId)
            result.onSuccess {
                _updateState.value = UpdateState.UserDeleted
            }.onFailure {
                _updateState.value = UpdateState.Error("Ошибка при удалении профиля")
            }
        }
    }
}

sealed class UpdateState {
    object Idle : UpdateState()
    object Loading : UpdateState()
    data class Success(val message: String) : UpdateState()
    data class Error(val message: String) : UpdateState()
    object UserDeleted : UpdateState()
}
