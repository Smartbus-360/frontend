package com.smartbus360.app.data.model

//data class AttendanceItem(
//    val id: Int,
//    val registrationNumber: String,
//    val username: String?,
//    val instituteName: String?,
//    val scan_time: String
//)
data class AttendanceItem(
    val id: Int,
    val registrationNumber: String,
    val username: String,
    val instituteName: String,
    val bus_id: String?,
    val scan_time: String,
    val note: String?,
    val marked_by_role: String? = null


)
