package com.undp.fleettracker.models.vehicle

import java.io.Serializable

data class VehicleInfo(
    val Direction: Int,
    val IsFaultPresent: Boolean,
    val LastConnectedLocalDateTime: String,
    val LastConnectedTimeEpoch: Int,
    val Latitude: Double,
    val Longitude: Double,
    val Metric: Int,
    val Odometer: Double,
    val VehicleId: Int,
    val VehicleName: String,
    val VehicleState: Int

) : Serializable {

    override fun toString(): String {
        return "VehicleInfo(Direction=$Direction, IsFaultPresent=$IsFaultPresent, LastConnectedLocalDateTime='$LastConnectedLocalDateTime', LastConnectedTimeEpoch=$LastConnectedTimeEpoch, Latitude=$Latitude, Longitude=$Longitude, Metric=$Metric, Odometer=$Odometer, VehicleId=$VehicleId, VehicleName='$VehicleName', VehicleState=$VehicleState)"
    }
}

