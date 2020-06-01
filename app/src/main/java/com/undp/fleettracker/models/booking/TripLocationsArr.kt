package com.undp.fleettracker.models.booking

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TripLocationsArr(
    val formattedAddress: String?,
    val index: Int,
    val lat: Double,
    val lng: Double,
    val name: String
):Parcelable