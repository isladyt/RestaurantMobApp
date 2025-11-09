package com.example.restaurant.ui.order

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurant.data.entity.Order
import com.example.restaurant.data.entity.OrderItemWithDish
import com.example.restaurant.repository.AuthRepository
import com.example.restaurant.repository.HistoryRepository
import com.example.restaurant.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val historyRepository: HistoryRepository,
    private val orderRepository: OrderRepository,
    authRepository: AuthRepository, // Внедряем AuthRepository
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val orderId: Int = savedStateHandle.get<Int>("orderId") ?: -1

    val orderItems: StateFlow<List<OrderItemWithDish>> = historyRepository.getOrderItems(orderId)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        
    val order: StateFlow<Order?> = orderRepository.getOrder(orderId)
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val isAdmin: StateFlow<Boolean> = authRepository.isUserAdmin()
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

}
