package com.undp.fleettracker.models.auth


import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("Email")
    var email: String? = "",
    @SerializedName("MobileNumber")
    var mobileNumber: String? = "",
    @SerializedName("Name")
    var name: String? = "",
    @SerializedName("RoleId")
    var roleId: Int? = 0,
    @SerializedName("Status")
    var status: Int? = 0,
    @SerializedName("TenantId")
    var tenantId: Int? = 0,
    @SerializedName("UserID")
    var userID: Int? = 0
)