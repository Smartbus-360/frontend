package com.smartbus360.app.parent.models

data class DailyAttendanceResponse(
    val records: List<DailyRecord>
)

data class DailyRecord(
    val date: String,
    val status: String,
    val remarks: String?
)
