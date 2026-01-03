package com.smartbus360.app.parent.leave

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbus360.app.parent.api.ParentApiClient
import com.smartbus360.app.parent.models.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ParentLeaveViewModel : ViewModel() {

    private val _leaveList = MutableStateFlow<List<LeaveItem>>(emptyList())
    val leaveList: StateFlow<List<LeaveItem>> = _leaveList

    private val _applyStatus = MutableStateFlow<Boolean?>(null)
    val applyStatus: StateFlow<Boolean?> = _applyStatus

    fun loadLeaveHistory(studentId: Int) {
        viewModelScope.launch {
            val res = ParentApiClient.api.leaveHistory(studentId)
            if (res.isSuccessful && res.body()?.success == true) {
                _leaveList.value = res.body()!!.leaves
            }
        }
    }

    fun applyLeave(studentId: Int, from: String, to: String, reason: String) {
        viewModelScope.launch {
            val body = ApplyLeaveRequest(studentId,fromDate = from, toDate = to, reason = reason)
            val res = ParentApiClient.api.applyLeave(studentId, body)
            _applyStatus.value = res.isSuccessful
        }
    }
}
