package com.example.restaurant.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val user_id: Int,
    val created_at: Long = System.currentTimeMillis(),
    val status_order: String,
    val payment_method: String,
    val total_amount: Int // Меняем на Int
)
