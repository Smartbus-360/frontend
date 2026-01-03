package com.smartbus360.app.teacher.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.smartbus360.app.teacher.data.repository.TeacherRepository
import com.smartbus360.app.teacher.data.model.*

sealed class ExamState {
    object Loading : ExamState()
    data class ExamListLoaded(val exams: List<ExamItem>) : ExamState()
    data class SubjectsLoaded(val subjects: List<SubjectItem>) : ExamState()
    data class ResultsLoaded(val results: List<ExamResultItem>) : ExamState()
    data class CircularsLoaded(val circulars: List<CircularItem>) : ExamState()
    object MessageSent : ExamState()
    object MarksSubmitted : ExamState()
    object ExamCreated : ExamState()
    object ExamUpdated : ExamState()
    object ExamDeleted : ExamState()
    data class Error(val message: String) : ExamState()
}

class ExamViewModel : ViewModel() {

    private val repository = TeacherRepository()

    private val _state = MutableStateFlow<ExamState>(ExamState.Loading)
    val state: StateFlow<ExamState> = _state


    fun loadExams(token: String) {
        viewModelScope.launch {
            val result = repository.getExams(token)
            _state.value = result.fold(
                onSuccess = { ExamState.ExamListLoaded(it.exams) },
                onFailure = { ExamState.Error(it.message ?: "Error") }
            )
        }
    }

    fun submitMarks(token: String, students: List<StudentMarkItem>, examId: Int,subject: String) {
        viewModelScope.launch {

            val payload = students.map {
                AddMarksPayload(
                    examId = examId,
                    studentId = it.studentId,
                    marksObtained = it.marksObtained,
                    maxMarks = it.maxMarks,
                    subject = subject // or dynamic later
                )
            }

            val result = repository.addMarks(
                token,
                AddMarksRequest(payload)
            )

            _state.value = result.fold(
                onSuccess = { ExamState.MarksSubmitted },
                onFailure = { ExamState.Error(it.message ?: "Error") }
            )
        }
    }

    fun loadResults(token: String, studentId: Int) {
        viewModelScope.launch {
            _state.value = ExamState.Loading

            val result = repository.getExamResults(token, studentId)
            _state.value = result.fold(
                onSuccess = { ExamState.ResultsLoaded(it.results) },
                onFailure = { ExamState.Error(it.message ?: "Error") }
            )
        }
    }

    fun loadSubjects(token: String) {
        viewModelScope.launch {
            val result = repository.getTeacherSubjects(token)
            _state.value = result.fold(
                onSuccess = { ExamState.SubjectsLoaded(it.subjects) },
                onFailure = { ExamState.Error(it.message ?: "Error") }
            )
        }
    }

    fun loadCirculars(token: String) {
        viewModelScope.launch {
            val result = repository.getCirculars(token)
            _state.value = result.fold(
                onSuccess = { ExamState.CircularsLoaded(it.circulars) },
                onFailure = { ExamState.Error(it.message ?: "Error") }
            )
        }
    }

    fun sendBroadcast(
        token: String,
        classId: Int,
        sectionId: Int,
        message: String
    ) {
        viewModelScope.launch {
            val result = repository.broadcastMessage(
                token,
                BroadcastMessageRequest(classId, sectionId, message)
            )

            _state.value = result.fold(
                onSuccess = { ExamState.MessageSent },
                onFailure = { ExamState.Error(it.message ?: "Error") }
            )
        }
    }

    fun createExam(token: String, request: CreateExamRequest) {
        viewModelScope.launch {
            val result = repository.createExam(token, request)
            _state.value = result.fold(
                onSuccess = { ExamState.ExamCreated },
                onFailure = { ExamState.Error(it.message ?: "Error") }
            )
        }
    }

    fun updateExam(token: String, examId: Int, examName: String, date: String) {
        viewModelScope.launch {
            val result = repository.updateExam(
                token,
                examId,
                examName,
                date
            )
            _state.value = result.fold(
                onSuccess = { ExamState.ExamUpdated },
                onFailure = { ExamState.Error(it.message ?: "Error") }
            )
        }
    }


    fun deleteExam(token: String, examId: Int) {
        viewModelScope.launch {
            val result = repository.deleteExam(token, examId)
            _state.value = result.fold(
                onSuccess = { ExamState.ExamDeleted },
                onFailure = { ExamState.Error(it.message ?: "Error") }
            )
        }
    }

}
