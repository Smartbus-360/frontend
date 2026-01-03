package com.smartbus360.app.erpadmin.data.model

data class SchoolClass(
    val id: Int,
    val className: String,
    val instituteId: Int,
    val tbl_sm360_sections: List<Section> = emptyList()
)
