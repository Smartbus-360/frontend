package com.smartbus360.app.parent.models

data class ApplyLeaveRequest(
    val studentId: Int,
    val fromDate: String,
    val toDate: String,
    val reason: String
)
