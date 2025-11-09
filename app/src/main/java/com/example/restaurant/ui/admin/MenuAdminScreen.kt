package com.example.restaurant.ui.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.restaurant.ui.menu.DishItem

@Composable
fun MenuAdminScreen(
    viewModel: MenuAdminViewModel = hiltViewModel(),
    onAddDish: () -> Unit,
    onEditDish: (Int) -> Unit
) {
    val dishes by viewModel.dishes.collectAsState()
    val dishToDelete by viewModel.dishToDelete.collectAsState()

    if (dishToDelete != null) {
        AlertDialog(
            onDismissRequest = { viewModel.onDismissDelete() },
            title = { Text("Подтверждение удаления") },
            text = { Text("Вы уверены, что хотите удалить блюдо '${dishToDelete!!.name}'?") },
            confirmButton = {
                Button(onClick = { viewModel.onConfirmDelete() }) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                Button(onClick = { viewModel.onDismissDelete() }) {
                    Text("Отмена")
                }
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddDish) {
                Icon(Icons.Default.Add, contentDescription = "Добавить блюдо")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(dishes) { dish ->
                Row(modifier = Modifier.clickable { onEditDish(dish.id) }) {
                    DishItem(
                        dish = dish, 
                        onAddToCart = null, 
                        onSetFavorite = null,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { viewModel.onDeleteDishClick(dish) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Удалить")
                    }
                }
            }
        }
    }
}
