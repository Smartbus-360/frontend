package com.smartbus360.app.erpadmin.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbus360.app.erpadmin.data.api.RetrofitClient
import com.smartbus360.app.erpadmin.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class StudentState {
    object Loading : StudentState()
    data class Success(val students: List<Student>) : StudentState()
    data class Error(val message: String) : StudentState()
}

class StudentViewModel : ViewModel() {

    private val _state = MutableStateFlow<StudentState>(StudentState.Loading)
    val state: StateFlow<StudentState> = _state

    fun loadStudents(token: String) {
        viewModelScope.launch {
            try {
                val res = RetrofitClient.studentApi.getStudents(token)
                if (res.isSuccessful) {
                    _state.value = StudentState.Success(res.body() ?: emptyList())
                } else {
                    _state.value = StudentState.Error("Failed to load students")
                }
            } catch (e: Exception) {
                _state.value = StudentState.Error(e.message ?: "Error")
            }
        }
    }

    fun addStudent(token: String, req: AddStudentRequest, onDone: () -> Unit) {
        viewModelScope.launch {
            RetrofitClient.studentApi.addStudent(token, req)
            onDone()
        }
    }

    fun generateQr(token: String, studentId: Int, onDone: () -> Unit) {
        viewModelScope.launch {
            RetrofitClient.studentApi.generateQr(token, studentId)
            onDone()
        }
    }

    fun revokeQr(token: String, studentId: Int, onDone: () -> Unit) {
        viewModelScope.launch {
            RetrofitClient.studentApi.revokeQr(token, studentId)
            onDone()
        }
    }
}
