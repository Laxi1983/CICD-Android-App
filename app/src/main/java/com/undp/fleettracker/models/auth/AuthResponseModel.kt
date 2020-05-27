package com.undp.fleettracker.models.auth


import com.google.gson.annotations.SerializedName

data class AuthResponseModel(
    @SerializedName("data")
    var `data`: UserModel? = null,
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("status")
    var status: Int? = 0
)