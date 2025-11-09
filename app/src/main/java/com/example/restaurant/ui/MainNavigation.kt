package com.example.restaurant.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.restaurant.ui.cart.CartScreen
import com.example.restaurant.ui.favorites.FavoritesScreen
import com.example.restaurant.ui.history.HistoryScreen
import com.example.restaurant.ui.menu.MenuScreen
import com.example.restaurant.ui.order.CheckoutScreen
import com.example.restaurant.ui.order.OrderDetailScreen
import com.example.restaurant.ui.profile.ProfileScreen

const val MENU_ROUTE = "menu"
const val CART_ROUTE = "cart"
const val CHECKOUT_ROUTE = "checkout/{userId}"
const val HISTORY_ROUTE = "history/{userId}"
const val PROFILE_ROUTE = "profile/{userLogin}"
const val FAVORITES_ROUTE = "favorites"
const val ORDER_DETAIL_ROUTE = "order_detail/{orderId}"

@Composable
fun MainNavigation(
    navController: NavHostController, 
    mainViewModel: MainViewModel, 
    userId: Int,
    userLogin: String,
    modifier: Modifier = Modifier,
    onUserDeleted: () -> Unit
) {
    NavHost(navController = navController, startDestination = MENU_ROUTE, modifier = modifier) {
        composable(MENU_ROUTE) {
            MenuScreen(onAddToCart = { mainViewModel.onAddToCart(it) })
        }
        composable(CART_ROUTE) {
            CartScreen(onCheckout = { navController.navigate("checkout/$userId") })
        }
        composable(
            route = CHECKOUT_ROUTE,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) {
            CheckoutScreen(onOrderPlaced = {
                mainViewModel.onOrderPlaced()
                navController.navigate("history/$userId") {
                    popUpTo(MENU_ROUTE)
                }
            })
        }
        composable(
            route = HISTORY_ROUTE,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) {
            HistoryScreen(onOrderClick = { navController.navigate("order_detail/$it") })
        }
        composable(
            route = PROFILE_ROUTE,
            arguments = listOf(navArgument("userLogin") { type = NavType.StringType })
        ) {
            ProfileScreen(onUserDeleted = onUserDeleted)
        }
        composable(FAVORITES_ROUTE) {
            FavoritesScreen(onAddToCart = { mainViewModel.onAddToCart(it) })
        }
        composable(
            route = ORDER_DETAIL_ROUTE,
            arguments = listOf(navArgument("orderId") { type = NavType.IntType })
        ) {
            OrderDetailScreen(navController)
        }
    }
}
