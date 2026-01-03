package com.smartbus360.app.erpadmin.data.model


data class ExamResult(
    val subject: String,
    val marks: Int,
    val totalMarks: Int,
    val grade: String
)
data class ExamResultResponse(
    val success: Boolean,
    val results: List<ExamResult>
)
