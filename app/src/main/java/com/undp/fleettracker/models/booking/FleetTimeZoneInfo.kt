package com.undp.fleettracker.models.booking

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FleetTimeZoneInfo(
    val Id: String,
    val DisplayName: String,
    val BaseUtcOffset:Int
):Parcelable