package com.example.restaurant.repository

import com.example.restaurant.data.entity.Dish
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

data class CartItem(val dish: Dish, val quantity: Int)

@Singleton
class CartRepository @Inject constructor() {

    private val _cart = MutableStateFlow<Map<Int, CartItem>>(emptyMap())

    fun getCart(): Flow<List<CartItem>> = _cart.map { it.values.toList() }

    fun addToCart(dish: Dish) {
        _cart.value = _cart.value.toMutableMap().apply {
            val existingItem = this[dish.id]
            if (existingItem != null) {
                this[dish.id] = existingItem.copy(quantity = existingItem.quantity + 1)
            } else {
                this[dish.id] = CartItem(dish, 1)
            }
        }
    }

    fun removeFromCart(dishId: Int) {
        _cart.value = _cart.value.toMutableMap().apply {
            remove(dishId)
        }
    }

    fun increaseQuantity(dishId: Int) {
        _cart.value = _cart.value.toMutableMap().apply {
            val existingItem = this[dishId]
            if (existingItem != null) {
                this[dishId] = existingItem.copy(quantity = existingItem.quantity + 1)
            }
        }
    }

    fun decreaseQuantity(dishId: Int) {
        _cart.value = _cart.value.toMutableMap().apply {
            val existingItem = this[dishId]
            if (existingItem != null) {
                if (existingItem.quantity > 1) {
                    this[dishId] = existingItem.copy(quantity = existingItem.quantity - 1)
                } else {
                    remove(dishId)
                }
            }
        }
    }

    fun getTotalPrice(): Flow<Int> = getCart().map { cart ->
        cart.sumOf { it.dish.price * it.quantity }
    }

    fun clearCart() {
        _cart.value = emptyMap()
    }
}
