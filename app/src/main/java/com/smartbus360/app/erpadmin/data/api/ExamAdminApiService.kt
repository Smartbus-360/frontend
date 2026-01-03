package com.smartbus360.app.erpadmin.data.api


import com.smartbus360.app.erpadmin.data.model.*
import retrofit2.Response
import retrofit2.http.*

data class ExamListResponse(
    val success: Boolean,
    val exams: List<Exam>
)

interface ExamAdminApiService {

    @GET("erp/exams")
    suspend fun getAllExams(
        @Header("Authorization") token: String
    ): Response<ExamListResponse>


    @GET("erp/exams/results/{studentId}")
    suspend fun getStudentResults(
        @Header("Authorization") token: String,
        @Path("studentId") studentId: Int
    ): Response<ExamResultResponse>

}
