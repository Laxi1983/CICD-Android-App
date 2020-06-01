package com.undp.fleettracker.models.booking

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class BookingVehicleInfo (
    val DriverEmail: String?,
    val DriverName: String?,
    val DriverUserId: Int,
    val FleetId: Int,
    val FleetName: String?,
    val FleetTimeZoneId: String?,
    val FleetTimeZoneInfo: FleetTimeZoneInfo?,
    val Make: String?,
    val ManufacturedIn: String?,
    val Model: String?,
    val ModelYear: String?,
    val RegistrationNumber: String?,
    val VehicleId: Int,
    val VehicleName: String?
) : Parcelable