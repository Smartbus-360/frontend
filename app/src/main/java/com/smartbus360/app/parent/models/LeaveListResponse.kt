package com.smartbus360.app.parent.models

data class LeaveHistoryResponse(
    val success: Boolean,
    val leaves: List<LeaveItem>
)

data class LeaveItem(
    val id: Int,
    val studentId: Int,
    val fromDate: String,
    val toDate: String,
    val reason: String,
    val status: String,      // pending / approved / rejected
    val approvedBy: Int?,
    val createdAt: String,
    val updatedAt: String
)
