package com.undp.fleettracker.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.undp.fleettracker.R
import com.undp.fleettracker.activity.booking.fragment.BookingSummaryDetailsFragment
import com.undp.fleettracker.activity.booking.fragment.BookingTodaySummaryFragment

class BookingSummaryPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    private val TAB_TITLES = arrayOf(
        R.string.tab_booking_summary,
        R.string.tab_booking_history
    )


    val fragment1 = BookingTodaySummaryFragment.newInstance()

    val fragment2 = BookingSummaryDetailsFragment.newInstance()
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