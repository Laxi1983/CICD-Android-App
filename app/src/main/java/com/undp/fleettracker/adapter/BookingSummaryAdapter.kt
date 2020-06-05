package com.undp.fleettracker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.undp.fleettracker.R
import com.undp.fleettracker.models.booking.GetBookingSummaryDetailsModel
import com.undp.fleettracker.util.AppUtil

class BookingSummaryAdapter(private val getBookingSummaryDetailsList: List<GetBookingSummaryDetailsModel>) :
    RecyclerView.Adapter<BookingSummaryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.booking_summary_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (null == getBookingSummaryDetailsList) {
            return 0
        }
        return getBookingSummaryDetailsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (null == getBookingSummaryDetailsList || getBookingSummaryDetailsList.isEmpty()) {
            return
        }
        var getBookingSummaryDetails = getBookingSummaryDetailsList.get(position)

        holder.txtDriver?.text = getBookingSummaryDetails.driverName?.toString()
        holder.txtVehicleNo?.text = getBookingSummaryDetails.vehicleName?.toString()
        holder.txtBookingTitle?.text = "Booking ${position + 1}"
        holder.txtStartDate?.text =
            AppUtil.convertTimeStringToSimpleDate(getBookingSummaryDetails.starttime)
        holder.txtEndDate?.text =
            AppUtil.convertTimeStringToSimpleDate(getBookingSummaryDetails.endtime)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtBookingTitle: TextView? = null
        var txtVehicleNo: TextView? = null
        var txtDriver: TextView? = null
        var txtStartDate: TextView? = null
        var txtEndDate: TextView? = null

        init {
            initViews()
        }

        private fun initViews() {
            txtBookingTitle = itemView.findViewById(R.id.txtBookingTitle)
            txtVehicleNo = itemView.findViewById(R.id.txtVehicleNo)
            txtDriver = itemView.findViewById(R.id.txtDriver)
            txtStartDate = itemView.findViewById(R.id.txtStartDate)
            txtEndDate = itemView.findViewById(R.id.txtEndDate)
        }
    }
}