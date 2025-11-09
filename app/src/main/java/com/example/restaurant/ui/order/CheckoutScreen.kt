package com.example.restaurant.ui.order

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CheckoutScreen(
    viewModel: CheckoutViewModel = hiltViewModel(),
    onOrderPlaced: () -> Unit
) {
    val paymentOptions = listOf("Наличными", "Картой")
    var selectedPaymentMethod by remember { mutableStateOf(paymentOptions[0]) }

    LaunchedEffect(Unit) {
        viewModel.orderPlaced.collect {
            onOrderPlaced()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Оформление заказа")
        Spacer(modifier = Modifier.height(16.dp))

        paymentOptions.forEach { text ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (text == selectedPaymentMethod),
                        onClick = { selectedPaymentMethod = text }
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == selectedPaymentMethod),
                    onClick = { selectedPaymentMethod = text }
                )
                Text(
                    text = text,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { viewModel.placeOrder(selectedPaymentMethod) }) {
            Text("Оплатить")
        }
    }
}
