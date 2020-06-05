package com.undp.fleettracker.models.base

import com.google.gson.annotations.SerializedName

open class BaseGetRequestModel {
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

    @SerializedName("startRecordNo")
    var startRecordNo: Int? = 0

    @SerializedName("StartDate")
    var startDate: String? = ""

    @SerializedName("EndDate")
    var endDate: String? = ""
}