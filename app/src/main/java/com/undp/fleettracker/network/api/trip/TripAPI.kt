package com.undp.fleettracker.network.api.trip

import com.undp.fleettracker.constants.AUTHORIZATION
import com.undp.fleettracker.constants.GET_TRIP_LIST_BY_FILTER
import com.undp.fleettracker.models.trip.TripRequestModel
import com.undp.fleettracker.models.trip.TripResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface TripAPI {

    @POST(GET_TRIP_LIST_BY_FILTER)
    fun getTripList(
        @Header(AUTHORIZATION) token: String,
        @Body tripRequestModel: TripRequestModel
    ): Call<TripResponse>
}