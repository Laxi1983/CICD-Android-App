package com.undp.fleettracker.network.api.fleet

import com.undp.fleettracker.constants.AUTHORIZATION
import com.undp.fleettracker.constants.FLEET_OWNER_FLEET_LIST
import com.undp.fleettracker.constants.GET_FLEET_VEHICLE
import com.undp.fleettracker.constants.GET_TENANT_VEHICLE_FLEET_LIST
import com.undp.fleettracker.models.fleet.FleetDetailModel
import com.undp.fleettracker.models.fleet.FleetRequestModel
import com.undp.fleettracker.models.fleet.GetFleetVehicleModel
import com.undp.fleettracker.models.fleet.GetFleetVehicleResponseModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


/**
 * @author: << sandip.mahajan >>
 */

public interface FleetAPI {

    @POST(FLEET_OWNER_FLEET_LIST)
    fun getFleetOwnerFleetList(
        @Header(AUTHORIZATION) token: String,
        @Body fleetRequestModel: FleetRequestModel
    ): Call<FleetDetailModel>

    @POST(GET_TENANT_VEHICLE_FLEET_LIST)
    fun getTenantVehicleFleetList(
        @Header(AUTHORIZATION) token: String,
        @Body fleetRequestModel: FleetRequestModel
    ): Call<FleetDetailModel>

    @POST(GET_FLEET_VEHICLE)
    fun getFleetVehicle(
        @Header(AUTHORIZATION) taken: String,
        @Body fleetRequestModel: FleetRequestModel
    ) : Call<GetFleetVehicleResponseModel>

}