package com.example.restaurant.ui.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.restaurant.ui.menu.DishItem
import com.example.restaurant.ui.menu.MenuViewModel

@Composable
fun FavoritesScreen(
    menuViewModel: MenuViewModel = hiltViewModel()

)
{
    val favoriteDishes by menuViewModel.favoriteDishes.collectAsState()

    if (favoriteDishes.isEmpty()) {
        // Оборачиваем текст в Box для центрирования
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("У вас пока нет избранных блюд")
        }
    } else {
        LazyColumn {
            items(favoriteDishes) { dish ->
                DishItem(
                    dish = dish,
                    onAddToCart = {},
                    onSetFavorite = { dishId, isFavorite ->
                        menuViewModel.setFavorite(dishId, isFavorite)
                    }
                )
            }
        }
    }
}
