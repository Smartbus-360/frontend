package com.smartbus360.app.data.model.response

data class MapAccessResponse(
    val allowed: Boolean,
    val source: String? = null,
    val expiresOn: String? = null,
    val daysLeft: Int? = null,
    val expired: Boolean? = null,
    val message: String? = null
)
