package com.undp.fleettracker.models.trip


import com.google.gson.annotations.SerializedName

class TripRequestModel {
    @SerializedName("RecordEndDateStr")
    var recordEndDateStr: String? = ""

    @SerializedName("RecordStartDateStr")
    var recordStartDateStr: String? = ""

    @SerializedName("TenantId")
    var tenantId: Int? = 0

    @SerializedName("VehicleId")
    var vehicleId: Int? = 0
}