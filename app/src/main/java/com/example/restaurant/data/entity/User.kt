package com.example.restaurant.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val login: String,
    val password_hash: String,
    val name: String,
    // Новые поля для профиля
    val email: String? = null,
    val phone: String? = null,
    val created_at: Long = System.currentTimeMillis(),
    val is_admin: Boolean = false
)
