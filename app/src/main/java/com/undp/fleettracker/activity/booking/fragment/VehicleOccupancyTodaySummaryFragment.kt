package com.undp.fleettracker.activity.booking.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.undp.fleettracker.R
import com.undp.fleettracker.business.BookingsManager
import com.undp.fleettracker.callbacks.HttpResponseCallback
import com.undp.fleettracker.constants.ApprovalStatus
import com.undp.fleettracker.constants.HttpRequests
import com.undp.fleettracker.constants.RequestStatus
import com.undp.fleettracker.constants.TENANT_ID
import com.undp.fleettracker.models.booking.GetBookingRequestModel
import com.undp.fleettracker.models.booking.GetSpecificBookingStatusModel
import com.undp.fleettracker.models.booking.GetSpecificBookingStatusResponseModel
import kotlinx.android.synthetic.main.fragment_vehicle_occupancy_today_summary.*
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class VehicleOccupancyTodaySummaryFragment : Fragment(), HttpResponseCallback {

    private val TAG = VehicleOccupancyTodaySummaryFragment::class.java.simpleName

    private var getSpecificBookingStatusModelList: List<GetSpecificBookingStatusModel>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vehicle_occupancy_today_summary, container, false)
    }

    companion object {
        fun newInstance(): VehicleOccupancyTodaySummaryFragment {
            return VehicleOccupancyTodaySummaryFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendGetSpecificBookingRequest()
    }


    private fun sendGetSpecificBookingRequest() {
        var bookingRequestModel: GetBookingRequestModel = GetBookingRequestModel()
        bookingRequestModel.tenantId = TENANT_ID
        bookingRequestModel.fleetId = 56
        bookingRequestModel.startDate = "2020-05-01"
        bookingRequestModel.endDate = "2020-05-29"
        BookingsManager.instance.getTodaysSpecificBookingStatus(
            HttpRequests.GET_TODAY_SPECIFIC_BOOKING_STATUS,
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
            HttpRequests.GET_TODAY_SPECIFIC_BOOKING_STATUS -> {
                if (requestStatus == RequestStatus.SUCCESS) {
                    val responseModel: Response<GetSpecificBookingStatusResponseModel> =
                        response as Response<GetSpecificBookingStatusResponseModel>
                    if (null == responseModel) {
                        Log.d(TAG, "GET_TODAY_SPECIFIC_BOOKING_STATUS response null")
                        return
                    }
                    if (responseModel.code() == 200) {
                        var getSpecificBookingStatusResponseModel = response.body()
                        if (null == getSpecificBookingStatusResponseModel) {
                            return
                        }
                        if (null != getSpecificBookingStatusResponseModel!!.data) {
                            getSpecificBookingStatusModelList =
                                getSpecificBookingStatusResponseModel.data
                            setDataFromResponse()
                            Log.d(
                                TAG,
                                "today specific Booking details counr:${getSpecificBookingStatusModelList?.size}"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun setDataFromResponse() {
        if (null == getSpecificBookingStatusModelList || getSpecificBookingStatusModelList?.size == 0) {
            return
        }
        for (getSpecificBookingStatus in getSpecificBookingStatusModelList!!) {
            if (getSpecificBookingStatus.status == ApprovalStatus.Approved.ordinal) {
                txtBookingApprovedCount.text = getSpecificBookingStatus.count.toString()
            } else if (getSpecificBookingStatus.status == ApprovalStatus.Cancelled.ordinal) {
                txtBookingCancelledCount.text = getSpecificBookingStatus.count.toString()
            } else if (getSpecificBookingStatus.status == ApprovalStatus.Pending.ordinal) {
                txtBookingPendingCount.text = getSpecificBookingStatus.count.toString()
            } else if (getSpecificBookingStatus.status == ApprovalStatus.Rejected.ordinal) {
                txtBookingRejectedCount.text = getSpecificBookingStatus.count.toString()
            }
        }
    }

}
