package com.smartbus360.app.teacher.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.smartbus360.app.teacher.data.repository.TeacherRepository
import com.smartbus360.app.teacher.data.model.*
import com.smartbus360.app.data.api.UpdateAttendanceRequest

/* ===================== STATES ===================== */

sealed class AttendanceState {

    object Idle : AttendanceState()
    object Loading : AttendanceState()

    data class ClassLoaded(
        val classes: List<TeacherClass>
    ) : AttendanceState()

    data class StudentsLoaded(
        val students: List<StudentAttendanceItem>
    ) : AttendanceState()

    data class AttendanceDateLoaded(
        val students: List<StudentAttendanceItem>,
        val attendance: List<AttendanceRecord>
    ) : AttendanceState()

    data class AttendanceSummaryLoaded(
        val summary: Map<String, Int>
    ) : AttendanceState()

    data class Success(val message: String) : AttendanceState()
    data class Error(val message: String) : AttendanceState()
}

/* ===================== VIEWMODEL ===================== */

class AttendanceViewModel : ViewModel() {

    private val repository = TeacherRepository()

    private val _state = MutableStateFlow<AttendanceState>(AttendanceState.Idle)
    val state: StateFlow<AttendanceState> = _state

    /* ---------- CLASSES ---------- */
    fun loadClasses(token: String) {
        viewModelScope.launch {
            _state.value = AttendanceState.Loading

            repository.getTeacherClasses(token)
                .onSuccess {
                    _state.value = AttendanceState.ClassLoaded(it.classes)
                }
                .onFailure {
                    _state.value = AttendanceState.Error(it.message ?: "Failed to load classes")
                }
        }
    }

    /* ---------- CHECK STATUS ---------- */
//    fun startAttendanceFlow(
//        token: String,
//        classId: Int,
//        sectionId: Int,
//        date: String
//    ) {
//        viewModelScope.launch {
//            _state.value = AttendanceState.Loading
//
//            val statusResult = repository.getAttendanceStatus(
//                token,
//                classId,
//                sectionId,
//                date
//            )
//
//            // 1️⃣ Always load students first (like curl)
//            repository.getStudentsForAttendance(token, classId, sectionId)
//                .onSuccess { studentRes ->
//
//                    // 2️⃣ Then check status
//                    repository.getAttendanceStatus(token, classId, sectionId, date)
//                        .onSuccess { if (statusRes.taken) {
//                            _state.value = AttendanceState.AttendanceDateLoaded(
//                                students = emptyList(),
//                                attendance = emptyList()
//                            )
//                        } else {
//                            _state.value = AttendanceState.StudentsLoaded(studentRes.students)
//                        }
//                        }
//                }
//                .onFailure {
//                    _state.value = AttendanceState.Error("Failed to load students")
//                }
//        }
//    }

    fun startAttendanceFlow(
        token: String,
        classId: Int,
        sectionId: Int,
        date: String
    ) {
        viewModelScope.launch {
            _state.value = AttendanceState.Loading

            // 1️⃣ check status
            val statusResult = repository.getAttendanceStatus(
                token,
                classId,
                sectionId,
                date
            )

            statusResult.fold(
                onSuccess = { status ->
                    if (status.taken) {
                        // SAME AS CURL
                        loadAttendanceByDate(token, classId, sectionId, date)
                    } else {
                        // load students
                        val studentsResult =
                            repository.getStudentsForAttendance(token, classId, sectionId)

                        studentsResult.fold(
                            onSuccess = {
                                _state.value =
                                    AttendanceState.StudentsLoaded(it.students)
                            },
                            onFailure = {
                                _state.value =
                                    AttendanceState.Error(it.message ?: "Failed")
                            }
                        )
                    }
                },
                onFailure = {
                    _state.value =
                        AttendanceState.Error(it.message ?: "Failed")
                }
            )
        }
    }

    /* ---------- STUDENTS ---------- */
    fun loadStudents(token: String, classId: Int, sectionId: Int) {
        viewModelScope.launch {
            _state.value = AttendanceState.Loading

            repository.getStudentsForAttendance(token, classId, sectionId)
                .onSuccess {
                    _state.value = AttendanceState.StudentsLoaded(it.students)
                }
                .onFailure {
                    _state.value = AttendanceState.Error(it.message ?: "Failed to load students")
                }
        }
    }

    /* ---------- MARK ATTENDANCE ---------- */
    fun submitAttendance(
        token: String,
        classId: Int,
        sectionId: Int,
        date: String,
        students: List<StudentAttendanceItem>
    ) {
        viewModelScope.launch {
            val payload = students.map {
                StudentAttendancePayload(it.studentId, it.status)
            }

            repository.markAttendance(
                token,
                MarkAttendanceRequest(classId, sectionId, date, payload)
            )
                .onSuccess {
                    _state.value = AttendanceState.Success("Attendance marked successfully")

                    loadAttendanceByDate(
                        token,
                        classId,
                        sectionId,
                        date
                    )
                }
                .onFailure {
                    _state.value = AttendanceState.Error(it.message ?: "Failed to mark attendance")
                }
        }
    }

    /* ---------- BY DATE ---------- */
    fun loadAttendanceByDate(
        token: String,
        classId: Int,
        sectionId: Int,
        date: String
    ) {
        viewModelScope.launch {
            _state.value = AttendanceState.Loading

            repository.getAttendanceByDate(token, classId, sectionId, date)
                .onSuccess {
                    _state.value = AttendanceState.AttendanceDateLoaded(
                        it.students,
                        it.attendance
                    )
                }
                .onFailure {
                    _state.value = AttendanceState.Error(it.message ?: "Failed to load attendance")
                }
        }
    }

    /* ---------- UPDATE ---------- */
    fun updateAttendance(token: String, request: UpdateAttendanceRequest) {
        viewModelScope.launch {
            repository.updateAttendance(token, request)
                .onSuccess {
                    _state.value = AttendanceState.Success("Attendance updated")
                }
                .onFailure {
                    _state.value = AttendanceState.Error(it.message ?: "Update failed")
                }
        }
    }

    /* ---------- SUMMARY ---------- */
    fun loadAttendanceSummary(
        token: String,
        classId: Int,
        sectionId: Int,
        month: Int,
        year: Int
    ) {
        viewModelScope.launch {
            _state.value = AttendanceState.Loading

            repository.getAttendanceSummary(token, classId, sectionId, month, year)
                .onSuccess {
                    _state.value = AttendanceState.AttendanceSummaryLoaded(it.summary)
                }
                .onFailure {
                    _state.value = AttendanceState.Error(it.message ?: "Failed to load summary")
                }
        }
    }
}
