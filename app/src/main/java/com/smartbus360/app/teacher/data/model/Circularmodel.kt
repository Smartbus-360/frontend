package com.smartbus360.app.teacher.data.model


data class CircularItem(
    val id: Int,
    val title: String?,
    val message: String?,
    val createdAt: String?
)

data class CircularListResponse(
    val success: Boolean,
    val circulars: List<CircularItem>
)

data class BroadcastMessageRequest(
    val classId: Int,
    val sectionId: Int,
    val message: String
)
