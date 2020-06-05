package com.undp.fleettracker.models.booking

data class VehicleOccupancySummaryResponseModel(
    val `data`: List<VehicleOccupancySummaryModel>,
    val filtered: Int,
    val message: String,
    val status: Int,
    val total: Int
)