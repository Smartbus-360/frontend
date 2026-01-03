package com.smartbus360.app.parent.models

data class ParentProfileResponse(
    val parent: ParentInfo,
    val children: List<ChildInfo>
)

data class ParentInfo(
    val id: Int,
    val fullName: String,
    val email: String,
    val phone: String,
    val address: String?
)

data class ChildInfo(
    val id: Int,
    val name: String,
    val className: String,
    val section: String,
    val rollNumber: String,
    val profilePic: String?
)
