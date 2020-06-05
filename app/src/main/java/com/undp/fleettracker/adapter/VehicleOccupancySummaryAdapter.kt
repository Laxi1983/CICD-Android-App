package com.undp.fleettracker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.undp.fleettracker.R
import com.undp.fleettracker.models.booking.VehicleOccupancySummaryModel

class VehicleOccupancySummaryAdapter(private val mVehicleOccupancySummaryList: List<VehicleOccupancySummaryModel>) :
    RecyclerView.Adapter<VehicleOccupancySummaryAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtBookingTitle: TextView? = null
        var txtVehicleNo: TextView? = null
        var txtDriver: TextView? = null
        var txtOccupancy: TextView? = null


        init {
            initViews()
        }

        private fun initViews() {
            txtBookingTitle = itemView.findViewById(R.id.txtBookingTitle)
            txtVehicleNo = itemView.findViewById(R.id.txtVehicleNo)
            txtDriver = itemView.findViewById(R.id.txtDriver)
            txtOccupancy = itemView.findViewById(R.id.txtOccupancy)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.vehicle_occupancy_summary_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mVehicleOccupancySummaryList?.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var vehicleSummaryModel = mVehicleOccupancySummaryList?.get(position)
        holder.txtBookingTitle?.text = "Booking ${position + 1}"
        holder.txtDriver?.text = vehicleSummaryModel.DriverName
        holder.txtOccupancy?.text = vehicleSummaryModel.Occupancy
        holder.txtVehicleNo?.text = vehicleSummaryModel.VehicleName
    }
}