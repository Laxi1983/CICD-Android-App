package com.undp.fleettracker.models.booking

data class GetBookingsResponseModel(
    val data: List<GetBookingsDetails>,
    val filtered: Int,
    val message: String,
    val status: Int,
    val total: Int
)