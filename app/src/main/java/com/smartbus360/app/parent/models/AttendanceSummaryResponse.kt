package com.smartbus360.app.parent.models

data class AttendanceSummaryResponse(
    val totalPresent: Int,
    val totalAbsent: Int,
    val totalLate: Int,
    val totalExcused: Int,
    val percentage: Double
)
