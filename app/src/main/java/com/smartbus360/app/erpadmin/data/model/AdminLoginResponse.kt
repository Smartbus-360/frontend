package com.smartbus360.app.erpadmin.data.model


data class AdminLoginResponse(
    val accessToken: String,
    val user: AdminUser
)

data class AdminUser(
    val id: Int,
    val username: String?,
    val email: String?,
    val role: String?,
    val instituteId: Int?,
    val instituteName: String?
)

