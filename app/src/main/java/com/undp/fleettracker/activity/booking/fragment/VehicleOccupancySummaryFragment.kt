package com.undp.fleettracker.activity.booking.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.AppCompatSpinner
import androidx.recyclerview.widget.LinearLayoutManager
import com.undp.fleettracker.R
import com.undp.fleettracker.adapter.VehicleOccupancySummaryAdapter
import com.undp.fleettracker.business.BookingsManager
import com.undp.fleettracker.callbacks.HttpResponseCallback
import com.undp.fleettracker.constants.HttpRequests
import com.undp.fleettracker.constants.RequestStatus
import com.undp.fleettracker.constants.TENANT_ID
import com.undp.fleettracker.constants.fleetList
import com.undp.fleettracker.models.booking.GetBookingRequestModel
import com.undp.fleettracker.models.booking.VehicleOccupancySummaryModel
import com.undp.fleettracker.models.booking.VehicleOccupancySummaryResponseModel
import com.undp.fleettracker.models.vehicle.VehicleDetailsResponse
import com.undp.fleettracker.models.vehicle.VehicleInfo
import com.undp.fleettracker.network.VehicleManager
import com.undp.fleettracker.util.AppUtil
import kotlinx.android.synthetic.main.fragment_vehicle_occupancy_summary.*
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


/**
 * A simple [Fragment] subclass.
 */
class VehicleOccupancySummaryFragment : Fragment(), HttpResponseCallback, View.OnClickListener {

    private val TAG = VehicleOccupancySummaryFragment::class.java.simpleName

    var fleetAdapter: ArrayAdapter<String>? = null
    private var vehicleOccupancySummaryAdapter: VehicleOccupancySummaryAdapter? = null
    private var vehicleOccupancySummaryList: MutableList<VehicleOccupancySummaryModel> = ArrayList()
    private var fleetId: Int = 0
    private var startDateCalender: Calendar? = null
    private var vehicleManager: VehicleManager? = null
    private var vehicleCollectionMap: HashMap<Int, VehicleInfo> = HashMap()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vehicle_occupancy_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVehicleOccupancyRecyclerView()
        init()
        sendGetOccupancySummaryDetailsRequest(
            fleetId,
            AppUtil.convertToRequestedDate(startDateCalender!!)
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(): VehicleOccupancySummaryFragment {
            return VehicleOccupancySummaryFragment()
        }
    }

    private fun init() {
        startDateCalender = Calendar.getInstance()
        btnApply.setOnClickListener(this)
        txtViewDatePicker.setOnClickListener(this)
        initFleetSpinner()
        initVehicleManager()
        txtViewDatePicker.text = AppUtil.convertToAppDate(startDateCalender!!)
        if (fleetList?.size != 0) {
            fleetId = fleetList[0].FleetId
        }
    }

    private fun initVehicleOccupancyRecyclerView() {
        vehicleOccupancySummaryAdapter = VehicleOccupancySummaryAdapter(vehicleOccupancySummaryList)
        recyclerViewOccupancySummary.layoutManager = LinearLayoutManager(context)
        recyclerViewOccupancySummary.adapter = vehicleOccupancySummaryAdapter
    }

    private fun refreshVehicleOccupancyList() {
        vehicleOccupancySummaryAdapter?.notifyDataSetChanged()
    }

    private fun sendGetOccupancySummaryDetailsRequest(fleetId: Int, startDate: String) {
        var bookingRequestModel: GetBookingRequestModel = GetBookingRequestModel()
        bookingRequestModel.tenantId = TENANT_ID
        bookingRequestModel.fleetId = fleetId
        bookingRequestModel.startDate = startDate
        bookingRequestModel.endDate = startDate
        bookingRequestModel.pageNo = 1
        bookingRequestModel.pageSize = 10
        bookingRequestModel.sortColumnName = "index"
        bookingRequestModel.sortType = "asc"
        BookingsManager.instance.getVehicleOccupancySummary(
            HttpRequests.VEHICLE_OCCUPANCY_SUMMARY,
            bookingRequestModel,
            this
        )
    }


