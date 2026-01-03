package com.smartbus360.app.teacher.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.smartbus360.app.teacher.data.repository.TeacherRepository
import com.smartbus360.app.teacher.data.model.*

sealed class SyllabusState {
    object Loading : SyllabusState()
    data class Loaded(val chapters: List<SyllabusChapter>) : SyllabusState()
    object Updated : SyllabusState()
    data class Error(val message: String) : SyllabusState()
}

class SyllabusViewModel : ViewModel() {

    private val repository = TeacherRepository()

    private val _state = MutableStateFlow<SyllabusState>(SyllabusState.Loading)
    val state: StateFlow<SyllabusState> = _state

    fun loadSyllabus(
        token: String,
        classId: Int,
        sectionId: Int,
        subjectId: Int
    ) {
        viewModelScope.launch {
            val result = repository.getSyllabusProgress(token, classId, sectionId, subjectId)
            _state.value = result.fold(
                onSuccess = {
                    SyllabusState.Loaded(it.progress ?: emptyList())
                },
                onFailure = { SyllabusState.Error(it.message ?: "Error") }
            )
        }
    }

    fun updateChapter(
        token: String,
        request: AddSyllabusProgressRequest
    ) {
        viewModelScope.launch {
            val result = repository.addSyllabusProgress(token, request)
            _state.value = result.fold(
                onSuccess = { SyllabusState.Updated },
                onFailure = { SyllabusState.Error(it.message ?: "Error") }
            )
        }
    }
}
