package com.smartbus360.app.erpadmin.data.model


data class Teacher(
    val id: Int,
    val full_name: String,
    val email: String,
    val phone: String,
    val status: String,
    val profilePicture: String? = null
)
