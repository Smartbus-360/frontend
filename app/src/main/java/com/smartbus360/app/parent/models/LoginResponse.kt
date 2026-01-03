package com.smartbus360.app.parent.models

data class LoginResponse(
    val success: Boolean,
    val token: String,
    val userId: Int,
    val userName: String,
    val email: String
)

//data class ParentUserData(
//    val id: Int,
//    val full_name: String,
//    val instituteId: Int,
//    val students: List<StudentInfo>
//)
//
//data class StudentInfo(
//    val id: Int,
//    val name: String,
//    val classId: Int,
//    val sectionId: Int,
//)
