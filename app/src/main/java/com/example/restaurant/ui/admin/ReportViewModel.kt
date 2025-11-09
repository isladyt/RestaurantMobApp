package com.example.restaurant.ui.admin

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurant.data.entity.Order
import com.example.restaurant.repository.ReportRepository
import com.example.restaurant.util.ExcelExporter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(private val reportRepository: ReportRepository) : ViewModel() {

    private val _reportState = MutableStateFlow<ReportState>(ReportState.Idle)
    val reportState: StateFlow<ReportState> = _reportState

    private var currentOrders: List<Order> = emptyList()

    fun generateReport() {
        viewModelScope.launch {
            _reportState.value = ReportState.Loading
            try {
                currentOrders = reportRepository.getTodaysOrders()
                val totalRevenue = currentOrders.sumOf { it.total_amount }
                val orderCount = currentOrders.size
                _reportState.value = ReportState.Success(orderCount, totalRevenue)
            } catch (e: Exception) {
                _reportState.value = ReportState.Error(e.message ?: "Ошибка при генерации отчета")
            }
        }
    }

    fun exportReport(context: Context) {
        if (currentOrders.isNotEmpty()) {
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "Отчет_по_заказам_$timestamp.xlsx"
            ExcelExporter.exportOrdersToExcel(context, currentOrders, fileName)
        }
    }
}

sealed class ReportState {
    object Idle : ReportState()
    object Loading : ReportState()
    data class Success(val orderCount: Int, val totalRevenue: Int) : ReportState()
    data class Error(val message: String) : ReportState()
}
