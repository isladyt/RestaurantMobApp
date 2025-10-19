package com.example.restaurant.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "orders",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["user_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["user_id"])]
)
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val user_id: Int,
    val status_order: String,
    val payment_method: String,
    val total_amount: Double,
    val created_at: Long = System.currentTimeMillis()
)
