package com.example.restaurant.ui.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.restaurant.repository.CartItem

@Composable
fun CartScreen(
    viewModel: CartViewModel = hiltViewModel(),
    onCheckout: () -> Unit
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val totalPrice by viewModel.totalPrice.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        // Заголовок экрана
        Text(
            text = "Ваша корзина",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(cartItems) { item ->
                CartItemRow(
                    item = item, 
                    onIncrease = { viewModel.increaseQuantity(it) },
                    onDecrease = { viewModel.decreaseQuantity(it) }
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(text = "Итого: $totalPrice руб.", style = MaterialTheme.typography.headlineSmall)
            Button(onClick = onCheckout, enabled = cartItems.isNotEmpty()) {
                Text("Оформить заказ")
            }
        }
    }
}

@Composable
fun CartItemRow(
    item: CartItem, 
    onIncrease: (Int) -> Unit,
    onDecrease: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(item.dish.name, modifier = Modifier.weight(1f))
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onDecrease(item.dish.id) }) {
                Icon(Icons.Default.Remove, contentDescription = "Уменьшить")
            }
            Text(text = "${item.quantity}")
            IconButton(onClick = { onIncrease(item.dish.id) }) {
                Icon(Icons.Default.Add, contentDescription = "Увеличить")
            }
        }
        Text("${item.dish.price * item.quantity} руб.", modifier = Modifier.width(80.dp))
    }
}
