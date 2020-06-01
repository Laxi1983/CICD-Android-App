package com.undp.fleettracker.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.undp.fleettracker.R
import com.undp.fleettracker.activity.notifications.NotificationFragment
import com.undp.fleettracker.models.notifications.NotificationRequestModel

private val TAB_TITLES = arrayOf(
    R.string.tab_events,
    R.string.tab_fault,
    R.string.tab_geofence
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    private val fragment1 = NotificationFragment.newInstance(
        0, NotificationRequestModel()
    )

    private val fragment2 = NotificationFragment.newInstance(
        2, NotificationRequestModel()
    )

    private val fragment3 = NotificationFragment.newInstance(
        3, NotificationRequestModel()
    )

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        when (position) {
            0 -> return fragment1
            1 -> return fragment2
            2 -> return fragment3
        }
        return NotificationFragment.newInstance(
            position, NotificationRequestModel()
        )
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 3
    }

    fun currentlySelectedPage(position: Int, notificationRequestModel: NotificationRequestModel) {
        when (position) {
            0 -> fragment1.setNotificationModel(notificationRequestModel)
            1 -> fragment2.setNotificationModel(notificationRequestModel)
            2 -> fragment3.setNotificationModel(notificationRequestModel)
        }
    }

    fun clearNotificationData(position: Int) {
        when (position) {
            0 -> fragment1.clearNotificationData()
            1 -> fragment2.clearNotificationData()
            2 -> fragment3.clearNotificationData()
        }
    }

}