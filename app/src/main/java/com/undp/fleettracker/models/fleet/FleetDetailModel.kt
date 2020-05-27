package com.undp.fleettracker.models.fleet

data class FleetDetailModel(
    val `data`: ArrayList<FleetModel>,
    val filtered: Int,
    val message: String,
    val status: Int,
    val total: Int
)