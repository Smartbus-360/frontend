package com.smartbus360.app.erpadmin.data.api


import com.smartbus360.app.erpadmin.data.model.*
import retrofit2.Response
import retrofit2.http.*

//interface ClassSectionApiService {
//
//    // CLASS
//    @GET("api/admin/class/list")
//    suspend fun getClasses(
//        @Header("Authorization") token: String
//    ): Response<ApiListResponse<SchoolClass>>
//
//    @POST("api/admin/class")
//    suspend fun addClass(
//        @Header("Authorization") token: String,
//        @Body body: Map<String, String>
//    ): Response<Unit>
//
//    @DELETE("api/admin/class/{id}")
//    suspend fun deleteClass(
//        @Header("Authorization") token: String,
//        @Path("id") classId: Int
//    ): Response<Unit>
//
//    // SECTION
//    @GET("api/admin/section/list/{classId}")
//    suspend fun getSections(
//        @Header("Authorization") token: String,
//        @Path("classId") classId: Int
//    ): Response<ApiListResponse<SchoolClass>>
//
//    @POST("api/admin/section")
//    suspend fun addSection(
//        @Header("Authorization") token: String,
//        @Body body: Map<String, Any>
//    ): Response<Unit>
//
//    @DELETE("api/admin/section/{id}")
//    suspend fun deleteSection(
//        @Header("Authorization") token: String,
//        @Path("id") sectionId: Int
//    ): Response<Unit>
//}


data class SectionListResponse(
    val success: Boolean,
    val sections: List<Section>
)

interface ClassSectionApiService {

    @GET("api/admin/class/list")
    suspend fun getClasses(
        @Header("Authorization") token: String
    ): Response<ClassListResponse>

    @POST("api/admin/class")
    suspend fun addClass(
        @Header("Authorization") token: String,
        @Body body: Map<String, String> // className
    ): Response<Unit>

    @DELETE("api/admin/class/{id}")
    suspend fun deleteClass(
        @Header("Authorization") token: String,
        @Path("id") classId: Int
    ): Response<Unit>

    @POST("api/admin/section")
    suspend fun addSection(
        @Header("Authorization") token: String,
        @Body request: AddSectionRequest
    ): Response<Unit>

    @GET("api/admin/section/list/{classId}")
    suspend fun getSections(
        @Header("Authorization") token: String,
        @Path("classId") classId: Int
    ): Response<SectionListResponse>

    @DELETE("api/admin/section/{id}")
    suspend fun deleteSection(
        @Header("Authorization") token: String,
        @Path("id") sectionId: Int
    ): Response<Unit>
}