    override fun onResponse(
        httpRequests: HttpRequests,
        requestStatus: RequestStatus,
        response: Any?,
        throwable: Throwable?
    ) {
        when (httpRequests) {
            HttpRequests.VEHICLE_OCCUPANCY_SUMMARY -> {
                if (requestStatus == RequestStatus.SUCCESS) {
                    val responseModel: Response<VehicleOccupancySummaryResponseModel> =
                        response as Response<VehicleOccupancySummaryResponseModel>
                    if (null == responseModel) {
                        Log.d(TAG, "VEHICLE_OCCUPANCY_SUMMARY response null")
                        return
                    }
                    if (responseModel.code() == 200) {
                        var getBookingSummaryResponseModel = response.body()
                        if (null == getBookingSummaryResponseModel) {
                            return
                        }
                        if (null != getBookingSummaryResponseModel!!.data) {
                            vehicleOccupancySummaryList.clear()
                            val responseList = getBookingSummaryResponseModel.data
                            vehicleOccupancySummaryList.addAll(responseList)
                            for (vehicleOccpancyModel in vehicleOccupancySummaryList) {
                                if (vehicleCollectionMap.containsKey(vehicleOccpancyModel.VehicleId)) {
                                    vehicleOccpancyModel.VehicleName =
                                        vehicleCollectionMap.get(vehicleOccpancyModel.VehicleId)?.VehicleName
                                }
                            }
                            refreshVehicleOccupancyList()
                            Log.d(
                                TAG,
                                "vehicle occupancy count:${vehicleOccupancySummaryList?.size}"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun initFleetSpinner() {
        //val spinner = findViewById<AppCompatSpinner>(R.id.spinnerFleet)
        fleetAdapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                AppUtil.getFleetList(fleetList)
            )
        fleetAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFleet.adapter = fleetAdapter
        spinnerFleet.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (null != parent) {
                    val data = parent.selectedItem
                    if (null != fleetList) {
                        for (fleetModel in fleetList) {
                            if (data === fleetModel.FleetName) {
                                fleetId = fleetModel.FleetId
                                vehicleManager?.getVehicleListForFleet(fleetId, TENANT_ID)
                            }
                        }
                    }
                    Log.d(TAG, "Selected Fleet $data")
                }
            }
        }
        if (fleetList.size > 0) {
            vehicleManager?.getVehicleListForFleet(fleetList[0].FleetId, TENANT_ID)
        }
    }

    private fun openDatePickerDialog() {

        AppUtil.onDateSetListener = object : AppUtil.OnDateSetListener {
            override fun onDateChanged(calendar: Calendar) {
                if (calendar.timeInMillis > System.currentTimeMillis()) {
                    Toast.makeText(
                        requireContext(),
                        "Start date should not be greater than current date.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                txtViewDatePicker.text = AppUtil.convertToAppDate(calendar)
                startDateCalender = calendar


            }
        }
        AppUtil.showPickerDialog(requireContext(), startDateCalender!!)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.txtViewDatePicker -> {
                openDatePickerDialog()
            }
            R.id.btnApply -> {
                sendGetOccupancySummaryDetailsRequest(
                    fleetId,
                    AppUtil.convertToRequestedDate(startDateCalender!!)
                )
            }
        }
    }

    private fun initVehicleManager() {
        vehicleManager = VehicleManager()
        vehicleManager?.iResponse = object : VehicleManager.IVehicleResponse {
            override fun onResponse(data: VehicleDetailsResponse) {
                if (null != data) {
                    var vehicleDetailsResponse = data
                    val vehicleDetails = vehicleDetailsResponse.data
                    vehicleCollectionMap.clear()
                    for (vehicleInfo in vehicleDetails.VehicleInfo) {
                        vehicleCollectionMap.put(vehicleInfo.VehicleId, vehicleInfo)
                    }
                }
            }

            override fun onFailure(data: String) {

            }
        }
    }

}
