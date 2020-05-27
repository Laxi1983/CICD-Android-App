package com.undp.fleettracker.constants

/**
 * @author: << sandip.mahajan >>
 */

// User
const val LOGIN = "api/user/signin"
const val GET_USERS = "api/user/users"

//Fleet
const val FLEET_OWNER_FLEET_LIST = "api/fleet/getFleetOwnerFleetList"
const val GET_TENANT_VEHICLE_FLEET_LIST = "api/fleet/getTenantVehicleFleetList"
const val GET_VEHICLE_LIST_OF_FLEET = "api/fleet/getVehicleListOfFleet"
const val GET_FLEET_VEHICLE = "api/fleet/getFleetVehicle"
const val GET_TENANT_ADMIN_FLEET_LIST = "api/fleet/getTenantAdminFleetList"

//Vehicle
const val GET_VEHICLE_LIST_BY_FILTER = "api/vehicle/getVehicleListByFilter"
const val GET_PENDING_APPROVALS = "api/vehicle/pendingApprovals"
const val GET_BOOKING_STATUS_COUNT = "api/vehicle/getBookingStatusCount"
const val GET_BOOKING_SUMMARY = "api/vehicle/bookingSummary"
const val GET_VEHICLE_DATA_ASSOCIATED_TO_DRIVER = "api/vehicle/getVehicleDataAssociatedToDriver"
const val GET_TODAYS_BOOKING_STATUS_COUNT = "api/vehicle/getTodaysBookingStatusCount"
const val GET_BOOKED_VEHICLE_COUNT = "api/vehicle/getBookedVehicleCount"
const val GET_SPECIFIC_BOOKING_STATUS = "api/vehicle/getSpecificBookingStatus"
const val GET_VEHICLE_BOOKING_SUMMARY = "api/vehicle/getVehicleBookingSummary"

//User Bookings
const val GET_USER_BOOKINGS = "api/vehicle/getLoggedInUserBookings"

//Report
const val GET_UTILIZATION_REPORT_DATA = "api/report/getUtilizationReportData"
const val GET_SAFETY_REPORT_DATA = "api/report/getSafetyReportData"
const val GET_VEHICLE_HEALTH_DATA = "api/report/getVehicleHealthData"
const val GET_POWER_BI_REPORTS_INFO = "api/report/getPowerBIReportsInfo"

//Tenant
const val GET_TENANT_LIST = "api/tenant/getTenantList"

//Dashboard
const val GET_SA_DASHBOARD_SUMMARY = "api/dashboard/getSADashboardSummary"

//Trip
const val GET_TRIP_LIST_BY_FILTER = "api/trip/getTripListByFilter"

//Route
const val GET_ROUTE_LIST = "api/route/getRouteList"
const val DELETE_ROUTE = "api/route/deleteRoute"

//Notifications/Alerts
const val GET_GEOFENCE_ALERTS = "/api/geofence/alerts"