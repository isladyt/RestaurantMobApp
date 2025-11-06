package com.example.restaurant.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.restaurant.R
import com.example.restaurant.data.AppDatabase
import com.example.restaurant.data.dao.CategoryDao
import com.example.restaurant.data.dao.DishDao
import com.example.restaurant.data.dao.OrderDao
import com.example.restaurant.data.dao.OrderItemDao
import com.example.restaurant.data.dao.UserDao
import com.example.restaurant.data.entity.Category
import com.example.restaurant.data.entity.Dish
import com.example.restaurant.data.entity.User
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        userDao: Provider<UserDao>,
        categoryDao: Provider<CategoryDao>,
        dishDao: Provider<DishDao>
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "restaurant-final-v2.db" // Новое, финальное имя
        )
        .fallbackToDestructiveMigration()
        .addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    prepopulateDatabase(userDao.get(), categoryDao.get(), dishDao.get())
                }
            }
        })
        .build()
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

    private suspend fun prepopulateDatabase(userDao: UserDao, categoryDao: CategoryDao, dishDao: DishDao) {
        // Сохраняем пароль как обычный текст
        val adminUser = User(login = "admin", name = "Admin", password = "admin", is_admin = true)
        userDao.insert(adminUser)

        val categories = listOf(
            Category(id = 1, name = "Пицца"),
            Category(id = 2, name = "Напитки"),
            Category(id = 3, name = "Салаты"),
            Category(id = 4, name = "Супы"),
            Category(id = 5, name = "Десерты")
        )
        categoryDao.insertAll(categories)

        val dishes = listOf(
            Dish(name = "Маргарита", description = "Классическая пицца с томатами и моцареллой", price = 450.0, ingredients = "Тесто, томатный соус, сыр моцарелла, помидоры", category_id = 1, image_res_id = R.drawable.margarita),
            Dish(name = "Пепперони", description = "Острая пицца с салями пепперони", price = 520.0, ingredients = "Тесто, томатный соус, сыр моцарелла, пепперони", category_id = 1, image_res_id = R.drawable.pepperoni),
            Dish(name = "Четыре сыра", description = "Пицца с дорблю, пармезаном, чеддером и моцареллой", price = 610.0, ingredients = "Тесто, сливочный соус, сыры", category_id = 1, image_res_id = R.drawable.four_cheeses),
            Dish(name = "Кола", description = "Освежающий газированный напиток", price = 100.0, ingredients = "Газированная вода, сахар, краситель, ароматизатор", category_id = 2, image_res_id = R.drawable.cola),
            Dish(name = "Морс клюквенный", description = "Натуральный ягодный морс", price = 120.0, ingredients = "Клюква, вода, сахар", category_id = 2, image_res_id = R.drawable.cranberry_juice),
            Dish(name = "Цезарь", description = "Салат с курицей, сухариками и соусом Цезарь", price = 350.0, ingredients = "Куриное филе, салат романо, гренки, сыр пармезан, соус Цезарь", category_id = 3, image_res_id = R.drawable.caesar_salad),
            Dish(name = "Борщ", description = "Традиционный борщ со сметаной", price = 280.0, ingredients = "Говядина, свекла, капуста, картофель", category_id = 4, image_res_id = R.drawable.borscht),
            Dish(name = "Солянка", description = "Насыщенный мясной суп с лимоном и маслинами", price = 320.0, ingredients = "Несколько видов мяса, соленые огурцы, маслины, лимон", category_id = 4, image_res_id = R.drawable.solyanka),
            Dish(name = "Тирамису", description = "Итальянский десерт с маскарпоне и кофе", price = 250.0, ingredients = "Сыр маскарпоне, печенье савоярди, кофе, яйца", category_id = 5, image_res_id = R.drawable.tiramisu),
            Dish(name = "Чизкейк Нью-Йорк", description = "Классический чизкейк на песочной основе", price = 270.0, ingredients = "Сливочный сыр, песочное печенье, сливки, яйца", category_id = 5, image_res_id = R.drawable.cheesecake)
        )
        dishDao.insertAll(dishes)
    }
}
