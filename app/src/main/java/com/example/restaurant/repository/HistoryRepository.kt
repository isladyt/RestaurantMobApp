package com.example.restaurant.repository

import com.example.restaurant.data.dao.OrderDao
import com.example.restaurant.data.dao.OrderItemDao
import com.example.restaurant.data.entity.Order
import com.example.restaurant.data.entity.OrderItemWithDish
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HistoryRepository @Inject constructor(
    private val orderDao: OrderDao,
    private val orderItemDao: OrderItemDao
) {

    fun getOrderHistory(userId: Int): Flow<List<Order>> = orderDao.getOrderHistory(userId)

    // Исправлено: вызываем правильный метод
    fun getOrderItems(orderId: Int): Flow<List<OrderItemWithDish>> = orderItemDao.getOrderItemsWithDishes(orderId)
}
