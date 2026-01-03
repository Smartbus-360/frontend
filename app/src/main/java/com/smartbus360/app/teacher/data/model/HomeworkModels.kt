package com.smartbus360.app.teacher.data.model

data class HomeworkItem(
    val id: Int,
    val title: String,
    val description: String?,
    val dueDate: String,
    val classId: Int,
    val sectionId: Int,
    val subject: String    // ✅ FIX
)


data class HomeworkListResponse(
    val success: Boolean,
    val homework: List<HomeworkItem>
)

data class CreateHomeworkRequest(
    val classId: Int,
    val sectionId: Int,
    val subjectId: Int,
    val title: String,
    val description: String?,
    val dueDate: String
)
