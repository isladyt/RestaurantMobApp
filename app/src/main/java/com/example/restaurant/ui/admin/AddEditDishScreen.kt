package com.example.restaurant.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditDishScreen(
    viewModel: AddEditDishViewModel = hiltViewModel(),
    onDishSaved: () -> Unit
) {
    val dish by viewModel.dish.collectAsState()
    val categories by viewModel.categories.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    var name by remember(dish) { mutableStateOf(dish?.name ?: "") }
    var description by remember(dish) { mutableStateOf(dish?.description ?: "") }
    var ingredients by remember(dish) { mutableStateOf(dish?.ingredients ?: "") }
    var price by remember(dish) { mutableStateOf(dish?.price?.toString() ?: "") }
    var categoryId by remember(dish) { mutableStateOf(dish?.category_id ?: -1) }

    val selectedCategoryName = categories.find { it.id == categoryId }?.name ?: "Выберите категорию"

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = if (dish == null) "Новое блюдо" else "Редактировать блюдо",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Название") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Описание") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = ingredients,
                onValueChange = { ingredients = it },
                label = { Text("Ингредиенты") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = price, 
                onValueChange = { price = it }, 
                label = { Text("Цена") }, 
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(
                    value = selectedCategoryName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Категория") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    categories.forEach {
                        DropdownMenuItem(
                            text = { Text(it.name) },
                            onClick = {
                                categoryId = it.id
                                expanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(70.dp))
        }

        Button(
            onClick = {
                viewModel.saveDish(name, description, ingredients, price, categoryId)
                onDishSaved()
            },
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
        ) {
            Text("Сохранить")
        }
    }
}
