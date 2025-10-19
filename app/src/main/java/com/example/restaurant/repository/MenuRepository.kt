package com.example.restaurant.repository

import com.example.restaurant.data.dao.CategoryDao
import com.example.restaurant.data.dao.DishDao
import com.example.restaurant.data.entity.Category
import com.example.restaurant.data.entity.Dish
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MenuRepository @Inject constructor(
    private val categoryDao: CategoryDao,
    private val dishDao: DishDao
) {

    fun getAllCategories(): Flow<List<Category>> = categoryDao.getAllCategories()

    fun getDishesByCategory(categoryId: Int): Flow<List<Dish>> = dishDao.getDishesByCategory(categoryId)

    fun getFavoriteDishes(): Flow<List<Dish>> = dishDao.getFavoriteDishes()

    suspend fun setFavorite(dishId: Int, isFavorite: Boolean) {
        dishDao.setFavorite(dishId, isFavorite)
    }
}
