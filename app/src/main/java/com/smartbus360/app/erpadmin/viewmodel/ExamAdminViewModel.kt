package com.smartbus360.app.erpadmin.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbus360.app.erpadmin.data.api.RetrofitClient
import com.smartbus360.app.erpadmin.data.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class ExamAdminState {
    object Loading : ExamAdminState()
    data class Success(val exams: List<Exam>) : ExamAdminState()
    data class Error(val message: String) : ExamAdminState()
}

sealed class ResultState {
    object Loading : ResultState()
    data class Success(val results: List<ExamResult>) : ResultState()
    data class Error(val message: String) : ResultState()
}

class ExamAdminViewModel : ViewModel() {

    private val _examState = MutableStateFlow<ExamAdminState>(ExamAdminState.Loading)
    val examState: StateFlow<ExamAdminState> = _examState

    private val _resultState = MutableStateFlow<ResultState>(ResultState.Loading)
    val resultState: StateFlow<ResultState> = _resultState

    fun loadExams(token: String) {
        viewModelScope.launch {
            try {
                val res = RetrofitClient.examAdminApi.getAllExams(token)
                if (res.isSuccessful) {
                    if (res.isSuccessful && res.body() != null) {
                        _examState.value = ExamAdminState.Success(res.body()!!.exams)
                    } else {
                        _examState.value = ExamAdminState.Error("Failed to load exams")
                    }
                } else {
                    _examState.value = ExamAdminState.Error("Failed to load exams")
                }
            } catch (e: Exception) {
                _examState.value = ExamAdminState.Error(e.message ?: "Error")
            }
        }
    }

    fun loadStudentResults(token: String, studentId: Int) {
        viewModelScope.launch {
            try {
                val res = RetrofitClient.examAdminApi.getStudentResults(token, studentId)
                if (res.isSuccessful) {
                    if (res.isSuccessful && res.body() != null) {
                        _resultState.value = ResultState.Success(res.body()!!.results)
                    } else {
                        _resultState.value = ResultState.Error("Failed to load results")
                    }
                } else {
                    _resultState.value = ResultState.Error("Failed to load results")
                }
            } catch (e: Exception) {
                _resultState.value = ResultState.Error(e.message ?: "Error")
            }
        }
    }
}
