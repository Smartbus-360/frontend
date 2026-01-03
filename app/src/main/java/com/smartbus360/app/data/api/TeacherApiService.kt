package com.smartbus360.app.data.api

import retrofit2.Response
import retrofit2.http.*
import com.smartbus360.app.teacher.data.model.*

data class UpdateAttendanceRequest(
    val studentId: Int,
    val classId: Int,
    val sectionId: Int,
    val date: String,
    val status: String
)
data class UpdateExamRequest(
    val examName: String,
    val date: String
)

interface TeacherApiService {

    // ---------------- AUTH ----------------
    @POST("erp/teacher/login")
    suspend fun teacherLogin(
        @Body request: TeacherLoginRequest
    ): Response<TeacherLoginResponse>

    // ---------------- DASHBOARD ----------------
    @GET("erp/teacher/dashboard")
    suspend fun getDashboard(
        @Header("Authorization") token: String
    ): Response<TeacherDashboardResponse>

    // ---------------- ATTENDANCE ----------------
    @GET("erp/teacher/attendance/students/{classId}/{sectionId}")
    suspend fun getStudentsForAttendance(
        @Header("Authorization") token: String,
        @Path("classId") classId: Int,
        @Path("sectionId") sectionId: Int
    ): Response<StudentListResponse>


    @POST("erp/teacher/attendance/mark")
    suspend fun markAttendance(
        @Header("Authorization") token: String,
        @Body body: MarkAttendanceRequest
    ): Response<GeneralResponse>

    @GET("erp/teacher/attendance/status")
    suspend fun getAttendanceStatus(
        @Header("Authorization") token: String,
        @Query("classId") classId: Int,
        @Query("sectionId") sectionId: Int,
        @Query("date") date: String
    ): Response<AttendanceStatusResponse>


    @GET("/erp/teacher/classes")
    suspend fun getTeacherClasses(
        @Header("Authorization") token: String
    ): Response<ClassListResponse>

    // ---------------- HOMEWORK ----------------
    @GET("erp/homework/{teacherId}")
    suspend fun getHomework(
        @Header("Authorization") token: String,
        @Path("teacherId") teacherId: Int
    ): Response<HomeworkListResponse>



    @POST("erp/homework/create")
    suspend fun createHomework(
        @Header("Authorization") token: String,
        @Body request: CreateHomeworkRequest
    ): Response<HomeworkResponse>

    @PUT("erp/homework/update/{id}")
    suspend fun updateHomework(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body body: Map<String, Any>
    ): Response<GeneralResponse>

    @DELETE("erp/homework/delete/{id}")
    suspend fun deleteHomework(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<GeneralResponse>

    // ---------------- EXAMS ----------------
    @GET("erp/exams")
    suspend fun getExams(
        @Header("Authorization") token: String
    ): Response<ExamListResponse>

    @POST("erp/exams/marks/add")
    suspend fun addMarks(
        @Header("Authorization") token: String,
        @Body request: AddMarksRequest
    ): Response<GeneralResponse>


    // ---------------- SYLLABUS ----------------
    @GET("erp/teacher/syllabus/list")
    suspend fun getSyllabusProgress(
        @Header("Authorization") token: String,
        @Query("classId") classId: Int,
        @Query("sectionId") sectionId: Int,
        @Query("subjectId") subjectId: Int
    ): Response<SyllabusProgressResponse>

    @POST("erp/teacher/syllabus/add")
    suspend fun addSyllabusProgress(
        @Header("Authorization") token: String,
        @Body request: AddSyllabusProgressRequest
    ): Response<GeneralResponse>

    // ---------------- STUDENT ANALYSIS ----------------
    @GET("erp/teacher/student-analysis/{studentId}")
    suspend fun getStudentAnalysis(
        @Header("Authorization") token: String,
        @Path("studentId") studentId: Int
    ): Response<StudentAnalysisResponse>

    @GET("erp/teacher/attendance/date")
    suspend fun getAttendanceByDate(
        @Header("Authorization") token: String,
        @Query("classId") classId: Int,
        @Query("sectionId") sectionId: Int,
        @Query("date") date: String
    ): Response<AttendanceDateResponse>


    @POST("erp/teacher/attendance/update")
    suspend fun updateAttendance(
        @Header("Authorization") token: String,
        @Body request: UpdateAttendanceRequest
    ): Response<Unit>


    @GET("erp/teacher/attendance/summary")
    suspend fun getAttendanceSummary(
        @Header("Authorization") token: String,
        @Query("classId") classId: Int,
        @Query("sectionId") sectionId: Int,
        @Query("month") month: Int,
        @Query("year") year: Int
    ): Response<AttendanceSummaryResponse>

    @GET("erp/exams/results/{studentId}")
    suspend fun getExamResults(
        @Header("Authorization") token: String,
        @Path("studentId") studentId: Int
    ): Response<ExamResultResponse>


    @GET("erp/teacher/subjects")
    suspend fun getTeacherSubjects(
        @Header("Authorization") token: String
    ): Response<SubjectListResponse>

    @GET("erp/teacher/circulars")
    suspend fun getTeacherCirculars(
        @Header("Authorization") token: String
    ): Response<CircularListResponse>

    @POST("erp/teacher/messages/broadcast")
    suspend fun broadcastMessage(
        @Header("Authorization") token: String,
        @Body request: BroadcastMessageRequest
    ): Response<GeneralResponse>

    @POST("erp/exams/create")
    suspend fun createExam(
        @Header("Authorization") token: String,
        @Body body: CreateExamRequest
    ): Response<CreateExamResponse>



    @PUT("erp/exams/update/{id}")
    suspend fun updateExam(
        @Header("Authorization") token: String,
        @Path("id") examId: Int,
        @Body body: UpdateExamRequest
    ): Response<CreateExamResponse>

    @DELETE("erp/exams/delete/{id}")
    suspend fun deleteExam(
        @Header("Authorization") token: String,
        @Path("id") examId: Int
    ): Response<GeneralResponse>

}
