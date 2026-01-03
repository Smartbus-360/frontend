package com.smartbus360.app.erpadmin.data.api


import com.smartbus360.app.erpadmin.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface AdminApiService {

    @POST("api/admin/signin")
    suspend fun adminLogin(
        @Body request: AdminLoginRequest
    ): Response<AdminLoginResponse>

    @GET("api/admin/get-details")
    suspend fun getAdminDetails(
        @Header("Authorization") token: String
    ): Response<AdminProfile>
}
