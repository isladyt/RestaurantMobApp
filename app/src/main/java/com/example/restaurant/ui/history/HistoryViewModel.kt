package com.example.restaurant.ui.history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurant.data.entity.Order
import com.example.restaurant.repository.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyRepository: HistoryRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    // Получаем userId из SavedStateHandle
    private val currentUserId: Int = savedStateHandle.get<Int>("userId") ?: -1

    init {
        if (currentUserId != -1) {
            historyRepository.getOrderHistory(currentUserId)
                .onEach { _orders.value = it }
                .launchIn(viewModelScope)
        }
    }
}
