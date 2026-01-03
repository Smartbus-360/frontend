package com.smartbus360.app.parent.models


data class MessageListResponse(
    val success: Boolean,
    val messages: List<MessageItem>
)

data class MessageItem(
    val id: Int,
    val senderId: Int,
    val receiverId: Int?,
    val classId: Int,
    val sectionId: Int,
    val message: String,
    val fileUrl: String?,
    val type: String,      // broadcast / personal
    val seen: Boolean,
    val createdAt: String
)
