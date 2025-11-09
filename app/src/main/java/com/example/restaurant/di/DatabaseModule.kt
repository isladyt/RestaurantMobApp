package com.example.restaurant.di

import android.content.Context
import androidx.room.Room
import com.example.restaurant.data.AppDatabase
import com.example.restaurant.data.dao.CategoryDao
import com.example.restaurant.data.dao.DishDao
import com.example.restaurant.data.dao.OrderDao
import com.example.restaurant.data.dao.OrderItemDao
import com.example.restaurant.data.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "restaurant-app-v3.db" // Финальное, финальное имя
        )
        .fallbackToDestructiveMigration()
        .build() // Убираем ненадежный addCallback
    }

    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao = appDatabase.userDao()

    @Provides
    fun provideCategoryDao(appDatabase: AppDatabase): CategoryDao = appDatabase.categoryDao()

    @Provides
    fun provideDishDao(appDatabase: AppDatabase): DishDao = appDatabase.dishDao()

    @Provides
    fun provideOrderDao(appDatabase: AppDatabase): OrderDao = appDatabase.orderDao()

    @Provides
    fun provideOrderItemDao(appDatabase: AppDatabase): OrderItemDao = appDatabase.orderItemDao()
}
