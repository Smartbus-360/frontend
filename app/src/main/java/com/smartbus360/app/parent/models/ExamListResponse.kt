//package com.smartbus360.app.parent.models
//
//data class ExamListResponse(
//    val exams: List<ExamItem>
//)
//
//data class ExamItem(
//    val id: Int,
//    val examName: String,
//    val date: String,
//    val subjects: Int,
//    val status: String   // Upcoming, Completed
//)
//
//data class ExamResultItem(
//    val subject: String,
//    val maxMarks: Int,
//    val obtainedMarks: Int,
//    val grade: String
//)

package com.smartbus360.app.parent.models

data class ExamScheduleResponse(
    val success: Boolean,
    val schedule: List<ExamItem>
)

data class ExamItem(
    val id: Int,
    val examName: String,
    val classId: Int,
    val sectionId: Int,
    val date: String
)
