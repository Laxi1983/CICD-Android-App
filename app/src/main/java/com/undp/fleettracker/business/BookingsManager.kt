package com.undp.fleettracker.business

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.undp.fleettracker.callbacks.HttpResponseCallback
import com.undp.fleettracker.constants.BEARER_TOKEN
import com.undp.fleettracker.constants.HttpRequests
import com.undp.fleettracker.constants.RequestStatus
import com.undp.fleettracker.di.module.NetworkModule
import com.undp.fleettracker.models.booking.*
import com.undp.fleettracker.network.api.booking.BookingAPI
import com.undp.fleettracker.util.GMapV2Direction
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookingsManager private constructor() {

    private object HOLDER {
        var INSTANCE = BookingsManager()
    }

    companion object {
        val instance: BookingsManager by lazy { HOLDER.INSTANCE }
    }

    private var TAG = BookingsManager::class.simpleName

    fun getUserBookings(
        httpRequests: HttpRequests,
        getBookingRequestModel: GetBookingRequestModel,
        httpResponseCallback: HttpResponseCallback
    ) {


        try {
            val service = NetworkModule.provideRetrofitInstance().create(BookingAPI::class.java)

            val call = service.getUserBookings(BEARER_TOKEN, getBookingRequestModel)

            call.enqueue(object : retrofit2.Callback<GetBookingsResponseModel> {
                override fun onFailure(call: Call<GetBookingsResponseModel>, t: Throwable) {
                    Log.d(TAG, "GetBookingsResponseModel Response fail:" + t.message)
                    httpResponseCallback.onResponse(httpRequests, RequestStatus.ERROR, null, t)
                }

                override fun onResponse(
                    call: Call<GetBookingsResponseModel>,
                    response: Response<GetBookingsResponseModel>
                ) {
                    Log.d(TAG, "getUserBooking request success $response")
                    httpResponseCallback.onResponse(
                        httpRequests,
                        RequestStatus.SUCCESS,
                        response,
                        null
                    );

                }

            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //return getBookingResponseModel
    }

    fun getMapDirections(
        httpRequests: HttpRequests,
        start: LatLng,
        end: LatLng,
        httpResponseCallback: HttpResponseCallback
    ) {
        try {
            val gMapV2Direction: GMapV2Direction = GMapV2Direction()
//            var url: String =
//                gMapV2Direction.getDocumentUrl(start, end, gMapV2Direction.MODE_DRIVING)

            val url: String =
                gMapV2Direction.getDirectionsUrl(start, end)

            val service = NetworkModule.provideRetrofitInstance().create(BookingAPI::class.java)

            val call = service.getMapDirection(url)

            call.enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d(TAG, "getMapDirections Response fail:" + t.message)
                    httpResponseCallback.onResponse(httpRequests, RequestStatus.ERROR, null, t)
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val data = response.body()
                    Log.d(TAG, "getMapDirections request success $data")
                    httpResponseCallback.onResponse(
                        httpRequests,
                        RequestStatus.SUCCESS,
                        response,
                        null
                    );
                }

            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getVehicleBookingSummary(
        httpRequests: HttpRequests,
        getBookingRequestModel: GetBookingRequestModel,
        httpResponseCallback: HttpResponseCallback
    ) {

        val service = NetworkModule.provideRetrofitInstance().create(BookingAPI::class.java)

        val call =
            service.getVehicleBookingSummary(BEARER_TOKEN, getBookingRequestModel)

        call.enqueue(object : Callback<GetBookingSummaryResponseModel> {
            override fun onFailure(call: Call<GetBookingSummaryResponseModel>, t: Throwable) {
                Log.d(TAG, "getVehicleBookingSummary Response fail:" + t.message)
                httpResponseCallback.onResponse(httpRequests, RequestStatus.ERROR, null, t)
            }

            override fun onResponse(
                call: Call<GetBookingSummaryResponseModel>,
                response: Response<GetBookingSummaryResponseModel>
            ) {
                Log.d(TAG, "getVehicleBookingSummary request success $response")
                httpResponseCallback.onResponse(
                    httpRequests,
                    RequestStatus.SUCCESS,
                    response,
                    null
                )

            }

        })
    }

    fun getSpecificBookingStatus(
        httpRequests: HttpRequests,
        getBookingRequestModel: GetBookingRequestModel,
        httpResponseCallback: HttpResponseCallback
    ) {

        val service = NetworkModule.provideRetrofitInstance().create(BookingAPI::class.java)

        val call = service.getSpecificBookingStatus(BEARER_TOKEN, getBookingRequestModel)

        call.enqueue(object : Callback<GetSpecificBookingStatusResponseModel> {
            override fun onFailure(
                call: Call<GetSpecificBookingStatusResponseModel>,
                t: Throwable
            ) {
                Log.d(TAG, "getSpecificBookingStatus Response fail:" + t.message)
                httpResponseCallback.onResponse(httpRequests, RequestStatus.ERROR, null, t)
            }

            override fun onResponse(
                call: Call<GetSpecificBookingStatusResponseModel>,
                response: Response<GetSpecificBookingStatusResponseModel>
            ) {
                Log.d(TAG, "getSpecificBookingStatus request success $response")
                httpResponseCallback.onResponse(
                    httpRequests,
                    RequestStatus.SUCCESS,
                    response,
                    null
                )

            }

        })
    }

    fun getTodaysSpecificBookingStatus(
        httpRequests: HttpRequests,
        getBookingRequestModel: GetBookingRequestModel,
        httpResponseCallback: HttpResponseCallback
    ) {

        val service = NetworkModule.provideRetrofitInstance().create(BookingAPI::class.java)

        val call = service.getTodaysSpecificBookingStatus(BEARER_TOKEN, getBookingRequestModel)

        call.enqueue(object : Callback<GetSpecificBookingStatusResponseModel> {
            override fun onFailure(
                call: Call<GetSpecificBookingStatusResponseModel>,
                t: Throwable
            ) {
                Log.d(TAG, "getTodaysSpecificBookingStatus Response fail:" + t.message)
                httpResponseCallback.onResponse(httpRequests, RequestStatus.ERROR, null, t)
            }

            override fun onResponse(
                call: Call<GetSpecificBookingStatusResponseModel>,
                response: Response<GetSpecificBookingStatusResponseModel>
            ) {
                Log.d(TAG, "getTodaysSpecificBookingStatus request success $response")
                httpResponseCallback.onResponse(
                    httpRequests,
                    RequestStatus.SUCCESS,
                    response,
                    null
                )

            }

        })
    }

    fun getBookedVehicleCount(
        httpRequests: HttpRequests,
        getBookingRequestModel: GetBookingRequestModel,
        httpResponseCallback: HttpResponseCallback
    ) {
        val service = NetworkModule.provideRetrofitInstance().create(BookingAPI::class.java)

        val call = service.getBookedVehicleCount(BEARER_TOKEN, getBookingRequestModel)

        call.enqueue(object : Callback<GetBookedVehicleCountResponseModel> {
            override fun onFailure(call: Call<GetBookedVehicleCountResponseModel>, t: Throwable) {
                Log.d(TAG, "getBookedVehicleCount Response fail:" + t.message)
                httpResponseCallback.onResponse(httpRequests, RequestStatus.ERROR, null, t)
            }

            override fun onResponse(
                call: Call<GetBookedVehicleCountResponseModel>,
                response: Response<GetBookedVehicleCountResponseModel>
            ) {
                Log.d(TAG, "getBookedVehicleCount request success $response")
                httpResponseCallback.onResponse(
                    httpRequests,
                    RequestStatus.SUCCESS,
                    response,
                    null
                )
            }
        })
    }

    fun getVehicleOccupancySummary(
        httpRequests: HttpRequests,
        getBookingRequestModel: GetBookingRequestModel,
        httpResponseCallback: HttpResponseCallback
    ) {
        val service = NetworkModule.provideRetrofitInstance().create(BookingAPI::class.java)

        val call = service.getVehicleOccupancySummary(BEARER_TOKEN,getBookingRequestModel)

        call.enqueue(object : Callback<VehicleOccupancySummaryResponseModel>{
            override fun onFailure(call: Call<VehicleOccupancySummaryResponseModel>, t: Throwable) {
                Log.d(TAG, "getVehicleOccupancySummary Response fail:" + t.message)
                httpResponseCallback.onResponse(httpRequests, RequestStatus.ERROR, null, t)
            }

            override fun onResponse(
                call: Call<VehicleOccupancySummaryResponseModel>,
                response: Response<VehicleOccupancySummaryResponseModel>
            ) {
                Log.d(TAG, "getVehicleOccupancySummary request success $response")
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