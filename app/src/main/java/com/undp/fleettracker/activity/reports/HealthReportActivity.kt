package com.undp.fleettracker.activity.reports

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.undp.fleettracker.R
import com.undp.fleettracker.activity.base.BaseActivity

class HealthReportActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_report)
        initBaseActivityViews()
    }

    override fun getBottomNavigationMenuItemId(): Int {
        return R.id.action_reports
    }
}
