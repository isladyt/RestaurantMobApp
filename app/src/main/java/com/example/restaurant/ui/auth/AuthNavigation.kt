package com.example.restaurant.ui.auth

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.restaurant.data.entity.User

const val LOGIN_ROUTE = "login"
const val REGISTER_ROUTE = "register"

// Обновляем лямбду, чтобы она принимала User
@Composable
fun AuthNavigation(onLoginSuccess: (User) -> Unit) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = LOGIN_ROUTE) {
        composable(LOGIN_ROUTE) {
            LoginScreen(
                onLoginSuccess = onLoginSuccess, 
                onNavigateToRegister = { navController.navigate(REGISTER_ROUTE) }
            )
        }
        composable(REGISTER_ROUTE) {
            RegisterScreen(
                onRegisterSuccess = { navController.popBackStack() }
            )
        }
    }
}
