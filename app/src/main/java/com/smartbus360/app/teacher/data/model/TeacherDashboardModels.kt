package com.smartbus360.app.teacher.data.model

data class TeacherDashboardResponse(
    val success: Boolean,
    val teacher: TeacherProfile,
    val dashboard: TeacherDashboardData
)

data class TeacherDashboardData(
    val todaysPeriods: List<PeriodItem>,
    val totalStudents: Int,
    val attendanceSummary: AttendanceSummary,
    val homeworkToday: List<HomeworkItem>,
    val upcomingExams: List<ExamItem>,
    val unreadMessages: Int
)

data class PeriodItem(
    val id: Int,
    val day: String,
    val startTime: String,
    val endTime: String,
    val subjectId: Int,
    val classId: Int,
    val sectionId: Int
)

data class AttendanceSummary(
    val present: Int,
    val absent: Int,
    val late: Int,
    val excused: Int
)
data class StoredTeacherProfile(
    val classId: Int?,
    val sectionId: Int?,
    val fullName: String?,
    val username: String?,
    val email: String?,
    val phone: String?
)
