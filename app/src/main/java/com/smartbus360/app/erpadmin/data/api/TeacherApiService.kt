package com.smartbus360.app.erpadmin.data.api


import com.smartbus360.app.erpadmin.data.model.*
import retrofit2.Response
import retrofit2.http.*

data class TeacherListResponse(
    val success: Boolean,
    val teachers: List<Teacher>
)

interface TeacherApiService {

    @GET("api/admin/teachers")
    suspend fun getTeachers(
        @Header("Authorization") token: String
    ): Response<TeacherListResponse>

    @POST("api/admin/add-teacher")
    suspend fun addTeacher(
        @Header("Authorization") token: String,
        @Body request: AddTeacherRequest
    ): Response<Unit>

    @DELETE("api/admin/teachers/{id}")
    suspend fun deleteTeacher(
        @Header("Authorization") token: String,
        @Path("id") teacherId: Int
    ): Response<Unit>
}
