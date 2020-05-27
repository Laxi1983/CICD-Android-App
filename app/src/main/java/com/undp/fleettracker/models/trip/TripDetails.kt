package com.undp.fleettracker.models.trip


import com.google.gson.annotations.SerializedName

data class TripDetails(
    @SerializedName("COMM_Connected")
    var COMMConnected: Int? = 0,
    @SerializedName("DTC_Faults")
    var DTCFaults: Int? = 0,
    @SerializedName("Date")
    var date: Any? = Any(),
    @SerializedName("Device_on_External_Battery")
    var deviceOnExternalBattery: Int? = 0,
    @SerializedName("Device_on_Internal_BackUp_Battery")
    var deviceOnInternalBackUpBattery: Int? = 0,
    @SerializedName("Device_WakeUp_or_PowerUp")
    var deviceWakeUpOrPowerUp: Int? = 0,
    @SerializedName("EndLocalTime")
    var endLocalTime: String? = "",
    @SerializedName("EndTimeEpoch")
    var endTimeEpoch: Int? = 0,
    @SerializedName("FaultCount")
    var faultCount: Int? = 0,
    @SerializedName("FleetId")
    var fleetId: Int? = 0,
    @SerializedName("FuelDifference")
    var fuelDifference: Double? = 0.0,
    @SerializedName("GPS_Quality_Fix_Acquired")
    var GPSQualityFixAcquired: Int? = 0,
    @SerializedName("Geofence_IN")
    var geofenceIN: Int? = 0,
    @SerializedName("Geofence_OUT")
    var geofenceOUT: Int? = 0,
    @SerializedName("Harsh_Acceleration")
    var harshAcceleration: Int? = 0,
    @SerializedName("Harsh_Braking")
    var harshBraking: Int? = 0,
    @SerializedName("Harsh_Left")
    var harshLeft: Int? = 0,
    @SerializedName("Harsh_Right")
    var harshRight: Int? = 0,
    @SerializedName("Ignition_OFF")
    var ignitionOFF: Int? = 0,
    @SerializedName("Ignition_OFF_10s")
    var ignitionOFF10s: Int? = 0,
    @SerializedName("Ignition_OFF_1Hr")
    var ignitionOFF1Hr: Int? = 0,
    @SerializedName("Ignition_OFF_1day")
    var ignitionOFF1day: Int? = 0,
    @SerializedName("Ignition_ON")
    var ignitionON: Int? = 0,
    @SerializedName("Ignition_ON_1500_meters_travelled")
    var ignitionON1500MetersTravelled: Int? = 0,
    @SerializedName("Ignition_ON_180s")
    var ignitionON180s: Int? = 0,
    @SerializedName("Ignition_ON_Heading_change_of_more_than_5_degrees")
    var ignitionONHeadingChangeOfMoreThan5Degrees: Int? = 0,
    @SerializedName("Possible_Accident")
    var possibleAccident: Int? = 0,
    @SerializedName("StartLocalTime")
    var startLocalTime: String? = "",
    @SerializedName("StartTimeEpoch")
    var startTimeEpoch: Int? = 0,
    @SerializedName("TenantId")
    var tenantId: Int? = 0,
    @SerializedName("TimezoneDate")
    var timezoneDate: Any? = Any(),
    @SerializedName("Tow_Event_Detected")
    var towEventDetected: Int? = 0,
    @SerializedName("TripDistance")
    var tripDistance: Double? = 0.0,
    @SerializedName("TripLocations")
    var tripLocations: List<TripLocation>? = listOf(),
    @SerializedName("TripName")
    var tripName: String? = "",
    @SerializedName("TripStatus")
    var tripStatus: Int? = 0,
    @SerializedName("VehicleId")
    var vehicleId: Int? = 0,
    @SerializedName("VehicleName")
    var vehicleName: Any? = Any()
)