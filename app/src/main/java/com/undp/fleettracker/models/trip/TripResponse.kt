package com.undp.fleettracker.models.trip


import com.google.gson.annotations.SerializedName

data class TripResponse(
    @SerializedName("data")
    var data: List<TripDetails> = listOf(),
    @SerializedName("filtered")
    var filtered: Int? = 0,
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("status")
    var status: Int? = 0,
    @SerializedName("total")
    var total: Int? = 0
)