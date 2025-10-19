package com.example.restaurant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.restaurant.ui.*
import com.example.restaurant.ui.admin.AdminNavigation
import com.example.restaurant.ui.auth.AuthNavigation
import com.example.restaurant.ui.theme.RestaurantAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private val mainViewModel: MainViewModel by viewModels()
    
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RestaurantAppTheme {
                var isLoggedIn by remember { mutableStateOf(false) }
                var isAdmin by remember { mutableStateOf(false) }
                var currentUserId by remember { mutableStateOf(-1) }
                var currentUserLogin by remember { mutableStateOf("") }

                if (isLoggedIn) {
                    if (isAdmin) {
                        AdminNavigation()
                    } else {
                        val navController = rememberNavController()
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route

                        Scaffold(
                            bottomBar = {
                                BottomAppBar {
                                    NavigationBarItem(
                                        icon = { Icon(Icons.Default.List, contentDescription = "Меню") },
                                        label = { Text("Меню") },
                                        selected = currentRoute == MENU_ROUTE,
                                        onClick = { navController.navigate(MENU_ROUTE) }
                                    )
                                    NavigationBarItem(
                                        icon = { Icon(Icons.Default.Favorite, contentDescription = "Избранное") },
                                        label = { Text("Избранное") },
                                        selected = currentRoute == FAVORITES_ROUTE,
                                        onClick = { navController.navigate(FAVORITES_ROUTE) }
                                    )
                                    NavigationBarItem(
                                        icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Корзина") },
                                        label = { Text("Корзина") },
                                        selected = currentRoute == CART_ROUTE,
                                        onClick = { navController.navigate(CART_ROUTE) }
                                    )
                                    NavigationBarItem(
                                        icon = { Icon(Icons.Default.History, contentDescription = "История") },
                                        label = { Text("История") },
                                        selected = currentRoute == "history/$currentUserId",
                                        onClick = { navController.navigate("history/$currentUserId") }
                                    )
                                    NavigationBarItem(
                                        icon = { Icon(Icons.Default.Person, contentDescription = "Профиль") },
                                        label = { Text("Профиль") },
                                        selected = currentRoute == "profile/$currentUserLogin",
                                        onClick = { navController.navigate("profile/$currentUserLogin") }
                                    )
                                }
                            }
                        ) { padding ->
                            MainNavigation(
                                navController = navController,
                                mainViewModel = mainViewModel,
                                userId = currentUserId,
                                userLogin = currentUserLogin,
                                modifier = Modifier.padding(padding),
                                onUserDeleted = {
                                    isLoggedIn = false
                                    isAdmin = false
                                    currentUserId = -1
                                    currentUserLogin = ""
                                }
                            )
                        }
                    }
                } else {
                    AuthNavigation { id, login, admin ->
                        currentUserId = id
                        currentUserLogin = login
                        isAdmin = admin
                        isLoggedIn = true
                    }
                }
            }
        }
    }
}
