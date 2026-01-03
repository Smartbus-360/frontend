package com.smartbus360.app.teacher.data.model

data class StudentAnalysisResponse(
    val success: Boolean,
    val strengths: List<StudentStrength>,
    val weaknesses: List<StudentWeakness>
)

data class StudentStrength(
    val id: Int,
    val title: String,
    val remarks: String?
)

data class StudentWeakness(
    val id: Int,
    val issue: String,
    val improvementNote: String?
)
