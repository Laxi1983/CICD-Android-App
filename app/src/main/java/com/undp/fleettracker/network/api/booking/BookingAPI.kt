package com.undp.fleettracker.network.api.booking

import com.undp.fleettracker.constants.*
import com.undp.fleettracker.models.booking.*
import okhttp3.ResponseBody
import org.w3c.dom.Document
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url
import java.io.InputStream

/**
 * @author: << sandip.mahajan >>
 */

interface BookingAPI {

    @POST(GET_USER_BOOKINGS)
    fun getUserBookings(
        @Header(AUTHORIZATION) token: String,
        @Body getBookingModel: GetBookingRequestModel
    ): Call<GetBookingsResponseModel>

    @POST
    fun getMapDirection(@Url url: String): Call<ResponseBody>

    @POST(GET_VEHICLE_BOOKING_SUMMARY)
    fun getVehicleBookingSummary(
        @Header(AUTHORIZATION) token: String,
        @Body getBookingModel: GetBookingRequestModel
    ): Call<GetBookingSummaryResponseModel>

    @POST(GET_SPECIFIC_BOOKING_STATUS)
    fun getSpecificBookingStatus(
        @Header(AUTHORIZATION) token: String,
        @Body getBookingModel: GetBookingRequestModel
    ): Call<GetSpecificBookingStatusResponseModel>

    @POST(GET_BOOKED_VEHICLE_COUNT)
    fun getBookedVehicleCount(
        @Header(AUTHORIZATION) token: String,
        @Body getBookingModel: GetBookingRequestModel
    ): Call<GetBookedVehicleCountResponseModel>


    @POST(GET_TODAYS_BOOKING_STATUS_COUNT)
    fun getTodaysSpecificBookingStatus(
        @Header(AUTHORIZATION) token: String,
        @Body getBookingModel: GetBookingRequestModel
    ): Call<GetSpecificBookingStatusResponseModel>

    @POST(GET_BOOKING_SUMMARY)
    fun getVehicleOccupancySummary(
        @Header(AUTHORIZATION) token: String,
        @Body getBookingModel: GetBookingRequestModel
    ): Call<VehicleOccupancySummaryResponseModel>

}