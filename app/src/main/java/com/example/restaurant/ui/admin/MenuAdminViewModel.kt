package com.example.restaurant.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class MenuAdminViewModel @Inject constructor(private val menuAdminRepository: MenuAdminRepository) : ViewModel() {

    private val _dishes = MutableStateFlow<List<Dish>>(emptyList())
    val dishes: StateFlow<List<Dish>> = _dishes

    init {
        menuAdminRepository.getAllDishes()
            .onEach { _dishes.value = it }
            .launchIn(viewModelScope)
    }

    fun deleteDish(dish: Dish) {
        viewModelScope.launch {
            menuAdminRepository.deleteDish(dish)
        }
    }
}
