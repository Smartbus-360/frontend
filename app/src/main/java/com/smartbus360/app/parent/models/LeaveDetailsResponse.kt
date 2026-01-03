package com.smartbus360.app.parent.models

data class LeaveDetailsResponse(
    val id: Int,
    val fromDate: String,
    val toDate: String,
    val reason: String,
    val status: String,
    val appliedOn: String,
    val teacherRemark: String?
)
