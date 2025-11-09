package com.example.restaurant.ui.order

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.restaurant.data.entity.OrderItemWithDish
import com.example.restaurant.ui.admin.AdminOrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    navController: NavController, // Добавляем NavController
    viewModel: OrderDetailViewModel = hiltViewModel(),
    adminViewModel: AdminOrderViewModel = hiltViewModel()
) {
    val orderItems by viewModel.orderItems.collectAsState()
    val order by viewModel.order.collectAsState()
    val isAdmin by viewModel.isAdmin.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    val statuses = listOf("принят", "готовится", "готов", "отменен")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Детали заказа") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            // Основной контент (список блюд)
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)) {
                order?.let {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Заказ №${it.id}", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 8.dp))
                        Text("Сумма: ${it.total_amount} ₽", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 8.dp))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    items(orderItems) { item ->
                        OrderItemRow(item)
                    }
                }
            }

            // Панель управления для админа внизу
            if (isAdmin) {
                order?.let { currentOrder ->
                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth(),
                        shadowElevation = 8.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Управление заказом", style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            Box {
                                OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                                    Text("Статус: ${currentOrder.status_order}")
                                }
                                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                    statuses.forEach { status ->
                                        DropdownMenuItem(text = { Text(status) }, onClick = { 
                                            adminViewModel.updateOrderStatus(currentOrder.id, status)
                                            expanded = false
                                        })
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderItemRow(item: OrderItemWithDish) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text("${item.dish_name} x${item.orderItem.quantity}")
        Spacer(modifier = Modifier.width(16.dp))
        Text("${item.orderItem.price_at_time_of_order * item.orderItem.quantity} ₽")
    }
}
