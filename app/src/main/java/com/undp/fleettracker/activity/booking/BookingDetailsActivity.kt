package com.undp.fleettracker.activity.booking

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.undp.fleettracker.R
import com.undp.fleettracker.business.BookingsManager
import com.undp.fleettracker.callbacks.HttpResponseCallback
import com.undp.fleettracker.constants.*
import com.undp.fleettracker.models.booking.GetBookingsDetails
import com.undp.fleettracker.util.AppUtil
import com.undp.fleettracker.util.DirectionsJSONParser
import kotlinx.android.synthetic.main.content_booking_details.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response


class BookingDetailsActivity : AppCompatActivity(), OnMapReadyCallback, HttpResponseCallback {

    private val TAG = BookingDetailsActivity::class.java.simpleName
    private val LOCATION_PERMISSION_REQUEST_CODE: Int = 1

    var getBookingsDetails: GetBookingsDetails? = null

    private var googleMap: GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_details)
        setDataFromIntent()
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.title = "Booking Details"

        var mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment

        setUpMap()
        mapFragment.getMapAsync(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_booking_details, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_edit_booking -> {
                return true
            }
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun setDataFromIntent() {
        if (!intent.hasExtra(EXTRA_MY_BOOKING_DETAILS)) {
            return
        }
        getBookingsDetails = intent.getParcelableExtra<GetBookingsDetails>(
            EXTRA_MY_BOOKING_DETAILS
        )
        if (null == getBookingsDetails) {
            return
        }

        txtViewService.text = BookingType.values()[getBookingsDetails!!.BookingType].dispName
        var tripText =
            "${TripCategory.values()[getBookingsDetails!!.TripCategory].dispName} - ${TripType.values()[getBookingsDetails!!.TripType].dispName}"
        txtViewTrip.text = tripText
        txtStartDate.text =
            AppUtil.convertEpochSecondsToTimeString(getBookingsDetails!!.StartDateTimeEpoch)
        txtEndDate.text =
            AppUtil.convertEpochSecondsToTimeString(getBookingsDetails!!.EndDateTimeEpoch)
        if (getBookingsDetails!!.TripLocationsArr.size >= 2) {
            txtDropLocation?.text = getBookingsDetails!!.TripLocationsArr[1].formattedAddress
            txtPickupLcation?.text = getBookingsDetails!!.TripLocationsArr[0].formattedAddress
        }

        txtVehicleNo.text = getBookingsDetails!!.BookingVehicleInfo.VehicleName?.toString()
        txtLuggage.text = getBookingsDetails!!.Luggage.toString()
        txtPassengers.text = getBookingsDetails!!.NoOfPassengers.toString()
        txtUserComments.text = getBookingsDetails!!.UserComments?.toString()
        txtApproverComments.text = getBookingsDetails!!.ApproverComments?.toString()

    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        this.googleMap = p0
//        plotRouteOnMap(getBookingsDetails?.TripLocationsArr)
        sendGetRouteDirectionsRequest()
    }

    private fun sendGetRouteDirectionsRequest() {
        if (null == getBookingsDetails) {
            return
        }

        if (null == getBookingsDetails?.TripLocationsArr) {
            return
        }

        if (getBookingsDetails!!.TripLocationsArr.size >= 2) {
            var start: LatLng = LatLng(
                getBookingsDetails!!.TripLocationsArr[0].lat,
                getBookingsDetails!!.TripLocationsArr[0].lng
            )
            var end: LatLng = LatLng(
                getBookingsDetails!!.TripLocationsArr[1].lat,
                getBookingsDetails!!.TripLocationsArr[1].lng
            )
            BookingsManager.instance.getMapDirections(
                HttpRequests.GET_MAP_DIRECTIONS,
                start,
                end,
                this
            )
        }
    }

//    private fun plotRouteOnMap(tripLocationArr: List<TripLocationsArr>?) {
//
//        if (null == tripLocationArr) {
//            return
//        }
//
//        val points = ArrayList<LatLng>()
//        val builder = LatLngBounds.Builder()
//        googleMap?.clear()
//        val patternItem = listOf<PatternItem>(Dash(20F), Gap(20F))
//        var oldLocation: LatLng? = null
//        for (tripLocation in tripLocationArr) {
//
//            val latlong = LatLng(tripLocation.lat!!, tripLocation.lng!!)
//            if (null == oldLocation) {
//                oldLocation = latlong
//            }
//            points.add(latlong)
//            builder.include(latlong)
////            if (tripLocation.speed!! < speedLimit) {
//            val polyLineOption = PolylineOptions()
//            polyLineOption.width(12F)
//            polyLineOption.geodesic(true)
//            polyLineOption.jointType(JointType.ROUND)
//            polyLineOption.startCap(RoundCap())
//            polyLineOption.endCap(RoundCap())
//            polyLineOption.add(oldLocation).add(latlong)
//                .color(ContextCompat.getColor(this, R.color.app_color))
//
////                polyLineOption.pattern(patternItem)
//            googleMap?.addPolyline(polyLineOption)
////            } else {
////                val polyLineOption = PolylineOptions()
////                polyLineOption.width(12F)
////                polyLineOption.geodesic(true)
////                polyLineOption.jointType(JointType.ROUND)
////                polyLineOption.startCap(RoundCap())
////                polyLineOption.endCap(RoundCap())
////                polyLineOption.add(oldLocation).add(latlong)
////                    .color(ContextCompat.getColor(this, R.color.color_scarlet))
//////                polyLineOption.pattern(patternItem)
////                googleMap.addPolyline(polyLineOption)
////            }
//
//            oldLocation = latlong
//        }
////        val patternItem = listOf<PatternItem>(Dash(20F), Gap(20F))
////        polyLineOption.pattern(patternItem)
////
////        polyLineOption.addAll(points)
////        polyLineOption.color(ContextCompat.getColor(this, R.color.app_color))
//
//
//        val bounds = builder.build()
//
//        val origin = points[0]
//        val dest = points[points.size - 1]
//        googleMap?.addMarker(
//            MarkerOptions().position(origin).title("Start point")
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car))
//        )
//        googleMap?.addMarker(MarkerOptions().position(dest).title("End point"))
////        googleMap.addPolyline(polyLineOption)
//        googleMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
////        googleMap.animateCamera(CameraUpdateFactory.zoomTo(11F));
//    }

    private fun plotRouteOnMap(directions: List<LatLng>?) {

        if (null == directions || directions.size == 0) {
            return
        }

        val points = ArrayList<LatLng>()
        val builder = LatLngBounds.Builder()
        googleMap?.clear()
        val patternItem = listOf<PatternItem>(Dash(20F), Gap(20F))
        var oldLocation: LatLng? = null
        for (latlong in directions) {

//            val latlong = LatLng(tripLocation.lat!!, tripLocation.lng!!)
            if (null == oldLocation) {
                oldLocation = latlong
            }
            points.add(latlong)
            builder.include(latlong)
//            if (tripLocation.speed!! < speedLimit) {
            val polyLineOption = PolylineOptions()
            polyLineOption.width(12F)
            polyLineOption.geodesic(true)
            polyLineOption.jointType(JointType.ROUND)
            polyLineOption.startCap(RoundCap())
            polyLineOption.endCap(RoundCap())
            polyLineOption.add(oldLocation).add(latlong)
                .color(ContextCompat.getColor(this, R.color.app_color))

//                polyLineOption.pattern(patternItem)
            googleMap?.addPolyline(polyLineOption)
//            } else {
//                val polyLineOption = PolylineOptions()
//                polyLineOption.width(12F)
//                polyLineOption.geodesic(true)
//                polyLineOption.jointType(JointType.ROUND)
//                polyLineOption.startCap(RoundCap())
//                polyLineOption.endCap(RoundCap())
//                polyLineOption.add(oldLocation).add(latlong)
//                    .color(ContextCompat.getColor(this, R.color.color_scarlet))
////                polyLineOption.pattern(patternItem)
//                googleMap.addPolyline(polyLineOption)
//            }

            oldLocation = latlong
        }
//        val patternItem = listOf<PatternItem>(Dash(20F), Gap(20F))
//        polyLineOption.pattern(patternItem)
//
//        polyLineOption.addAll(points)
//        polyLineOption.color(ContextCompat.getColor(this, R.color.app_color))


        val bounds = builder.build()

        val origin = points[0]
        val dest = points[points.size - 1]
        googleMap?.addMarker(
            MarkerOptions().position(origin).title("Start point")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car))
        )
        googleMap?.addMarker(MarkerOptions().position(dest).title("End point"))
