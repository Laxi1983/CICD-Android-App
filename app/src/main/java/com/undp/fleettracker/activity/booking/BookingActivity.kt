package com.undp.fleettracker.activity.booking

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import com.undp.fleettracker.R
import com.undp.fleettracker.activity.base.BaseActivity


class BookingActivity : BaseActivity(), View.OnClickListener {

    private var rlForMyBookings: RelativeLayout? = null
    private var rlForBookingRequests: RelativeLayout? = null
    private var rlForBookingSummary: RelativeLayout? = null
    private var rlForVehicleOccupancy: RelativeLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)
        initBaseActivityViews()
        initViews()
    }

    private fun initViews() {
        rlForMyBookings = findViewById<RelativeLayout>(R.id.rlForMyBookings) as RelativeLayout
        rlForMyBookings!!.setOnClickListener(this)

        rlForBookingRequests =
            findViewById<RelativeLayout>(R.id.rlForBookingRequests) as RelativeLayout
        rlForBookingRequests!!.setOnClickListener(this)

        rlForBookingSummary =
            findViewById<RelativeLayout>(R.id.rlForBookingSummary) as RelativeLayout
        rlForBookingSummary!!.setOnClickListener(this)

        rlForVehicleOccupancy =
            findViewById<RelativeLayout>(R.id.rlForVehicleOccupancy) as RelativeLayout
        rlForVehicleOccupancy!!.setOnClickListener(this)

    }

    override fun getBottomNavigationMenuItemId(): Int {
        return R.id.action_booking
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rlForMyBookings -> startActivity(Intent(this, MyBookingsActivity::class.java))
            R.id.rlForBookingRequests -> startActivity(
                Intent(this, BookingRequestsActivity::class.java)
            )
            R.id.rlForBookingSummary -> startActivity(
                Intent(this, BookingSummaryActivity::class.java)
            )
            R.id.rlForVehicleOccupancy -> startActivity(
                Intent(this, VehicleOccupancyActivity::class.java)
            )
        }
    }
}
