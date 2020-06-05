package com.undp.fleettracker.activity.booking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.undp.fleettracker.R
import com.undp.fleettracker.activity.base.BaseActivity
import com.undp.fleettracker.adapter.VehicleOccupancyPagerAdapter
import kotlinx.android.synthetic.main.content_vehicle_occupancy.*

class VehicleOccupancyActivity : BaseActivity() {

    private var vehicleOccupancyPagerAdapter: VehicleOccupancyPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_occupancy)
        initBaseActivityViews()
        supportActionBar?.show()
        supportActionBar?.title = resources.getString(R.string.title_vehicle_occupancy)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        initPager()
    }

    override fun getBottomNavigationMenuItemId(): Int {
        return R.id.action_booking
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun initPager() {
        vehicleOccupancyPagerAdapter = VehicleOccupancyPagerAdapter(this, supportFragmentManager)
        view_pager.adapter = vehicleOccupancyPagerAdapter

        tabs.setupWithViewPager(view_pager)
    }
}
