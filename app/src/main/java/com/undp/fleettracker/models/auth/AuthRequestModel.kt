package com.undp.fleettracker.models.auth


import com.google.gson.annotations.SerializedName

data class AuthRequestModel(
    @SerializedName("userName")
    var userName: String? = "",
    @SerializedName("name")
    var name: String? = "",
    @SerializedName("oid")
    var oid: String? = "",
    @SerializedName("password")
    var password: String? = "default"

)