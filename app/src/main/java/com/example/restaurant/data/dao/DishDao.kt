package com.example.restaurant.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.restaurant.data.entity.Dish
import kotlinx.coroutines.flow.Flow

@Dao
interface DishDao {
    @Query("SELECT * FROM dishes WHERE category_id = :categoryId")
    fun getDishesByCategory(categoryId: Int): Flow<List<Dish>>

    @Query("SELECT * FROM dishes")
    fun getAllDishes(): Flow<List<Dish>>

    @Query("SELECT * FROM dishes WHERE is_favorite = 1")
    fun getFavoriteDishes(): Flow<List<Dish>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDish(dish: Dish)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(dishes: List<Dish>)

    @Delete
    suspend fun deleteDish(dish: Dish)

    @Query("DELETE FROM dishes")
    suspend fun clearTable()

    @Query("UPDATE dishes SET is_favorite = :isFavorite WHERE id = :dishId")
    suspend fun setFavorite(dishId: Int, isFavorite: Boolean)
}
