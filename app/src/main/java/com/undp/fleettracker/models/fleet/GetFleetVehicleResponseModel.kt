package com.undp.fleettracker.models.fleet

data class GetFleetVehicleResponseModel(
    val `data`: List<GetFleetVehicleModel>,
    val filtered: Int,
    val message: String,
    val status: Int,
    val total: Int
)