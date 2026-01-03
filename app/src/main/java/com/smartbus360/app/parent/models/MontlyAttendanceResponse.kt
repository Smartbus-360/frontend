package com.smartbus360.app.parent.models

data class MonthlyAttendanceResponse(
    val month: String,
    val year: Int,
    val records: List<AttendanceRecord>
)

data class AttendanceRecord(
    val date: String,
    val status: String  // "P", "A", "L", "E"
)
