package com.undp.fleettracker.network

import android.util.Log
import com.undp.fleettracker.constants.BEARER_TOKEN
import com.undp.fleettracker.di.module.NetworkModule
import com.undp.fleettracker.models.vehicle.VehicleDetailsResponse
import com.undp.fleettracker.models.vehicle.VehicleRequestModel
import com.undp.fleettracker.network.api.vehicle.VehicleAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VehicleManager {

    interface IVehicleResponse {
        fun onResponse(data: VehicleDetailsResponse)
        fun onFailure(data: String)
    }

    val TAG = VehicleManager::class.java.simpleName
    var iResponse: IVehicleResponse? = null

    fun getVehicleListForFleet(fleetID: Int, tenantId: Int) {
        val service = NetworkModule.provideRetrofitInstance().create(VehicleAPI::class.java)
        val vehicleRequestModel = VehicleRequestModel()
        vehicleRequestModel.fleetId = fleetID
        vehicleRequestModel.tenantId = tenantId
        vehicleRequestModel.pageNo = 1
        vehicleRequestModel.pageSize = 20
        vehicleRequestModel.sortColumnName = "index"
        vehicleRequestModel.sortType = "ASC"
        vehicleRequestModel.startRecordNo = "0"
        vehicleRequestModel.searchKey = ""

        val call = service.getVehicleListByFilter(BEARER_TOKEN, vehicleRequestModel)
        call.enqueue(object : Callback<VehicleDetailsResponse> {
            override fun onFailure(call: Call<VehicleDetailsResponse>, t: Throwable) {
                Log.d(TAG, "Response Failure::" + t.message)
                iResponse?.onFailure(t.localizedMessage!!)
            }

            override fun onResponse(
                call: Call<VehicleDetailsResponse>,
                response: Response<VehicleDetailsResponse>
            ) {
                Log.d(TAG, "Response Success:$response")
                if (response.code() == 200) {
                    val data = response.body()
                    Log.d(TAG, "Response object: $data")
                    if (data != null) {
                        iResponse?.onResponse(data)
                    }
                }
            }
        })
    }


}
