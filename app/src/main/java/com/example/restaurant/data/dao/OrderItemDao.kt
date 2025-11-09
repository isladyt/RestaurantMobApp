package com.example.restaurant.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.restaurant.data.entity.OrderItem
import com.example.restaurant.data.entity.OrderItemWithDish
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItem(orderItem: OrderItem)

    @Query("SELECT * FROM OrderItemWithDish WHERE order_id = :orderId")
    fun getOrderItemsWithDishes(orderId: Int): Flow<List<OrderItemWithDish>>
}
