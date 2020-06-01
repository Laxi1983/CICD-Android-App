package com.undp.fleettracker.activity.booking

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.undp.fleettracker.R
import com.undp.fleettracker.activity.base.BaseActivity
import com.undp.fleettracker.adapter.MyBookingsAdapter
import com.undp.fleettracker.business.BookingsManager
import com.undp.fleettracker.business.FleetManager
import com.undp.fleettracker.callbacks.HttpResponseCallback
import com.undp.fleettracker.callbacks.MyBookingItemClickListener
import com.undp.fleettracker.constants.EXTRA_MY_BOOKING_DETAILS
import com.undp.fleettracker.constants.HttpRequests
import com.undp.fleettracker.constants.RequestStatus
import com.undp.fleettracker.constants.TENANT_ID
import com.undp.fleettracker.models.booking.GetBookingRequestModel
import com.undp.fleettracker.models.booking.GetBookingsDetails
import com.undp.fleettracker.models.booking.GetBookingsResponseModel
import com.undp.fleettracker.models.fleet.FleetDetailModel
import com.undp.fleettracker.models.fleet.FleetModel
import com.undp.fleettracker.util.AppUtil
import kotlinx.android.synthetic.main.content_my_bookings.*
import retrofit2.Response

class MyBookingsActivity : BaseActivity(),
    HttpResponseCallback, MyBookingItemClickListener {
    private val TAG: String = MyBookingsActivity::class.java.simpleName

    private var searchText = ""
    private var mFleetList: ArrayList<FleetModel>? = null
    private var mBookingDetailsList: MutableList<GetBookingsDetails> =
        ArrayList<GetBookingsDetails>()
    private var mMyBookingsAdapter: MyBookingsAdapter? = null
    private var mBookingResponseList: MutableList<GetBookingsDetails> = ArrayList()
    //private var recyclerViewMyBookings: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_bookings)
        initBaseActivityViews()
        initViews()
        getTenantFleetList()
    }

    private fun initViews() {
        // recyclerViewMyBookings = findViewById<RecyclerView>(R.id.recyclerViewMyBookings)
        mMyBookingsAdapter = MyBookingsAdapter(mBookingDetailsList, this)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerViewMyBookings!!.layoutManager = linearLayoutManager
        recyclerViewMyBookings!!.adapter = mMyBookingsAdapter

        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                searchText = editable.toString().trim()
                if (searchText.length >= 3 || searchText.isEmpty()) {
                    performSearchOperation(searchText)
                }
            }

            override fun beforeTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
    }

    override fun getBottomNavigationMenuItemId(): Int {
        return R.id.action_booking
    }

    private fun getTenantFleetList() {
        AppUtil.showProgress(this)
        FleetManager.instance.getTenantVehicleFleetList(
            HttpRequests.GET_TENANT_VEHICLE_FLEET_LIST,
            TENANT_ID,
            this
        )
    }

    private fun sendGetMyBookingsRequest(fleetModel: FleetModel?) {
        var getBookingRequest: GetBookingRequestModel = GetBookingRequestModel()
        getBookingRequest.fleetId = fleetModel?.FleetId
        getBookingRequest.tenantId = TENANT_ID
        getBookingRequest.sortColumnName = "StartTime"
        getBookingRequest.sortType = "desc"
        getBookingRequest.pageNo = 1
        getBookingRequest.pageSize = 10
        BookingsManager.instance.getUserBookings(
            HttpRequests.GET_USER_BOOKINGS,
            getBookingRequest,
            this
        )
//        if (null == getBookingDetails) {
//            Log.d(TAG, "get booking details null")
//        } else {
//            mBookingDetailsList = getBookingDetails.data
//            Log.d(TAG, "Booking details counr:${mBookingDetailsList!!.size}")
//        }
    }

    override fun onResponse(
        httpRequests: HttpRequests,
        requestStatus: RequestStatus,
        response: Any?,
        throwable: Throwable?
    ) {
        when (httpRequests) {
            HttpRequests.GET_USER_BOOKINGS -> {
                AppUtil.hideProgress()
                if (requestStatus == RequestStatus.SUCCESS) {
                    var getBookingResponseModel: GetBookingsResponseModel? = null
                    val responseModel: Response<GetBookingsResponseModel> =
                        response as Response<GetBookingsResponseModel>
                    if (null == responseModel) {
                        Log.d(TAG, "GET_USER_BOOKINGS response null")
                        return
                    }
                    if (responseModel.code() == 200) {
                        getBookingResponseModel = response.body()
                        if (null != getBookingResponseModel!!.data) {
                            mBookingResponseList.clear()
                            mBookingResponseList.addAll(getBookingResponseModel.data)
                            mBookingDetailsList.clear()
                            mBookingDetailsList.addAll(getBookingResponseModel.data)
                            refreshRecyclerView()
                            Log.d(TAG, "Booking details counr:${mBookingDetailsList.size}")
                        } else {
                            refreshRecyclerView()
                        }
                    }
                } else {
                    refreshRecyclerView()
                }
            }
            HttpRequests.GET_TENANT_VEHICLE_FLEET_LIST -> {

                if (requestStatus == RequestStatus.SUCCESS) {
                    val responseModel: Response<FleetDetailModel> =
                        response as Response<FleetDetailModel>
                    if (null == responseModel) {
                        Log.d(TAG, "GET_TENANT_VEHICLE_FLEET_LIST response null")
                        return
                    }
                    if (response.code() == 200) {
                        var fleetDetailsModel: FleetDetailModel? = null
                        fleetDetailsModel = responseModel.body()
                        if (null == fleetDetailsModel) {
                            Log.d(TAG, "getTenantFleetList null")
                            AppUtil.hideProgress()
                        } else {
                            mFleetList = fleetDetailsModel.data
                            Log.d(TAG, "getTenantFleetList size: ${mFleetList?.size}")
                            sendGetMyBookingsRequest(mFleetList?.get(2))
                        }
                    }
                } else {
                    AppUtil.hideProgress()
                }
            }
        }
    }

    private fun performSearchOperation(searchText: String) {
        if (null == mBookingResponseList || mBookingResponseList.size == 0) {
            return
        }

        if (searchText.isEmpty()) {
            mBookingDetailsList.clear()
            mBookingDetailsList.addAll(mBookingResponseList)
            refreshRecyclerView()
            return
        }

        val searchList: MutableList<GetBookingsDetails> = ArrayList()

        for (getBookingDetails in mBookingResponseList) {
            for (tripLocationArr in getBookingDetails.TripLocationsArr) {
                if (tripLocationArr.formattedAddress!!.contains(searchText)) {
                    searchList.add(getBookingDetails)
                    break
                }
            }
        }
        mBookingDetailsList.clear()
        mBookingDetailsList.addAll(searchList)
        refreshRecyclerView()
    }

    private fun refreshRecyclerView() {
        mMyBookingsAdapter!!.notifyDataSetChanged()
        if (mBookingDetailsList.size == 0) {
            txtErrorMessage.visibility = View.VISIBLE
            recyclerViewMyBookings.visibility = View.GONE
        } else {
            txtErrorMessage.visibility = View.GONE
            recyclerViewMyBookings.visibility = View.VISIBLE
        }
    }

    override fun onMapViewClicked(position: Int) {
        val getBookingDetails = mBookingDetailsList[position]
        val intent: Intent = Intent(this, BookingDetailsActivity::class.java)
        intent.putExtra(EXTRA_MY_BOOKING_DETAILS, getBookingDetails)
        startActivity(intent)
    }


}
