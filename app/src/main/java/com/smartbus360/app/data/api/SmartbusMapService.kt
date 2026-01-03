//package com.smartbus360.app.data.api
//
//
//import retrofit2.Response
//import retrofit2.http.GET
//import retrofit2.http.Query
//import com.google.gson.annotations.SerializedName
//
//// Routing API (adapt path to your backend)
//interface SmartBusRouteService {
//    @GET("route/v1/driving")
//    suspend fun getRoute(
//        @Query("coordinates") coordinates: String, // "lon1,lat1;lon2,lat2"
//        @Query("overview") overview: String = "false",
//        @Query("steps") steps: Boolean = false,
//        @Query("annotations") annotations: String = "duration,distance"
//    ): Response<RouteResponse>
//}
//
//// Minimal route response (only distance & duration)
//data class RouteResponse(
//    @SerializedName("routes") val routes: List<RouteInfo>
//)
//
//data class RouteInfo(
//    @SerializedName("distance") val distance: Double,
//    @SerializedName("duration") val duration: Double
//)
