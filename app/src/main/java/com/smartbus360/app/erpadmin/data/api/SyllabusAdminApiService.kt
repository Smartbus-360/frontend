package com.smartbus360.app.erpadmin.data.api


import com.smartbus360.app.erpadmin.data.model.SyllabusProgress
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface SyllabusAdminApiService {

    @GET("erp/teacher/syllabus/list")
    suspend fun getSyllabusProgress(
        @Header("Authorization") token: String
    ): Response<List<SyllabusProgress>>
}
