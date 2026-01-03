package com.smartbus360.app.erpadmin.data.model

data class Section(
    val id: Int,
    val classId: Int,
    val sectionName: String,
    val instituteId: Int
)


data class AddSectionRequest(
    val sectionName: String,
    val classId: Int,
    val instituteId: Int
)
