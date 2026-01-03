package com.smartbus360.app.parent.models

//data class ExamDetailsResponse(
//    val id: Int,
//    val examName: String,
//    val date: String,
//    val subjects: List<ExamSubject>
//)
//
//data class ExamSubject(
//    val subjectName: String,
//    val marks: Int?,
//    val maxMarks: Int,
//    val status: String  // Pending or Completed
//)
data class ExamResultsResponse(
    val success: Boolean,
    val results: List<ExamResultItem>
)

data class ExamResultItem(
    val subject: String,
    val maxMarks: Int,
    val obtainedMarks: Int,
    val grade: String
)
