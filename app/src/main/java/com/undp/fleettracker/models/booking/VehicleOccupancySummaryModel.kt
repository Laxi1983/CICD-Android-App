package com.undp.fleettracker.models.booking

data class VehicleOccupancySummaryModel(
    val DeviceSerialNumber: String?,
    val DriverName: String?,
    val FilteredCount: Int,
    val FleetTimeZoneId: String?,
    val Occupancy: String?,
    val VehicleCount: Int,
    val VehicleId: Int,
    var VehicleName: String?
)