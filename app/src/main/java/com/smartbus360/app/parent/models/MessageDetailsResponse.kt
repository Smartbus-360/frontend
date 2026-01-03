package com.smartbus360.app.parent.models

data class MessageDetailsResponse(
    val id: Int,
    val title: String,
    val message: String,
    val date: String,
    val sender: String,
    val fileUrl: String?
)
