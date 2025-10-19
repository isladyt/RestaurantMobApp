package com.example.restaurant.ui.order

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurant.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _orderState = MutableStateFlow<OrderState>(OrderState.Idle)
    val orderState: StateFlow<OrderState> = _orderState

    // Получаем userId из SavedStateHandle, который будет передан через навигацию
    private val currentUserId: Int = savedStateHandle.get<Int>("userId") ?: -1

    fun createOrder(paymentMethod: String) {
        // Проверяем, что у нас есть валидный userId
        if (currentUserId == -1) {
            _orderState.value = OrderState.Error("Ошибка: ID пользователя не найден")
            return
        }

        viewModelScope.launch {
            _orderState.value = OrderState.Loading
            val result = orderRepository.createOrder(currentUserId, paymentMethod)
            _orderState.value = result.fold(
                onSuccess = { OrderState.Success },
                onFailure = { OrderState.Error(it.message ?: "Произошла неизвестная ошибка") }
            )
        }
    }
}

sealed class OrderState {
    object Idle : OrderState()
    object Loading : OrderState()
    object Success : OrderState()
    data class Error(val message: String) : OrderState()
}
