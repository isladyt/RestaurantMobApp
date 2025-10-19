package com.example.restaurant.ui.menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.restaurant.data.entity.Dish

@Composable
fun MenuScreen(
    viewModel: MenuViewModel = hiltViewModel(),
    onAddToCart: (Dish) -> Unit
) {
    val categories by viewModel.categories.collectAsState()
    val dishes by viewModel.dishes.collectAsState()
    val selectedCategoryId by viewModel.selectedCategoryId.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        // Заголовок экрана
        Text(
            text = "Меню",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(16.dp)
        )

        TabRow(selectedTabIndex = categories.indexOfFirst { it.id == selectedCategoryId }.coerceAtLeast(0)) {
            categories.forEach {
                Tab(
                    selected = it.id == selectedCategoryId,
                    onClick = { viewModel.selectCategory(it.id) },
                    text = { Text(it.name) }
                )
            }
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(dishes) { dish ->
                DishItem(dish, onAddToCart, viewModel::setFavorite)
            }
        }
    }
}

@Composable
fun DishItem(
    dish: Dish, 
    onAddToCart: (Dish) -> Unit,
    onSetFavorite: (Int, Boolean) -> Unit
) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Row(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = dish.image_uri,
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
            Column(modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)) {
                Text(dish.name, style = MaterialTheme.typography.titleMedium)
                Text(dish.description, style = MaterialTheme.typography.bodySmall)
                Text("Цена: ${dish.price} руб.", style = MaterialTheme.typography.bodyMedium)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(onClick = { onAddToCart(dish) }) {
                        Text("В корзину")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { onSetFavorite(dish.id, !dish.is_favorite) }) {
                        Icon(
                            imageVector = if (dish.is_favorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "В избранное",
                            tint = if (dish.is_favorite) Color.Red else Color.Gray
                        )
                    }
                }
            }
        }
    }
}
