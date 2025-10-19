package com.example.restaurant.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.restaurant.data.entity.Order
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order): Long

    @Query("SELECT * FROM orders WHERE user_id = :userId ORDER BY created_at DESC")
    fun getOrderHistory(userId: Int): Flow<List<Order>>

    @Query("SELECT * FROM orders ORDER BY created_at DESC")
    fun getAllOrders(): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE created_at >= :startTime")
    suspend fun getTodaysOrders(startTime: Long): List<Order>

    @Query("UPDATE orders SET status_order = :status WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: Int, status: String)
}
