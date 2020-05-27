package com.undp.fleettracker.models.notifications


import com.google.gson.annotations.SerializedName
import java.io.Serializable

class NotificationRequestModel : Serializable {
    @SerializedName("AlertType")
    var alertType: Int? = 0

    @SerializedName("isSearchTextLengthValid")
    var isSearchTextLengthValid: Boolean? = false

    @SerializedName("pageNo")
    var pageNo: Int? = 0

    @SerializedName("pageSize")
    var pageSize: Int? = 0

    @SerializedName("RecordEndDateStr")
    var recordEndDateStr: String? = ""

    @SerializedName("RecordStartDateStr")
    var recordStartDateStr: String? = ""

    @SerializedName("searchKey")
    var searchKey: String? = ""

    @SerializedName("sortColumnName")
    var sortColumnName: String? = ""

    @SerializedName("sortType")
    var sortType: String? = ""

    @SerializedName("startRecordNo")
    var startRecordNo: Int? = 0

    @SerializedName("TenantId")
    var tenantId: Int? = 0

    @SerializedName("UserId")
    var userId: Int? = 0

    @SerializedName("VehicleId")
    var vehicleId: Int? = 0
    override fun toString(): String {
        return "NotificationRequestModel(alertType=$alertType, isSearchTextLengthValid=$isSearchTextLengthValid, pageNo=$pageNo, pageSize=$pageSize, recordEndDateStr=$recordEndDateStr, recordStartDateStr=$recordStartDateStr, searchKey=$searchKey, sortColumnName=$sortColumnName, sortType=$sortType, startRecordNo=$startRecordNo, tenantId=$tenantId, userId=$userId, vehicleId=$vehicleId)"
    }


}