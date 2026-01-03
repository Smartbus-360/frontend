package com.smartbus360.app.parent.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbus360.app.parent.api.ParentApiClient
import com.smartbus360.app.parent.models.DashboardResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ParentDashboardViewModel : ViewModel() {

    private val _dashboardState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val dashboardState: StateFlow<DashboardUiState> = _dashboardState

    fun loadDashboard(studentId: Int) {
        if (_dashboardState.value is DashboardUiState.Success) return

        viewModelScope.launch {
            _dashboardState.value = DashboardUiState.Loading

            try {
//                val response = ParentApiClient.api.getDashboard(studentId)

                val response = ParentApiClient.api.dashboard(studentId)

                if (response.isSuccessful && response.body() != null) {
                    _dashboardState.value = DashboardUiState.Success(response.body()!!)
                } else {
                    _dashboardState.value = DashboardUiState.Error("Unable to load dashboard")
                }

            } catch (ex: Exception) {
                _dashboardState.value = DashboardUiState.Error(ex.localizedMessage ?: "Error")
            }
        }
    }
}

sealed class DashboardUiState {
    object Loading : DashboardUiState()
    data class Success(val data: DashboardResponse) : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
}
