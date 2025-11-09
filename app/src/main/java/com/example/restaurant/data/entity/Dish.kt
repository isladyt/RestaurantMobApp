package com.example.restaurant.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dishes")
data class Dish(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val ingredients: String,
    val price: Int, // Меняем на Int
    val category_id: Int,
    val image_res_id: Int? = null,
    var is_favorite: Boolean = false
)
