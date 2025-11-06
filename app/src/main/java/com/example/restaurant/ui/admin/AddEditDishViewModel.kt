package com.example.restaurant.ui.admin

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurant.R
import com.example.restaurant.data.entity.Category
import com.example.restaurant.data.entity.Dish
import com.example.restaurant.repository.MenuAdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditDishViewModel @Inject constructor(
    private val menuAdminRepository: MenuAdminRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _dish = MutableStateFlow<Dish?>(null)
    val dish: StateFlow<Dish?> = _dish

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val dishId: Int = savedStateHandle.get<Int>("dishId") ?: -1

    init {
        if (dishId != -1) {
            viewModelScope.launch {
                _dish.value = menuAdminRepository.getDish(dishId)
            }
        }
        menuAdminRepository.getAllCategories()
            .onEach { _categories.value = it }
            .launchIn(viewModelScope)
    }

    fun saveDish(name: String, description: String, ingredients: String, price: String, categoryId: Int) {
        val priceDouble = price.toDoubleOrNull() ?: 0.0
        
        // Простая логика для присвоения ID картинки (можно улучшить)
        val imageResId = when {
            name.contains("Маргарита", ignoreCase = true) -> R.drawable.margarita
            name.contains("Пепперони", ignoreCase = true) -> R.drawable.pepperoni
            // ... добавьте остальные
            else -> null
        }

        val dishToSave = dish.value?.copy(
            name = name,
            description = description,
            ingredients = ingredients,
            price = priceDouble,
            category_id = categoryId,
            image_res_id = imageResId
        ) ?: Dish(
            name = name,
            description = description,
            ingredients = ingredients,
            price = priceDouble,
            category_id = categoryId,
            image_res_id = imageResId
        )

        viewModelScope.launch {
            if (dishToSave.id == 0) {
                menuAdminRepository.addDish(dishToSave)
            } else {
                menuAdminRepository.updateDish(dishToSave)
            }
        }
    }
}
