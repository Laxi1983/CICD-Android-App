package com.undp.fleettracker.activity.booking.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.undp.fleettracker.R
import com.undp.fleettracker.adapter.BookingSummaryAdapter
import com.undp.fleettracker.business.BookingsManager
import com.undp.fleettracker.callbacks.HttpResponseCallback
import com.undp.fleettracker.constants.HttpRequests
import com.undp.fleettracker.constants.RequestStatus
import com.undp.fleettracker.constants.TENANT_ID
import com.undp.fleettracker.models.booking.GetBookingRequestModel
import com.undp.fleettracker.models.booking.GetBookingSummaryDetailsModel
import com.undp.fleettracker.models.booking.GetBookingSummaryResponseModel
import kotlinx.android.synthetic.main.fragment_booking_summary_details.*
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class BookingSummaryDetailsFragment : Fragment(), HttpResponseCallback {
    private val TAG = BookingSummaryDetailsFragment::class.java.simpleName
    private var bookingSummaryAdapter: BookingSummaryAdapter? = null
    private var getBookingSummaryDetailsList: MutableList<GetBookingSummaryDetailsModel> =
        ArrayList()

    companion object {
        fun newInstance(): BookingSummaryDetailsFragment {
            return BookingSummaryDetailsFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booking_summary_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        sendGetBookingSummaryDetailsRequest()
    }

    private fun sendGetBookingSummaryDetailsRequest() {
        var bookingRequestModel: GetBookingRequestModel = GetBookingRequestModel()
        bookingRequestModel.tenantId = TENANT_ID
        bookingRequestModel.fleetId = 56
        bookingRequestModel.startDate = "2020-05-01"
        bookingRequestModel.endDate = "2020-05-29"
        bookingRequestModel.pageNo = 1
        bookingRequestModel.pageSize = 10
        bookingRequestModel.sortColumnName="index"
        bookingRequestModel.sortType= "asc"
        BookingsManager.instance.getVehicleBookingSummary(
            HttpRequests.GET_VEHICLE_BOOKING_SUMMARY,
            bookingRequestModel,
            this
        )
    }

    /**
     * init recycler view
     */
    private fun initRecyclerView() {
        bookingSummaryAdapter = BookingSummaryAdapter(getBookingSummaryDetailsList)
        val layoutManager = LinearLayoutManager(context)
        recyclerViewBookingSummary.layoutManager = layoutManager
        recyclerViewBookingSummary.adapter = bookingSummaryAdapter
    }

    override fun onResponse(
        httpRequests: HttpRequests,
        requestStatus: RequestStatus,
        response: Any?,
        throwable: Throwable?
    ) {
        when (httpRequests) {
            HttpRequests.GET_VEHICLE_BOOKING_SUMMARY -> {
                if (requestStatus == RequestStatus.SUCCESS) {
                    val responseModel: Response<GetBookingSummaryResponseModel> =
                        response as Response<GetBookingSummaryResponseModel>
                    if (null == responseModel) {
                        Log.d(TAG, "GET_VEHICLE_BOOKING_SUMMARY response null")
                        return
                    }
                    if (responseModel.code() == 200) {
                        var getBookingSummaryResponseModel = response.body()
                        if (null == getBookingSummaryResponseModel) {
                            return
                        }
                        if (null != getBookingSummaryResponseModel!!.data) {
                            getBookingSummaryDetailsList.clear()
                            val responseList = getBookingSummaryResponseModel.data
                            getBookingSummaryDetailsList.addAll(responseList)
                            refreshBookingSummaryList()
                            Log.d(
                                TAG,
                                "specific Booking details counr:${getBookingSummaryDetailsList?.size}"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun refreshBookingSummaryList() {
        bookingSummaryAdapter?.notifyDataSetChanged()
    }

}
