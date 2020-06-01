package com.undp.fleettracker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.viewpager.widget.PagerAdapter
import com.undp.fleettracker.R
import com.undp.fleettracker.models.trip.TripDetails
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

class TripInfoAdapter(
    private val mContext: Context,
    private var tripDetails: ArrayList<TripDetails>
) :
    PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val tripDetail = tripDetails[position]

        val inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(R.layout.trip_info_card, container, false) as ViewGroup

        val depart = view.findViewById<AppCompatTextView>(R.id.txtDepartValue)
        val arrive = view.findViewById<AppCompatTextView>(R.id.txtArriveValue)
        val miles = view.findViewById<AppCompatTextView>(R.id.txtMilesValue)
        val fuel = view.findViewById<AppCompatTextView>(R.id.txtFuelsValue)

        depart.text =
            tripDetail.startLocalTime //AppUtil.convertTimeStampToTime24Hr((tripDetail.startTimeEpoch!! * 1000L))
        arrive.text =
            tripDetail.endLocalTime//AppUtil.convertTimeStampToTime24Hr((tripDetail.endTimeEpoch!! * 1000L))
        miles.text = BigDecimal(tripDetail.tripDistance!!).setScale(2, RoundingMode.HALF_EVEN).toString()
        fuel.text = BigDecimal(tripDetail.fuelDifference!!).setScale(2, RoundingMode.HALF_EVEN).toString()

        container.addView(view)

        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return `object` === view
    }

    override fun getItemPosition(`object`: Any): Int {
        if (tripDetails.contains(`object`))
            return tripDetails.indexOf(`object`)
        return POSITION_NONE
    }

    override fun getCount(): Int {
        return tripDetails.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }

    fun updateTripList(tripDetails: ArrayList<TripDetails>) {
        this.tripDetails.clear()
        this.tripDetails.addAll(tripDetails)
        notifyDataSetChanged()
    }
}