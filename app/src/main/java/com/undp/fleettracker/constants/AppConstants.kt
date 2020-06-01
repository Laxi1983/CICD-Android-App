package com.undp.fleettracker.constants

import com.undp.fleettracker.models.auth.UserModel
import com.undp.fleettracker.models.fleet.FleetModel

/**
 * @author: << sandip.mahajan >>
 */
const val AUTHORIZATION = "Authorization"
var TENANT_ID = 0
var userModel:UserModel? = null
var BEARER_TOKEN = ""
var fleetList = arrayListOf<FleetModel>()

const val APP_DATE_FORMAT = "MM/dd/yyyy"
const val REQUEST_DATE_FORMAT = "yyyy-MM-dd"
const val START_TIME = "00:00:00"
const val END_TIME = "23:59:59"
const val SHARED_PREFERENCE_USER_NAME = "USER_NAME"
const val BOOKING_DATE_FORMAT = "dd-MM-yyyy hh:mm:ss"

enum class ApprovalStatus {
    All,
    Pending,
    Approved,
    Rejected,
    Cancelled
}

enum class BookingType(val dispName: String) {
    NONE("NA"),
    BookVehicle("Book Vehicle"),
    SelfDrive("Self Drive"),
    Shuttle("Shuttle")

}

enum class TripType(val dispName: String) {
    ONE_WAY("One Way"),
    ROUND_TRIP("Round Trip")
}

enum class TripCategory(val dispName: String) {
    OFFICIAL("Official"),
    PRIVATE("Private")
}

enum class HttpRequests {
    GET_TENANT_VEHICLE_FLEET_LIST,
    GET_USER_BOOKINGS,
    GET_MAP_DIRECTIONS
}

enum class RequestStatus {
    SUCCESS,
    ERROR
}

const val EXTRA_MY_BOOKING_DETAILS = "EXTRA_MY_BOOKING_DETAILS"