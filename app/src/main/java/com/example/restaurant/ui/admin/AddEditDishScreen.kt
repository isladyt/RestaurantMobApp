package com.example.restaurant.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AddEditDishScreen(
    viewModel: AddEditDishViewModel = hiltViewModel(),
    onDishSaved: () -> Unit
) {
    val categories by viewModel.categories.collectAsState()
    val dishName by viewModel.dishName.collectAsState()
    val dishDescription by viewModel.dishDescription.collectAsState()
    val dishIngredients by viewModel.dishIngredients.collectAsState()
    val dishPrice by viewModel.dishPrice.collectAsState()
    val dishImageUri by viewModel.dishImageUri.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        OutlinedTextField(value = dishName, onValueChange = { viewModel.onNameChange(it) }, label = { Text("Name") })
        OutlinedTextField(value = dishDescription, onValueChange = { viewModel.onDescriptionChange(it) }, label = { Text("Description") })
        OutlinedTextField(value = dishIngredients, onValueChange = { viewModel.onIngredientsChange(it) }, label = { Text("Ingredients") })
        OutlinedTextField(value = dishPrice, onValueChange = { viewModel.onPriceChange(it) }, label = { Text("Price") })
        OutlinedTextField(value = dishImageUri ?: "", onValueChange = { viewModel.onImageUriChange(it) }, label = { Text("Image URL") })
        
        // TODO: Add a dropdown for categories

        Button(onClick = {
            viewModel.saveDish()
            onDishSaved()
        }) {
            Text("Save")
        }
    }
}
