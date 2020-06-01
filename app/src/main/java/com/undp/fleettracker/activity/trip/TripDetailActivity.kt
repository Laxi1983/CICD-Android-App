package com.undp.fleettracker.activity.trip

import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.undp.fleettracker.R
import com.undp.fleettracker.adapter.TripInfoAdapter
import com.undp.fleettracker.constants.BEARER_TOKEN
import com.undp.fleettracker.constants.END_TIME
import com.undp.fleettracker.constants.START_TIME
import com.undp.fleettracker.di.module.NetworkModule
import com.undp.fleettracker.models.trip.TripDetails
import com.undp.fleettracker.models.trip.TripRequestModel
import com.undp.fleettracker.models.trip.TripResponse
import com.undp.fleettracker.models.vehicle.VehicleInfo
import com.undp.fleettracker.network.api.trip.TripAPI
import com.undp.fleettracker.util.AppUtil
import com.undp.fleettracker.util.AppUtil.convertToRequestedDate
import kotlinx.android.synthetic.main.content_trip.*
import kotlinx.android.synthetic.main.content_trips_details_graph.*
import kotlinx.android.synthetic.main.content_trips_details_info.*
import kotlinx.android.synthetic.main.content_trips_details_stats.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class TripDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private val TAG = TripDetailActivity::class.java.canonicalName
    private lateinit var googleMap: GoogleMap
    private lateinit var tripResponse: TripResponse
    private lateinit var tripInfoAdapter: TripInfoAdapter
    private lateinit var vehicleInfo: VehicleInfo
    private lateinit var calendar: Calendar

    private var tenantId: Int = 0

    private var lastConnectedDate = ""
    private var lastConnectedRequestDate = ""
    private var speedLimit = 40f
    private var tripDetail: TripDetails? = null

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_detail)
        supportActionBar!!.hide()

        tenantId = intent.getIntExtra("TENANT_ID", 0)
        val lastConnectedEpoch = intent.getStringExtra("LAST_CONNECTED")
        if (!TextUtils.isEmpty(lastConnectedEpoch)) {
            lastConnectedDate = lastConnectedEpoch!!.split(" ")[0]
            lastConnectedRequestDate = convertToRequestedDate(lastConnectedEpoch.split(" ")[0])
        }

        vehicleInfo = intent.getSerializableExtra("VEHICLE_INFO") as VehicleInfo
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as SupportMapFragment
        setUpMap()
        mapFragment.getMapAsync(this)

        initUIElements()
    }

    private fun initUIElements() {
        txtTripName.text = String.format(getString(R.string.label_trip), vehicleInfo.VehicleName)
        txtDate.text = lastConnectedDate
        tripInfoAdapter = TripInfoAdapter(this, ArrayList<TripDetails>())
        smoolider.adapter = tripInfoAdapter

        txtTripDetails.text =
            String.format(getString(R.string.ride_details), 0)

        calendar = Calendar.getInstance()
        calendar.timeInMillis = vehicleInfo.LastConnectedTimeEpoch * 1000L

        fixMapScrolling()
        registerListeners()
    }

    private fun registerListeners() {
        smoolider.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                Log.d(TAG, "Page changes:$position")
                if (null != tripResponse && null != tripResponse.data) {
                    tripDetail = tripResponse.data[position]
                    drawGraphData(tripDetail!!)
                    plotRouteOnMap(tripDetail!!)
                }
            }
        })

        edtSpeedLimit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                try {
                    speedLimit = if (editable!!.isNotEmpty()) {
                        editable.toString().trim().toFloat()
                    } else {
                        40f
                    }
                    drawGraphData(tripDetail!!)
                    plotRouteOnMap(tripDetail!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
//                drawLineLimit()
            }

            override fun beforeTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }

    fun back(view: View) {
        finish()
    }

    fun refresh(view: View) {
//        lastConnectedRequestDate =
//            convertToRequestedDate(lastConnectedDate.split(" ")[0])
//        txtDate.text = lastConnectedDate
        getTripDetails()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap!!
        getTripDetails()
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

    /**
     * Get the Fleet details
     */
    private fun getTripDetails() {
        try {
            AppUtil.showProgress(this)
            val service = NetworkModule.provideRetrofitInstance().create(TripAPI::class.java)

            var tripRequestModel = TripRequestModel()
            if (null == vehicleInfo) {
                return
            }

            tripRequestModel.tenantId = tenantId
            tripRequestModel.vehicleId = vehicleInfo.VehicleId
            tripRequestModel.recordStartDateStr = "$lastConnectedRequestDate $START_TIME"
            tripRequestModel.recordEndDateStr = "$lastConnectedRequestDate $END_TIME"

            val call = service.getTripList(BEARER_TOKEN, tripRequestModel)
            call.enqueue(object : Callback<TripResponse> {
                override fun onFailure(call: Call<TripResponse>, t: Throwable) {
                    AppUtil.hideProgress()
                    Log.d(TAG, "Response Failure::" + t.message)
                    tripResponse = TripResponse()
                }

                override fun onResponse(
                    call: Call<TripResponse>,
                    response: Response<TripResponse>
                ) {
                    AppUtil.hideProgress()
                    Log.d(TAG, "Response Success:" + response)
                    if (response.code() == 200) {
                        val data = response.body()
                        Log.d(TAG, "Response object: $data")
                        tripResponse = response.body()!!

                        runOnUiThread {
                            updateUI()
                        }
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateUI() {

        if (null != tripResponse.data) {

            txtTripDetails.text =
                String.format(getString(R.string.ride_details), tripResponse.data.size)

            tripInfoAdapter = TripInfoAdapter(
                this,
                tripResponse.data as java.util.ArrayList<TripDetails>
            )
            smoolider.adapter = tripInfoAdapter
            smoolider.visibility = View.VISIBLE
            txtErrorMessage.visibility = View.GONE
            tripDetail = tripResponse.data[0]
            plotRouteOnMap(tripDetail = tripDetail!!)
            txtSpeed.visibility = View.VISIBLE
            txtDateTime.visibility = View.VISIBLE
            drawGraphData(tripDetail!!)


        } else {
            txtTripDetails.text =
                String.format(getString(R.string.ride_details), 0)
            smoolider.visibility = View.GONE
            txtErrorMessage.visibility = View.VISIBLE
            txtSpeed.visibility = View.GONE
            txtDateTime.visibility = View.GONE
            googleMap.clear()
            lineChart.clear()
            lineChart.invalidate()
        }
    }

    private fun drawGraphData(tripDetail: TripDetails) {
        val data = ArrayList<Entry>()
        val xAxisLabel = ArrayList<String>()
        var maxY = speedLimit
        var index = 0f
        for (tripLocation in tripDetail.tripLocations!!) {
            if (null != tripLocation.speed) {
                val speed = tripLocation.speed!!.toFloat()
                val entry = Entry(index, speed)
                data.add(entry)
                if (null != tripLocation.recordLocalDateTime) {
                    xAxisLabel.add(tripLocation.recordLocalDateTime!!)
                } else {
                    xAxisLabel.add("")
                }
                if (speed > maxY) {
                    maxY = speed + 20
                }
            }
            index++
        }

        Log.d(TAG, "****** Data size:${data.size} , AxisLabelSize:${xAxisLabel.size}")

        val lineDateSet = LineDataSet(data, "")
        lineDateSet.color = ContextCompat.getColor(this, R.color.app_color)
        lineDateSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        lineDateSet.setDrawValues(false)
        lineDateSet.lineWidth = 2f
        lineDateSet.setCircleColor(ContextCompat.getColor(this, R.color.app_color))
        lineDateSet.circleRadius = 7f
        lineDateSet.circleHoleRadius = 5f

        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLinesBehindData(false)
        val valueFormatter: ValueFormatter = object : ValueFormatter() {
//            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
//                Log.d(TAG, "****** getAxisLabel:${xAxisLabel.size}")
//                if (xAxisLabel.size > 0) {
//                    return xAxisLabel[value.toInt()]
//                }
//                return super.getAxisLabel(value, axis)
//            }

            override fun getFormattedValue(value: Float): String {
                Log.d(TAG, "****** getFormattedValue:${xAxisLabel.size}")
                if (xAxisLabel.size > 0 && xAxisLabel.size >= value) {
                    return xAxisLabel[value.toInt()]
                }
                return super.getFormattedValue(value)
            }
        }

        xAxis.valueFormatter = valueFormatter
        lineChart.axisRight.isEnabled = false
        lineChart.axisLeft.axisMaximum = maxY
        lineChart.axisLeft.axisMinimum = 0f

//        val description = Description()
//        description.text = ""
//        lineChart.description = description
        lineChart.description.isEnabled = false;
        drawLineLimit()
        val lineData = LineData(lineDateSet)
        lineChart.legend.isEnabled = false
        lineChart.data = lineData
        lineChart.data.setHighlightEnabled(false)
        lineChart.animateX(1500)
        lineChart.invalidate()

    }

    private fun drawLineLimit() {
        lineChart.axisLeft.removeAllLimitLines()
        val limitLine = LimitLine(speedLimit, "")
        limitLine.lineColor = ContextCompat.getColor(this, R.color.color_scarlet)
        limitLine.lineWidth = 1.5f
        lineChart.axisLeft.addLimitLine(limitLine)
    }

    fun plotRouteOnMap(tripDetail: TripDetails) {

        val points = ArrayList<LatLng>()
        val builder = LatLngBounds.Builder()
        googleMap.clear()
        var oldLocation: LatLng? = null
        for (tripLocation in tripDetail.tripLocations!!) {

            val latlong = LatLng(tripLocation.latitude!!, tripLocation.longitude!!)
            if (null == oldLocation) {
                oldLocation = latlong
            }
            points.add(latlong)
            builder.include(latlong)
            if (tripLocation.speed!! < speedLimit) {
                val polyLineOption = PolylineOptions()
                polyLineOption.width(12F)
                polyLineOption.geodesic(true)
                polyLineOption.jointType(JointType.ROUND)
                polyLineOption.startCap(RoundCap())
                polyLineOption.endCap(RoundCap())
                polyLineOption.add(oldLocation).add(latlong)
                    .color(ContextCompat.getColor(this, R.color.app_color))

//                polyLineOption.pattern(patternItem)
                googleMap.addPolyline(polyLineOption)
            } else {
                val polyLineOption = PolylineOptions()
                polyLineOption.width(12F)
                polyLineOption.geodesic(true)
                polyLineOption.jointType(JointType.ROUND)
                polyLineOption.startCap(RoundCap())
                polyLineOption.endCap(RoundCap())
                polyLineOption.add(oldLocation).add(latlong)
                    .color(ContextCompat.getColor(this, R.color.color_scarlet))
//                polyLineOption.pattern(patternItem)
                googleMap.addPolyline(polyLineOption)
            }

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
        googleMap.addMarker(
            MarkerOptions().position(origin).title("Start point")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car))
        )
        googleMap.addMarker(MarkerOptions().position(dest).title("End point"))
//        googleMap.addPolyline(polyLineOption)
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
//        googleMap.animateCamera(CameraUpdateFactory.zoomTo(11F));
    }

    /**
     * Initialize map polyline options
     */
    private fun initMapPolylineOptions(points: ArrayList<LatLng>): PolylineOptions {
        val polyLineOption = PolylineOptions()
        polyLineOption.addAll(points)
        polyLineOption.width(12F)
        polyLineOption.color(ContextCompat.getColor(this, R.color.app_color))
        polyLineOption.geodesic(true)
        val patternItem = listOf<PatternItem>(Dash(20F), Gap(20F))
        polyLineOption.pattern(patternItem)
        return polyLineOption
    }


    fun openDatePickerDialog(v: View) {

        AppUtil.onDateSetListener = object : AppUtil.OnDateSetListener {
            override fun onDateChanged(calendar: Calendar) {
                lastConnectedRequestDate = convertToRequestedDate(calendar)
                txtDate.text = AppUtil.convertToAppDate(calendar)
                getTripDetails()
            }
        }

        AppUtil.showPickerDialog(this, calendar)
    }

    private fun showPickerDialog(year: Int, month: Int, day: Int) {
        val dpd = DatePickerDialog(
            this, R.style.DatePickerDialogTheme,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                lastConnectedRequestDate = convertToRequestedDate(calendar)
                txtDate.text = AppUtil.convertToAppDate(calendar)
                getTripDetails()

            },
            year,
            month,
            day
        )

        dpd.datePicker.maxDate = Calendar.getInstance().timeInMillis
        dpd.show()
    }

    /**
     * @see [nested scrolling workaround](http://stackoverflow.com/a/17317176)
     */
    private fun fixMapScrolling() {
        transparentImage.setOnTouchListener { _, event ->
            when (event.action) {

                MotionEvent.ACTION_DOWN -> {
                    // Disallow ScrollView to intercept touch events.
                    scrollView.requestDisallowInterceptTouchEvent(true)
                    // Disable touch on transparent view
                    false
                }
                MotionEvent.ACTION_UP -> {
                    // Allow ScrollView to intercept touch events.
                    scrollView.requestDisallowInterceptTouchEvent(false)
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    scrollView.requestDisallowInterceptTouchEvent(true)
                    false
                }
                else -> {
                    true
                }
            }
        }
    }

}
