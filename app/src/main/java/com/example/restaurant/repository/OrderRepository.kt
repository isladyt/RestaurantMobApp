package com.example.restaurant.repository

import com.example.restaurant.data.dao.OrderDao
import com.example.restaurant.data.dao.OrderItemDao
import com.example.restaurant.data.entity.Order
import com.example.restaurant.data.entity.OrderItem
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val orderDao: OrderDao,
    private val orderItemDao: OrderItemDao,
    private val cartRepository: CartRepository
) {

    suspend fun createOrder(userId: Int, paymentMethod: String): Result<Unit> {
        return try {
            // Исправлено: получаем текущее значение из Flow с помощью .first()
            val cartItems = cartRepository.getCart().first()
            if (cartItems.isEmpty()) {
                return Result.failure(Exception("Корзина пуста"))
            }

            val totalAmount = cartItems.sumOf { it.dish.price * it.quantity }

            val order = Order(
                user_id = userId,
                status_order = "принят",
                payment_method = paymentMethod,
                total_amount = totalAmount
            )
            val orderId = orderDao.insertOrder(order).toInt()

            cartItems.forEach { cartItem ->
                val orderItem = OrderItem(
                    order_id = orderId,
                    dish_id = cartItem.dish.id,
                    quantity = cartItem.quantity,
                    price_at_time_of_order = cartItem.dish.price
                )
                orderItemDao.insertOrderItem(orderItem)
            }
            cartRepository.clearCart()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
