package com.example.restaurant.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurant.data.entity.Dish
import com.example.restaurant.repository.MenuAdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuAdminViewModel @Inject constructor(private val menuAdminRepository: MenuAdminRepository) : ViewModel() {

    val dishes: StateFlow<List<Dish>> = menuAdminRepository.getAllDishes()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _dishToDelete = MutableStateFlow<Dish?>(null)
    val dishToDelete = _dishToDelete.asStateFlow()

    fun onDeleteDishClick(dish: Dish) {
        _dishToDelete.value = dish
    }

    fun onConfirmDelete() {
        _dishToDelete.value?.let { dish ->
            viewModelScope.launch {
                menuAdminRepository.deleteDish(dish)
                _dishToDelete.value = null // Сбрасываем после удаления
            }
        }
    }

    fun onDismissDelete() {
        _dishToDelete.value = null
    }
}
