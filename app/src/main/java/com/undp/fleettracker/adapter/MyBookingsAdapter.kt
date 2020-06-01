package com.undp.fleettracker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.undp.fleettracker.R
import com.undp.fleettracker.callbacks.MyBookingItemClickListener
import com.undp.fleettracker.constants.ApprovalStatus
import com.undp.fleettracker.constants.BookingType
import com.undp.fleettracker.models.booking.GetBookingsDetails
import com.undp.fleettracker.util.AppUtil.convertEpochSecondsToTimeString

class MyBookingsAdapter(
    private val myBookings: List<GetBookingsDetails>?,
    private val myBookingItemClickListener: MyBookingItemClickListener
) :
    RecyclerView.Adapter<MyBookingsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_booking_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (null == myBookings)
            return 0
        return myBookings.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val getBookingDetails = myBookings?.get(position)
        if (null == getBookingDetails) {
            return
        }
        val bookingNum = "Booking " + (position + 1)
        holder.txtBookingTitle?.text = bookingNum
        if (getBookingDetails.TripLocationsArr.size >= 2) {
            holder.txtDropLocation?.text = getBookingDetails.TripLocationsArr[1].formattedAddress
            holder.txtPickupLcation?.text = getBookingDetails.TripLocationsArr[0].formattedAddress
        }

        holder.txtStartDate?.text =
            convertEpochSecondsToTimeString(getBookingDetails.StartDateTimeEpoch)
        holder.txtEndDate?.text =
            convertEpochSecondsToTimeString(getBookingDetails.EndDateTimeEpoch)
        holder.txtStatus?.text =
            ApprovalStatus.values()[getBookingDetails.ApprovalStatus].toString()

        holder.txtVehicleNo?.text = getBookingDetails.BookingVehicleInfo.VehicleName?.toString()
        holder.txtViewBookingType?.text =
            BookingType.values()[getBookingDetails.BookingType].dispName

        holder.btnMapView!!.setOnClickListener { v ->
            myBookingItemClickListener.onMapViewClicked(
                position
            )
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgBtnCancelBooking: ImageButton? = null
        var imgBtnBookingEdit: ImageButton? = null
        var txtViewBookingType: TextView? = null
        var txtStartDate: TextView? = null
        var txtEndDate: TextView? = null
        var txtPickupLcation: TextView? = null
        var txtDropLocation: TextView? = null
        var txtVehicleNo: TextView? = null
        var txtStatus: TextView? = null
        var btnMapView: Button? = null
        var txtBookingTitle: TextView? = null

        init {
            initViews()
        }

        private fun initViews() {
            imgBtnCancelBooking =
                itemView.findViewById<ImageButton>(R.id.imgBtnCancelBooking) as ImageButton
            imgBtnBookingEdit =
                itemView.findViewById<ImageButton>(R.id.imgBtnBookingEdit) as ImageButton
            txtViewBookingType = itemView.findViewById(R.id.txtViewBookingType)
            txtStartDate = itemView.findViewById(R.id.txtStartDate)
            txtEndDate = itemView.findViewById(R.id.txtEndDate)
            txtPickupLcation = itemView.findViewById(R.id.txtPickupLcation)
            txtDropLocation = itemView.findViewById(R.id.txtDropLocation)
            txtVehicleNo = itemView.findViewById(R.id.txtVehicleNo)
            txtStatus = itemView.findViewById(R.id.txtStatus)
            btnMapView = itemView.findViewById(R.id.btnMapView)
            txtBookingTitle = itemView.findViewById(R.id.txtBookingTitle)
        }


    }
}