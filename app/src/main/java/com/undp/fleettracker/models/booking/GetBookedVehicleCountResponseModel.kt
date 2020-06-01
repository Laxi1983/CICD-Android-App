package com.undp.fleettracker.models.booking

data class GetBookedVehicleCountResponseModel(
    val `data`: GetBookedVehicleCountModel,
    val filtered: Int,
    val message: String,
    val status: Int,
    val total: Int
)