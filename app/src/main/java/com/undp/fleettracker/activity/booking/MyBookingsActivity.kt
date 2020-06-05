package com.undp.fleettracker.activity.booking

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.undp.fleettracker.R
import com.undp.fleettracker.activity.base.BaseActivity
import com.undp.fleettracker.activity.booking.customviews.BookingFilterView
import com.undp.fleettracker.activity.booking.customviews.BookingFilterView.OnChangeListeners
import com.undp.fleettracker.adapter.MyBookingsAdapter
import com.undp.fleettracker.business.BookingsManager
import com.undp.fleettracker.business.FleetManager
import com.undp.fleettracker.callbacks.HttpResponseCallback
import com.undp.fleettracker.callbacks.MyBookingItemClickListener
import com.undp.fleettracker.constants.*
import com.undp.fleettracker.models.booking.GetBookingRequestModel
import com.undp.fleettracker.models.booking.GetBookingsDetails
import com.undp.fleettracker.models.booking.GetBookingsResponseModel
import com.undp.fleettracker.models.fleet.FleetDetailModel
import com.undp.fleettracker.models.fleet.FleetModel
import com.undp.fleettracker.util.AppUtil
import io.github.rokarpov.backdrop.BackdropController
import kotlinx.android.synthetic.main.activity_my_bookings.*
import kotlinx.android.synthetic.main.content_my_bookings.*
import retrofit2.Response

class MyBookingsActivity : BaseActivity(),
    HttpResponseCallback, MyBookingItemClickListener {
    private val TAG: String = MyBookingsActivity::class.java.simpleName

    private lateinit var backdropController: BackdropController
    private lateinit var bookingFilterView: BookingFilterView

    private var searchText = ""
    private var mBookingDetailsList: MutableList<GetBookingsDetails> =
        ArrayList<GetBookingsDetails>()
    private var mMyBookingsAdapter: MyBookingsAdapter? = null
    private var mBookingResponseList: MutableList<GetBookingsDetails> = ArrayList()
    private var getBookingRequest: GetBookingRequestModel = GetBookingRequestModel()
    //private var recyclerViewMyBookings: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_bookings)
        initBaseActivityViews()
        initBackdropController()
        initViews()
//        getTenantFleetList()
    }

    private fun initBackdropController() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "My Booking"
        toolbar.inflateMenu(R.menu.menu_navigation)
        bookingFilterView = findViewById(R.id.nav_filter_view)

        backdropController = BackdropController.build(rootLayout, applicationContext) {
            supportToolbar = toolbar
            menuItemRevealSettings(R.id.menu_nav_filter, bookingFilterView)
            interactionSettings(bookingFilterView) {
                hideHeader = true
                animationProvider = BookingFilterView.AnimatorProvider
            }
            concealedTitleId = R.string.lbl_my_bookings
            concealedNavigationIconId = R.drawable.ic_back
            revealedNavigationIconId = R.drawable.ic_back
        }


        bookingFilterView.onCloseListener = object : BookingFilterView.OnCloseListener {
            override fun onClose() {
                backdropController.conceal()
            }
        }

        bookingFilterView.onChangeListeners = object : OnChangeListeners {
            override fun fleetChange(fleetID: Int) {
                generateGetBookingRequestModel()
                getBookingRequest.fleetId = fleetID
                sendGetBookingRequest()
            }

            override fun serviceChange(service: String) {
                getBookingRequest.bookingType = service
                sendGetBookingRequest()
            }

            override fun bookingStatusChange(status: String) {
                getBookingRequest.approvalStatus = status
                sendGetBookingRequest()
            }
        }
    }

    private fun generateGetBookingRequestModel() {
        getBookingRequest = GetBookingRequestModel()
        getBookingRequest.tenantId = TENANT_ID
        getBookingRequest.sortColumnName = "StartTime"
        getBookingRequest.sortType = "desc"
        getBookingRequest.pageNo = 1
        getBookingRequest.pageSize = 10
    }

    fun sendGetBookingRequest() {
        AppUtil.showProgress(this)
        BookingsManager.instance.getUserBookings(
            HttpRequests.GET_USER_BOOKINGS,
            getBookingRequest,
            this
        )
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
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
        val getBookingRequest: GetBookingRequestModel = GetBookingRequestModel()
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
                            mBookingResponseList.clear()
                            mBookingDetailsList.clear()
                            refreshRecyclerView()
                        }
                    }
                } else {
                    mBookingResponseList.clear()
                    mBookingDetailsList.clear()
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
                            fleetList = fleetDetailsModel.data
                            Log.d(TAG, "getTenantFleetList size: ${fleetList!!.size}")
//                            sendGetMyBookingsRequest(fleetList?.get(0))
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

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        backdropController.syncState()
    }

    override fun onBackPressed() {
        backdropController.conceal()
    }

}
