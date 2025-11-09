package com.example.restaurant.data.entity

import androidx.room.DatabaseView
import androidx.room.Embedded

@DatabaseView("""
    SELECT order_items.*, dishes.name AS dish_name, dishes.image_res_id AS dish_image_res_id
    FROM order_items
    INNER JOIN dishes ON order_items.dish_id = dishes.id
""")
data class OrderItemWithDish(
    @Embedded val orderItem: OrderItem,
    val dish_name: String,
    val dish_image_res_id: Int?
)
