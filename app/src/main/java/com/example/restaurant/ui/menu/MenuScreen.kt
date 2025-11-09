package com.example.restaurant.ui.menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
        Text(
            text = "Ресторан Меню",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(16.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                FilterChip(
                    selected = category.id == selectedCategoryId,
                    onClick = { viewModel.selectCategory(category.id) },
                    label = { Text(category.name) }
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
    onAddToCart: ((Dish) -> Unit)? = null,
    onSetFavorite: ((Int, Boolean) -> Unit)?,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = dish.image_res_id,
                contentDescription = dish.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)) {
                Text(dish.name, style = MaterialTheme.typography.titleMedium)
                Text(dish.description, style = MaterialTheme.typography.bodySmall)
                Text("Цена: ${dish.price} ₽", style = MaterialTheme.typography.bodyMedium)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (onAddToCart != null) {
                        Button(onClick = { onAddToCart(dish) }) {
                            Text("В корзину")
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    if (onSetFavorite != null) {
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
}
