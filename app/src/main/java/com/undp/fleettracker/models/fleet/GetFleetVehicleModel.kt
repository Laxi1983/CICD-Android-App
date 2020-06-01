package com.undp.fleettracker.models.fleet

data class GetFleetVehicleModel(
    val DeviceSerialNumber: String?,
    val DriverName: String?,
    val DriverUserId: Int,
    val FilteredCount: Int,
    val FleetId: Int,
    val FleetName: String?,
    val FleetTimeZoneId: String?,
    val FleetUnitId: Int,
    val LastConnectedLocalDateTime: String?,
    val LastConnectedTimeEpoch: Int,
    val Make: String?,
    val ManufacturedIn: String?,
    val Metric: Int,
    val Model: String?,
    val ModelYear: Int,
    val Odometer: Double,
    val ParametersData: String?,
    val PassengersCount: Int,
    val RegistrationNumber: String?,
    val VIN: String?,
    val VehicleCount: Int,
    val VehicleId: Int,
    val VehicleName: String?
)