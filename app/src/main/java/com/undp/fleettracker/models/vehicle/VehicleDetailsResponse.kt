package com.undp.fleettracker.models.vehicle

data class VehicleDetailsResponse(
    val `data`: VehicleDetails,
    val filtered: Int,
    val message: String,
    val status: Int,
    val total: Int
)