package com.smartbus360.app.parent.models

data class CircularListResponse(
    val circulars: List<CircularItem>
)

data class CircularItem(
    val id: Int,
    val title: String,
    val date: String,
    val shortDescription: String,
    val fileUrl: String?
)
