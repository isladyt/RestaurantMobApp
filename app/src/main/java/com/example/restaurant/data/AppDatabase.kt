package com.example.restaurant.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.restaurant.data.dao.CategoryDao
import com.example.restaurant.data.dao.DishDao
import com.example.restaurant.data.dao.OrderDao
import com.example.restaurant.data.dao.OrderItemDao
import com.example.restaurant.data.dao.UserDao
import com.example.restaurant.data.entity.Category
import com.example.restaurant.data.entity.Dish
import com.example.restaurant.data.entity.Order
import com.example.restaurant.data.entity.OrderItem
import com.example.restaurant.data.entity.User

@Database(
    entities = [User::class, Category::class, Dish::class, Order::class, OrderItem::class],
    version = 20, // Увеличиваем версию
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun dishDao(): DishDao
    abstract fun orderDao(): OrderDao
    abstract fun orderItemDao(): OrderItemDao
}
