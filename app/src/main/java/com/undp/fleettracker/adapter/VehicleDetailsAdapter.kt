package com.undp.fleettracker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.PagerAdapter
import com.undp.fleettracker.R
import com.undp.fleettracker.models.vehicle.VehicleInfo
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

class VehicleDetailsAdapter(
    private val mContext: Context,
    private var vehicleInfoList: ArrayList<VehicleInfo>,
    private var onItemClickListener: onClickListener
) :
    PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val vehicleInfo = vehicleInfoList[position]

        val inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(R.layout.vehicle_details_card, container, false) as ViewGroup
        view.tag = position
        val vehicleName = view.findViewById<AppCompatTextView>(R.id.txtVehicleName)
        val lastSeen = view.findViewById<AppCompatTextView>(R.id.txtLastSeenValue)
        val distance = view.findViewById<AppCompatTextView>(R.id.txtDistanceValue)
        vehicleName.text = vehicleInfo.VehicleName
        lastSeen.text = vehicleInfo.LastConnectedLocalDateTime
        distance.text = BigDecimal(vehicleInfo.Odometer).setScale(2, RoundingMode.HALF_EVEN).toString()
        container.addView(view)

        val parentLayout = view.findViewById<CardView>(R.id.parentLayout)
        parentLayout.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                onItemClickListener.onItemClick(position, vehicleInfo)
            }
        })

        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return `object` === view
    }

    override fun getItemPosition(`object`: Any): Int {
        if (vehicleInfoList.contains(`object`))
            return vehicleInfoList.indexOf(`object`)
        return POSITION_NONE
    }

    override fun getCount(): Int {
        return vehicleInfoList.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }

    fun updateVehicleInfoList(vehicleInfoList: ArrayList<VehicleInfo>) {
        this.vehicleInfoList.clear()
        this.vehicleInfoList.addAll(vehicleInfoList)
        notifyDataSetChanged()
    }

  public interface onClickListener {
        fun onItemClick(position: Int, vehicleInfo: VehicleInfo)
    }
}