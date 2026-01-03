package com.smartbus360.app.parent.models

data class HomeworkListResponse(
    val success: Boolean,
    val homework: List<HomeworkItem>
)

data class HomeworkItem(
    val id: Int,
    val classId: Int,
    val sectionId: Int,
    val subject: String,
    val title: String,
    val description: String,
    val fileUrl: String?,
    val priority: String,
    val dueDate: String,
    val createdBy: Int,
    val createdAt: String,
    val updatedAt: String
)
