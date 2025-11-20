package com.example.restaurant.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurant.data.entity.Dish
import com.example.restaurant.data.entity.User
import com.example.restaurant.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ScreenState {
    object Auth : ScreenState()
    data class Main(val user: User) : ScreenState()
}

@HiltViewModel
class MainViewModel @Inject constructor(private val cartRepository: CartRepository) : ViewModel() {

    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Auth)
    val screenState: StateFlow<ScreenState> = _screenState

    // Поток для всех пользовательских сообщений
    private val _userMessage = MutableSharedFlow<String>()
    val userMessage = _userMessage.asSharedFlow()

    fun onLoginSuccess(user: User) {
        _screenState.value = ScreenState.Main(user)
    }

    fun onLogout() {
        _screenState.value = ScreenState.Auth
        cartRepository.clearCart()
    }

    fun onAddToCart(dish: Dish) {
        cartRepository.addToCart(dish)
        viewModelScope.launch {
            _userMessage.emit("Блюдо '${dish.name}' добавлено в корзину")
        }
    }

    fun onOrderPlaced() {
        viewModelScope.launch {
            _userMessage.emit("Спасибо за заказ!")
        }
    }
    
    fun postMessage(message: String) {
        viewModelScope.launch {
            _userMessage.emit(message)
        }
    }
}
