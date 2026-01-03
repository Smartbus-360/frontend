//package com.smartbus360.app.data.model.response
//
//
//import com.google.gson.annotations.SerializedName
//
//data class RouteResponse(
//    @SerializedName("routes") val routes: List<Route>
//)
//
//data class Route(
//    @SerializedName("geometry") val geometry: Geometry,
//    @SerializedName("legs") val legs: List<Leg>,
//    @SerializedName("distance") val distance: Double,
//    @SerializedName("duration") val duration: Double
//)
//
//data class Leg(
//    @SerializedName("steps") val steps: List<Step>,
//    @SerializedName("distance") val distance: Double,
//    @SerializedName("duration") val duration: Double
//)
//
//data class Step(
//    @SerializedName("geometry") val geometry: Geometry,
//    @SerializedName("distance") val distance: Double,
//    @SerializedName("duration") val duration: Double,
//    @SerializedName("name") val name: String
//)
//
//data class Geometry(
//    @SerializedName("coordinates") val coordinates: List<List<Double>>,
//    @SerializedName("type") val type: String
//)
