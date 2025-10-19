package com.example.restaurant.repository

import com.example.restaurant.data.dao.OrderDao
import com.example.restaurant.data.dao.OrderItemDao
import com.example.restaurant.data.entity.Order
import com.example.restaurant.data.entity.OrderItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class OrderWithItems(val order: Order, val items: List<OrderItem>)

class HistoryRepository @Inject constructor(
    private val orderDao: OrderDao,
    private val orderItemDao: OrderItemDao
) {

    fun getOrderHistory(userId: Int): Flow<List<Order>> = orderDao.getOrderHistory(userId)

    fun getOrderItems(orderId: Int): Flow<List<OrderItem>> = orderItemDao.getOrderItems(orderId)
}
