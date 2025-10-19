package com.example.restaurant.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurant.repository.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(private val reportRepository: ReportRepository) : ViewModel() {

    private val _reportState = MutableStateFlow<ReportState>(ReportState.Idle)
    val reportState: StateFlow<ReportState> = _reportState

    fun generateReport() {
        viewModelScope.launch {
            _reportState.value = ReportState.Loading
            val result = reportRepository.generateReport()
            _reportState.value = result.fold(
                onSuccess = { ReportState.Success(it) },
                onFailure = { ReportState.Error(it.message ?: "Unknown error") }
            )
        }
    }
}

sealed class ReportState {
    object Idle : ReportState()
    object Loading : ReportState()
    data class Success(val filePath: String) : ReportState()
    data class Error(val message: String) : ReportState()
}
