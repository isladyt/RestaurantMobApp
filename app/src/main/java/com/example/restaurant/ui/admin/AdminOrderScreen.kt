package com.example.restaurant.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.restaurant.data.entity.Order

@Composable
fun AdminOrderScreen(viewModel: AdminViewModel = hiltViewModel()) {
    val orders by viewModel.orders.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(orders) { order ->
            AdminOrderItem(order, onStatusChange = { viewModel.updateOrderStatus(order.id, it) })
        }
    }
}

@Composable
fun AdminOrderItem(order: Order, onStatusChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val statuses = listOf("принят", "готовится", "готов", "выполнен")

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Заказ #${order.id}", style = MaterialTheme.typography.titleMedium)
            Text("ID пользователя: ${order.user_id}")
            Text("Итого: ${order.total_amount} руб.")
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Статус: ${order.status_order}")
                Box {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Изменить статус")
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        statuses.forEach { status ->
                            DropdownMenuItem(
                                text = { Text(status) },
                                onClick = {
                                    onStatusChange(status)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
