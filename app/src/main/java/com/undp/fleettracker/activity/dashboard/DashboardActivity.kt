package com.undp.fleettracker.activity.dashboard

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.app.ActivityCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.undp.fleettracker.R
import com.undp.fleettracker.activity.base.BaseActivity
import com.undp.fleettracker.activity.trip.TripDetailActivity
import com.undp.fleettracker.adapter.VehicleDetailsAdapter
import com.undp.fleettracker.constants.BEARER_TOKEN
import com.undp.fleettracker.constants.TENANT_ID
import com.undp.fleettracker.constants.fleetList
import com.undp.fleettracker.di.module.NetworkModule
import com.undp.fleettracker.models.fleet.FleetDetailModel
import com.undp.fleettracker.models.fleet.FleetRequestModel
import com.undp.fleettracker.models.vehicle.VehicleDetails
import com.undp.fleettracker.models.vehicle.VehicleDetailsResponse
import com.undp.fleettracker.models.vehicle.VehicleInfo
import com.undp.fleettracker.models.vehicle.VehicleRequestModel
import com.undp.fleettracker.network.api.fleet.FleetAPI
import com.undp.fleettracker.network.api.vehicle.VehicleAPI
import com.undp.fleettracker.util.AppUtil
import kotlinx.android.synthetic.main.content_dashboard.*
import kotlinx.android.synthetic.main.content_dashboard_fleet_details.*
import kotlinx.android.synthetic.main.content_dashboard_fleet_vehicles.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : BaseActivity(), OnMapReadyCallback,
    VehicleDetailsAdapter.onClickListener {

    private val TAG = DashboardActivity::class.java.canonicalName;

    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var vehicleDetailsAdapter: VehicleDetailsAdapter
    private lateinit var fleetDetailModel: FleetDetailModel
    private lateinit var vehicleDetailsResponse: VehicleDetailsResponse
    private var tenantId: Int = TENANT_ID
    private var searchText = ""
    private var fleetId = 0


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
//        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        initBaseActivityViews()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as SupportMapFragment
        setUpMap()
        mapFragment.getMapAsync(this)
        initUIElements()
    }

    override fun getBottomNavigationMenuItemId(): Int {
        return R.id.action_home
    }

    private fun initUIElements() {
        vehicleDetailsAdapter = VehicleDetailsAdapter(this, ArrayList<VehicleInfo>(), this)
        smoolider.adapter = vehicleDetailsAdapter
        fixMapScrolling()
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
                if (null != vehicleDetailsResponse && null != vehicleDetailsResponse.data) {
                    val vehicleInfoList = vehicleDetailsResponse.data.VehicleInfo
                    val vehicleInfo = vehicleInfoList[position]
                    updateGoogleMap(vehicleInfo)
                }
            }
        })
//        smoolider.adapter = IntroductionAdapter(this)

        val spinner = findViewById<AppCompatSpinner>(R.id.spinnerFleet)
        adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                AppUtil.getFleetList(ArrayList())
            )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (null != parent) {
                    val data = parent.selectedItem
                    if (null != fleetDetailModel && null != fleetList) {
                        for (fleetModel in fleetList) {
                            if (data === fleetModel.FleetName) {
                                fleetId = fleetModel.FleetId
                                searchText = ""
                                edtSearch.setText("")
                                getVehicleListForFleet(fleetId, tenantId)
                            }
                        }
                    }
                    Log.d(TAG, "Selected Fleet $data")
                }
            }

        }

        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                searchText = editable.toString().trim()
                if (searchText.length >= 3 || !searchText.isEmpty()) {
                    getVehicleListForFleet(fleetId, tenantId)
                }
            }

            override fun beforeTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        getFleetDetails()
