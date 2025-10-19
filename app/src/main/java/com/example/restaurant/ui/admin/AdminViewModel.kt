package com.example.restaurant.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurant.data.entity.Order
import com.example.restaurant.repository.AdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(private val adminRepository: AdminRepository) : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    init {
        adminRepository.getAllOrders()
            .onEach { _orders.value = it }
            .launchIn(viewModelScope)
    }

    fun updateOrderStatus(orderId: Int, status: String) {
        viewModelScope.launch {
            adminRepository.updateOrderStatus(orderId, status)
        }
    }
}
