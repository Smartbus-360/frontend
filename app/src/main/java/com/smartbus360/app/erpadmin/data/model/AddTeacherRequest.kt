package com.smartbus360.app.erpadmin.data.model


data class AddTeacherRequest(
    val full_name: String,
    val email: String,
    val phone: String,
    val password: String,
    val classId: Int,
    val sectionId: Int
)
