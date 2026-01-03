package com.smartbus360.app.data.model.response


import com.google.gson.annotations.SerializedName

data class GeocodeResponse(
    @SerializedName("display_name") val displayName: String?,
    @SerializedName("lat") val lat: String?,
    @SerializedName("lon") val lon: String?
)
