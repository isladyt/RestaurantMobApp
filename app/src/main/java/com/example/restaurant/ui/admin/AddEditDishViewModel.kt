package com.example.restaurant.ui.admin

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurant.data.entity.Category
import com.example.restaurant.data.entity.Dish
import com.example.restaurant.repository.MenuAdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditDishViewModel @Inject constructor(
    private val menuAdminRepository: MenuAdminRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _dishName = MutableStateFlow("")
    val dishName: StateFlow<String> = _dishName.asStateFlow()

    private val _dishDescription = MutableStateFlow("")
    val dishDescription: StateFlow<String> = _dishDescription.asStateFlow()

    private val _dishIngredients = MutableStateFlow("")
    val dishIngredients: StateFlow<String> = _dishIngredients.asStateFlow()

    private val _dishPrice = MutableStateFlow("")
    val dishPrice: StateFlow<String> = _dishPrice.asStateFlow()

    private val _dishCategoryId = MutableStateFlow<Int?>(null)
    val dishCategoryId: StateFlow<Int?> = _dishCategoryId.asStateFlow()

    private val _dishImageUri = MutableStateFlow<String?>("")
    val dishImageUri: StateFlow<String?> = _dishImageUri.asStateFlow()

    init {
        menuAdminRepository.getAllCategories().onEach { 
            _categories.value = it
            if (_dishCategoryId.value == null && it.isNotEmpty()) {
                _dishCategoryId.value = it.first().id
            }
        }.launchIn(viewModelScope)
    }

    fun onNameChange(name: String) { _dishName.value = name }
    fun onDescriptionChange(description: String) { _dishDescription.value = description }
    fun onIngredientsChange(ingredients: String) { _dishIngredients.value = ingredients }
    fun onPriceChange(price: String) { _dishPrice.value = price }
    fun onImageUriChange(uri: String) { _dishImageUri.value = uri }

    fun saveDish() {
        viewModelScope.launch {
            val dish = Dish(
                id = savedStateHandle.get<Int>("dishId") ?: 0,
                name = _dishName.value,
                description = _dishDescription.value,
                ingredients = _dishIngredients.value,
                price = _dishPrice.value.toDoubleOrNull() ?: 0.0,
                category_id = _dishCategoryId.value ?: 0,
                image_uri = _dishImageUri.value
            )
            menuAdminRepository.insertDish(dish)
        }
    }
}
