package com.example.restaurant.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurant.data.entity.Category
import com.example.restaurant.data.entity.Dish
import com.example.restaurant.repository.MenuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(private val menuRepository: MenuRepository) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _dishes = MutableStateFlow<List<Dish>>(emptyList())
    val dishes: StateFlow<List<Dish>> = _dishes

    private val _selectedCategoryId = MutableStateFlow<Int?>(null)
    val selectedCategoryId: StateFlow<Int?> = _selectedCategoryId

    val favoriteDishes: StateFlow<List<Dish>> = menuRepository.getFavoriteDishes()
        .stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.Lazily, emptyList())

    init {
        menuRepository.getAllCategories()
            .onEach { categoryList ->
                _categories.value = categoryList
                if (categoryList.isNotEmpty() && _selectedCategoryId.value == null) {
                    selectCategory(categoryList.first().id)
                }
            }
            .launchIn(viewModelScope)
    }

    fun selectCategory(categoryId: Int) {
        _selectedCategoryId.value = categoryId
        menuRepository.getDishesByCategory(categoryId)
            .onEach { _dishes.value = it }
            .launchIn(viewModelScope)
    }

    fun setFavorite(dishId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            menuRepository.setFavorite(dishId, isFavorite)
        }
    }
}
