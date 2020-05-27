package com.undp.fleettracker.network.api.fleet

import com.undp.fleettracker.constants.AUTHORIZATION
import com.undp.fleettracker.constants.FLEET_OWNER_FLEET_LIST
import com.undp.fleettracker.models.fleet.FleetDetailModel
import com.undp.fleettracker.models.fleet.FleetRequestModel
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

}