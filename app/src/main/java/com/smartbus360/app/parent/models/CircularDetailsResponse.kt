package com.smartbus360.app.parent.models

data class CircularDetailsResponse(
    val id: Int,
    val title: String,
    val description: String,
    val date: String,
    val issuedBy: String,
    val fileUrl: String?
)
