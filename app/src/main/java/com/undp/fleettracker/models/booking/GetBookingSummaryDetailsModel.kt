package com.undp.fleettracker.models.booking

data class GetBookingSummaryDetailsModel(
    val bookingType: String?,
    val distance: Double,
    val driverName: String?,
    val endtime: String?,
    val fleetid: Int,
    val route: String?,
    val runHours: Double,
    val starttime: String?,
    val tripLocations: List<TripLocation>?,
    val tripType: Int,
    val triptypeTxt: String?,
    val vehicleName: String?,
    val vehicleid: Int
)