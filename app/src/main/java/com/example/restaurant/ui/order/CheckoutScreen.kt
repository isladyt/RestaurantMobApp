package com.example.restaurant.ui.order

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CheckoutScreen(
    viewModel: OrderViewModel = hiltViewModel(),
    onOrderPlaced: () -> Unit
) {
    val orderState by viewModel.orderState.collectAsState()
    var selectedPaymentMethod by remember { mutableStateOf("Наличные") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Выберите способ оплаты", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        Row {
            RadioButton(
                selected = selectedPaymentMethod == "Наличные",
                onClick = { selectedPaymentMethod = "Наличные" }
            )
            Text("Наличные", modifier = Modifier.align(Alignment.CenterVertically))
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = selectedPaymentMethod == "Картой",
                onClick = { selectedPaymentMethod = "Картой" }
            )
            Text("Картой", modifier = Modifier.align(Alignment.CenterVertically))
        }

        Spacer(modifier = Modifier.height(32.dp))

        when (val state = orderState) {
            is OrderState.Success -> {
                LaunchedEffect(state) {
                    onOrderPlaced()
                }
            }
            is OrderState.Error -> {
                Text(state.message, color = MaterialTheme.colorScheme.error)
            }
            OrderState.Loading -> {
                CircularProgressIndicator()
            }
            OrderState.Idle -> {
                Button(onClick = { viewModel.createOrder(selectedPaymentMethod) }) {
                    Text("Оформить заказ")
                }
            }
        }
    }
}
