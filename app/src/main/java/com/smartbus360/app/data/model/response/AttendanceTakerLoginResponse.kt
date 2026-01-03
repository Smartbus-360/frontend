package com.smartbus360.app.data.model.response

data class AttendanceTakerLoginResponse(
    val success: Boolean,
    val message: String,
    val token: String?,
    val attendanceTakerId: Int?,
    val name: String?,
    val email: String?,
    val role: String?

)
