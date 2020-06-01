package com.undp.fleettracker.models.booking

import com.google.gson.annotations.SerializedName
import com.undp.fleettracker.models.base.BaseGetRequestModel

class GetBookingRequestModel : BaseGetRequestModel() {

    @SerializedName("tenantId")
    var tenantId: Int? = 0

    @SerializedName("fleetId")
    var fleetId: Int? = 0

    @SerializedName("bookingType")
    var bookingType: String? = "1,2,3"

    @SerializedName("approvalStatus")
    var approvalStatus: String? = "1,2,3,4"

    @SerializedName("isSearchTextLengthValid")
    var isSearchTextLengthValid: Boolean? = true

}