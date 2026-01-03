package com.smartbus360.app.erpadmin.data.model


data class SyllabusProgress(
    val id: Int,
    val classId: Int,
    val sectionId: Int,
    val subject: String,
    val topic: String,
    val status: String,   // completed / pending
    val teacherName: String,
    val updatedAt: String
)
