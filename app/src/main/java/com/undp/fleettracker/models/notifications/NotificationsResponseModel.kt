package com.undp.fleettracker.models.notifications


import com.google.gson.annotations.SerializedName
import java.io.Serializable

class NotificationsResponseModel : Serializable {
    @SerializedName("data")
    var `data`: ArrayList<NotificationModel>? = arrayListOf()

    @SerializedName("filtered")
    var filtered: Int? = 0

    @SerializedName("message")
    var message: String? = ""

    @SerializedName("status")
    var status: Int? = 0

    @SerializedName("total")
    var total: Int? = 0

    override fun toString(): String {
        return "NotificationsResponseModel(`data`=$`data`, filtered=$filtered, message=$message, status=$status, total=$total)"
    }
}