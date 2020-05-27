package com.undp.fleettracker.network.api.vehicle

import com.undp.fleettracker.constants.AUTHORIZATION
import com.undp.fleettracker.constants.GET_VEHICLE_LIST_BY_FILTER
import com.undp.fleettracker.models.vehicle.VehicleDetailsResponse
import com.undp.fleettracker.models.vehicle.VehicleRequestModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * @author: << sandip.mahajan >>
 */

interface VehicleAPI {

    @POST(GET_VEHICLE_LIST_BY_FILTER)
    fun getVehicleListByFilter(
        @Header(AUTHORIZATION) token: String,
        @Body vehicleRequestModel: VehicleRequestModel
    ): Call<VehicleDetailsResponse>
}