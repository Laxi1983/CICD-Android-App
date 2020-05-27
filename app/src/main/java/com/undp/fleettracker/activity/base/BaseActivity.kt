package com.undp.fleettracker.activity.base

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.undp.fleettracker.R
import com.undp.fleettracker.activity.booking.BookingActivity
import com.undp.fleettracker.activity.dashboard.DashboardActivity
import com.undp.fleettracker.activity.notifications.NotificationsActivity
import com.undp.fleettracker.activity.reports.ReportActivity


abstract class BaseActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    lateinit var navigationView: BottomNavigationView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun initBaseActivityViews() {
//        navigationView = findViewById<BottomNavigationView>(R.id.navigationView);
        if (null != supportActionBar) {
            supportActionBar!!.hide()
        }
        navigationView = findViewById(R.id.navigationView)
        navigationView.setOnNavigationItemSelectedListener(this)

    }

    override fun onStart() {
        super.onStart()
        updateNavigatioBarState()
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == R.id.action_home) {
            startActivity(Intent(this, DashboardActivity::class.java))
        } else if (itemId == R.id.action_reports) {
            startActivity(Intent(this, ReportActivity::class.java))
        } else if (itemId == R.id.action_booking) {
            startActivity(Intent(this, BookingActivity::class.java))
        } else if (itemId == R.id.action_notifications) {
            startActivity(Intent(this, NotificationsActivity::class.java))
        }
        finish()
        return true
    }

    fun updateNavigatioBarState() {
        selectBottomNavigationItem(getBottomNavigationMenuItemId())
    }

    fun selectBottomNavigationItem(itemID: Int) {
        var item = navigationView.menu.findItem(itemID)
        item.setChecked(true)
    }

    abstract fun getBottomNavigationMenuItemId(): Int //Which menu item selected and change the state of that menu item

}