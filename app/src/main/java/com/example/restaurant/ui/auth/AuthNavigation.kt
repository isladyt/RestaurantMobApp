package com.example.restaurant.ui.auth

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

const val LOGIN_ROUTE = "login"
const val REGISTER_ROUTE = "register"

@Composable
fun AuthNavigation(onLoginSuccess: (Int, String, Boolean) -> Unit) {
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
