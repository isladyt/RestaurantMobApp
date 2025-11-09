package com.example.restaurant.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurant.repository.CartItem
import com.example.restaurant.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val cartRepository: CartRepository) : ViewModel() {

    val cartItems: StateFlow<List<CartItem>> = cartRepository.getCart()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val totalPrice: StateFlow<Int> = cartRepository.getTotalPrice()
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    fun removeFromCart(dishId: Int) {
        cartRepository.removeFromCart(dishId)
    }

    fun increaseQuantity(dishId: Int) {
        cartRepository.increaseQuantity(dishId)
    }

    fun decreaseQuantity(dishId: Int) {
        cartRepository.decreaseQuantity(dishId)
    }
}
