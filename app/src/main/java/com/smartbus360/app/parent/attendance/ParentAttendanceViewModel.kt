package com.smartbus360.app.parent.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbus360.app.parent.api.ParentApiClient
import com.smartbus360.app.parent.models.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ParentAttendanceViewModel : ViewModel() {

    private val _monthly = MutableStateFlow<MonthlyAttendanceResponse?>(null)
    val monthly: StateFlow<MonthlyAttendanceResponse?> = _monthly

    private val _daily = MutableStateFlow<DailyAttendanceResponse?>(null)
    val daily: StateFlow<DailyAttendanceResponse?> = _daily

    private val _summary = MutableStateFlow<AttendanceSummaryResponse?>(null)
    val summary: StateFlow<AttendanceSummaryResponse?> = _summary


    fun loadMonthly(studentId: Int, month: Int, year: Int) {
        viewModelScope.launch {
            val res = ParentApiClient.api.monthlyAttendance(
                studentId = studentId,
                month = month,
                year = year
            )
            if (res.isSuccessful) {
                _monthly.value = res.body()
            }
        }
    }

    fun loadDaily(studentId: Int, date: String) {
        viewModelScope.launch {
            val res = ParentApiClient.api.dailyAttendance(
                studentId = studentId,
                date = date
            )
            if (res.isSuccessful) {
                _daily.value = res.body()
            }
        }
    }


    fun computeSummary(monthly: MonthlyAttendanceResponse): AttendanceSummaryResponse {
        val total = monthly.records.size
        val present = monthly.records.count { it.status == "P" }
        val absent = monthly.records.count { it.status == "A" }
        val late = monthly.records.count { it.status == "L" }
        val excused = monthly.records.count { it.status == "E" }

        val percentage = if (total == 0) 0.0 else (present * 100.0 / total)

        return AttendanceSummaryResponse(
            totalPresent = present,
            totalAbsent = absent,
            totalLate = late,
            totalExcused = excused,
            percentage = percentage
        )
    }

//    fun loadSummary(studentId: Int) {
//        viewModelScope.launch {
//            val res = ParentApiClient.api.getAttendanceSummary(studentId)
//            if (res.isSuccessful) _summary.value = res.body()
//        }
//    }
}
