package com.example.restaurant.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "order_items",
    foreignKeys = [
        ForeignKey(
            entity = Order::class,
            parentColumns = ["id"],
            childColumns = ["order_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Dish::class,
            parentColumns = ["id"],
            childColumns = ["dish_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["order_id"]), Index(value = ["dish_id"])]
)
data class OrderItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val order_id: Int,
    val dish_id: Int,
    val quantity: Int,
    val price_at_time_of_order: Double
)
