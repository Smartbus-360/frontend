package com.smartbus360.app.erpadmin.data.model


data class AdminLoginRequest(
    val email: String,
    val password: String,
    val client_id: String,
    val client_secret: String
)