//        getVehicleListForFleet(57, tenantId)
    }

    private fun updateGoogleMap(vehicleInfo: VehicleInfo) {
        if (null != vehicleInfo) {
            Log.d(TAG, "Page changes vehicle:${vehicleInfo.toString()}")
            val vehicle = LatLng(vehicleInfo.Latitude, vehicleInfo.Longitude)
            googleMap.clear()
            val options = MarkerOptions()
            options.position(vehicle).title(vehicleInfo.VehicleName)
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.car))
            googleMap.addMarker(options)
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(vehicle))
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
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
    private fun getFleetDetails() {
        AppUtil.showProgress(this)
        val service = NetworkModule.provideRetrofitInstance().create(FleetAPI::class.java)

        var fleetRequestModel: FleetRequestModel = FleetRequestModel()

        fleetRequestModel.tenantId = tenantId

        val call = service.getFleetOwnerFleetList(BEARER_TOKEN, fleetRequestModel)
        call.enqueue(object : Callback<FleetDetailModel> {
            override fun onFailure(call: Call<FleetDetailModel>, t: Throwable) {
                AppUtil.hideProgress()
                Log.d(TAG, "Response Failure::" + t.message)
            }

            override fun onResponse(
                call: Call<FleetDetailModel>,
                response: Response<FleetDetailModel>
            ) {
                AppUtil.hideProgress()
                Log.d(TAG, "Response Success:$response")
                if (response.code() == 200) {
                    val data = response.body()
                    Log.d(TAG, "Response object: $data")
                    fleetDetailModel = response.body()!!
                    fleetList.clear()
                    fleetList.addAll(fleetDetailModel.data)
                    runOnUiThread {
                        adapter.clear()
                        adapter.addAll(AppUtil.getFleetList(fleetDetailModel.data))
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }

    private fun getVehicleListForFleet(fleetID: Int, tenantId: Int) {
        val service = NetworkModule.provideRetrofitInstance().create(VehicleAPI::class.java)
        val vehicleRequestModel = VehicleRequestModel()
        vehicleRequestModel.fleetId = fleetID
        vehicleRequestModel.tenantId = tenantId
        vehicleRequestModel.pageNo = 1
        vehicleRequestModel.pageSize = 20
        vehicleRequestModel.sortColumnName = "index"
        vehicleRequestModel.sortType = "ASC"
        vehicleRequestModel.startRecordNo = "0"
        vehicleRequestModel.searchKey = searchText

        AppUtil.showProgress(this)
        val call = service.getVehicleListByFilter(BEARER_TOKEN, vehicleRequestModel)
        call.enqueue(object : Callback<VehicleDetailsResponse> {
            override fun onFailure(call: Call<VehicleDetailsResponse>, t: Throwable) {
                AppUtil.hideProgress()
                Log.d(TAG, "Response Failure::" + t.message)
            }

            override fun onResponse(
                call: Call<VehicleDetailsResponse>,
                response: Response<VehicleDetailsResponse>
            ) {
                AppUtil.hideProgress()
                Log.d(TAG, "Response Success:$response")
                if (response.code() == 200) {
                    val data = response.body()
                    Log.d(TAG, "Response object: $data")
                    vehicleDetailsResponse = response.body()!!
                    val vehicleDetails = vehicleDetailsResponse.data
                    runOnUiThread {
                        updateVehicleInfoUI(vehicleDetails)
                    }
                }
            }
        })
    }

    private fun updateVehicleInfoUI(vehicleDetails: VehicleDetails) {

        if (null != vehicleDetails && null != vehicleDetails.VehicleInfo) {

            updateVehicleStatus(vehicleDetails, false)

            vehicleDetailsAdapter =
                VehicleDetailsAdapter(
                    applicationContext,
                    vehicleDetails.VehicleInfo,
                    this
                )
            smoolider.adapter = vehicleDetailsAdapter
            updateGoogleMap(vehicleInfo = vehicleDetails.VehicleInfo[0])
            txtErrorMessage.visibility = View.GONE
            smoolider.visibility = View.VISIBLE

        } else {
            googleMap.clear()
            txtErrorMessage.visibility = View.VISIBLE
            smoolider.visibility = View.GONE
            updateVehicleStatus(null, true)
        }
    }

    private fun updateVehicleStatus(vehicleDetails: VehicleDetails?, isEmpty: Boolean) {
        if (!isEmpty) {
            txtFaulty.text = vehicleDetails!!.FaultyVehiclesCount.toString()
            txtOnline.text = vehicleDetails.RunningVehiclesCount.toString()
            val healthyVehicleCount =
                vehicleDetails.TotalVehiclesCount - vehicleDetails.FaultyVehiclesCount
            val offlineVehicleCount =
                vehicleDetails.TotalVehiclesCount - vehicleDetails.RunningVehiclesCount

            if (healthyVehicleCount > 0) {
                txtHealthy.text = healthyVehicleCount.toString()
            } else {
                txtHealthy.text = 0.toString()
            }

            if (offlineVehicleCount > 0) {
                txtOffline.text = offlineVehicleCount.toString()
            } else {
                txtOffline.text = 0.toString()
            }
        } else {
            txtHealthy.text = 0.toString()
            txtFaulty.text = 0.toString()
            txtOnline.text = 0.toString()
            txtOffline.text = 0.toString()
        }
    }

    override fun onItemClick(position: Int, vehicleInfo: VehicleInfo) {
        intent = Intent(this, TripDetailActivity::class.java)
        intent.putExtra("VEHICLE_INFO", vehicleInfo)
        intent.putExtra("TENANT_ID", tenantId)
        intent.putExtra("LAST_CONNECTED", vehicleInfo.LastConnectedLocalDateTime)
        startActivity(intent)
    }

    /**
     * @see [nested scrolling workaround](http://stackoverflow.com/a/17317176)
     */
    private fun fixMapScrolling() {
        transparentImage.setOnTouchListener { v, event ->
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
