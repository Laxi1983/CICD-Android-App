package com.undp.fleettracker.models.notifications


import com.google.gson.annotations.SerializedName

class NotificationModel {
    @SerializedName("AlertEpochTime")
    var alertEpochTime: Int? = 0

    @SerializedName("AlertId")
    var alertId: Int? = 0

    @SerializedName("date")
    var date: String? = ""

    @SerializedName("Description")
    var description: String? = ""

    @SerializedName("GeofenceName")
    var geofenceName: Any? = Any()

    @SerializedName("Severity")
    var severity: Int? = 0

    @SerializedName("Type")
    var type: Int? = 0

    @SerializedName("UpdatedAt")
    var updatedAt: String? = ""

    @SerializedName("VehicleName")
    var vehicleName: String? = ""
}