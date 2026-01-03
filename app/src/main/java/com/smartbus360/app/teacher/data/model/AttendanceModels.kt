package com.smartbus360.app.teacher.data.model


data class TeacherClass(
    val classId: Int,
    val sectionId: Int
)

data class ClassListResponse(
    val success: Boolean,
    val classes: List<TeacherClass>
)

data class StudentAttendanceItem(
    val studentId: Int,
    val name: String,
    val rollNumber: String,
    var status: String // present / absent / late
)

data class StudentListResponse(
    val success: Boolean,
    val students: List<StudentAttendanceItem>
)

data class MarkAttendanceRequest(
    val classId: Int,
    val sectionId: Int,
    val date: String,
    val records: List<StudentAttendancePayload>
)


data class StudentAttendancePayload(
    val studentId: Int,
    val status: String
)
data class AttendanceStatusResponse(
    val success: Boolean,
    val taken: Boolean
)
data class AttendanceDateResponse(
    val success: Boolean,
    val students: List<StudentAttendanceItem> = emptyList(),
    val attendance: List<AttendanceRecord>
)

data class AttendanceRecord(
    val id: Int,
    val studentId: Int,
    val classId: Int,
    val sectionId: Int,
    val date: String,
    val status: String,
    val markedBy: Int,
    val createdAt: String?,
    val updatedAt: String?
)
sealed class AttendanceState {
    object Idle : AttendanceState()
    object Loading : AttendanceState()
    object AttendanceTaken : AttendanceState()

    data class ClassLoaded(
        val classes: List<TeacherClass>
    ) : AttendanceState()

    data class StudentsLoaded(
        val students: List<StudentAttendanceItem>
    ) : AttendanceState()

    data class Success(val message: String) : AttendanceState()
    data class Error(val message: String) : AttendanceState()
}


data class AttendanceSummaryResponse(
    val success: Boolean,
    val summary: Map<String, Int>
)
data class UpdateAttendanceRequest(
    val studentId: Int,
    val classId: Int,
    val sectionId: Int,
    val date: String,
    val status: String
)

