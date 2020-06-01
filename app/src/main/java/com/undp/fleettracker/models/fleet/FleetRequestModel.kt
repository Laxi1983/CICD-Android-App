package com.undp.fleettracker.models.fleet


import com.google.gson.annotations.SerializedName

class FleetRequestModel {
    @SerializedName("AdapterId")
    var adapterId: String? = null

    @SerializedName("alertId")
    var alertId: Int? = 0

    @SerializedName("approvalStatus")
    var approvalStatus: String? = null

    @SerializedName("bookingType")
    var bookingType: String? = null

    @SerializedName("deviceTypeId")
    var deviceTypeId: Int? = 0

    @SerializedName("EndDate")
    var endDate: String? = null

    @SerializedName("fleetId")
    var fleetId: Int? = 0

    @SerializedName("Make")
    var make: String? = null

    @SerializedName("Model")
    var model: String? = null

    @SerializedName("pageNo")
    var pageNo: Int? = 0

    @SerializedName("pageSize")
    var pageSize: Int? = 0

    @SerializedName("searchKey")
    var searchKey: String? = null

    @SerializedName("sortColumnName")
    var sortColumnName: String? = null

    @SerializedName("sortType")
    var sortType: String? = null

    @SerializedName("StartDate")
    var startDate: String? = null

    @SerializedName("startRecordNo")
    var startRecordNo: String? = null

    @SerializedName("tenantId")
    var tenantId: Int? = 0

    @SerializedName("tripId")
    var tripId: Int? = 0

    @SerializedName("userId")
    var userId: Int? = 0

    @SerializedName("vehicleId")
    var vehicleId: Int? = 0

    @SerializedName("vehicleIds")
    var vehicleIds: String? = null


}

