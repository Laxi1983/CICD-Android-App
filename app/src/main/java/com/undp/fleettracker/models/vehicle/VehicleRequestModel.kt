package com.undp.fleettracker.models.vehicle


import com.google.gson.annotations.SerializedName

class VehicleRequestModel {
    @SerializedName("AdapterId")
    var adapterId: String? = ""

    @SerializedName("alertId")
    var alertId: Int? = 0

    @SerializedName("approvalStatus")
    var approvalStatus: String? = ""

    @SerializedName("bookingType")
    var bookingType: String? = ""

    @SerializedName("deviceTypeId")
    var deviceTypeId: Int? = 0

    @SerializedName("EndDate")
    var endDate: String? = ""

    @SerializedName("fleetId")
    var fleetId: Int? = 0

    @SerializedName("Make")
    var make: String? = ""

    @SerializedName("Model")
    var model: String? = ""

    @SerializedName("pageNo")
    var pageNo: Int? = 0

    @SerializedName("pageSize")
    var pageSize: Int? = 0

    @SerializedName("searchKey")
    var searchKey: String? = ""

    @SerializedName("sortColumnName")
    var sortColumnName: String? = ""

    @SerializedName("sortType")
    var sortType: String? = ""

    @SerializedName("StartDate")
    var startDate: String? = ""

    @SerializedName("startRecordNo")
    var startRecordNo: String? = ""

    @SerializedName("tenantId")
    var tenantId: Int? = 0

    @SerializedName("tripId")
    var tripId: Int? = 0

    @SerializedName("userId")
    var userId: Int? = 0

    @SerializedName("vehicleId")
    var vehicleId: Int? = 0

    @SerializedName("vehicleIds")
    var vehicleIds: String? = ""
}