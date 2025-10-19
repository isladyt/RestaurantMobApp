package com.example.restaurant.ui.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.restaurant.data.entity.Order

@Composable
fun HistoryScreen(viewModel: HistoryViewModel = hiltViewModel()) {
    val orders by viewModel.orders.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "История заказов",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(orders) { order ->
                OrderItem(order)
            }
        }
    }
}

@Composable
fun OrderItem(order: Order) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Заказ №${order.id}", style = MaterialTheme.typography.titleMedium)
            Text("Статус: ${order.status_order}")
            Text("Сумма заказа: ${order.total_amount} ₽")
            Text("Способ оплаты: ${order.payment_method}")
        }
    }
}
