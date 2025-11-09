package com.example.restaurant.ui.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.restaurant.data.entity.Order

@Composable
fun AdminOrderScreen(
    viewModel: AdminOrderViewModel = hiltViewModel(),
    onOrderClick: (Int) -> Unit
) {
    val orders by viewModel.orders.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(orders) { order ->
            AdminOrderItem(order, onOrderClick)
        }
    }
}

@Composable
fun AdminOrderItem(order: Order, onOrderClick: (Int) -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable { onOrderClick(order.id) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Заказ №${order.id}")
            Text("Пользователь ID: ${order.user_id}")
            Text("Статус: ${order.status_order}")
        }
    }
}
