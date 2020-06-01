package com.undp.fleettracker.models.booking

data class GetBookingSummaryResponseModel(
    val data: List<GetBookingSummaryDetailsModel>,
    val filtered: Int,
    val message: String,
    val status: Int,
    val total: Int
)