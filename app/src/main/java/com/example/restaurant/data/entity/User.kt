package com.example.restaurant.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val login: String,
    val password: String, // Больше не хеш
    val name: String,
    val email: String? = null,
    val phone: String? = null,
    val created_at: Long = System.currentTimeMillis(),
    val is_admin: Boolean = false
)
