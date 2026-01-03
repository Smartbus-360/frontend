package com.smartbus360.app.parent.models

data class HomeworkDetailsResponse(
    val success: Boolean,
    val homework: HomeworkDetails
)

data class HomeworkDetails(
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
