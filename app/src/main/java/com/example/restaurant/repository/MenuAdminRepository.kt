package com.example.restaurant.repository

import com.example.restaurant.data.dao.CategoryDao
import com.example.restaurant.data.dao.DishDao
import com.example.restaurant.data.entity.Category
import com.example.restaurant.data.entity.Dish
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MenuAdminRepository @Inject constructor(
    private val dishDao: DishDao,
    private val categoryDao: CategoryDao
) {

    fun getAllDishes(): Flow<List<Dish>> = dishDao.getAllDishes() // Assuming you add this to DishDao

    fun getAllCategories(): Flow<List<Category>> = categoryDao.getAllCategories()

    suspend fun insertDish(dish: Dish) {
        dishDao.insertDish(dish)
    }

    suspend fun deleteDish(dish: Dish) {
        dishDao.deleteDish(dish) // Assuming you add this to DishDao
    }
}
