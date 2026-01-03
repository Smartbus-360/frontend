package com.smartbus360.app.data.model.request

data class UpdateShiftRequest(
    val driverId: Int,
    val shift: String,
    val round: Int
)
