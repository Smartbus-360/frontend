package com.smartbus360.app.teacher.data.model

data class ExamItem(
    val id: Int,
    val examName: String,
    val classId: Int,
    val sectionId: Int,
    val date: String
)

data class ExamListResponse(
    val success: Boolean,
    val exams: List<ExamItem>
)

data class StudentMarkItem(
    val studentId: Int,
    val studentName: String?,
    var marksObtained: Int,
    val maxMarks: Int
)

data class AddMarksRequest(
    val marks: List<AddMarksPayload>
)

data class AddMarksPayload(
    val examId: Int,
    val studentId: Int,
    val marksObtained: Int,
    val maxMarks: Int,
    val subject: String
)


data class MarksResponse(
    val success: Boolean,
    val message: String
)

data class ExamResultItem(
    val examId: Int,
    val studentId: Int,
    val subject: String,
    val marksObtained: Int,
    val maxMarks: Int
)

data class ExamResultResponse(
    val success: Boolean,
    val results: List<ExamResultItem>
)

data class SubjectItem(
    val id: Int,
    val name: String?
)

data class SubjectListResponse(
    val success: Boolean,
    val subjects: List<SubjectItem>
)

data class CreateExamRequest(
    val examName: String,
    val classId: Int,
    val sectionId: Int,
    val date: String
)

data class CreateExamResponse(
    val success: Boolean,
    val exam: ExamItem
)
