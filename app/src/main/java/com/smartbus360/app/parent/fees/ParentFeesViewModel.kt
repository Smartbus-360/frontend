package com.smartbus360.app.parent.fees

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbus360.app.parent.api.ParentApiClient
import com.smartbus360.app.parent.models.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.smartbus360.app.parent.models.FeesDueResponse
class ParentFeesViewModel : ViewModel() {

    private val _pending = MutableStateFlow<List<FeeItem>>(emptyList())
    val pending: StateFlow<List<FeeItem>> = _pending

    private val _history = MutableStateFlow<List<FeeItem>>(emptyList())
    val history: StateFlow<List<FeeItem>> = _history

    fun loadFees(studentId: Int) {
        viewModelScope.launch {

            val dueRes = ParentApiClient.api.feesDue(studentId)
            if (dueRes.isSuccessful && dueRes.body() != null) {
                _pending.value = dueRes.body()!!.pending
            }

            val historyRes = ParentApiClient.api.feesHistory(studentId)
            if (historyRes.isSuccessful && historyRes.body() != null) {
                _history.value = historyRes.body()!!.history
            }
        }
    }
}

