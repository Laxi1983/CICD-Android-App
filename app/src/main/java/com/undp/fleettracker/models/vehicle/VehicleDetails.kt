package com.undp.fleettracker.models.vehicle

data class VehicleDetails(
    val FaultyVehiclesCount: Int,
    val FleetId: Int,
    val FleetName: String,
    val RunningVehiclesCount: Int,
    val TotalVehiclesCount: Int,
    val VehicleInfo: ArrayList<VehicleInfo>
)