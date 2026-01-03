package com.smartbus360.app.erpadmin.data.model


data class AddStudentRequest(
    val full_name: String,
    val registrationNumber: String,
    val classId: Int,
    val sectionId: Int,
    val password: String
)
