package com.example.restaurant.repository

import com.example.restaurant.data.dao.OrderDao
import com.example.restaurant.data.entity.Order
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AdminRepository @Inject constructor(private val orderDao: OrderDao) {

    fun getAllOrders(): Flow<List<Order>> = orderDao.getAllOrders()

    suspend fun updateOrderStatus(orderId: Int, status: String) {
        orderDao.updateOrderStatus(orderId, status)
    }
}
