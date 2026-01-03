package com.smartbus360.app.data.api


import com.google.android.gms.common.moduleinstall.ModuleAvailabilityResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import com.smartbus360.app.data.model.response.GetDriverDetailResponseNewXX
import retrofit2.http.Path
import retrofit2.http.Query


data class BusResponse(
    val busId: Int,
    val busNumber: String,
    val availabilityStatus: String?,
    val driverId : Int?,
    val latitude: Double?,
    val longitude: Double?,
    val driverName: String?,
    val instituteName: String?,
    val speed: Double?,
    val assignedRouteId: Int?,   // 👈 NEW FIELD
    val routeCurrentJourneyPhase: String?,   // 👈 NEW
    val routeCurrentRound: Int?,             // 👈 NEW
    val stopType: String?,
    val nextStoppage: String? = null  // ✅ NEW FIELD

)


data class MarkFinalStopNoAuthRequest(
    val driverId: Int,
    val routeId: Int
)

data class GenerateDriverQrRequest(
    val driverId: Int,
    val durationHours: Int = 6
)

data class GenerateDriverQrResponse(
    val success: Boolean,
    val id: Int,
    val token: String,
    val expiresAt: String,
    val png: String
)
data class DriverQrHistoryResponse(
    val success: Boolean,
    val items: List<DriverQrTokenResponse>
)

data class DriverQrTokenResponse(
    val id: Int,
    val originalDriverId: Int?,
    val token: String?,
    val status: String?,
    val usedCount: Int?,
    val createdBy: Int?,
    val createdAt: String?,
    val expiresAt: String?,
    val durationHours: Int?,
    val png: String?
)

interface AdminApiService {
    @GET("admin/buses")
    suspend fun getBuses(
        @Header("Authorization") token: String
    ): List<BusResponse>

    @GET("driver/{driverId}/route-details")
    suspend fun getDriverRouteDetails(
        @Path("driverId") driverId: Int,
        @Header("Authorization") token: String
    ): GetDriverDetailResponseNewXX

    @POST("mark-final-stop-noauth")
    suspend fun markFinalStopNoAuth(
        @Body request: MarkFinalStopNoAuthRequest
    ): Response<Unit>   // you can replace Unit with a custom response model if backend returns JSON

    @POST("admin/driver-qr/generate")
    suspend fun generateDriverQr(
        @Header("Authorization") token: String,
        @Body body: GenerateDriverQrRequest
    ): Response<GenerateDriverQrResponse>

//    @GET("admin/driver-qr/history")
//    suspend fun getDriverQrHistory(
//        @Header("Authorization") token: String
//    ): Response<List<DriverQrTokenResponse>>
@GET("driver-qr/history")
suspend fun getDriverQrHistory(
    @Header("Authorization") token: String,
    @Query("driverId") driverId: Int,
    @Query("limit") limit: Int = 50,
    @Query("status") status: String = "all"
): DriverQrHistoryResponse


}
