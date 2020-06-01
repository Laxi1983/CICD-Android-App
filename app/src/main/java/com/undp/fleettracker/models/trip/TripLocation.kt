package com.undp.fleettracker.models.trip


import com.google.gson.annotations.SerializedName

data class TripLocation(
    @SerializedName("DistanceFromPrevPoint")
    var distanceFromPrevPoint: Double? = 0.0,
    @SerializedName("Latitude")
    var latitude: Double? = 0.0,
    @SerializedName("Longitude")
    var longitude: Double? = 0.0,
    @SerializedName("RecordLocalDateTime")
    var recordLocalDateTime: String? = "",
    @SerializedName("RecordTimeEpoch")
    var recordTimeEpoch: Int? = 0,
    @SerializedName("Speed")
    var speed: Double? = 0.0
)