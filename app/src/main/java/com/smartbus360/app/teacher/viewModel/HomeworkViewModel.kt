package com.smartbus360.app.teacher.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.smartbus360.app.teacher.data.repository.TeacherRepository
import com.smartbus360.app.teacher.data.model.*

sealed class HomeworkState {
    object Loading : HomeworkState()
    data class ListLoaded(val homework: List<HomeworkItem>) : HomeworkState()
    object Created : HomeworkState()
    data class Error(val message: String) : HomeworkState()
}

class HomeworkViewModel : ViewModel() {

    private val repository = TeacherRepository()

    private val _state = MutableStateFlow<HomeworkState>(HomeworkState.Loading)
    val state: StateFlow<HomeworkState> = _state

    fun loadHomework(token: String, teacherId: Int) {
        viewModelScope.launch {
            _state.value = HomeworkState.Loading
            val result = repository.getHomework(token, teacherId)
            _state.value = result.fold(
                onSuccess = { HomeworkState.ListLoaded(it.homework) },
                onFailure = { HomeworkState.Error(it.message ?: "Error") }
            )
        }
    }
    fun createHomework(
        token: String,
        teacherId: Int,
        request: CreateHomeworkRequest
    ) {
        viewModelScope.launch {
            val result = repository.createHomework(token, request)
            result.fold(
                onSuccess = {
                    loadHomework(token, teacherId) // 🔥 refresh list
                },
                onFailure = {
                    _state.value = HomeworkState.Error(it.message ?: "Error")
                }
            )
        }
    }
    fun updateHomework(
        token: String,
        homeworkId: Int,
        teacherId: Int,
        body: Map<String, Any>      // ✅ FIX
    ) {
        viewModelScope.launch {
            val result = repository.updateHomework(token, homeworkId, body)
            result.fold(
                onSuccess = {
                    loadHomework(token, teacherId)
                },
                onFailure = {
                    _state.value = HomeworkState.Error(it.message ?: "Update failed")
                }
            )
        }
    }

    fun deleteHomework(
        token: String,
        teacherId: Int,
        homeworkId: Int
    ) {
        viewModelScope.launch {
            val result = repository.deleteHomework(token, homeworkId)
            result.fold(
                onSuccess = {
                    loadHomework(token, teacherId)
                },
                onFailure = {
                    _state.value = HomeworkState.Error(it.message ?: "Delete failed")
                }
            )
        }
    }




}
