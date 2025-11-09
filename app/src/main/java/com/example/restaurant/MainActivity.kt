package com.example.restaurant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import com.example.restaurant.data.DatabaseSeeder
import com.example.restaurant.ui.AppNavigation
import com.example.restaurant.ui.theme.RestaurantAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var databaseSeeder: DatabaseSeeder
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RestaurantAppTheme {
                LaunchedEffect(Unit) {
                    databaseSeeder.seedDatabaseIfEmpty()
                }
                AppNavigation()
            }
        }
    }
}
