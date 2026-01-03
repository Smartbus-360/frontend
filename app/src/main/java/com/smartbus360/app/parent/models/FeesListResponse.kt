package com.smartbus360.app.parent.models

data class FeesDueResponse(
    val success: Boolean,
    val pending: List<FeeItem>
)
data class FeesHistoryResponse(
    val success: Boolean,
    val history: List<FeeItem>
)
data class FeeItem(
    val id: Int,
    val title: String?,
    val amount: Int?,
    val dueDate: String?,
    val status: String?   // "pending" / "paid"
)
