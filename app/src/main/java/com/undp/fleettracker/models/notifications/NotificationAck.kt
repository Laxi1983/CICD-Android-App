package com.undp.fleettracker.models.notifications


import com.google.gson.annotations.SerializedName

class NotificationAck {
    @SerializedName("AlertId")
    var alertId: Int? = 0

    @SerializedName("TenantId")
    var tenantId: Int? = 0

    @SerializedName("UserId")
    var userId: Int? = 0

    override fun toString(): String {
        return "NotificationAck(alertId=$alertId, tenantId=$tenantId, userId=$userId)"
    }


}