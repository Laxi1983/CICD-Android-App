package com.undp.fleettracker.business

import android.util.Log
import com.undp.fleettracker.callbacks.HttpResponseCallback
import com.undp.fleettracker.constants.BEARER_TOKEN
import com.undp.fleettracker.constants.HttpRequests
import com.undp.fleettracker.constants.RequestStatus
import com.undp.fleettracker.di.module.NetworkModule
import com.undp.fleettracker.models.fleet.FleetDetailModel
import com.undp.fleettracker.models.fleet.FleetRequestModel
import com.undp.fleettracker.models.fleet.GetFleetVehicleResponseModel
import com.undp.fleettracker.network.api.fleet.FleetAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FleetManager private constructor() {

    private object HOLDER {
        var INSTANCE = FleetManager()
    }

    companion object {
        val instance: FleetManager by lazy { HOLDER.INSTANCE }
    }

    private val TAG = FleetManager::class.java.simpleName
    fun getTenantVehicleFleetList(
        httpRequests: HttpRequests,
        tenantId: Int,
        httpResponseCallback: HttpResponseCallback
    ) {
        try {
            val service = NetworkModule.provideRetrofitInstance().create(FleetAPI::class.java)

            var fleetRequestModel: FleetRequestModel = FleetRequestModel()
            fleetRequestModel.tenantId = tenantId
            val call = service.getTenantVehicleFleetList(BEARER_TOKEN, fleetRequestModel)

            call.enqueue(object : Callback<FleetDetailModel> {
                override fun onFailure(call: Call<FleetDetailModel>, t: Throwable) {
                    Log.d(TAG, "getTenantVehicleFleetList Failure")
                    httpResponseCallback.onResponse(httpRequests, RequestStatus.ERROR, null, t)
                }

                override fun onResponse(
                    call: Call<FleetDetailModel>,
                    response: Response<FleetDetailModel>
                ) {
                    Log.d(TAG, "getTenantVehicleFleetList success ${response.code()}")
                    httpResponseCallback.onResponse(
                        httpRequests,
                        RequestStatus.SUCCESS,
                        response,
                        null
                    )

                }

            })
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun getFleetVehicle(
        httpRequests: HttpRequests,
        fleetRequestModel: FleetRequestModel,
        httpResponseCallback: HttpResponseCallback
    ) {
        val service = NetworkModule.provideRetrofitInstance().create(FleetAPI::class.java)

        val call = service.getFleetVehicle(BEARER_TOKEN,fleetRequestModel)

        call.enqueue(object : Callback<GetFleetVehicleResponseModel>{
            override fun onFailure(call: Call<GetFleetVehicleResponseModel>, t: Throwable) {
                Log.d(TAG, "getFleetVehicle Failure")
                httpResponseCallback.onResponse(httpRequests, RequestStatus.ERROR, null, t)
            }

            override fun onResponse(
                call: Call<GetFleetVehicleResponseModel>,
                response: Response<GetFleetVehicleResponseModel>
            ) {
                Log.d(TAG, "getFleetVehicle success ${response.code()}")
                httpResponseCallback.onResponse(
                    httpRequests,
                    RequestStatus.SUCCESS,
                    response,
                    null
                )
            }
        })
    }
}