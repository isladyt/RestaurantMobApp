package com.example.restaurant.ui.order

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurant.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val userId: Int = savedStateHandle.get<Int>("userId") ?: -1

    private val _orderPlaced = MutableSharedFlow<Unit>()
    val orderPlaced = _orderPlaced.asSharedFlow()

    fun placeOrder(paymentMethod: String) {
        viewModelScope.launch {
            orderRepository.createOrder(userId, paymentMethod)
            _orderPlaced.emit(Unit)
        }
    }
}
