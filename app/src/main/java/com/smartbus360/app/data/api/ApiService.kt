package com.smartbus360.app.data.api

import com.smartbus360.app.data.model.request.BusReachedStoppageRequest
import com.smartbus360.app.data.model.request.LoginRequest
import com.smartbus360.app.data.model.request.LoginStudentRequest
import com.smartbus360.app.data.model.request.MarkFinalStopRequest
import com.smartbus360.app.data.model.request.UpdateLocationRequest
import com.smartbus360.app.data.model.response.AdvertisementBannerResponse
import com.smartbus360.app.data.model.response.BusNotificationResponse
import com.smartbus360.app.data.model.response.BusReachedStoppageResponse
import com.smartbus360.app.data.model.response.BusReplacedResponse
import com.smartbus360.app.data.model.response.DateLogResponse
import com.smartbus360.app.data.model.response.DriverLocationsListResponse
import com.smartbus360.app.data.model.response.DriverLoginResponse
import com.smartbus360.app.data.model.response.DriversList
import com.smartbus360.app.data.model.response.GetDriverDetailResponseNewXX
import com.smartbus360.app.data.model.response.GetUserDetailResponseX
import com.smartbus360.app.data.model.response.MarkFinalStopResponse
import com.smartbus360.app.data.model.response.NotificationResponse
import com.smartbus360.app.data.model.response.ReachTimeResponse
import com.smartbus360.app.data.model.response.StudentLoginResponse
import com.smartbus360.app.data.model.response.UpdateLocationResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.Response
import com.smartbus360.app.data.model.response.AdminLoginResponse
import retrofit2.http.Headers
import com.smartbus360.app.data.model.request.AdminLoginRequest
import com.smartbus360.app.data.model.request.QrExchangeRequest
import com.smartbus360.app.data.model.response.StopX
import retrofit2.Call
import com.smartbus360.app.data.model.AttendanceItem
import com.smartbus360.app.data.model.response.AttendanceTakerLoginResponse
import com.smartbus360.app.data.model.request.UpdateShiftRequest
import com.smartbus360.app.data.model.response.GeneralResponse


data class AttendanceRequest(
    val registrationNumber: String,
    val token: String,
    val attendance_taker_id: Int,
//    val bus_id: String,
    val latitude: Double,
    val longitude: Double
)

//data class AttendanceResponse(
//    val success: Boolean,
//    val message: String,
//    val attendance_id: Int? = null   // 👈 REQUIRED FOR NOTE FEATURE
//
//)

data class AttendanceResponse(
    val success: Boolean,
    val message: String,
    val attendance_id: Int? = null,
    val noteRequired: Boolean? = false,
    val role: String? = null
)

data class Stop(
    val stopId: Int,
    val name: String,
    val lat: Double,
    val lng: Double
)

data class JourneyResponse(
    val driverId: Int,
    val phase: String,
    val round: Int,
    val routeId: Int,
    val stops: List<Stop>
)
data class LastStopResponse(
    val success: Boolean,
    val lastStop: StopX? // use StopX since its fields are nullable
)
data class MyAttendanceResponse(
    val success: Boolean,
    val registrationNumber: String?,
    val total: Int?,
    val attendance: List<AttendanceItem>?
)

data class StudentAttendanceResponse(
    val success: Boolean,
    val registrationNumber: String?,
    val total: Int?,
    val attendance: List<AttendanceItem>?
)
data class TakerAttendanceResponse(
    val success: Boolean,
    val total: Int,
    val attendance: List<AttendanceItem>?
)
data class AddNoteRequest(
    val attendance_id: Int,
    val note: String?,
    val note_type: String?,
    val added_by: Int
)



interface ApiService {

    @GET("drivers")
    suspend fun getDrivers(): DriversList

    @POST("attendance/mark")
    suspend fun markAttendance(
        @Header("Authorization") token: String,
        @Header("role") role: String,
        @Body request: AttendanceRequest
    ) : Response<AttendanceResponse>

    @GET("attendance/driver/{driverId}")
    suspend fun getDriverAttendance(@Path("driverId") driverId: Int): Response<List<AttendanceItem>>

    @GET("attendance/taker-sheet/{takerId}")
    suspend fun getTakerSheet(
        @Path("takerId") takerId: Int
    ): Response<TakerAttendanceResponse>



    @POST("api/login/driver")
    suspend fun login(@Body data: LoginRequest): DriverLoginResponse

    @POST("api/login/attendance-taker")
    suspend fun loginAttendanceTaker(@Body data: LoginRequest): AttendanceTakerLoginResponse

