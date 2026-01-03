package com.smartbus360.app.erpadmin.data.api


import com.smartbus360.app.erpadmin.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface StudentApiService {

    @GET("api/admin/users/school-students")
    suspend fun getStudents(
        @Header("Authorization") token: String
    ): Response<List<Student>>

    @POST("api/admin/add-student-direct")
    suspend fun addStudent(
        @Header("Authorization") token: String,
        @Body request: AddStudentRequest
    ): Response<Unit>

    @POST("api/admin/generate/{studentId}")
    suspend fun generateQr(
        @Header("Authorization") token: String,
        @Path("studentId") studentId: Int
    ): Response<Unit>

    @POST("api/admin/revoke/{studentId}")
    suspend fun revokeQr(
        @Header("Authorization") token: String,
        @Path("studentId") studentId: Int
    ): Response<Unit>
}
