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

    // ИСПРАВЛЕНО: Используем прямой JOIN-запрос
    @Query("""
        SELECT 
            order_items.*, 
            dishes.name AS dish_name, 
            dishes.image_res_id AS dish_image_res_id
        FROM order_items 
        INNER JOIN dishes ON order_items.dish_id = dishes.id 
        WHERE order_items.order_id = :orderId
    """)
    fun getOrderItemsWithDishes(orderId: Int): Flow<List<OrderItemWithDish>>
}
