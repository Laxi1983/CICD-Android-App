package com.undp.fleettracker.models.fleet

data class FleetModel(

    val Description: String,
    val FleetId: Int,
    val FleetName: String,
    val FleetTimeZoneId: String,
    val FleetTimeZoneInfo: Any,
    val Metric: Int,
    val UpdatedBy: Int,
    val UpdatedDateTime: String
)