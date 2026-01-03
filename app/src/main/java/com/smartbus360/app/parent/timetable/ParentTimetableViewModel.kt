package com.smartbus360.app.parent.timetable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbus360.app.parent.api.ParentApiClient
import com.smartbus360.app.parent.models.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ParentTimetableViewModel : ViewModel() {

    private val _timetable =
        MutableStateFlow<Map<String, List<TimetablePeriod>>>(emptyMap())
    val timetable: StateFlow<Map<String, List<TimetablePeriod>>> = _timetable

    fun loadTimetable(studentId: Int) {
        viewModelScope.launch {
            val res = ParentApiClient.api.getTimetable(studentId)
            if (res.isSuccessful && res.body() != null) {
                _timetable.value = res.body()!!.timetable
            }
        }
    }
}

