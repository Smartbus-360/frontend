package com.smartbus360.app.erpadmin.data.model


data class Student(
    val id: Int,
    val full_name: String,
    val registrationNumber: String,
    val classId: Int,
    val sectionId: Int,
    val status: String
)
