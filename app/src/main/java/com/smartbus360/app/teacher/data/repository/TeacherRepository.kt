package com.smartbus360.app.teacher.data.repository

import com.smartbus360.app.data.api.ErpRetrofitInstance
import com.smartbus360.app.teacher.data.model.*
import com.smartbus360.app.data.api.UpdateAttendanceRequest
import com.smartbus360.app.teacher.data.model.BroadcastMessageRequest
import com.smartbus360.app.data.api.UpdateExamRequest

class TeacherRepository {

    private val api = ErpRetrofitInstance.teacherApi

    // ---------------- AUTH ----------------
    suspend fun loginTeacher(
        email: String,
        password: String
    ): Result<TeacherLoginResponse> {
        return try {
            val response = api.teacherLogin(
                TeacherLoginRequest(email, password)
            )

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(
                    Exception(response.errorBody()?.string() ?: "Login failed")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ---------------- DASHBOARD ----------------
    suspend fun getDashboard(
        token: String
    ): Result<TeacherDashboardResponse> {
        return try {
            val response = api.getDashboard("Bearer $token")

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(
                    Exception("Failed to load dashboard")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    // ---------------- HOMEWORK ----------------

    suspend fun getHomework(
        token: String,
        teacherId: Int
    ): Result<HomeworkListResponse> {
        return try {
            val response = api.getHomework("Bearer $token", teacherId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to load homework"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateHomework(
        token: String,
        id: Int,
        body: Map<String, Any>
    ): Result<Unit> {
        return try {
            val response = api.updateHomework("Bearer $token", id, body)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Update failed"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteHomework(
        token: String,
        id: Int
    ): Result<Unit> {
        return try {
            val response = api.deleteHomework("Bearer $token", id)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Delete failed"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun createHomework(
        token: String,
        request: CreateHomeworkRequest
    ): Result<Unit> {
        return try {
            val response = api.createHomework("Bearer $token", request)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Homework creation failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
// ---------------- EXAMS ----------------

    suspend fun getExams(
        token: String
    ): Result<ExamListResponse> {
        return try {
            val response = api.getExams(
                "Bearer $token"
            )
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to load exams"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

// ---------------- MARKS ----------------

    suspend fun addMarks(
        token: String,
        request: AddMarksRequest
    ): Result<Unit> {
        return try {
            val response = api.addMarks(
                "Bearer $token",
                request
            )
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to submit marks"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
// ---------------- SYLLABUS ----------------

    suspend fun getSyllabusProgress(
        token: String,
        classId: Int,
        sectionId: Int,
        subjectId: Int
    ): Result<SyllabusProgressResponse> {
        return try {
            val response = api.getSyllabusProgress(
                "Bearer $token",
                classId,
                sectionId,
                subjectId
            )
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to load syllabus"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addSyllabusProgress(
        token: String,
        request: AddSyllabusProgressRequest
    ): Result<Unit> {
        return try {
            val response = api.addSyllabusProgress(
                "Bearer $token",
                request
            )
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to update syllabus"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
// ---------------- ATTENDANCE ----------------

//    suspend fun getTeacherClasses(token: String): Result<ClassListResponse> {
//        return try {
//            val response = api.getDashboard("Bearer $token")
//            if (response.isSuccessful && response.body() != null) {
//                Result.success(response.body()!!.classes)
//            } else {
//                Result.failure(Exception("Failed to load classes"))
//            }
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
suspend fun getTeacherClasses(token: String): Result<ClassListResponse> {
    return try {
        val response = api.getTeacherClasses("Bearer $token")

        if (response.isSuccessful && response.body() != null) {
            Result.success(response.body()!!)
        } else {
            Result.failure(Exception("Failed to load classes"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}



    suspend fun getStudentsForAttendance(
        token: String,
        classId: Int,
        sectionId: Int
    ): Result<StudentListResponse> {
        return try {
            val response = api.getStudentsForAttendance("Bearer $token", classId, sectionId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to load students"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun markAttendance(
        token: String,
        request: MarkAttendanceRequest
    ): Result<Unit> {
        return try {
            val response = api.markAttendance("Bearer $token", request)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Attendance failed"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getAttendanceStatus(
        token: String,
        classId: Int,
        sectionId: Int,
        date: String
    ): Result<AttendanceStatusResponse> {
        return try {
            val response = api.getAttendanceStatus(
                "Bearer $token",
                classId,
                sectionId,
                date
            )
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get attendance status"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAttendanceByDate(
        token: String,
        classId: Int,
        sectionId: Int,
        date: String
    ): Result<AttendanceDateResponse> {
        return try {
            val response = api.getAttendanceByDate(
                "Bearer $token", classId, sectionId, date
            )
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to load attendance"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateAttendance(
        token: String,
        request: UpdateAttendanceRequest
    ): Result<Unit> {
        return try {
            val response = api.updateAttendance("Bearer $token", request)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Update failed"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAttendanceSummary(
        token: String,
        classId: Int,
        sectionId: Int,
        month: Int,
        year: Int
    ): Result<AttendanceSummaryResponse> {
        return try {
            val response = api.getAttendanceSummary(
                "Bearer $token", classId, sectionId, month, year
            )
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to load summary"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getExamResults(
        token: String,
        studentId: Int
    ): Result<ExamResultResponse> {
        return try {
            val response = api.getExamResults("Bearer $token", studentId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to load results"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTeacherSubjects(
        token: String
    ): Result<SubjectListResponse> {
        return try {
            val response = api.getTeacherSubjects("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to load subjects"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCirculars(token: String): Result<CircularListResponse> {
        return try {
            val response = api.getTeacherCirculars("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to load circulars"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun broadcastMessage(
        token: String,
        request: BroadcastMessageRequest
    ): Result<Unit> {
        return try {
            val response = api.broadcastMessage("Bearer $token", request)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Broadcast failed"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createExam(
        token: String,
        request: CreateExamRequest
    ): Result<ExamItem> {
        return try {
            val response = api.createExam("Bearer $token", request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.exam)
            } else {
                Result.failure(Exception("Create exam failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateExam(
        token: String,
        examId: Int,
        examName: String,
        date: String
    ): Result<ExamItem> {
        return try {
            val response = api.updateExam("Bearer $token", examId, UpdateExamRequest(examName = examName,
                date = date))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.exam)
            } else {
                Result.failure(Exception("Update exam failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteExam(
        token: String,
        examId: Int
    ): Result<Unit> {
        return try {
            val response = api.deleteExam("Bearer $token", examId)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Delete exam failed"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ================= RAW ATTENDANCE (CURL-STYLE) =================

    suspend fun attendanceStudentsRaw(
        token: String,
        classId: Int,
        sectionId: Int
    ): StudentListResponse {
        return api.getStudentsForAttendance(
            "Bearer $token",
            classId,
            sectionId
        ).body()!!
    }

    suspend fun attendanceStatusRaw(
        token: String,
        classId: Int,
        sectionId: Int,
        date: String
    ): AttendanceStatusResponse {
        return api.getAttendanceStatus(
            "Bearer $token",
            classId,
            sectionId,
            date
        ).body()!!
    }

    suspend fun attendanceMarkRaw(
        token: String,
        request: MarkAttendanceRequest
    ) {
        api.markAttendance(
            "Bearer $token",
            request
        )
    }

    suspend fun attendanceByDateRaw(
        token: String,
        classId: Int,
        sectionId: Int,
        date: String
    ): AttendanceDateResponse {
        return api.getAttendanceByDate(
            "Bearer $token",
            classId,
            sectionId,
            date
        ).body()!!
    }

    suspend fun attendanceUpdateRaw(
        token: String,
        request: UpdateAttendanceRequest
    ) {
        api.updateAttendance(
            "Bearer $token",
            request
        )
    }

}
