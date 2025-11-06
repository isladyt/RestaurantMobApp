package com.example.restaurant.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurant.data.entity.Dish
import com.example.restaurant.repository.MenuAdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuAdminViewModel @Inject constructor(private val menuAdminRepository: MenuAdminRepository) : ViewModel() {

    val dishes: StateFlow<List<Dish>> = menuAdminRepository.getAllDishes()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun deleteDish(dish: Dish) {
        viewModelScope.launch {
            menuAdminRepository.deleteDish(dish)
        }
    }
}
