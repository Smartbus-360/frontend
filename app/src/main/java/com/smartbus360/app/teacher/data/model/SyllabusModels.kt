package com.smartbus360.app.teacher.data.model

data class SyllabusChapter(
    val id: Int,
    val chapterName: String,
    val status: String, // covered / pending
    val coveredDate: String?,
    val remarks: String?
)

data class SyllabusProgressResponse(
    val success: Boolean,
    val progress: List<SyllabusChapter>
)

data class AddSyllabusProgressRequest(
    val classId: Int,
    val sectionId: Int,
    val subjectId: Int,
    val chapterName: String,
    val status: String,
    val remarks: String?
)
