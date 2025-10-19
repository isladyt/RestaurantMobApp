package com.example.restaurant.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.restaurant.data.entity.Dish

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuAdminScreen(
    viewModel: MenuAdminViewModel = hiltViewModel(),
    onAddDish: () -> Unit,
    onEditDish: (Dish) -> Unit
) {
    val dishes by viewModel.dishes.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddDish) {
                Icon(Icons.Default.Add, contentDescription = "Add Dish")
            }
        }
    ) {
        LazyColumn(modifier = Modifier.padding(it)) {
            items(dishes) { dish ->
                DishAdminItem(
                    dish = dish,
                    onEdit = { onEditDish(dish) },
                    onDelete = { viewModel.deleteDish(dish) })
            }
        }
    }
}

@Composable
fun DishAdminItem(dish: Dish, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(dish.name, style = MaterialTheme.typography.titleMedium)
                Text(dish.description, style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}
