package com.example.restaurant.ui.admin

import androidx.compose.runtime.Composable

@Composable
fun AdminNavigation() {
    // Теперь AdminNavigation просто показывает экран заказов, так как проверка входа уже была сделана в MainActivity
    AdminOrderScreen()
}
