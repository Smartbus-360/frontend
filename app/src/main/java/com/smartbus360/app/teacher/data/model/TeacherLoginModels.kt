package com.smartbus360.app.teacher.data.model

data class TeacherLoginRequest(
    val email: String,
    val password: String
)

data class TeacherLoginResponse(
    val success: Boolean,
    val message: String,
    val token: String,
    val teacher: TeacherProfile
)

data class TeacherProfile(
    val id: Int,
    val full_name: String,
    val username: String?,
    val email: String?,
    val phone: String?,
    val classId: Int?,
    val sectionId: Int?,
    val accountType: String?,
)
