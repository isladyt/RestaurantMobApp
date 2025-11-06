package com.example.restaurant.repository

import com.example.restaurant.data.dao.CategoryDao
import com.example.restaurant.data.dao.DishDao
import com.example.restaurant.data.entity.Category
import com.example.restaurant.data.entity.Dish
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MenuAdminRepository @Inject constructor(
    private val categoryDao: CategoryDao,
    private val dishDao: DishDao
) {

    fun getAllCategories(): Flow<List<Category>> = categoryDao.getAllCategories()

    fun getAllDishes(): Flow<List<Dish>> = dishDao.getAllDishes()

    suspend fun getDish(dishId: Int): Dish? = dishDao.getDishById(dishId)

    suspend fun addDish(dish: Dish) {
        dishDao.insertDish(dish)
    }

    suspend fun updateDish(dish: Dish) {
        dishDao.insertDish(dish) // insertDish работает как update из-за OnConflictStrategy.REPLACE
    }

    suspend fun deleteDish(dish: Dish) {
        dishDao.deleteDish(dish)
    }
}
