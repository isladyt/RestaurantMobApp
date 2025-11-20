package com.example.restaurant.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.restaurant.ui.admin.AdminNavigation
import com.example.restaurant.ui.auth.AuthNavigation
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(mainViewModel: MainViewModel = hiltViewModel()) {
    val screenState by mainViewModel.screenState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Централизованный обработчик сообщений
    LaunchedEffect(Unit) {
        mainViewModel.userMessage.collect { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    when (val state = screenState) {
        is ScreenState.Auth -> {
            AuthNavigation(onLoginSuccess = { user -> mainViewModel.onLoginSuccess(user) })
        }
        is ScreenState.Main -> {
            if (state.user.is_admin) {
                AdminNavigation(onLogout = { mainViewModel.onLogout() })
            } else {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    bottomBar = {
                        BottomAppBar {
                            NavigationBarItem(
                                icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Меню") },
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
                                selected = currentRoute == "history/${state.user.id}",
                                onClick = { navController.navigate("history/${state.user.id}") }
                            )
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.Person, contentDescription = "Профиль") },
                                label = { Text("Профиль") },
                                selected = currentRoute == "profile/${state.user.login}",
                                onClick = { navController.navigate("profile/${state.user.login}") }
                            )
                        }
                    }
                ) { padding ->
                    MainNavigation(
                        navController = navController,
                        mainViewModel = mainViewModel,
                        userId = state.user.id,
                        userLogin = state.user.login,
                        modifier = Modifier.padding(padding),
                        onUserDeleted = { mainViewModel.onLogout() }
                    )
                }
            }
        }
    }
}
