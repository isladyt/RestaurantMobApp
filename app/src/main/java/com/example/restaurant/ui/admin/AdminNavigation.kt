package com.example.restaurant.ui.admin

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// Определяем маршруты для админки
const val ADMIN_ORDERS_ROUTE = "admin_orders"
const val ADMIN_MENU_ROUTE = "admin_menu"
const val ADMIN_REPORTS_ROUTE = "admin_reports"
const val ADD_EDIT_DISH_ROUTE = "add_edit_dish"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            BottomAppBar {
                NavigationBarItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Заказы") },
                    label = { Text("Заказы") },
                    selected = currentRoute == ADMIN_ORDERS_ROUTE,
                    onClick = { navController.navigate(ADMIN_ORDERS_ROUTE) }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Add, contentDescription = "Меню") },
                    label = { Text("Меню") },
                    selected = currentRoute == ADMIN_MENU_ROUTE,
                    onClick = { navController.navigate(ADMIN_MENU_ROUTE) }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Warning, contentDescription = "Отчеты") },
                    label = { Text("Отчеты") },
                    selected = currentRoute == ADMIN_REPORTS_ROUTE,
                    onClick = { navController.navigate(ADMIN_REPORTS_ROUTE) }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController, 
            startDestination = ADMIN_ORDERS_ROUTE, 
            modifier = Modifier.padding(padding)
        ) {
            composable(ADMIN_ORDERS_ROUTE) {
                AdminOrderScreen()
            }
            composable(ADMIN_MENU_ROUTE) {
                MenuAdminScreen(
                    onAddDish = { navController.navigate("$ADD_EDIT_DISH_ROUTE/-1") },
                    onEditDish = { navController.navigate("$ADD_EDIT_DISH_ROUTE/$it") }
                )
            }
            composable(
                route = "$ADD_EDIT_DISH_ROUTE/{dishId}",
                arguments = listOf(navArgument("dishId") { type = NavType.IntType })
            ) {
                AddEditDishScreen(onDishSaved = { navController.popBackStack() })
            }
            composable(ADMIN_REPORTS_ROUTE) {
                ReportScreen()
            }
        }
    }
}
