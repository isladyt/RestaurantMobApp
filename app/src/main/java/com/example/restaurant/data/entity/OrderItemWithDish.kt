package com.example.restaurant.data.entity

import androidx.room.Embedded

// Убираем @DatabaseView, это теперь просто класс для данных
data class OrderItemWithDish(
    @Embedded val orderItem: OrderItem,
    val dish_name: String,
    val dish_image_res_id: Int?
)
