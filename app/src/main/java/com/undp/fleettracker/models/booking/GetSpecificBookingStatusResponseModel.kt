package com.undp.fleettracker.models.booking

data class GetSpecificBookingStatusResponseModel(
    val `data`: List<GetSpecificBookingStatusModel>,
    val filtered: Int,
    val message: String,
    val status: Int,
    val total: Int
)