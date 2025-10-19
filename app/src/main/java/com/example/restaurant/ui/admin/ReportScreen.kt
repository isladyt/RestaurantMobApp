package com.example.restaurant.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ReportScreen(viewModel: ReportViewModel = hiltViewModel()) {
    val reportState by viewModel.reportState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (val state = reportState) {
            is ReportState.Success -> {
                Text("Report saved to: ${state.filePath}")
            }
            is ReportState.Error -> {
                Text("Error: ${state.message}")
            }
            ReportState.Loading -> {
                CircularProgressIndicator()
            }
            ReportState.Idle -> {
                Button(onClick = { viewModel.generateReport() }) {
                    Text("Generate Daily Report")
                }
            }
        }
    }
}
