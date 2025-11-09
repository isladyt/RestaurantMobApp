package com.example.restaurant.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_items")
data class OrderItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val order_id: Int,
    val dish_id: Int,
    val quantity: Int,
    val price_at_time_of_order: Int // Меняем на Int
)
