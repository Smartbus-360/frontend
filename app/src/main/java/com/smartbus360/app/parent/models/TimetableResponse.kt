package com.smartbus360.app.parent.models


data class TimetableResponse(
    val timetable: Map<String, List<TimetablePeriod>>
)

data class TimetablePeriod(
    val subject: String,
    val teacher: String?,
    val startTime: String,
    val endTime: String
)
