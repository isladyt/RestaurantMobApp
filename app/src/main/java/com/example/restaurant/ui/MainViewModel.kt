package com.example.restaurant.ui

import androidx.lifecycle.ViewModel
import com.example.restaurant.data.entity.Dish
import com.example.restaurant.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val cartRepository: CartRepository) : ViewModel() {

    fun onAddToCart(dish: Dish) {
        cartRepository.addToCart(dish)
    }
}
