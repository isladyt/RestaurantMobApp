package com.example.restaurant.ui.admin

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ReportScreen(viewModel: ReportViewModel = hiltViewModel()) {
    val reportState by viewModel.reportState.collectAsState()
    val context = LocalContext.current

    val storagePermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.exportReport(context)
        } else {
            Toast.makeText(context, "Разрешение на запись не предоставлено", Toast.LENGTH_SHORT).show()
        }
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                storagePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            } else {
                viewModel.exportReport(context)
            }
        } else {
            Toast.makeText(context, "Разрешение на показ уведомлений не предоставлено", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val state = reportState) {
            is ReportState.Idle -> {
                Button(onClick = { viewModel.generateReport() }) {
                    Text("Сформировать отчет за сегодня")
                }
            }
            is ReportState.Loading -> {
                CircularProgressIndicator()
            }
            is ReportState.Success -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Отчет за сегодня", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Количество заказов: ${state.orderCount}", style = MaterialTheme.typography.bodyLarge)
                    Text("Общая выручка: ${state.totalRevenue} руб.", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(24.dp))
                    Row {
                        Button(onClick = { viewModel.generateReport() }) {
                            Text("Обновить отчет")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(onClick = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                viewModel.exportReport(context)
                            } else {
                                storagePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            }
                        }) {
                            Text("Экспорт в Excel")
                        }
                    }
                }
            }
            is ReportState.Error -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(state.message, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.generateReport() }) {
                        Text("Попробовать снова")
                    }
                }
            }
        }
    }
}
