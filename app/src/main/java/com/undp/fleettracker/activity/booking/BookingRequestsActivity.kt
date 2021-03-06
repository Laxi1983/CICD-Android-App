package com.undp.fleettracker.activity.booking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.undp.fleettracker.R
import com.undp.fleettracker.activity.base.BaseActivity

class BookingRequestsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_requests)
        initBaseActivityViews()
    }

    override fun getBottomNavigationMenuItemId(): Int {
        return R.id.action_booking
    }
}