    @POST("attendance-taker/qr-login")
    suspend fun qrLoginAttendanceTaker(
        @Body body: Map<String, String>
    ): Response<AttendanceTakerLoginResponse>

    @POST("api/login/user")
    suspend fun loginStudent(@Body data: LoginStudentRequest): StudentLoginResponse

//    @GET("attendance/student/self")
//    suspend fun getMyAttendance(
//        @Header("Authorization") token: String
//    ): List<AttendanceItem>

    @GET("attendance/student/self")
    suspend fun getMyAttendance(
        @Header("Authorization") token: String
    ): Response<MyAttendanceResponse>

//    @POST("attendance/add-note")
//    suspend fun addAttendanceNote(
//        @Header("Authorization") token: String,
//        @Body body: Map<String, Any?>
//    ): Response<GeneralResponse>

    @POST("attendance/add-note")
    suspend fun addAttendanceNote(
        @Header("Authorization") token: String,
        @Body request: AddNoteRequest
    ): Response<GeneralResponse>


    @GET("api/driver/details/{driverId}")
    suspend fun getDriverDetail(
        @Path("driverId") userId: Int,
        @Header("Authorization") token: String
    ): GetDriverDetailResponseNewXX

    @GET("api/user/details/{userId}")
    suspend fun getUserDetail(
        @Path("userId") userId: Int,
        @Header("Authorization") token: String
    ): GetUserDetailResponseX

    @GET("api/user/details/{userId}")
    suspend fun getUserDetail2(
        @Path("userId") userId: Int,
        @Header("Authorization") token: String
    ): GetUserDetailResponseX

    @GET("attendance/unread-count")
    suspend fun getUnreadAttendanceCount(
        @Header("Authorization") token: String
    ): Response<Map<String, Int>> // or Response<UnreadCountResponse> if model exists


    @GET("api/driver/{driverId}/route-details")
    suspend fun getDriverRouteDetails(
        @Path("driverId") driverId: Int,
        @Header("Authorization") token: String
    ): GetDriverDetailResponseNewXX

//    @POST("update-shift")
//    suspend fun updateShift(@Body data: UpdateShiftRequest): Response<GeneralResponse>

    @POST("update-shift")
    suspend fun updateShift(
        @Header("Authorization") token: String,
        @Body data: UpdateShiftRequest
    ): Response<GeneralResponse>

    @GET("api/stoppage/last")
    suspend fun getLastReachedStop(
        @Header("Authorization") token: String,
        @retrofit2.http.Query("routeId") routeId: Int,
        @retrofit2.http.Query("tripType") tripType: String
    ): LastStopResponse


    @GET("api/driver/details/{driverId}")
    suspend fun getDriverDetail2(
        @Path("driverId") userId: Int,
        @Header("Authorization") token: String
    ): GetDriverDetailResponseNewXX

    @GET("api/driver/details/{driverId}")
    suspend fun getDriverDetailNew(
        @Path("driverId") userId: Int,
        @Header("Authorization") token: String
    ): GetDriverDetailResponseNewXX

    @POST("api/stoppage/reached")
    suspend fun busReachedStoppage(@Header("Authorization") token: String,
                                   @Body data: BusReachedStoppageRequest): BusReachedStoppageResponse

    @GET("api/bus/replacement/{busId}")
    suspend fun getBusReplacedStatus(
        @Path("busId") busId: Int,
        @Header("Authorization") token: String
    ): BusReplacedResponse

    @POST("api/mark-final-stop")
    suspend fun markFinalStop(@Header("Authorization") token: String,
                              @Body data: MarkFinalStopRequest): MarkFinalStopResponse

    @GET("api/advertisement/banner")
    suspend fun advertisementBanner(): AdvertisementBannerResponse

    @GET("api/notifications")
    suspend fun notifications(@Header("Authorization") token: String): NotificationResponse

    @GET("api/bus-notifications")
    suspend fun busNotifications(@Header("Authorization") token: String): NotificationResponse

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/admin/signin")
    suspend fun adminLogin(@Body request: AdminLoginRequest): Response<AdminLoginResponse>

    @GET("drivers/{driverId}/journey")
    suspend fun getDriverJourney(
        @Path("driverId") driverId: Int,
        @Header("Authorization") token: String
    ): JourneyResponse

    @GET("api/reach-times/{routeId}")
    suspend fun getReachTimes(
        @Path("routeId") routeId: Int,
        @Header("Authorization") token: String
    ): DateLogResponse

    @POST("api/driver-qr/exchange")
    suspend fun exchangeQr(@Body body: QrExchangeRequest): Response<DriverLoginResponse>


}

//api/bus/replacement/3