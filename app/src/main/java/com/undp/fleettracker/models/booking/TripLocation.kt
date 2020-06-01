package com.undp.fleettracker.models.booking

data class TripLocation(
    val formattedAddress: String?,
    val index: Int,
    val lat: Double,
    val lng: Double,
    val name: String?
)