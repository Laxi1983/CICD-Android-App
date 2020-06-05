package com.undp.fleettracker.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.undp.fleettracker.R
import com.undp.fleettracker.activity.booking.fragment.VehicleOccupancySummaryFragment
import com.undp.fleettracker.activity.booking.fragment.VehicleOccupancyTodaySummaryFragment

class VehicleOccupancyPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {
    private val TAB_TITLES = arrayOf(
        R.string.tab_today_summary,
        R.string.tab_vehicle_occupancy
    )

    val fragment1 = VehicleOccupancyTodaySummaryFragment.newInstance()

    val fragment2 = VehicleOccupancySummaryFragment.newInstance()

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return fragment1
            1 -> return fragment2
            else -> return fragment1
        }

    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 2
    }
}