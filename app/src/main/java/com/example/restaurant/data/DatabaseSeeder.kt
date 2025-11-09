package com.example.restaurant.data

import com.example.restaurant.R
import com.example.restaurant.data.dao.CategoryDao
import com.example.restaurant.data.dao.DishDao
import com.example.restaurant.data.dao.UserDao
import com.example.restaurant.data.entity.Category
import com.example.restaurant.data.entity.Dish
import com.example.restaurant.data.entity.User
import javax.inject.Inject

class DatabaseSeeder @Inject constructor(
    private val userDao: UserDao,
    private val categoryDao: CategoryDao,
    private val dishDao: DishDao
) {

    suspend fun seedDatabaseIfEmpty() {
        if (userDao.count() == 0) { // Проверяем, пуста ли база
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
                Dish(name = "Маргарита", description = "Классическая пицца с томатами и моцареллой", price = 450, ingredients = "Тесто, томатный соус, сыр моцарелла, помидоры", category_id = 1, image_res_id = R.drawable.margarita),
                Dish(name = "Пепперони", description = "Острая пицца с салями пепперони", price = 520, ingredients = "Тесто, томатный соус, сыр моцарелла, пепперони", category_id = 1, image_res_id = R.drawable.pepperoni),
                Dish(name = "Четыре сыра", description = "Пицца с дорблю, пармезаном, чеддером и моцареллой", price = 610, ingredients = "Тесто, сливочный соус, сыры", category_id = 1, image_res_id = R.drawable.four_cheeses),
                Dish(name = "Кола", description = "Освежающий газированный напиток", price = 100, ingredients = "Газированная вода, сахар, краситель, ароматизатор", category_id = 2, image_res_id = R.drawable.cola),
                Dish(name = "Морс клюквенный", description = "Натуральный ягодный морс", price = 120, ingredients = "Клюква, вода, сахар", category_id = 2, image_res_id = R.drawable.cranberry_juice),
                Dish(name = "Цезарь", description = "Салат с курицей, сухариками и соусом Цезарь", price = 350, ingredients = "Куриное филе, салат романо, гренки, сыр пармезан, соус Цезарь", category_id = 3, image_res_id = R.drawable.caesar_salad),
                Dish(name = "Борщ", description = "Традиционный борщ со сметаной", price = 280, ingredients = "Говядина, свекла, капуста, картофель", category_id = 4, image_res_id = R.drawable.borscht),
                Dish(name = "Солянка", description = "Насыщенный мясной суп с лимоном и маслинами", price = 320, ingredients = "Несколько видов мяса, соленые огурцы, маслины, лимон", category_id = 4, image_res_id = R.drawable.solyanka),
                Dish(name = "Тирамису", description = "Итальянский десерт с маскарпоне и кофе", price = 250, ingredients = "Сыр маскарпоне, печенье савоярди, кофе, яйца", category_id = 5, image_res_id = R.drawable.tiramisu),
                Dish(name = "Чизкейк Нью-Йорк", description = "Классический чизкейк на песочной основе", price = 270, ingredients = "Сливочный сыр, песочное печенье, сливки, яйца", category_id = 5, image_res_id = R.drawable.cheesecake)
            )
            dishDao.insertAll(dishes)
        }
    }
}
