package com.smartbus360.app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BlockedInfo(
    val message: String,
    val until: String? = null
) : Parcelable
