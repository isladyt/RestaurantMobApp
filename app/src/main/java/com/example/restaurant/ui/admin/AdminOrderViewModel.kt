package com.example.restaurant.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurant.data.entity.Order
import com.example.restaurant.repository.AdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminOrderViewModel @Inject constructor(private val adminRepository: AdminRepository) : ViewModel() {

    val orders: StateFlow<List<Order>> = adminRepository.getAllOrders()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun updateOrderStatus(orderId: Int, newStatus: String) {
        viewModelScope.launch {
            adminRepository.updateOrderStatus(orderId, newStatus)
        }
    }
}
