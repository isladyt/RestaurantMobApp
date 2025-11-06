package com.example.restaurant.repository

import com.example.restaurant.data.dao.OrderDao
import com.example.restaurant.data.entity.Order
import java.util.Calendar
import javax.inject.Inject

class ReportRepository @Inject constructor(private val orderDao: OrderDao) {

    suspend fun getTodaysOrders(): List<Order> {
        // Устанавливаем время на начало сегодняшнего дня
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startTime = calendar.timeInMillis
        return orderDao.getTodaysOrders(startTime)
    }
}
