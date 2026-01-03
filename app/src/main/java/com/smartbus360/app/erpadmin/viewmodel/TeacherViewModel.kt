package com.smartbus360.app.erpadmin.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbus360.app.erpadmin.data.api.RetrofitClient
import com.smartbus360.app.erpadmin.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class TeacherState {
    object Loading : TeacherState()
    data class Success(val teachers: List<Teacher>) : TeacherState()
    data class Error(val message: String) : TeacherState()
}

class TeacherViewModel : ViewModel() {

    private val _teacherState = MutableStateFlow<TeacherState>(TeacherState.Loading)
    val teacherState: StateFlow<TeacherState> = _teacherState

    fun loadTeachers(token: String) {
        viewModelScope.launch {
            try {
                val res = RetrofitClient.teacherApi.getTeachers(token)
                if (res.isSuccessful && res.body() != null) {
                    _teacherState.value =
                        TeacherState.Success(res.body()!!.teachers)
                } else {
                    _teacherState.value = TeacherState.Error("Failed to load teachers")
                }
            } catch (e: Exception) {
                _teacherState.value = TeacherState.Error(e.message ?: "Error")
            }
        }
    }

    fun addTeacher(token: String, request: AddTeacherRequest, onDone: () -> Unit) {
        viewModelScope.launch {
            RetrofitClient.teacherApi.addTeacher(token, request)
            onDone()
        }
    }

    fun deleteTeacher(token: String, teacherId: Int, onDone: () -> Unit) {
        viewModelScope.launch {
            RetrofitClient.teacherApi.deleteTeacher(token, teacherId)
            onDone()
        }
    }
}