//        googleMap.addPolyline(polyLineOption)
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
//        googleMap.animateCamera(CameraUpdateFactory.zoomTo(11F));
    }

    override fun onResponse(
        httpRequests: HttpRequests,
        requestStatus: RequestStatus,
        response: Any?,
        throwable: Throwable?
    ) {
        when (httpRequests) {
            HttpRequests.GET_MAP_DIRECTIONS -> {
                if (requestStatus == RequestStatus.SUCCESS) {
                    val responseModel: Response<ResponseBody> =
                        response as Response<ResponseBody>
                    if (null == responseModel) {
                        Log.d(TAG, "GET_MAP_DIRECTIONS response null")
                        return
                    }

//                    val jObject: JSONObject
//                    var routes: List<List<HashMap?>?>? = null
//
//                    try {
//                        jObject = JSONObject(jsonData.get(0))
//                        val parser = DirectionsJSONParser()
//                        routes = parser.parse(jObject)
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }

//                    var inputStream: InputStream? = responseModel.body()?.byteStream()
//                    if (null == inputStream) {
//                        return
//                    }
//                    val builder: DocumentBuilder =
//                        DocumentBuilderFactory.newInstance().newDocumentBuilder()
//                    var document = builder.parse(inputStream)
//                    var directions = GMapV2Direction().getDirection(document!!)
//                    plotRouteOnMap(directions)
                }
            }
        }
    }
}
